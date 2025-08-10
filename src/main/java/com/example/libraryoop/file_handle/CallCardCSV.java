package com.example.libraryoop.file_handle;

import com.example.libraryoop.model.CallCard;
import com.example.libraryoop.service.ReaderManagementService;
import com.example.libraryoop.model.Reader;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.StringJoiner;

public class CallCardCSV {
    private static final Path EXTERNAL_PATH = Paths.get("data", "CallCardData.csv");
    private static final String HEADER = "ID || READER || BOOKLOANDAY";
    private static final String DELIM_REGEX = "\\s*\\|\\|\\s*"; // split theo " || " (có thể có khoảng trắng)
    public static final String NULLVALUE = ""; // định nghĩa giá trị null trong file CSV

    private static String nullToEmpty(String value) {
        return value == null ? NULLVALUE : value;
    }

    private static String emptyToNull(String s) {
        return (s == null || s.isEmpty()) ? null : s;
    }

    /**
     * Ghi danh sách CallCard ra file external (data/CallCardData.csv).
     */
    public static void writeCallCardListToFile(List<CallCard> callCardList) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(EXTERNAL_PATH.toFile()))) {
            writer.write(HEADER);
            writer.newLine();
            for (CallCard callCard : callCardList) {
                StringJoiner sj = new StringJoiner(" || ");
                sj.add(nullToEmpty(callCard.getIdCallCard()));
                sj.add(nullToEmpty(callCard.getReader().getIdReader()));
                sj.add(callCard.getBookLoanDay() == null ? NULLVALUE : callCard.getBookLoanDay().toString());
                writer.write(sj.toString());
                writer.newLine();
            }
        } catch (IOException e) {
            throw new RuntimeException("Error writing CallCardData.csv: " + e.getMessage(), e);
        }
    }

    /**
     * Đọc file CSV vào danh sách CallCard.
     * Nếu file không tồn tại sẽ tạo file rỗng và trả về danh sách trống.
     */
    public static void readFile(List<CallCard> callCardList) {
        try{
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
        //đọc file
        try (BufferedReader br = Files.newBufferedReader(EXTERNAL_PATH, StandardCharsets.UTF_8)) {
                String line;
                boolean first = true;
                callCardList.clear();
                
                while ((line = br.readLine()) != null) {
                    if (first) { // bỏ header nếu có
                        first = false;
                        if (line.trim().startsWith("ID")) continue;
                    }
                    if (line.trim().isEmpty()) continue;


                    // split theo "||" (cho phép khoảng trắng xung quanh)
                    String[] cols = line.split(DELIM_REGEX, -1); // -1 giữ các trường rỗng

                    String id = cols.length > 0 ? emptyToNull(cols[0].trim()) : "";
                    String readerId = cols.length > 1 ? emptyToNull(cols[1].trim()) : "";

                    LocalDateTime bookLoanDay = null;
                    if (cols.length > 2 && cols[2] != null && !cols[2].trim().isEmpty()) {
                        try {
                            bookLoanDay = LocalDateTime.parse(cols[2].trim());
                        } catch (DateTimeParseException ex) {
                            System.err.println("Invalid date format for book loan day: " + cols[2].trim());
                            bookLoanDay = null;
                        }
                    }

                    // Tạo đối tượng Reader từ readerId (cần có ReaderManagementService)
                    ReaderManagementService readerService = new ReaderManagementService();
                    Reader reader = readerService.getReaderById(readerId);
                    if (reader == null) {
                        System.err.println("Reader not found for ID: " + readerId);
                        continue;
                    }

                    CallCard callCard = new CallCard(id, reader, bookLoanDay);
                    callCardList.add(callCard);
                }
            } catch (IOException e) {
                throw new RuntimeException("Error reading CallCardData.csv: " + e.getMessage(), e);
            } 
        }
        // End of outer try block
        catch (IOException e) {
            throw new RuntimeException("Error initializing CallCardData.csv: " + e.getMessage(), e);
        }
    }
}    
