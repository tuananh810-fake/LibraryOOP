package com.example.libraryoop.service;

import java.time.Year;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.example.libraryoop.file_handle.FileBookCSV;
import com.example.libraryoop.model.Book;
import com.example.libraryoop.util.IdGenerator;

public class BookManagementService {
    private List<Book> bookCatalog;

    public BookManagementService() {
        bookCatalog = com.example.libraryoop.file_handle.FileBookCSV.readBooksFromCSV();
    }

    public List<Book> getBookCatalog() {
        return bookCatalog;
    }

    public void setBookCatalog(List<Book> bookCatalog) {
        this.bookCatalog = bookCatalog;
    }

    /**
     * Giữ lại method cũ (cũng có thể dùng), nhưng thêm overload nhận Book.
     * Nếu book.id rỗng -> tự tạo id.
     */
    // public void addBook(String idBook, String nameBook, String author, String
    // category, String publishingCompany, Year publishingYear, int numberOfBooks) {
    // Book newBook = new Book(idBook, nameBook, author, category,
    // publishingCompany, publishingYear, numberOfBooks);
    // idBook = IdGenerator.generateId("B");
    // newBook.setIdBook(idBook);
    // bookCatalog.add(newBook);
    // FileBookCSV.writeBookToCSV(newBook);
    // System.out.println("Book added: " + nameBook + " by " + author);
    // }

    /** MỚI: cho controller truyền nguyên Book object */
    public void addBook(Book book) {
        // Tạo Id dựa trên prefix "B"
        String idBook;
        do {
            idBook = IdGenerator.generateId("B");
        } while (getBookById(idBook) != null); // đảm bảo id là duy nhất
        book.setIdBook(idBook); // Set id cho book
        bookCatalog.add(book); // Thêm vào List
        FileBookCSV.writeBookToCSV(book); // viết vào file CSV
        System.out.println("Book added: " + book.getNameBook() + " by " + book.getAuthor());
    }

    public void removeBook(String idBook) {
        bookCatalog.removeIf(book -> book.getIdBook().equals(idBook));
        FileBookCSV.overwriteBooksToCSV(bookCatalog);
    }

    /** wrapper/alias để đồng bộ tên với controller */
    public void delete(String idBook) {
        removeBook(idBook);
    }

    /**
     * Update cũ (giữ nguyên) — vẫn dùng nếu cần.
     */
    public void updateBook(
            String idBook, String oldNameBook, String oldAuthor, String oldCategory, String oldPublishingCompany,
            Year oldPublishingYear, int oldNumberOfBooks,
            String newNameBook, String newAuthor, String newCategory, String newPublishingCompany,
            Year newPublishingYear, int newNumberOfBooks) {
        for (Book book : bookCatalog) {
            if (book.getNameBook().equals(oldNameBook)
                    && book.getAuthor().equals(oldAuthor)
                    && book.getCategory().equals(oldCategory)
                    && book.getPublishingCompany().equals(oldPublishingCompany)
                    && book.getPublishingYear().equals(oldPublishingYear)
                    && book.getNumberOfBooks() == oldNumberOfBooks) {
                book.setNameBook(newNameBook);
                book.setAuthor(newAuthor);
                book.setCategory(newCategory);
                book.setPublishingCompany(newPublishingCompany);
                book.setPublishingYear(newPublishingYear);
                book.setNumberOfBooks(newNumberOfBooks);
                break;
            }
        }
        FileBookCSV.overwriteBooksToCSV(bookCatalog);
        System.out.println("Book updated: " + oldNameBook + " to " + newNameBook + " by " + newAuthor);
    }

    /**
     * MỚI: update theo id + map fields -> controller gọi dễ dàng
     * Hỗ trợ các key: nameBook, author, category, publishingCompany, publishingYear
     * (Year or String/Integer), numberOfBooks
     */
    public void update(String id, Map<String, Object> fieldsToUpdate) throws Exception {
        int idx = findBookIndexById(id);
        if (idx == -1)
            throw new Exception("Không tìm thấy sách id=" + id);
        Book b = bookCatalog.get(idx);

        for (Map.Entry<String, Object> e : fieldsToUpdate.entrySet()) {
            String k = e.getKey();
            Object v = e.getValue();
            switch (k) {
                case "nameBook":
                    b.setNameBook(v == null ? "" : v.toString());
                    break;
                case "author":
                    b.setAuthor(v == null ? "" : v.toString());
                    break;
                case "category":
                    b.setCategory(v == null ? "" : v.toString());
                    break;
                case "publishingCompany":
                    b.setPublishingCompany(v == null ? "" : v.toString());
                    break;
                case "publishingYear":
                    if (v instanceof Year)
                        b.setPublishingYear((Year) v);
                    else if (v instanceof Number)
                        b.setPublishingYear(Year.of(((Number) v).intValue()));
                    else if (v instanceof String && !((String) v).isBlank()) {
                        try {
                            b.setPublishingYear(Year.of(Integer.parseInt((String) v)));
                        } catch (NumberFormatException ex) {
                            /* ignore */ }
                    }
                    break;
                case "numberOfBooks":
                    if (v instanceof Number)
                        b.setNumberOfBooks(((Number) v).intValue());
                    else if (v instanceof String) {
                        try {
                            b.setNumberOfBooks(Integer.parseInt((String) v));
                        } catch (NumberFormatException ex) {
                            /* ignore */ }
                    }
                    break;
                default:
                    // bỏ qua các key không nhận diện
                    break;
            }
        }
        FileBookCSV.overwriteBooksToCSV(bookCatalog);
    }

    public int findBookIndexById(String id) {
        for (int i = 0; i < bookCatalog.size(); i++) {
            if (bookCatalog.get(i).getIdBook().equals(id))
                return i;
        }
        return -1;
    }

    public List<Book> findBookByIdOrName(String input) {
        List<Book> results = new ArrayList<>();
        for (Book book : bookCatalog) {
            if (book.getIdBook().equals(input) || book.getNameBook().equalsIgnoreCase(input)) {
                results.add(book);
            }
        }
        return results;
    }

    public List<String> getBookListId() {
        List<String> ids = new ArrayList<>();
        for (Book book : bookCatalog)
            ids.add(book.getIdBook());
        return ids;
    }

    public List<Book> getAllBooks() {
        return bookCatalog;
    }

    /** Tên chuẩn: getBookById */
    public Book getBookById(String idBook) {
        for (Book book : bookCatalog) {
            if (book.getIdBook().equals(idBook)) {
                return book;
            }
        }
        return null;
    }

    /*
     * Cập nhập số lượng sách
     */
    public void updateBookQuantity(Book book, int quantity) {
        if (book == null) {
            throw new IllegalArgumentException("Book cannot be null");
        }
        int currentQuantity = book.getNumberOfBooks();
        book.setNumberOfBooks(currentQuantity + quantity);
        FileBookCSV.overwriteBooksToCSV(bookCatalog);
    }

    /**
     * Cập nhật thông tin sách trực tiếp
     */
    public void updateBook(Book book) {
        if (book == null) {
            throw new IllegalArgumentException("Book cannot be null");
        }

        int index = findBookIndexById(book.getIdBook());
        if (index != -1) {
            bookCatalog.set(index, book);
            FileBookCSV.overwriteBooksToCSV(bookCatalog);
        }
    }


    /**
     * Tìm kiếm sách theo nhiều tiêu chí
     * @param searchText Văn bản tìm kiếm
     * @return Danh sách sách phù hợp
     */
    public List<Book> searchBooks(String searchText) {
        if (searchText == null || searchText.trim().isEmpty()) {
            return new ArrayList<>(bookCatalog); // Trả về tất cả nếu không có từ khóa
        }

        String searchLower = searchText.toLowerCase().trim();
        return bookCatalog.stream()
                .filter(book -> {
                    // Tìm theo tên sách
                    if (book.getNameBook() != null &&
                            book.getNameBook().toLowerCase().contains(searchLower)) {
                        return true;
                    }
                    // Tìm theo tác giả
                    if (book.getAuthor() != null &&
                            book.getAuthor().toLowerCase().contains(searchLower)) {
                        return true;
                    }
                    // Tìm theo thể loại
                    if (book.getCategory() != null &&
                            book.getCategory().toLowerCase().contains(searchLower)) {
                        return true;
                    }
                    // Tìm theo nhà xuất bản
                    if (book.getPublishingCompany() != null &&
                            book.getPublishingCompany().toLowerCase().contains(searchLower)) {
                        return true;
                    }
                    // Tìm theo ID
                    return book.getIdBook() != null &&
                            book.getIdBook().toLowerCase().contains(searchLower);
                })
                .collect(Collectors.toList());
    }
}

