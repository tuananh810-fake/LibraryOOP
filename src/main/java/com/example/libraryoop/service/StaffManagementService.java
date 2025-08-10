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

//    public void addStaff(Staff staff) {
//        if (staff == null) {
//            throw new IllegalArgumentException("Illegal Argument");
//        }
//
//        staffList.add(staff);
//        FileStaffCSV.writeStaffFile(staffList);
//    }
//
//    public void addNewStaff(Staff staff) {
//        if (staff == null) {
//            throw new IllegalArgumentException("Illegal Argument");
//        }
//
//        do {
//            idStaff++;
//        } while (!checkingStaffId(String.valueOf(idStaff)));
//
//        staff.setIdStaff(String.valueOf(idStaff));
//        addStaff(staff);
//
//    }
//
//    public void update(String id, Map<String, Object> fieldsToUpdate) throws Exception {
//        int index = findIndexStaff(id);
//        if (index == -1) {
//            throw new Exception("Không tìm thấy độc giả để cập nhật!");
//        }
//
//        Staff existingStaff = staffList.get(index);
//
//        // Cập nhật từng trường thông tin
//        for (Map.Entry<String, Object> entry : fieldsToUpdate.entrySet()) {
//            String fieldName = entry.getKey();
//            Object value = entry.getValue();
//
//            switch (fieldName) {
//                case "nameStaff":
//                    String newName = (String) value;
//                    if (newName == null || newName.trim().isEmpty()) {
//                        throw new Exception("Tên độc giả không được để trống.");
//                    }
//                    existingStaff.setNameStaff(newName);
//                    break;
//
//                case "addressReader":
//                    existingStaff.setAddressStaff((String) value);
//                    break;
//
//                case "emailReader":
//                    String newEmail = (String) value;
//                    if (!newEmail.isEmpty()) {
//                        throw new Exception("Email không hợp lệ.");
//                    }
//                    existingStaff.setEmailStaff(newEmail);
//                    break;
//
//                case "phoneNumber":
//                    String newPhone = (String) value;
//                    if (!newPhone.isEmpty()) {
//                        throw new Exception("Số điện thoại không hợp lệ.");
//                    }
//                    existingStaff.setNumberPhoneStaff(newPhone);
//                    break;
//                case "role":
//                    String newRole = (String) value;
//                    existingStaff.setRole(newRole);
//                    break;
//
//                case "username":
//                    String username = (String) value;
//                    existingStaff.setUsername(username);
//                    break;
//                case "password":
//                    String password = (String) value;
//                    existingStaff.setPassWord(password);
//                default:
//                    System.out.println("Cảnh báo: Trường '" + fieldName + "' không hợp lệ.");
//                    break;
//            }
//        }
//
//        FileStaffCSV.writeStaffFile(staffList);
//    }
//
//    public void deleteStaff(String id) throws Exception {
//        if (checkingStaffId(id) == false) {
//            throw new Exception("Khong tim thay staff");
//        }
//        Iterator<Staff> iterator = staffList.iterator();
//
//        while (iterator.hasNext()) {
//
//            Staff staff = iterator.next();
//
//            if (staff.getIdStaff().equals(id)) {
//                iterator.remove();
//                System.out.println("Xoa staff thanh cong");
//            }
//        }
//    }
//
//    public int findIndexStaff(String id) {
//        for (int i = 0; i < staffList.size(); i++) {
//            if (staffList.get(i).getIdStaff().equals(id)) {
//                return i;
//            }
//        }
//
//        return -1;
//    }
//
//    public boolean checkingStaffId(String id) {
//        for (Staff staff : staffList) {
//            if (staff.getIdStaff().equals(id)) {
//                return true;
//            }
//        }
//
//        return false;
//    }

    public List<Staff> getAllStaff() {
        return new ArrayList<>(staffList);
    }
}
