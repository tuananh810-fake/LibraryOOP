package com.example.libraryoop.control;

/**
 * Import các 
 */
import com.example.libraryoop.model.Reader;
import com.example.libraryoop.validate.EmailValidate;

import java.io.FileReader;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.example.libraryoop.file_handle.FileReaderCSV;

public class ReaderManagementControl implements IManagement<Reader> {
    private final static List<Reader> readers = new ArrayList<>(); // Danh sách lưu trữ Reader

    /**
     * Constructer.
     */
    public ReaderManagementControl() {
        FileReaderCSV.readFile(readers); // Đọc dữ liệu từ file CSV vào danh sách readers
        autoSetLock();   // Tự động thiết lập trạng thái khóa cho các độc giả
  }

    @Override
    public void add(Reader reader) {
        readers.add(reader); // Thêm độc giả vào danh sách
        FileReaderCSV.writeFile(readers); // Ghi lại danh sách độc giả vào file CSV
    }

    @Override
public void update(String id, Map<String, Object> fieldsToUpdate) throws Exception {
    // 1. Tìm đối tượng gốc cần cập nhật
    int index = findIndexById(id);
    if (index == -1) {
        throw new Exception("Lỗi: Không tìm thấy độc giả để cập nhật!");
    }
    Reader existingReader = readers.get(index);

    // 2. Duyệt qua "túi" dữ liệu và cập nhật từng trường một
    for (Map.Entry<String, Object> entry : fieldsToUpdate.entrySet()) {
        String fieldName = entry.getKey();
        Object value = entry.getValue();

        // Dùng switch-case để xử lý từng trường hợp
        switch (fieldName) {
            case "nameReader":
                String newName = (String) value;
                if (newName == null || newName.trim().isEmpty()) { // Kiểm tra nếu tên độc giả không hợp lệ
                    throw new Exception("Tên độc giả không được để trống.");
                }
                existingReader.setNameReader(newName);  // Cập nhật tên độc giả
                break;
            case "addressReader":
                existingReader.setAddressReader((String) value);  // Cập nhật địa chỉ độc giả
                break;
            case "emailReader":
                String newEmail = (String) value;
                if (!newEmail.isEmpty() && !EmailValidate.isValid(newEmail)) {  // Kiểm tra nếu email không hợp lệ
                    throw new Exception("Địa chỉ email không hợp lệ.");
                }
                existingReader.setEmailReader(newEmail);   // Cập nhật email độc giả
                break;
            case "phoneNumber":
                existingReader.setPhoneNumber((String) value);  // Cập nhật số điện thoại độc giả
                break;
            default:
                // Có thể bỏ qua các trường không xác định hoặc ném lỗi
                System.out.println("Cảnh báo: Trường không xác định '" + fieldName + "' bị bỏ qua.");
                break;
        }
    }

    // 3. Lưu lại sau khi đã cập nhật xong
    FileReaderCSV.writeFile(readers);
    }

    @Override
    public void delete(String id) {
    readers.remove(findIndexById(id)); // Xóa độc giả theo ID
    FileReaderCSV.writeFile(readers);
    }

    @Override
    public int findIndexById(String id) {  // Tìm kiếm vị trí của độc giả theo ID
        for (int i = 0; i < readers.size(); i++) {
            if (readers.get(i).getIdReader().equals(id)) {
                return i;
            }
        }
        return -1;
    }

    
    public List<Reader> findReaderByIdOrName(String inp) { 
        List<Reader> listFind = new ArrayList<>();
        for (Reader reader : readers) {
            if (String.valueOf(reader.getIdReader()).contains(inp) || reader.getNameReader().contains(inp)) {
                listFind.add(reader);
            }
        }
        return listFind;
    }


    public static void autoSetLock() { // Tự động thiết lập trạng thái khóa cho các độc giả
        LocalDateTime localDateTime = LocalDateTime.now();
        for (Reader reader : readers) {
            if(!reader.getExpiry().isAfter(localDateTime)) {
                reader.setLock(true);
            }
        }
        FileReaderCSV.writeFile(readers); // Ghi lại danh sách độc giả vào file CSV sau khi cập nhật trạng thái khóa
    }
    
        public void extendExpiry(String id, Reader reader) {  // Gia hạn ngày hết hạn thẻ mượn sách cho độc giả
//        readers.set(findIndexById(id), reader);
        readers.get(findIndexById(id)).setExpiry(reader.getExpiry());
        readers.get(findIndexById(id)).setLock(false);
        FileReaderCSV.writeFile(readers);
    }

    public List<String> getListId() {  // Lấy danh sách ID của tất cả độc giả
        List<String> list = new ArrayList<>();
        for (Reader reader: readers) {
            list.add(reader.getIdReader());
        }
        return list;
    }
}


