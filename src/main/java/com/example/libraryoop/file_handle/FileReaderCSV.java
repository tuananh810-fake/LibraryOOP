package com.example.libraryoop.file_handle;

import java.io.*;
import java.time.LocalDateTime;
import java.util.List;

import com.example.libraryoop.model.Reader;

public class FileReaderCSV {
    public static final String NULLVALUE = ""; // Định nghĩa giá trị null trong file CSV
 
    // Phương thức ghi danh sách Reader vào file CSV
    public static void writeFile(List<Reader> list) {
        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(
             "src\\main\\java\\com\\example\\libraryoop\\database\\ReaderData.csv"))) {
                bufferedWriter.write("ID || NAME || ADDRESS || EMAIL || PHONENUMBER || EXPIRY || ISLOCK"); // Tiêu đề cột Reader.CSV
                bufferedWriter.newLine(); // Xuống dòng sau tiêu đề
                for (Reader reader : list) {
                    bufferedWriter.write(reader.getIdReader() + " || " +
                            reader.getNameReader() + " || " +
                            reader.getAddressReader() + " || " +
                            reader.getEmailReader() + " || " +
                            reader.getPhoneNumber() + " || " +
                            reader.getExpiry() + " || " +
                            reader.isLock());
                    bufferedWriter.newLine(); // Xuống dòng sau mỗi bản ghi
                }
        } catch (IOException e) {
            throw new RuntimeException(e); 
        }
    }
 
    // Phương thức đọc file CSV và trả về danh sách Reader
    public static void readFile(List<Reader> list) {
        String line = ""; // Biến để lưu từng dòng đọc từ file
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(
             "src\\main\\java\\org\\example\\libraryoop\\database\\ReaderData.csv"))) {
            // Đọc tiêu đề cột
            bufferedReader.readLine();
            while (true) {
                line = bufferedReader.readLine(); // Đọc từng dòng
                if (line == null || line.isEmpty()) { // Kiểm tra nếu dòng đọc là null hoặc rỗng
                    break; // Dừng nếu không còn dòng nào để đọc
                }
                String readerArray[] = line.split("||"); // Tách các trường trong dòng
                String id = readerArray[0]; // Lấy ID độc giả
                String name = readerArray[1]; // Lấy tên độc giả
                String address;
                if (readerArray[2].equals(NULLVALUE)) { // Kiểm tra nếu địa chỉ là NULLVALUE
                    address = NULLVALUE; // Không cần ghi ngay,có thể thêm vào sau
                } else {
                    address = readerArray[2]; // Lấy địa chỉ độc giả
                }
                String email;
                if (readerArray[3].equals(NULLVALUE)) { 
                    email = NULLVALUE; 
                } else {
                    email = readerArray[3]; // Lấy email độc giả
                }
                String phoneNumber;
                if (readerArray[4].equals(NULLVALUE)) { 
                    phoneNumber = NULLVALUE; 
                } else {
                    phoneNumber = readerArray[4]; // Lấy số điện thoại độc giả
                }
                LocalDateTime expiry = LocalDateTime.parse(readerArray[5]); // Lấy ngày hết hạn thẻ mượn sách
                boolean lock = Boolean.parseBoolean(readerArray[6]); // Lấy trạng thái khóa của độc giả
                list.add(new Reader(id, name, address, email, phoneNumber, expiry, lock)); // Thêm thông tin độc giả vào danh sách
            }
        } catch (IOException e) {
                throw new RuntimeException(e); 
            }
        }
}


