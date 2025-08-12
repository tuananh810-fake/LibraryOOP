package com.example.libraryoop.model;

import java.time.LocalDateTime;

public class BorrowCard {
    private String idCallCard;      // id thẻ mượn sách
    private Book book;             // Thông tin sách mượn
    private Reader reader;         // Thông tin người mượn
    private int quantity;          // Số lượng mượn
    private LocalDateTime bookLoanDay;  // Ngày mượn sách

    public BorrowCard(String idCallCard, Book book, Reader reader, int quantity, LocalDateTime bookLoanDay) {
        this.idCallCard = idCallCard;
        this.book = book;
        this.reader = reader;
        this.quantity = quantity;
        this.bookLoanDay = bookLoanDay;
    }

    // Getters and Setters
    public String getIdCallCard() {
        return idCallCard;
    }

    public void setIdCallCard(String idCallCard) {
        this.idCallCard = idCallCard;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public Reader getReader() {
        return reader;
    }

    public void setReader(Reader reader) {
        this.reader = reader;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public LocalDateTime getBookLoanDay() {
        return bookLoanDay;
    }

    public void setBookLoanDay(LocalDateTime bookLoanDay) {
        this.bookLoanDay = bookLoanDay;
    }
}