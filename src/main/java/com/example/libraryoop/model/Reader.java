package com.example.libraryoop.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class Reader {
    private String idReader;// Mã độc giả
    private String nameReader; // Tên độc giả
    private String addressReader;// Địa chỉ độc giả
    private String emailReader;// Email độc giả
    private String phoneNumber;// Số điện thoại độc giả
    private LocalDateTime expiry;// Ngày hết hạn thẻ mượn sách
    // lock = true -> KHÓA
    // lock = false -> MỞ
    private boolean lock;
    
    /**
     * Default constructor.
     */
    public Reader() {
    }

    /**
     * Constructor with parameters.
     */
    public Reader(String idReader, String nameReader, String addressReader, String emailReader, String phoneNumber, LocalDateTime expiry, boolean lock) {
        this.idReader = idReader;
        this.nameReader = nameReader;
        this.addressReader = addressReader;
        this.emailReader = emailReader;
        this.phoneNumber = phoneNumber;
        this.expiry = expiry;
        this.lock = lock;
    }

    /**
     * Getter and Setter
     */
    public String getIdReader() {
        return idReader;
    }

    public String getNameReader() {
        return nameReader;
    }

    public void setNameReader(String nameReader) {
        this.nameReader = nameReader;
    }

    public String getAddressReader() {
        return addressReader;
    }

    public void setAddressReader(String addressReader) {
        this.addressReader = addressReader;
    }

    public String getEmailReader() {
        return emailReader;
    }

    public void setEmailReader(String emailReader) {
        this.emailReader = emailReader;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public LocalDateTime getExpiry() {
        return expiry;
    }

    public void setExpiry(LocalDateTime expiry) {
        this.expiry = expiry;
    }

    public boolean isLock() {
        return lock;
    }

    public void setLock(boolean lock) {
        this.lock = lock;
    }
}
