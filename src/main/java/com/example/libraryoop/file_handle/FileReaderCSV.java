package com.example.libraryoop.file_handle;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.StringJoiner;

import com.example.libraryoop.model.Reader;

public class FileReaderCSV {

    private static final Path EXTERNAL_PATH = Paths.get("data", "ReaderData.csv");
    private static final String HEADER = "ID || NAME || ADDRESS || EMAIL || PHONENUMBER || EXPIRY || ISLOCK";
    private static final String DELIM_REGEX = "\\s*\\|\\|\\s*"; // split theo " || " (có thể có khoảng trắng)
    public static final String NULLVALUE = ""; // định nghĩa giá trị null trong file CSV

    /**
     * Ghi danh sách Reader ra file external (data/ReaderData.csv).
     */
    public static void writeFile(List<Reader> list) {
        try {
            // tạo folder nếu chưa tồn tại
            if (Files.notExists(EXTERNAL_PATH.getParent())) {
                Files.createDirectories(EXTERNAL_PATH.getParent());
            }
            // ghi file (utf-8), xóa nội dung cũ
            try (BufferedWriter bw = Files.newBufferedWriter(EXTERNAL_PATH,
                    StandardCharsets.UTF_8, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING)) {
                bw.write(HEADER);
                bw.newLine();
                for (Reader reader : list) {
                    StringJoiner sj = new StringJoiner(" || ");
                    sj.add(nullToEmpty(reader.getIdReader()));
                    sj.add(nullToEmpty(reader.getNameReader()));
                    sj.add(nullToEmpty(reader.getAddressReader()));
                    sj.add(nullToEmpty(reader.getEmailReader()));
                    sj.add(nullToEmpty(reader.getPhoneNumber()));
                    sj.add(reader.getExpiry() == null ? "" : reader.getExpiry().toString()); // ISO format
                    sj.add(Boolean.toString(reader.isLock()));
                    bw.write(sj.toString());
                    bw.newLine();
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Error writing ReaderData.csv: " + e.getMessage(), e);
        }
    }

    /**
     * Đọc file CSV vào list (list được truyền vào và sẽ được clear trước khi load).
     * Nếu file không tồn tại sẽ tạo file rỗng và trả về (list để trống).
     */
    public static void readFile(List<Reader> list) {
        try {
            // tạo folder/file nếu chưa có để tránh FileNotFoundException
            if (Files.notExists(EXTERNAL_PATH.getParent())) {
                Files.createDirectories(EXTERNAL_PATH.getParent());
            }
            if (Files.notExists(EXTERNAL_PATH)) {
                // tạo file rỗng với header
                try (BufferedWriter bw = Files.newBufferedWriter(EXTERNAL_PATH,
                        StandardCharsets.UTF_8, StandardOpenOption.CREATE)) {
                    bw.write(HEADER);
                    bw.newLine();
                }
                return; // file mới, danh sách để trống
            }

            // đọc file
            try (BufferedReader br = Files.newBufferedReader(EXTERNAL_PATH, StandardCharsets.UTF_8)) {
                String line;
                boolean first = true;
                list.clear();
                while ((line = br.readLine()) != null) {
                    if (first) { // bỏ header nếu có
                        first = false;
                        if (line.trim().startsWith("ID")) continue;
                    }
                    if (line.trim().isEmpty()) continue;

                    // split theo "||" (cho phép khoảng trắng xung quanh)
                    String[] cols = line.split(DELIM_REGEX, -1); // -1 giữ các trường rỗng

                    String id = cols.length > 0 ? emptyToNull(cols[0].trim()) : "";
                    String name = cols.length > 1 ? emptyToNull(cols[1].trim()) : "";
                    String address = cols.length > 2 ? emptyToNull(cols[2].trim()) : "";
                    String email = cols.length > 3 ? emptyToNull(cols[3].trim()) : "";
                    String phone = cols.length > 4 ? emptyToNull(cols[4].trim()) : "";

                    LocalDateTime expiry = null;
                    if (cols.length > 5 && cols[5] != null && !cols[5].trim().isEmpty()) {
                        try {
                            expiry = LocalDateTime.parse(cols[5].trim());
                        } catch (DateTimeParseException ex) {
                            System.err.println("Invalid date format for expiry: " + cols[5].trim());
                            expiry = null;
                        }
                    }

                    boolean lock = false;
                    if (cols.length > 6 && cols[6] != null && !cols[6].trim().isEmpty()) {
                        lock = Boolean.parseBoolean(cols[6].trim());
                    }

                    Reader r = new Reader(id, name, address, email, phone, expiry, lock);
                    list.add(r);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Error reading ReaderData.csv: " + e.getMessage(), e);
        }
    }

    private static String nullToEmpty(String s) {
        return s == null ? "" : s;
    }

    private static String emptyToNull(String s) {
        return (s == null || s.isEmpty()) ? null : s;
    }
}
