# Phần mềm Quản lý Thư viện
[![License: MIT](https://img.shields.io/badge/License-MIT-blue.svg)](https://opensource.org/licenses/MIT)
[![Java Version](https://img.shields.io/badge/Java-17%2B-orange.svg)](https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html)
[![JavaFX](https://img.shields.io/badge/UI-JavaFX-blueviolet.svg)](https://openjfx.io/)
[![Maven](https://img.shields.io/badge/Built%20with-Maven-red.svg)](https://maven.apache.org/)

Một ứng dụng Desktop được phát triển bằng **Java** và **JavaFX**, mô phỏng hoạt động quản lý của một thư viện hiện đại. Dự án được thiết kế với mục tiêu cốt lõi là áp dụng và thực hành các nguyên lý Lập trình Hướng đối tượng (OOP) một cách triệt để, từ cấu trúc dữ liệu đến logic nghiệp vụ.

---

## Mục lục
- [Tổng quan & Mục tiêu](#-tổng-quan--mục-tiêu)
- [Điểm nhấn Kỹ thuật & Thiết kế](#-điểm-nhấn-kỹ-thuật--thiết-kế)
- [Tính năng Chính](#-tính-năng-chính)
- [Kiến trúc & Công nghệ](#-kiến-trúc--công-nghệ)
- [Cấu trúc Thư mục](#-cấu-trúc-thư-mục)
- [Hướng dẫn Cài đặt & Vận hành](#-hướng-dẫn-cài-đặt--vận-hành)
- [Thông tin Nhóm](#-thông-tin-nhóm)

---

## 📖 Tổng quan & Mục tiêu

### Bối cảnh
Dự án được xây dựng nhằm giải quyết bài toán quản lý thủ công trong thư viện, đồng thời là một bài tập lớn để áp dụng sâu sắc kiến thức về Lập trình Hướng đối tượng. Hệ thống tập trung vào việc tự động hóa các quy trình cốt lõi như quản lý sách, độc giả và các lượt mượn/trả.

### Mục tiêu chính
*   **Về sản phẩm:** Xây dựng một phần mềm quản lý thư viện đơn giản, trực quan và hiệu quả.
*   **Về kỹ thuật:** Rèn luyện kỹ năng phân tích, thiết kế hệ thống và vận dụng thành thạo 4 nguyên lý OOP (Đóng gói, Kế thừa, Đa hình, Trừu tượng).
*   **Về định hướng:** Tạo nền tảng vững chắc để có thể mở rộng các tính năng nâng cao trong tương lai như quản lý trực tuyến, đặt sách online, và thống kê báo cáo.

---

## 💎 Điểm nhấn Kỹ thuật & Thiết kế
Dự án không chỉ hoàn thành các chức năng cơ bản mà còn chú trọng vào chất lượng mã nguồn và các giải pháp kỹ thuật hiện đại:

*   **Lưu trữ dữ liệu bằng CSV với NIO:** Thay vì dùng `java.io` truyền thống, dự án tận dụng API **Java NIO (New I/O - `java.nio.file.*`)** để tương tác với hệ thống tệp. Cách tiếp cận này mang lại hiệu suất cao hơn, hỗ trợ tốt hơn cho việc làm việc với `Path` và `Files`, đồng thời đảm bảo mã nguồn xử lý file trở nên gọn gàng và an toàn hơn.

*   **Tối ưu hiệu năng với Cấu trúc dữ liệu:**
    *   **HashMap:** Dữ liệu sau khi đọc từ file được lưu trữ trong bộ nhớ dưới dạng `List` và `HashMap` để tối ưu hóa tốc độ truy xuất và cập nhật theo key (O(1)), đặc biệt hiệu quả khi xử lý số lượng lớn đối tượng.
    *   **ArrayList:** Sử dụng `ArrayList` làm cấu trúc lưu trữ chính cho các danh sách, linh hoạt trong việc thêm/xóa và duyệt qua các phần tử.

*   **Xử lý dữ liệu hiệu quả với Java Stream API:** Dự án sử dụng **Stream API (`java.util.stream.*`)** để thực hiện các thao tác lọc (filter), biến đổi (map), và thu thập (collect) dữ liệu một cách ngắn gọn và mạnh mẽ, giúp giảm thiểu các vòng lặp `for` truyền thống và làm cho mã nguồn dễ đọc hơn.

*   **Tìm kiếm thông minh:** Triển khai thuật toán "Tìm kiếm mờ" (Fuzzy Keyword Search), cho phép người dùng tìm kiếm thông tin một cách linh hoạt mà không cần nhập chính xác hoàn toàn từ khóa.

*   **Quản lý ID tự động:** Hệ thống tự sinh ID duy nhất cho mỗi đối tượng thông qua lớp tiện ích `IdGenerator` theo một format chung (`prefix` + `random number`), giúp việc quản lý định danh trở nên nhất quán.

*   **Áp dụng triệt để Nguyên lý OOP:**
    *   **Đóng gói (Encapsulation):** Toàn bộ thuộc tính của các `model` được đặt ở chế độ `private` và chỉ có thể truy cập thông qua các phương thức công khai, đảm bảo tính toàn vẹn dữ liệu.
    *   **Trừu tượng (Abstraction):** Sử dụng `interface` trong tầng `Service` (ví dụ: `BookManagementService`) để định nghĩa các hành vi chung, tách biệt phần định nghĩa và phần triển khai.

*   **Xử lý lỗi chủ động (Exception Handling):** Sử dụng cơ chế `try-catch` và các lớp `Exception` (`IOException`, `DateTimeParseException`) để xử lý các lỗi có thể xảy ra trong quá trình vận hành, giúp chương trình hoạt động ổn định.

*   **Xử lý thời gian hiện đại:** Tận dụng **`java.time.LocalDateTime`** để quản lý thông tin ngày giờ mượn/trả, cung cấp cách xử lý ngày giờ an toàn, bất biến (immutable) và mạnh mẽ.

---

## ✨ Tính năng Chính
-   ✅ **Quản lý Sách:** Thêm, sửa, xóa, và tìm kiếm sách theo nhiều tiêu chí (tên, tác giả, thể loại...).
-   ✅ **Quản lý Độc giả:** Quản lý thông tin cá nhân của các thành viên thư viện.
-   ✅ **Quản lý Mượn/Trả sách:** Ghi nhận và theo dõi lịch sử, trạng thái của các lượt mượn.
-   ✅ **Hệ thống Đăng nhập:** Phân quyền truy cập cho Thủ thư (Admin) với đầy đủ chức năng quản trị.

---

## 🛠️ Kiến trúc & Công nghệ

| Lĩnh vực | Công nghệ / Thư viện | Mục đích sử dụng |
| :--- | :--- | :--- |
| **Nền tảng** | **Java** (JDK 17+) | Ngôn ngữ lập trình chính. |
| **Giao diện** | **JavaFX** | Framework xây dựng giao diện người dùng Desktop. |
| **Lưu trữ Dữ liệu** | **File CSV** | Lưu trữ dữ liệu một cách đơn giản, gọn nhẹ. |
| **Quản lý Dự án** | **Apache Maven** | Quản lý dependencies, build và vòng đời dự án. |
| **Kỹ thuật & API Cốt lõi**| **Java NIO (`java.nio.file.*`)** | Thao tác file hiệu suất cao và hiện đại. |
| | **Stream API** | Xử lý collection dữ liệu theo phong cách lập trình hàm. |
| | **`java.time` API** | Xử lý ngày giờ chính xác và an toàn. |
| | **Java Collections (List, Map)**| Quản lý dữ liệu trong bộ nhớ (in-memory).|
| **Công cụ Phát triển**| **IntelliJ IDEA**, **VS Code**, **Git** | Môi trường phát triển, quản lý phiên bản. |

---

## 📁 Cấu trúc Thư mục
Dự án được tổ chức theo cấu trúc phân lớp rõ ràng, giúp dễ dàng bảo trì và mở rộng:
<img width="956" height="1041" alt="image" src="https://github.com/user-attachments/assets/3ff55865-3303-47f4-a7dd-518b18b98e92" />
LibraryOOP/
│
├── data/
│   # Directory for .csv data files
│
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   ├── com/
│   │   │   │   ├── example/
│   │   │   │   │   ├── libraryoop/
│   │   │   │   │   │   ├── controller/
│   │   │   │   │   │   │   # Classes handling UI logic
│   │   │   │   │   │   ├── file_handle/
│   │   │   │   │   │   │   # Classes for reading/writing CSV files
│   │   │   │   │   │   ├── model/
│   │   │   │   │   │   │   # POJO classes (Book, User, Reader, etc.)
│   │   │   │   │   │   ├── service/
│   │   │   │   │   │   │   # Core business logic
│   │   │   │   │   │   ├── util/
│   │   │   │   │   │   │   # Utility classes (IdGenerator, DateTime Formatter, etc.)
│   │   │   │   │   │   ├── validate/
│   │   │   │   │   │   │   # Input validation classes
│   │   │   │   │   │   ├── Launcher.java
│   │   │   │   │   │   │   # JavaFX application entry point
│   │   │   │   │   │   └── Main.java
│   │   │   │   │   │       # Main entry point
│   │   │   │   │   │
│   │   └── resources/
│   │       # Contains .fxml, .css, images, etc.
│   │
│   └── pom.xml
│       # Maven configuration file

---

## 🚀 Hướng dẫn Cài đặt & Vận hành

### Yêu cầu Môi trường
-   **Java Development Kit (JDK):** phiên bản 17 trở lên.
-   **Apache Maven:** phiên bản 3.6 trở lên.
-   **IDE:** IntelliJ IDEA hoặc Eclipse (Khuyến khích IntelliJ).

### Các bước thực hiện
1.  **Clone Repository:**
    ```bash
    git clone https://github.com/tuananh810-fake/LibraryOOP.git
    cd LibraryOOP
    ```

2.  **Mở Dự án bằng IDE:**
    *   Mở IntelliJ IDEA, chọn `File -> Open...` và trỏ đến thư mục `LibraryOOP` vừa clone về.
    *   IDE sẽ tự động nhận diện file `pom.xml` và tải về các thư viện cần thiết.

3.  **Build và Chạy Ứng dụng:**
    *   **Cách 1: Sử dụng Maven (khuyến khích):**
        Mở Terminal tích hợp trong IDE (hoặc terminal của hệ điều hành) tại thư mục gốc và chạy lệnh:
        ```bash
        mvn clean javafx:run
        ```
    *   **Cách 2: Chạy trực tiếp từ IDE:**
        Tìm đến file `Launcher.java` trong `src/main/java/com/example/libraryoop/`, chuột phải và chọn `Run 'Launcher.main()'`.

### 🔑 Tài khoản Demo
Sử dụng tài khoản sau để đăng nhập với quyền Thủ thư:
*   **Tài khoản:** `ad`
*   **Mật khẩu:** `123`

---

## 👨‍💻 Thông tin Nhóm
| STT | Họ và Tên | Mã số sinh viên |
|:---:|:---|:---|
| 1 | Nguyễn Sỹ Tuấn Anh | `24020389` |
| 2 | Phạm Quốc Anh | `24023174` |
| 3 | Nguyễn Quang Anh | `23020781` |
| 4 | Nguyễn Tiến Dũng | `23021498` |

**Môn học:** Lập trình Hướng đối tượng
**Giảng viên:** Trần Ngọc Trúc Linh
