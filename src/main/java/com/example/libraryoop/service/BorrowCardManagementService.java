package com.example.libraryoop.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.libraryoop.file_handle.BorrowCardCSV;
import com.example.libraryoop.model.Book;
import com.example.libraryoop.model.BorrowCard;
import com.example.libraryoop.util.IdGenerator;

public class BorrowCardManagementService {
    private static final List<BorrowCard> borrowCardList = new ArrayList<>();
    private static final BookManagementService bookService = new BookManagementService(); // Làm cho bookService là static

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
            try {
                // Cập nhật số lượng sách bằng phương thức update với Map
                Map<String, Object> updates = new HashMap<>();
                updates.put("numberOfBooks", numberOfBooks - quantity);
                bookService.update(book.getIdBook(), updates);
            } catch (Exception e) {
                throw new IllegalArgumentException("Không thể cập nhật số lượng sách: " + e.getMessage());
            }
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

        // Tìm sách trong kho và cập nhật số lượng
        Book book = bookService.getBookById(selectedCard.getBook().getIdBook());
        if (book != null) {
            book.setNumberOfBooks(book.getNumberOfBooks() + selectedCard.getQuantity());
            bookService.updateBook(book);
        }

        // Mở khóa độc giả
        try {
            ReaderManagementService readerService = new ReaderManagementService();
            Map<String, Object> updates = new HashMap<>();
            updates.put("lock", false);
            readerService.update(selectedCard.getReader().getIdReader(), updates);
        } catch (Exception e) {
            System.err.println("Không thể mở khóa độc giả: " + e.getMessage());
        }

        // Xóa phiếu mượn
        borrowCardList.remove(findIndexById(selectedCard.getIdBorrowCard()));
        BorrowCardCSV.writeBorrowCardListToFile(borrowCardList);
    }

    /**
     * Kiểm tra xem độc giả có phiếu mượn đang hoạt động không
     */
    public boolean hasActiveBorrowCard(String readerId) {
        return borrowCardList.stream()
                .anyMatch(card -> card.getReader().getIdReader().equals(readerId));
    }
}
