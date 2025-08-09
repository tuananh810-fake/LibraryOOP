package com.example.libraryoop.file_handle;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;
import com.example.libraryoop.model.Staff;

public class FileStaffCSV {

    private static final Path EXTERNAL_PATH = Paths.get("data", "StaffData.csv");
    private static final String HEADER = "ID || NAME || ADDRESS || EMAIL || PHONENUMBER || ROLE || USERNAME || PASSWORD";

    public void writeStaffFile(List<Staff> staffs) {
        try {
            // tạo folder nếu chưa tồn tại
            if (Files.notExists(EXTERNAL_PATH.getParent())) {
                Files.createDirectories(EXTERNAL_PATH.getParent());
            }
            // ghi file (utf-8), xóa nội dung cũ
            try (BufferedWriter bw = Files.newBufferedWriter(EXTERNAL_PATH,
                    StandardCharsets.UTF_8, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING)) {

                if (Files.size(EXTERNAL_PATH) == 0) {
                    bw.write(HEADER);
                    bw.newLine();
                }
            
                for (Staff staff : staffs) {
                    StringJoiner sj = new StringJoiner(" || ");

                    sj.add(nullToEmpty(staff.getIdStaff()));
                    sj.add(nullToEmpty(staff.getNameStaff()));
                    sj.add(nullToEmpty(staff.getAddressStaff()));
                    sj.add(nullToEmpty(staff.getEmailStaff()));
                    sj.add(nullToEmpty(staff.getNumberPhoneStaff()));
                    sj.add(nullToEmpty(staff.getRole()));
                    sj.add(nullToEmpty(staff.getUsername()));
                    sj.add(nullToEmpty(staff.getPassWord()));

                    bw.write(sj.toString());
                    bw.newLine();
                }
            }

        } catch (IOException e) {
            throw new RuntimeException("Error writing ReaderData.csv: " + e.getMessage(), e);
        }
    }
    
    public void readStaffFile() {
        try {
            if (Files.notExists(EXTERNAL_PATH.getParent())) {
                Files.createDirectories(EXTERNAL_PATH.getParent());
            }

            if (Files.notExists(EXTERNAL_PATH)) {
                try (BufferedWriter writer =  Files.newBufferedWriter(EXTERNAL_PATH, 
                StandardCharsets.UTF_8, StandardOpenOption.CREATE)) {
                    writer.write(HEADER);
                    writer.newLine();
                }
            }

            try (BufferedReader reader = Files.newBufferedReader(EXTERNAL_PATH, StandardCharsets.UTF_8)) {
                String line;
                List<Staff> staffs = new ArrayList<>();
                boolean isFirstLine = true;

                while ((line = reader.readLine()) != null) {
                    if (isFirstLine) {
                        isFirstLine = false;
                        continue;
                    }
                    
                    line.trim();

                    String[] parts = line.split (" \\|\\| ");

                    String id = parts.length > 0 ? emptyToNull(parts[0].trim()) : "";
                    String name = parts.length > 1 ? emptyToNull(parts[1].trim()) : "";
                    String address = parts.length > 2 ? emptyToNull(parts[2].trim()) : "";
                    String email = parts.length > 3 ? emptyToNull(parts[3].trim()) : "";
                    String phonenumber = parts.length > 4 ? emptyToNull(parts[4].trim()) : "";
                    String role = parts.length > 5 ? emptyToNull(parts[5].trim()) : "";
                    String username = parts.length > 6 ? emptyToNull(parts[6].trim()) : "";
                    String password = parts.length > 7 ? emptyToNull(parts[7].trim()) : "";

                    Staff staff = new Staff(id, name, email, phonenumber, address, role, username, password);
                    staffs.add(staff);
                }
            }
        } catch (IOException e) {
            System.err.println("Loi khi xu ly file" + e.getMessage());
        }
    }

    private static String nullToEmpty(String s) {
        return s == null ? "" : s;
    }

    private static String emptyToNull(String s) {
        return (s == null || s.isEmpty()) ? null : s;
    }
}
