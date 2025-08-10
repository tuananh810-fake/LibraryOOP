package com.example.libraryoop.service;

import java.util.List;
import java.util.Arrays;
import java.util.ArrayList;
import com.example.libraryoop.file_handle.FileStaffCSV;
import com.example.libraryoop.model.Staff;
import com.example.libraryoop.util.IdGenerator;

public class StaffManagementService {
    private static final List<Staff> staffList = new ArrayList<>();

    public void addStaff(Staff staff) {
        if (staff == null) {
            throw new IllegalArgumentException("Illegal Argument");
        }   

        staffList.add(staff);
        FileStaffCSV.writeStaffFile(staffList);
    }

    public void addNewStaff(Staff staff) {
        if (staff == null) {
            throw new IllegalArgumentException("Illegal Argument");
        }

        String newId;
        
        do {
            newId = IdGenerator.generateId("S");
        } while (getReaderById(newId) != null);
        
        String name = staff.getNameStaff();
        String address = staff.getAddressStaff();
        String email = staff.getEmailStaff();
        String numberPhone = staff.getNumberPhoneStaff();
        String role = staff.getRole();
        String username = staff.getUsername();
        String password = staff.getPassWord();

        Staff newStaff = new Staff(newId, name, address, email, numberPhone, role, username, password);

    }
}
