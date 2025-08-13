package com.example.libraryoop.service;

import java.util.ArrayList;
import java.util.List;

import com.example.libraryoop.file_handle.BorrowCardCSV;
import com.example.libraryoop.model.Book;
import com.example.libraryoop.model.BorrowCard;
import com.example.libraryoop.util.IdGenerator;

public class BorrowCardManagementService {
    private static final List<BorrowCard> borrowCardList = new ArrayList<>();
    private final BookManagementService bookService = new BookManagementService();

    public BorrowCardManagementService() {
        BorrowCardCSV.readFile(borrowCardList);
    }

    public void addBorrowCard(BorrowCard borrowCard) {
        if (borrowCard == null) {
            throw new IllegalArgumentException("Illegal Argument");
        }

        int quantity = borrowCard.getQuantity();
        Book book = bookService.getBookById(borrowCard.getBook().getIdBook());
        if (book != null) {
            int numberOfBooks = book.getNumberOfBooks();
            if (numberOfBooks < quantity) {
                throw new IllegalArgumentException("Không đủ sách để mượn");
            }
            book.setNumberOfBooks(numberOfBooks - quantity);
            bookService.delete(book.getIdBook()); // Xóa cũ
            bookService.addBook(book); // Thêm lại với số lượng mới
        }
        borrowCardList.add(borrowCard);
        BorrowCardCSV.writeBorrowCardListToFile(borrowCardList);
    }

    public void addNewBorrowCard(BorrowCard borrowCard) {
        if (borrowCard == null) {
            throw new IllegalArgumentException("Illegal Argument");
        }

        String newID;
        do {
            newID = IdGenerator.generateId("C");
        } while (getBorrowCardById(newID) != null);

        borrowCard.setIdBorrowCard(newID);
        addBorrowCard(borrowCard);
    }

    public void deleteByIdBorrowCard(String id) {
        int idx = findIndexById(id);
        if (idx == -1) {
            throw new IllegalArgumentException("Không tìm thấy phiếu mượn " + id);
        }
        BorrowCard borrowCard = borrowCardList.get(idx);
        Book book = bookService.getBookById(borrowCard.getBook().getIdBook());
        if (book != null) {
            book.setNumberOfBooks(book.getNumberOfBooks() + borrowCard.getQuantity());
            bookService.delete(book.getIdBook());
            bookService.addBook(book);
        }
        borrowCardList.remove(idx);
        BorrowCardCSV.writeBorrowCardListToFile(borrowCardList);
    }

    public int findIndexById(String id) {
        for (int i = 0; i < borrowCardList.size(); i++) {
            if (borrowCardList.get(i).getIdBorrowCard().equals(id)) {
                return i;
            }
        }
        return -1;
    }

    public BorrowCard getBorrowCardById(String id) {
        int index = findIndexById(id);
        return index != -1 ? borrowCardList.get(index) : null;
    }

    // Lấy tất cả Borrow card
    public List<BorrowCard> getAllBorrowCards() {
        return new ArrayList<>(borrowCardList);
    }

    public void returnBook(BorrowCard selectedCard) {
        if (selectedCard == null) {
            throw new IllegalArgumentException("Phiếu mượn không thể null");
        }

        // Tăng số lượng sách trong kho
        Book book = bookService.getBookById(selectedCard.getBook().getIdBook());
        if (book != null) {
            book.setNumberOfBooks(book.getNumberOfBooks() + selectedCard.getQuantity());
            bookService.delete(book.getIdBook()); // Xóa cũ
            bookService.addBook(book); // Thêm lại với số lượng mới
        }

        // Xóa phiếu mượn
        deleteByIdBorrowCard(selectedCard.getIdBorrowCard());
    }
}
