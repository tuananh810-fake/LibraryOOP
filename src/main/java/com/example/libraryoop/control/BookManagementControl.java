package com.example.libraryoop.control;

import java.time.Year;
import java.util.ArrayList;
import java.util.List;

import com.example.libraryoop.file_handle.FileBookCSV;
import com.example.libraryoop.model.Book;


public class BookManagementControl {
    // This class will handle book management operations such as adding, removing, and updating books.

    private List<Book> bookCatalog;

    // Constructor to initialize the book catalog

    public BookManagementControl() {
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
        idBook = java.util.UUID.randomUUID().toString();
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
}