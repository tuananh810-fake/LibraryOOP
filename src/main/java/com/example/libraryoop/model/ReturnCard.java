package com.example.libraryoop.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class ReturnCard {
    private CallCard callCard; // thẻ mượn sách
    private Book book;  // sách được trả
    private Staff staff;  // nhân viên quản lý
    private LocalDateTime returnDate;  // ngày trả sách

    /**
     * Default constructor.
     */
    public ReturnCard() {
    }

    /**
     * Constructor with parameters.
     */
    public ReturnCard(CallCard callCard, Book book, Staff staff, LocalDateTime returnDate) {
        this.callCard = callCard;
        this.book = book;
        this.staff = staff;
        this.returnDate = returnDate;
    }

    /**
     * Getter and Setter.
     */
    public CallCard getCallCard() {
        return callCard;
    }

    public void setCallCard(CallCard callCard) {
        this.callCard = callCard;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public Staff getStaff() {
        return staff;
    }

    public void setStaff(Staff staff) {
        this.staff = staff;
    }

    public LocalDateTime getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(LocalDateTime returnDate) {
        this.returnDate = returnDate;
    }
}

