package com.example.libraryoop.service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.example.libraryoop.file_handle.FileStaffCSV;
import com.example.libraryoop.model.Staff;

public class StaffManagementService {
    private static final List<Staff> staffList = new ArrayList<>();
//    private static int idStaff = 0;

    public StaffManagementService() {
        staffList.clear();
        staffList.addAll(FileStaffCSV.readStaffFile());
    }

    public List<Staff> getAllStaff() {
        return new ArrayList<>(staffList);
    }
}
