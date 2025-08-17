# The Library Management Example Project

## Giới thiệu

**Yêu cầu:** Lập một chương trình quản lý thư viện

**Mô tả:** Phần mềm lưu trữ dữ liệu với cấu trúc đơn giản như csv để lưu trữ các dữ liệu đơn giản. Phần mềm bao gồm rất nhiều gói, gồm cấu trúc dữ liệu thô và các logic làm việc với dữ liệu và xử lý giao diện. Phần giao diện được 

## Cấu trúc lớp và đối tượng

Dự án này được chia làm 6 gói chính, với các phương thức khác nhau trong từng gói.

- com.example.libraryoop.model: Gói này bao gồm các phương thức cơ bản cho dữ liệu mà không bao gồm logic xử lý dữ liệu, bao gồm Book, Reader, Staff, and BorrowCard
- com.example.libraryoop.util: Gói này có lớp tạo ID, được sử dụng làm mẫu ID cho rất nhiều phần của chương trình
- com.example.libraryoop.service: Gói này có những logic để làm việc với dữ liệu trong thư viện, bao gồm tìm kiếm, thêm, xóa, sử dữ liệu
- com.example.libraryoop.file_handle: Gói này gồm những lớp có khả năng sửa dữ liệu bằng cách ghi/ xóa/ thay thế dữ liệu trên tệp CSV
- com.example.libraryoop.validate: Gói này có những phương thức để xác thực tính hợp lệ của định dạng dữ liệu cá nhân, bao gồm 
- com.example.libraryoop.controller: Gói này sử dụng mã giao diện JavaFX để hiển thị các kết quả hoặc lỗi trên giao diện ứng dụng

Ngoài các gói được sử dụng cho những mục đích trên, còn có 2 lớp khác được sử dụng để thực thi giao diện chính

- Lớp Main gọi đến các tệp FXML để gọi các phần của giao diện ứng dụng
- Launcher là lớp để khởi chạy ứng dụng, vì phương thức main của lớp này gọi lớp Main để chạy chương trình hoàn chỉnh.
