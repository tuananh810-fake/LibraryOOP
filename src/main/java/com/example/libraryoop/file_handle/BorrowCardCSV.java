package com.example.libraryoop.file_handle;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.Year;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.StringJoiner;

import com.example.libraryoop.model.Book;
import com.example.libraryoop.model.BorrowCard;
import com.example.libraryoop.model.Reader;
import com.example.libraryoop.service.BookManagementService;
import com.example.libraryoop.service.ReaderManagementService;

public class BorrowCardCSV {
    private static final Path EXTERNAL_PATH = Paths.get("data", "BorrowCardData.csv");
    private static final String HEADER = "ID_BORROW_CARD || BOOK || AUTHOR || PUBLISHINGYEAR || QUANTILY || ID_READER || BOOKLOANDAY";
    private static final String DELIM_REGEX = "\\s*\\|\\|\\s*"; // split theo " || " (có thể có khoảng trắng)
    public static final String NULLVALUE = ""; // định nghĩa giá trị null trong file CSV

    private static String nullToEmpty(String value) {
        return value == null ? NULLVALUE : value;
    }

    private static String emptyToNull(String s) {
        return (s == null || s.isEmpty()) ? null : s;
    }

    /**
     * Ghi danh sách BorrowCard ra file external (data/BorrowCardData.csv).
     */
    public static void writeBorrowCardListToFile(List<BorrowCard> borrowCardList) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(EXTERNAL_PATH.toFile(), false))) {
            writer.write(HEADER);
            writer.newLine();
            for (BorrowCard borrowCard : borrowCardList) {
                StringJoiner sj = new StringJoiner(" || ");
                sj.add(nullToEmpty(borrowCard.getIdBorrowCard()));
                sj.add(nullToEmpty(borrowCard.getBook().getNameBook())); // BOOK
                sj.add(nullToEmpty(borrowCard.getBook().getAuthor())); // AUTHOR
                sj.add(borrowCard.getBook().getPublishingYear() == null ? NULLVALUE
                        : String.valueOf(borrowCard.getBook().getPublishingYear().getValue())); // PUBLISHINGYEAR
                sj.add(String.valueOf(borrowCard.getQuantity())); // QUANTILY
                sj.add(nullToEmpty(borrowCard.getReader().getIdReader())); // ID_READER
                sj.add(borrowCard.getBookLoanDay() == null ? NULLVALUE : borrowCard.getBookLoanDay().toString()); // BOOKLOANDAY
                writer.write(sj.toString());
                writer.newLine();
            }
        } catch (IOException e) {
            throw new RuntimeException("Error writing BorrowCardData.csv: " + e.getMessage(), e);
        }
    }

    /**
     * Đọc file CSV vào danh sách BorrowCard.
     * Nếu file không tồn tại sẽ tạo file rỗng và trả về danh sách trống.
     */
    public static void readFile(List<BorrowCard> borrowCardList) {
        try {
            // Tạo folder nếu chưa có
            if (Files.notExists(EXTERNAL_PATH.getParent())) {
                Files.createDirectories(EXTERNAL_PATH.getParent());
            }
            // Tạo file rỗng với header nếu chưa có
            if (Files.notExists(EXTERNAL_PATH)) {
                try (BufferedWriter bw = Files.newBufferedWriter(EXTERNAL_PATH,
                        StandardCharsets.UTF_8, java.nio.file.StandardOpenOption.CREATE)) {
                    bw.write(HEADER);
                    bw.newLine();
                }
                borrowCardList.clear();
                return;
            }

            try (BufferedReader br = Files.newBufferedReader(EXTERNAL_PATH, StandardCharsets.UTF_8)) {
                String line;
                boolean first = true;
                borrowCardList.clear();

                // Tạo service một lần duy nhất
                ReaderManagementService readerService = new ReaderManagementService();
                BookManagementService bookService = new BookManagementService();

                while ((line = br.readLine()) != null) {
                    if (first) {
                        first = false;
                        if (line.trim().startsWith("ID"))
                            continue;
                    }
                    if (line.trim().isEmpty())
                        continue;

                    String[] cols = line.split(DELIM_REGEX, -1);

                    // Đọc các trường theo thứ tự trong header
                    String idBorrowCard = cols.length > 0 ? emptyToNull(cols[0].trim()) : "";
                    String bookName = cols.length > 1 ? emptyToNull(cols[1].trim()) : "";
                    String author = cols.length > 2 ? emptyToNull(cols[2].trim()) : "";

                    // Xử lý năm xuất bản
                    Year publishingYear = null;
                    if (cols.length > 3 && cols[3] != null && !cols[3].trim().isEmpty()) {
                        try {
                            publishingYear = Year.of(Integer.parseInt(cols[3].trim()));
                        } catch (NumberFormatException ex) {
                            System.err.println("Invalid year format: " + cols[3].trim());
                        }
                    }

                    // Xử lý số lượng
                    int quantity = 0;
                    if (cols.length > 4 && cols[4] != null && !cols[4].trim().isEmpty()) {
                        try {
                            quantity = Integer.parseInt(cols[4].trim());
                        } catch (NumberFormatException ex) {
                            System.err.println("Invalid quantity format: " + cols[4].trim());
                        }
                    }

                    // Đọc ID người đọc
                    String readerId = cols.length > 5 ? emptyToNull(cols[5].trim()) : "";

                    // Xử lý ngày mượn
                    LocalDateTime bookLoanDay = null;
                    if (cols.length > 6 && cols[6] != null && !cols[6].trim().isEmpty()) {
                        try {
                            bookLoanDay = LocalDateTime.parse(cols[6].trim());
                        } catch (DateTimeParseException ex) {
                            System.err.println("Invalid date format: " + cols[6].trim());
                        }
                    }

                    // Tạo đối tượng Reader
                    Reader idreader = readerService.getReaderById(readerId);
                    if (idreader == null) {
                        System.err.println("Reader not found for ID: " + readerId);
                        continue;
                    }

                    // Lấy Book từ BookManagementService để đồng bộ thông tin
                    Book book = null;
                    for (Book b : bookService.getBookCatalog()) {
                        if (b.getNameBook().equals(bookName) && b.getAuthor().equals(author)
                                && ((b.getPublishingYear() == null && publishingYear == null)
                                    || (b.getPublishingYear() != null && b.getPublishingYear().equals(publishingYear)))) {
                            book = b;
                            break;
                        }
                    }
                    // Nếu không tìm thấy, tạo Book tạm
                    if (book == null) {
                        book = new Book(bookName, author, publishingYear);
                    }

                    // Tạo BorrowCard với đầy đủ thông tin
                    BorrowCard borrowCard = new BorrowCard(idBorrowCard, book, idreader, quantity, bookLoanDay);
                    borrowCardList.add(borrowCard);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Error reading BorrowCardData.csv: " + e.getMessage(), e);
        }
    }
}
