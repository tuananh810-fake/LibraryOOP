package com.example.libraryoop.model;

import java.time.LocalDateTime;

public class CallCardInfor {
    private static int counter = 0;
    private int order;
    private CallCard callCard; // Thông tin thẻ mượn sách
    private Book book;
    private LocalDateTime returnDeadline;// Ngày hết hạn trả sách
 
    /**
     * Default constructor.
     */
    public CallCardInfor() {
    }

    /**
     * Constructor with parameters.
     */
    public CallCardInfor(CallCard callCard, Book book, Reader reader, LocalDateTime returnDeadline) {
        counter++;
        this.order = counter;
        this.callCard = callCard;
        this.book = book;
        this.returnDeadline = returnDeadline;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public int getOrder() {
        return order;
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

    public LocalDateTime getReturnDeadline() {
        return returnDeadline;
    }

    public void setReturnDeadline(LocalDateTime returnDeadline) {
        this.returnDeadline = returnDeadline;
    }
}

