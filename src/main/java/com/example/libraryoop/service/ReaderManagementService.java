package com.example.libraryoop.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.example.libraryoop.file_handle.FileReaderCSV;
import com.example.libraryoop.model.Reader;
import com.example.libraryoop.validate.EmailValidate;
import com.example.libraryoop.util.IdGenerator;
import com.example.libraryoop.validate.PhoneNumberValidate;

/**
 * Lớp quản lý các chức năng liên quan đến Reader (độc giả)
 */
public class ReaderManagementService implements IManagement<Reader> {
    // Danh sách lưu trữ tất cả độc giả
    private final static List<Reader> readers = new ArrayList<>();
    private final PhoneNumberValidate phoneValidator = new PhoneNumberValidate();
    private final EmailValidate emailValidator = new EmailValidate();
    /**
     * Constructor: Đọc dữ liệu từ file CSV và kiểm tra trạng thái khóa
     */
    public ReaderManagementService() {
        FileReaderCSV.readFile(readers);
        autoSetLock();
    }

    /**
     * Thêm một độc giả vào hệ thống và lưu vào file
     * @param reader Đối tượng Reader cần thêm
     */
    @Override
    public void add(Reader reader) {
        if (reader == null) {
            throw new IllegalArgumentException("Reader không thể null");
        }
        readers.add(reader);
        FileReaderCSV.writeFile(readers);
    }

    /**
     * Thêm độc giả mới với ID được tạo tự động
     * @param reader Thông tin độc giả cần thêm
     */
    public void addNewReader(Reader reader) {
        if (reader == null) {
            throw new IllegalArgumentException("Reader không thể null");
        }

        // Tạo ID mới và đảm bảo không trùng lặp
        String newId;
        do {
            newId = IdGenerator.generateId("R");
        } while (getReaderById(newId) != null);

        // Tạo reader mới với ID mới được tạo
        Reader newReader = new Reader(
            newId,
            reader.getNameReader(),
            reader.getAddressReader(),
            reader.getEmailReader(),
            reader.getPhoneNumber(),
            reader.getExpiry(),
            reader.isLock()
        );

        add(newReader);
    }

    /**
     * Cập nhật thông tin độc giả
     * @param id ID của độc giả cần cập nhật
     * @param fieldsToUpdate Map chứa các trường cần cập nhật
     */
    @Override
    public void update(String id, Map<String, Object> fieldsToUpdate) throws Exception {
        int index = findIndexById(id);
        if (index == -1) {
            throw new Exception("Không tìm thấy độc giả để cập nhật!");
        }
        
        Reader existingReader = readers.get(index);
        
        // Cập nhật từng trường thông tin
        for (Map.Entry<String, Object> entry : fieldsToUpdate.entrySet()) {
            String fieldName = entry.getKey();
            Object value = entry.getValue();
            
            switch (fieldName) {
                case "nameReader":
                    String newName = (String) value;
                    if (newName == null || newName.trim().isEmpty()) {
                        throw new Exception("Tên độc giả không được để trống.");
                    }
                    existingReader.setNameReader(newName);
                    break;
                    
                case "addressReader":
                    existingReader.setAddressReader((String) value);
                    break;
                    
                case "emailReader":
                    String newEmail = (String) value;
                    if (!newEmail.isEmpty() && !emailValidator.validate(newEmail)) {
                        throw new Exception("Email không hợp lệ.");
                    }
                    existingReader.setEmailReader(newEmail);
                    break;
                    
                case "phoneNumber":
                    String newPhone = (String) value;
                    if (!newPhone.isEmpty() && !phoneValidator.validate(newPhone)) {
                        throw new Exception("Số điện thoại không hợp lệ.");
                    }
                    existingReader.setPhoneNumber(newPhone);
                    break;
                case "expiry":
                    existingReader.setExpiry((LocalDateTime) value);
                    break;
                    
                case "lock":
                    existingReader.setLock((Boolean) value);
                    break;
                default:
                    System.out.println("Cảnh báo: Trường '" + fieldName + "' không hợp lệ.");
                    break;
            }
        }
        
        FileReaderCSV.writeFile(readers);
    }

    /**
     * Xóa độc giả khỏi hệ thống
     * @param id ID của độc giả cần xóa
     */
    @Override
    public void delete(String id) {
        int index = findIndexById(id);
        if (index != -1) {
            readers.remove(index);
            FileReaderCSV.writeFile(readers);
        }
    }

    /**
     * Tìm vị trí của độc giả trong danh sách theo ID
     * @param id ID cần tìm
     * @return vị trí trong danh sách, -1 nếu không tìm thấy
     */
    @Override
    public int findIndexById(String id) {
        for (int i = 0; i < readers.size(); i++) {
            if (readers.get(i).getIdReader().equals(id)) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Tìm kiếm độc giả theo ID hoặc tên
     * @param input Chuỗi tìm kiếm
     * @return Danh sách độc giả phù hợp
     */
    public List<Reader> findReaderByIdOrName(String input) {
        List<Reader> foundReaders = new ArrayList<>();
        for (Reader reader : readers) {
            if (reader.getIdReader().contains(input) || 
                reader.getNameReader().toLowerCase().contains(input.toLowerCase())) {
                foundReaders.add(reader);
            }
        }
        return foundReaders;
    }

    /**
     * Tự động khóa tài khoản độc giả hết hạn
     */
    public static void autoSetLock() {
        LocalDateTime now = LocalDateTime.now();
        boolean hasChanges = false;
        
        for (Reader reader : readers) {
            if (reader.getExpiry() != null && !reader.getExpiry().isAfter(now)) {
                reader.setLock(true);
                hasChanges = true;
            }
        }
        
        // Chỉ lưu file khi có sự thay đổi
        if (hasChanges) {
            FileReaderCSV.writeFile(readers);
        }
    }

    /**
     * Gia hạn thẻ độc giả
     * @param id ID của độc giả cần gia hạn
     * @param reader Thông tin gia hạn
     */
    public void extendExpiry(String id, Reader reader) {
        int index = findIndexById(id);
        if (index != -1) {
            Reader existingReader = readers.get(index);
            existingReader.setExpiry(reader.getExpiry());
            existingReader.setLock(false);
            FileReaderCSV.writeFile(readers);
        }
    }

    /**
     * Lấy danh sách tất cả ID độc giả
     */
    public List<String> getListId() {
        List<String> ids = new ArrayList<>();
        for (Reader reader : readers) {
            ids.add(reader.getIdReader());
        }
        return ids;
    }

    /**
     * Lấy danh sách tất cả độc giả
     */
    public List<Reader> getAllReaders() {
        return new ArrayList<>(readers);
    }

    /**
     * Lấy thông tin độc giả theo ID
     * @param id ID cần tìm
     * @return Đối tượng Reader, null nếu không tìm thấy
     */
    public Reader getReaderById(String id) {
        int index = findIndexById(id);
        return index != -1 ? readers.get(index) : null;
    }

    public static Reader getCallCardById(String readerId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getCallCardById'");
    }


}