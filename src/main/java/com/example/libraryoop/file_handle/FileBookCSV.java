package com.example.libraryoop.file_handle;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.Year;
import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;

import com.example.libraryoop.model.Book;

public class FileBookCSV {

    private static final Path CSV_PATH = Paths.get("data", "BookData.csv");
    private static final String HEADER = "ID,NAME,AUTHOR,CATEGORY,PUBLISHER,YEAR,NUMBER";
    private static final String DELIM_REGEX = ","; // split theo dấu phẩy

    /**
     * Đọc file CSV vào list Book.
     * Nếu file không tồn tại sẽ tạo file rỗng với header và trả về list rỗng.
     */
    public static List<Book> readBooksFromCSV() {
        List<Book> books = new ArrayList<>();
        try {
            // Tạo folder nếu chưa có
            if (Files.notExists(CSV_PATH.getParent())) {
                Files.createDirectories(CSV_PATH.getParent());
            }
            // Tạo file rỗng với header nếu chưa có
            if (Files.notExists(CSV_PATH)) {
                try (BufferedWriter bw = Files.newBufferedWriter(CSV_PATH,
                        StandardCharsets.UTF_8, StandardOpenOption.CREATE)) {
                    bw.write(HEADER);
                    bw.newLine();
                }
                return books;
            }

            try (BufferedReader br = Files.newBufferedReader(CSV_PATH, StandardCharsets.UTF_8)) {
                String line;
                boolean first = true;
                while ((line = br.readLine()) != null) {
                    if (first) { // bỏ header nếu có
                        first = false;
                        if (line.trim().startsWith("ID")) continue;
                    }
                    if (line.trim().isEmpty()) continue;

                    String[] fields = line.split(DELIM_REGEX, -1); // giữ trường rỗng
                    String idBook = fields.length > 0 ? emptyToNull(fields[0].trim()) : null;
                    String nameBook = fields.length > 1 ? emptyToNull(fields[1].trim()) : null;
                    String author = fields.length > 2 ? emptyToNull(fields[2].trim()) : null;
                    String category = fields.length > 3 ? emptyToNull(fields[3].trim()) : null;
                    String publishingCompany = fields.length > 4 ? emptyToNull(fields[4].trim()) : null;

                    Year publishingYear = null;
                    if (fields.length > 5 && fields[5] != null && !fields[5].trim().isEmpty()) {
                        try {
                            publishingYear = Year.parse(fields[5].trim());
                        } catch (Exception ex) {
                            System.err.println("Invalid year format: " + fields[5].trim());
                            publishingYear = null;
                        }
                    }

                    int numberOfBooks = 0;
                    if (fields.length > 6 && fields[6] != null && !fields[6].trim().isEmpty()) {
                        try {
                            numberOfBooks = Integer.parseInt(fields[6].trim());
                        } catch (NumberFormatException ex) {
                            numberOfBooks = 0;
                        }
                    }

                    Book book = new Book(idBook, nameBook, author, category, publishingCompany, publishingYear, numberOfBooks);
                    books.add(book);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Error reading BookData.csv: " + e.getMessage(), e);
        }
        return books;
    }

    /**
     * Ghi thêm 1 Book vào file CSV.
     */
    public static void writeBookToCSV(Book book) {
        try {
            if (Files.notExists(CSV_PATH.getParent())) {
                Files.createDirectories(CSV_PATH.getParent());
            }
            if (Files.notExists(CSV_PATH)) {
                try (BufferedWriter bw = Files.newBufferedWriter(CSV_PATH,
                        StandardCharsets.UTF_8, StandardOpenOption.CREATE)) {
                    bw.write(HEADER);
                    bw.newLine();
                }
            }
            try (BufferedWriter bw = Files.newBufferedWriter(CSV_PATH,
                    StandardCharsets.UTF_8, StandardOpenOption.APPEND)) {
                bw.write(bookToCSV(book));
                bw.newLine();
            }
        } catch (IOException e) {
            throw new RuntimeException("Error writing BookData.csv: " + e.getMessage(), e);
        }
    }

    /**
     * Ghi đè toàn bộ danh sách Book vào file CSV.
     */
    public static void overwriteBooksToCSV(List<Book> books) {
        try {
            if (Files.notExists(CSV_PATH.getParent())) {
                Files.createDirectories(CSV_PATH.getParent());
            }
            try (BufferedWriter bw = Files.newBufferedWriter(CSV_PATH,
                    StandardCharsets.UTF_8, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING)) {
                bw.write(HEADER);
                bw.newLine();
                for (Book book : books) {
                    bw.write(bookToCSV(book));
                    bw.newLine();
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Error overwriting BookData.csv: " + e.getMessage(), e);
        }
    }

    // Chuyển Book thành dòng CSV
    private static String bookToCSV(Book book) {
        StringJoiner sj = new StringJoiner(",");
        sj.add(nullToEmpty(book.getIdBook()));
        sj.add(nullToEmpty(book.getNameBook()));
        sj.add(nullToEmpty(book.getAuthor()));
        sj.add(nullToEmpty(book.getCategory()));
        sj.add(nullToEmpty(book.getPublishingCompany()));
        sj.add(book.getPublishingYear() == null ? "" : String.valueOf(book.getPublishingYear().getValue()));
        sj.add(String.valueOf(book.getNumberOfBooks()));
        return sj.toString();
    }

    private static String nullToEmpty(String s) {
        return s == null ? "" : s;
    }

    private static String emptyToNull(String s) {
        return (s == null || s.isEmpty()) ? null : s;
    }
}