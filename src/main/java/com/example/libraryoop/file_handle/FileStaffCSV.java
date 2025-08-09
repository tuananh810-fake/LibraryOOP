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
import com.example.libraryoop.model.Staff;

public class FileStaffCSV {
    //
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

                if (Files.notExists(EXTERNAL_PATH)) {
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
                    sj.add(nullToEmpty(staff.getPassWord()))

                    bw.write(sj.toString());
                    bw.newLine();
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Error writing ReaderData.csv: " + e.getMessage(), e);
        }
    }
    
    public void readStaffFIle() {
        try {
            if (Files.notExists(EXTERNAL_PATH.getParent())) {
                Files.createDirectories(EXTERNAL_PATH.getParent());
            }

            if (Files.notExists(EXTERNAL_PATH))
        }
    }

    private static String nullToEmpty(String s) {
        return s == null ? "" : s;
    }

    private static String emptyToNull(String s) {
        return (s == null || s.isEmpty()) ? null : s;
    }
}
