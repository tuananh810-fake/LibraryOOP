package com.example.libraryoop.model;

import java.time.LocalDateTime;

public class BorrowCard {
    private String idBorrowCard;      // id thẻ mượn sách
    private Book book;             // Thông tin sách mượn
    private Reader reader;         // Thông tin người mượn
    private int quantity;          // Số lượng mượn
    private LocalDateTime bookLoanDay;  // Ngày mượn sách

    public BorrowCard(String idBorrowCard, Book book, Reader reader, int quantity, LocalDateTime bookLoanDay) {
        this.idBorrowCard = idBorrowCard;
        this.book = book;
        this.reader = reader;
        this.quantity = quantity;
        this.bookLoanDay = bookLoanDay;
    }

    // Getters and Setters
    public String getIdBorrowCard() {
        return idBorrowCard;
    }

    public void setIdBorrowCard(String idBorrowCard) {
        this.idBorrowCard = idBorrowCard;
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