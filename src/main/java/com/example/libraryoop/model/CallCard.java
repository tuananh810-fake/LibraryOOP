package com.example.libraryoop.model;

import java.time.LocalDateTime;

public class CallCard {
    private String idCallCard; // id thẻ mượn sách
    private Reader reader;  // Thông tin người mượn sách
    // private Staff staff;  // Nhân viên quản lý thẻ mượn sách
    private LocalDateTime bookLoanDay;  // Ngày mượn sách

    /**
     * Default constructor.
     */
    public CallCard() {
    }

    /**
     * Constructor with parameters.
     */
    public CallCard(String idCallCard, Reader reader2, LocalDateTime bookLoanDay) {
        this.idCallCard = idCallCard;
        this.reader = reader2;
        // this.staff = staff;
        this.bookLoanDay = bookLoanDay;
    }


    /**
     * Getter and Setter.
     */
    public String getIdCallCard() {
        return idCallCard;
    }

    public void setIdCallCard(String idCallCard) {
        this.idCallCard = idCallCard;
    }

    public Reader getReader() {
        return reader;
    }

    public void setReader(Reader reader) {
        this.reader = reader;
    }

    // public Staff getStaff() {
    //     return staff;
    // }

    // public void setStaff(Staff staff) {
    //     this.staff = staff;
    // }

    public LocalDateTime getBookLoanDay() {
        return bookLoanDay;
    }

    public void setBookLoanDay(LocalDateTime bookLoanDay) {
        this.bookLoanDay = bookLoanDay;
    }
}

