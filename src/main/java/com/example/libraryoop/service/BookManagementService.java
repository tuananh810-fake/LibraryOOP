package com.example.libraryoop.service;

import java.time.Year;
import java.util.ArrayList;
import java.util.List;

import com.example.libraryoop.file_handle.FileBookCSV;
import com.example.libraryoop.model.Book;
import com.example.libraryoop.util.IdGenerator;


public class BookManagementService {
    // This class will handle book management operations such as adding, removing, and updating books.

    private List<Book> bookCatalog;

    // Constructor to initialize the book catalog

    public BookManagementService() {
        bookCatalog = new ArrayList<>();
    }

    // Getters and Setters

    public List<Book> getBookCatalog() {
        return bookCatalog;
    }

    public void setBookCatalog(List<Book> bookCatalog) {
        this.bookCatalog = bookCatalog;
    }

    // Methods to add, remove, and update books

    public void addBook(String idBook, String nameBook, String author, String category, String publishingCompany, Year publishingYear, int numberOfBooks) {
        Book newBook = new Book(idBook, nameBook, author, category, publishingCompany, publishingYear, numberOfBooks);
        idBook = IdGenerator.generateId("B");
        newBook.setIdBook(idBook);
        bookCatalog.add(newBook);
        FileBookCSV.writeBookToCSV(newBook);
        System.out.println("Book added: " + nameBook + " by " + author);
    }

    public void removeBook(String idBook) {
        bookCatalog.removeIf(book -> book.getIdBook().equals(idBook));
        FileBookCSV.overwriteBooksToCSV(bookCatalog);
    }

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

    public int findBookIndexById(String id) {
        // Find index of item by id
        for (int i = 0; i < bookCatalog.size(); i++) {
            if (bookCatalog.get(i).getIdBook().equals(id)) {
                return i;
            }
        }
        return -1;
    }

    public List<Book> findBookByIdOrName(String input) {
        // Search items by id or name
        List<Book> results = new ArrayList<>();
        for (Book book : bookCatalog) {
            if (book.getIdBook().equals(input) || book.getNameBook().equalsIgnoreCase(input)) {
                results.add(book);
            }
        }
        return results;
    }

    public List<String> getBookListId() {
        // Return list of all IDs
        List<String> ids = new ArrayList<>();
        for (Book book : bookCatalog) {
            ids.add(book.getIdBook());
        }
        return ids;
    }

    public List<Book> getAllBooks() {
        // Return list of all items
        return new ArrayList<>(bookCatalog);
    }

    public Book getBooksById(String id) {
        // Return item by id
        for (Book book : bookCatalog) {
            if (book.getIdBook().equals(id)) {
                return book;
            }
        }
        return null;
    }
}