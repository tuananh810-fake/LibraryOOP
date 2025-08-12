package com.example.libraryoop.model;

import java.time.Year;

public class Book {
    private String idBook;  // id sách
    private String nameBook;  // tên sách
    private String author;    // tác giả
    private String category;   // thể loại sách
    private String publishingCompany;   // nhà xuất bản
    private Year publishingYear;   // năm xuất bản
    private int numberOfBooks;   // số lượng sách

    /**
     * Constructer Default.
     */
    public Book() {
    }

    /**
     * Constructer with parameters.
     */
    public Book(String idBook, String nameBook, String author, String category, 
    String publishingCompany, Year publishingYear, int numberOfBooks) {
        this.idBook = idBook;
        this.nameBook = nameBook;
        this.author = author;
        this.category = category;
        this.publishingCompany = publishingCompany;
        this.publishingYear = publishingYear;
        this.numberOfBooks = numberOfBooks;
    }

    public Book(String nameBook, String author, Year publishingYear){
        this.nameBook=nameBook;
        this.author=author;
        this.publishingYear=publishingYear;
    }

    /**
     * Getter and Setter
     */
    public String getIdBook() {
        return idBook;
    }

    public void setIdBook(String idBook) {
        this.idBook = idBook;
    }

    public String getNameBook() {
        return nameBook;
    }

    public void setNameBook(String nameBook) {
        this.nameBook = nameBook;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getPublishingCompany() {
        return publishingCompany;
    }

    public void setPublishingCompany(String publishingCompany) {
        this.publishingCompany = publishingCompany;
    }

    public Year getPublishingYear() {
        return publishingYear;
    }

    public void setPublishingYear(Year publishingYear) {
        this.publishingYear = publishingYear;
    }

    public int getNumberOfBooks() {
        return numberOfBooks;
    }

    public void setNumberOfBooks(int numberOfBooks) {
        this.numberOfBooks = numberOfBooks;
    }

    @Override
    public String toString() {
        return idBook;
    }

    public String toCSV() {
        return idBook + "," + nameBook + "," + author + "," + category + "," + publishingCompany + "," + publishingYear.getValue() + "," + numberOfBooks + "\n";
    }
}
