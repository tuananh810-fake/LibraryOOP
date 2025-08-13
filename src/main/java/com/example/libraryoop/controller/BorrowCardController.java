package com.example.libraryoop.controller;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import com.example.libraryoop.model.Book;
import com.example.libraryoop.model.BorrowCard;
import com.example.libraryoop.model.Reader;
import com.example.libraryoop.service.BookManagementService;
import com.example.libraryoop.service.BorrowCardManagementService;
import com.example.libraryoop.service.ReaderManagementService;
import com.example.libraryoop.util.IdGenerator;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class BorrowCardController {

    @FXML
    private Button btnBorrow;

    @FXML
    private javafx.scene.control.TextField txtTitle;

    @FXML
    private javafx.scene.control.TextField txtAuthor;

    @FXML
    private javafx.scene.control.TextField txtYear;

    @FXML
    private javafx.scene.control.TextField txtBorrowCode;

    @FXML
    private javafx.scene.control.ComboBox<String> comboReaderId;

    @FXML
    private javafx.scene.control.TextField txtQuantity;

    @FXML
    private javafx.scene.control.DatePicker dpBorrowDate;

    private Book selectedBook;
    private List<Reader> validReaders; // lưu lại danh sách để tra cứu khi mượn

    public void setBook(Book book) {
        this.selectedBook = book;
        txtTitle.setText(book.getNameBook());
        txtTitle.setDisable(true);

        txtAuthor.setText(book.getAuthor());
        txtAuthor.setDisable(true);

        txtYear.setText(book.getPublishingYear() != null ? 
            String.valueOf(book.getPublishingYear().getValue()) : "");
        txtYear.setDisable(true);

        txtBorrowCode.setText(IdGenerator.generateId("C"));
        txtBorrowCode.setDisable(true);

        // Lấy danh sách độc giả hợp lệ
        ReaderManagementService readerService = new ReaderManagementService();
        validReaders = readerService.getAllValidReaders();

        // Lưu danh sách độc giả hợp lệ vào ComboBox
        comboReaderId.setItems(FXCollections.observableArrayList(
            validReaders.stream().map(Reader::getIdReader).collect(Collectors.toList())
        ));
    }

    @FXML
    void handleBorrow(ActionEvent event) {
        String readerId = comboReaderId.getValue();
        if (readerId == null) {
            showAlert("Lỗi", "Vui lòng chọn độc giả!");
            return;
        }

        int quantity;
        try {
            quantity = Integer.parseInt(txtQuantity.getText());
        } catch (NumberFormatException e) {
            showAlert("Lỗi", "Số lượng mượn không hợp lệ!");
            return;
        }
        if (quantity <= 0 || quantity > selectedBook.getNumberOfBooks()) {
            showAlert("Lỗi", "Số lượng mượn phải lớn hơn 0 và không vượt quá số lượng sách còn lại!");
            return;
        }

        LocalDateTime loanDay = dpBorrowDate.getValue() != null ? 
            dpBorrowDate.getValue().atStartOfDay() : null;
        if (loanDay == null) {
            showAlert("Lỗi", "Vui lòng chọn ngày mượn!");
            return;
        }

        // Tìm Reader từ danh sách validReaders
        Reader reader = validReaders.stream()
                .filter(r -> r.getIdReader().equals(readerId))
                .findFirst()
                .orElse(null);

        if (reader == null || reader.isLock()) {
            showAlert("Lỗi", "Mã độc giả không hợp lệ hoặc đã bị khóa!");
            return;
        }

        String borrowCode = txtBorrowCode.getText();
        BorrowCard card = new BorrowCard(borrowCode, selectedBook, reader, quantity, loanDay);

        BorrowCardManagementService service = new BorrowCardManagementService();
        service.addNewBorrowCard(card);

        // Cập nhật số lượng sách trong BookManagementService
        BookManagementService bookService = new BookManagementService();
        bookService.updateBookQuantity(selectedBook, -quantity);

        showAlert("Thông báo", "Mượn sách thành công!");
        ((Stage) btnBorrow.getScene().getWindow()).close();
    }

    private void showAlert(String title, String content) {
        Alert a = new Alert(Alert.AlertType.INFORMATION);
        a.setTitle(title);
        a.setHeaderText(null);
        a.setContentText(content);
        a.showAndWait();
    }
}