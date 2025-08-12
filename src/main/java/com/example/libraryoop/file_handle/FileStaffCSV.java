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

    public static void writeStaffFile(List<Staff> staffs) {
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
    
    public static List<Staff> readStaffFile() {
        try {
            if (Files.notExists(EXTERNAL_PATH.getParent())) {
                Files.createDirectories(EXTERNAL_PATH.getParent());
                return new ArrayList<>();
            }

            if (Files.notExists(EXTERNAL_PATH)) {
                try (BufferedWriter writer =  Files.newBufferedWriter(EXTERNAL_PATH, 
                StandardCharsets.UTF_8, StandardOpenOption.CREATE)) {
                    writer.write(HEADER);
                    writer.newLine();
                    return new ArrayList<>();
                }
            }
            
            List<Staff> staffs = new ArrayList<>();

            try (BufferedReader reader = Files.newBufferedReader(EXTERNAL_PATH, StandardCharsets.UTF_8)) {
                String line;

                reader.readLine();

                while ((line = reader.readLine()) != null) {
                    line = line.trim();
                    
                    if (line.isEmpty()) continue;

                    String[] parts = line.split ("\\|\\|", -1);

                    if (parts.length < 8) {
                        System.err.println("Invalid format: " + line);
                        continue;
                    }

                    String id = emptyToNull(parts[0].trim());
                    String name = emptyToNull(parts[1].trim());
                    String address = emptyToNull(parts[2].trim());
                    String email = emptyToNull(parts[3].trim());
                    String phonenumber = emptyToNull(parts[4].trim());
                    String role = emptyToNull(parts[5].trim());
                    String username = emptyToNull(parts[6].trim());
                    String password = emptyToNull(parts[7].trim());

                    Staff staff = new Staff(id, name, address, email, phonenumber, role, username, password);
                    staffs.add(staff);
                }
            }
            
            return staffs;

        } catch (IOException e) {
            System.err.println("Loi khi xu ly file" + e.getMessage());
            return new ArrayList<>();
        }
    }

    public void staffLongingIn(String username, String password) {
        List<Staff> staffs = readStaffFile();
        
        if (staffs.isEmpty()) {
            System.err.println("Please register before log in");
        }

        for (Staff staff : staffs) {
            if (staff.getUsername() == username && staff.getPassWord() == password) {
                System.out.println("Đăng nhập thành công");
            } else if (staff.getUsername() != username) {
                System.err.println("Không tìm thấy tên đăng nhập");
            } else if (staff.getPassWord() != password) {
                System.err.println("Sai tên đăng nhập hoặc tài khoản");
            }
        }
    }

    public void updateStaffAccount() {

    }

    private static String nullToEmpty(String s) {
        return s == null ? "" : s;
    }

    private static String emptyToNull(String s) {
        return (s == null || s.isEmpty()) ? null : s;
    }
}
