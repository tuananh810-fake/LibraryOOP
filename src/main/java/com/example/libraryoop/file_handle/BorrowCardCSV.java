package com.example.libraryoop.file_handle;

import com.example.libraryoop.model.BorrowCard;
import com.example.libraryoop.service.*;
import com.example.libraryoop.model.Reader;
import com.example.libraryoop.model.Book;


import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.Year;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.StringJoiner;

public class BorrowCardCSV {
    private static final Path EXTERNAL_PATH = Paths.get("data", "BorrowCardData.csv");
    private static final String HEADER = "ID_CALLCARD || BOOK || AUTHOR || PUBLISHINGYEAR || QUANTILY || ID_READER || BOOKLOANDAY";
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
    public static void writeCallCardListToFile(List<BorrowCard> borrowCardList) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(EXTERNAL_PATH.toFile()))) {
            writer.write(HEADER);
            writer.newLine();
            for (BorrowCard borrowCard : borrowCardList) {
                StringJoiner sj = new StringJoiner(" || ");
                sj.add(nullToEmpty(borrowCard.getIdCallCard()));
                sj.add(nullToEmpty(borrowCard.getBook().getNameBook())); // BOOK
                sj.add(nullToEmpty(borrowCard.getBook().getAuthor())); // AUTHOR
                sj.add(borrowCard.getBook().getPublishingYear() == null ? NULLVALUE
                        : String.valueOf(borrowCard.getBook())); // PUBLISHINGYEAR
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
    public static void readFile(List<BorrowCard> callCardList) {
        try {
            // ...existing code for file creation...

            try (BufferedReader br = Files.newBufferedReader(EXTERNAL_PATH, StandardCharsets.UTF_8)) {
                String line;
                boolean first = true;
                callCardList.clear();

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
                    String idCallCard = cols.length > 0 ? emptyToNull(cols[0].trim()) : "";
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
                    ReaderManagementService readerService = new ReaderManagementService();
                    Reader idreader = readerService.getReaderById(readerId);
                    if (idreader == null) {
                        System.err.println("Reader not found for ID: " + readerId);
                        continue;
                    }

                    // Tạo đối tượng Book (cần thêm BookManagementService)
                    Book book = new Book(bookName, author, publishingYear);

                    int quantily = 0;
                    // Tạo BorrowCard với đầy đủ thông tin
                    BorrowCard borrowCard = new BorrowCard(idCallCard ,book, idreader, quantily, bookLoanDay);
                    callCardList.add(borrowCard);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Error reading BorrowCardData.csv: " + e.getMessage(), e);
        }
    }
}
