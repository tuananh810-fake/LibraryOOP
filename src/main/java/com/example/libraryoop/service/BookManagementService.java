package com.example.libraryoop.service;

import java.time.Year;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.example.libraryoop.file_handle.FileBookCSV;
import com.example.libraryoop.model.Book;
import com.example.libraryoop.util.IdGenerator;

public class BookManagementService {
    private List<Book> bookCatalog;

    public BookManagementService() {
        bookCatalog = com.example.libraryoop.file_handle.FileBookCSV.readBooksFromCSV();
    }

    public List<Book> getBookCatalog() { return bookCatalog; }
    public void setBookCatalog(List<Book> bookCatalog) { this.bookCatalog = bookCatalog; }

    /**
     * Giữ lại method cũ (cũng có thể dùng), nhưng thêm overload nhận Book.
     * Nếu book.id rỗng -> tự tạo id.
     */
    public void addBook(String idBook, String nameBook, String author, String category, String publishingCompany, Year publishingYear, int numberOfBooks) {
        Book newBook = new Book(idBook, nameBook, author, category, publishingCompany, publishingYear, numberOfBooks);
        idBook = IdGenerator.generateId("B");
        newBook.setIdBook(idBook);
        bookCatalog.add(newBook);
        FileBookCSV.writeBookToCSV(newBook);
        System.out.println("Book added: " + nameBook + " by " + author);
    }

    /** MỚI: cho controller truyền nguyên Book object */
    public void addBook(Book book) {
        if (book == null) return;
        if (book.getIdBook() == null || book.getIdBook().isBlank()) {
            book.setIdBook(IdGenerator.generateId("B"));
        }
        bookCatalog.add(book);
        FileBookCSV.writeBookToCSV(book);
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
            String idBook, String oldNameBook, String oldAuthor, String oldCategory, String oldPublishingCompany, Year oldPublishingYear, int oldNumberOfBooks,
            String newNameBook, String newAuthor, String newCategory, String newPublishingCompany, Year newPublishingYear, int newNumberOfBooks
    ) {
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
     * Hỗ trợ các key: nameBook, author, category, publishingCompany, publishingYear (Year or String/Integer), numberOfBooks
     */
    public void update(String id, Map<String, Object> fieldsToUpdate) throws Exception {
        int idx = findBookIndexById(id);
        if (idx == -1) throw new Exception("Không tìm thấy sách id=" + id);
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
                    if (v instanceof Year) b.setPublishingYear((Year) v);
                    else if (v instanceof Number) b.setPublishingYear(Year.of(((Number) v).intValue()));
                    else if (v instanceof String && !((String) v).isBlank()) {
                        try { b.setPublishingYear(Year.of(Integer.parseInt((String) v))); } catch (NumberFormatException ex) { /* ignore */ }
                    }
                    break;
                case "numberOfBooks":
                    if (v instanceof Number) b.setNumberOfBooks(((Number) v).intValue());
                    else if (v instanceof String) {
                        try { b.setNumberOfBooks(Integer.parseInt((String) v)); } catch (NumberFormatException ex) { /* ignore */ }
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
            if (bookCatalog.get(i).getIdBook().equals(id)) return i;
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
        for (Book book : bookCatalog) ids.add(book.getIdBook());
        return ids;
    }

    public List<Book> getAllBooks() {
        return bookCatalog;
    }

    /** Tên chuẩn: getBookById */
    public Book getBookById(String id) {
        for (Book book : bookCatalog) if (book.getIdBook().equals(id)) return book;
        return null;
    }
}
