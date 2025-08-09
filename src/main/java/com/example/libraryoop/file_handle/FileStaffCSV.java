package com.example.libraryoop.file_handle;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import com.example.libraryoop.model.Staff;

public class FileStaffCSV {
    //
    public static final String FILE_STAFF_CSV = "src/main/java/com/example/libraryoop/database/StaffData.csv";

    public void writeStaffFile(List<Staff> staffs) {
        File file = new File(FILE_STAFF_CSV);
        boolean fileExisted = file.exists() && file.length() > 0;

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            if (!fileExisted) {
                writer.write ("ID || NAME || EMAIL || NUMBERPHONE || ADDRESS || ROLE || USERNAME || PASSWORD");
                writer.newLine();

                if(!staffs.isEmpty()) {
                    for (Staff staff : staffs) {
                        writer.write(staff.getIdStaff() + " || " 
                        + staff.getNameStaff() + " || "
                        + staff.getEmailStaff() + " || "
                        + staff.getNumberPhoneStaff() + " || " 
                        + staff.getAddressStaff() + " || " 
                        + staff.getRole() + " || "
                        + staff.getUsername() + " || " 
                        + staff.getPassWord() + " || ");

                        writer.newLine();
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Loi khi xu ly file" + e.getMessage());
        }
    }
    
    public void readStaffFIle() {
        File file = new File(FILE_STAFF_CSV);
        boolean fileExisted = file.exists() && file.length() > 0;
        List<Staff> staffs = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;

            while ((line = reader.readLine()) != null) {
                line.trim();
                 
                String[] splits = line.split(" \\|\\| ");


            }
        } catch (IOException e) {
            System.err.println("Loi khi xu ly file" + e.getMessage());
        }
    }
}
