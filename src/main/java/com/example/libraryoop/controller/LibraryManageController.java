package com.example.libraryoop.controller;

import com.example.libraryoop.service.BookManagementService;
import com.example.libraryoop.model.Book;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.time.Year;
import java.util.HashMap;
import java.util.Map;

public class LibraryManageController {

    // Table + columns
    @FXML private TableView<Book> tableBooks;
    @FXML private TableColumn<Book, String> colId;
    @FXML private TableColumn<Book, String> colName;
    @FXML private TableColumn<Book, String> colAuthor;
    @FXML private TableColumn<Book, String> colCategory;
    @FXML private TableColumn<Book, String> colPublisher;
    @FXML private TableColumn<Book, String> colYear;
    @FXML private TableColumn<Book, String> colNumber;

    // Form fields
    @FXML private TextField txtId;
    @FXML private TextField txtName;
    @FXML private TextField txtAuthor;
    @FXML private TextField txtCategory;
    @FXML private TextField txtPublisher;
    @FXML private TextField txtYear;    // nhập như "2023"
    @FXML private TextField txtNumber;  // số lượng sách

    // Buttons
    @FXML private Button btnAddBook;
    @FXML private Button btnUpdateBook;
    @FXML private Button btnDeleteBook;
    @FXML private Button btnClearBook;

    private final BookManagementService bookService = new BookManagementService();
    private final ObservableList<Book> bookList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        // bind columns -> model uses getters: getIdBook(), getNameBook()...
        colId.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getIdBook()));
        colName.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getNameBook()));
        colAuthor.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getAuthor()));
        colCategory.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getCategory()));
        colPublisher.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getPublishingCompany()));
        colYear.setCellValueFactory(c -> new SimpleStringProperty(
                c.getValue().getPublishingYear() == null ? "" : String.valueOf(c.getValue().getPublishingYear().getValue())
        ));
        colNumber.setCellValueFactory(c -> new SimpleStringProperty(String.valueOf(c.getValue().getNumberOfBooks())));

        tableBooks.setItems(bookList);
        tableBooks.getSelectionModel().selectedItemProperty().addListener((obs, o, n) -> fillForm(n));

        // handlers (requires fx:id in FXML)
        btnAddBook.setOnAction(e -> handleAddBook());
        btnUpdateBook.setOnAction(e -> handleUpdateBook());
        btnDeleteBook.setOnAction(e -> handleDeleteBook());
        btnClearBook.setOnAction(e -> clearFormBook());

        refreshTableBook();
    }

    private void fillForm(Book b) {
        if (b == null) { clearFormBook(); return; }
        txtId.setText(b.getIdBook()); txtId.setDisable(true);
        txtName.setText(b.getNameBook());
        txtAuthor.setText(b.getAuthor());
        txtCategory.setText(b.getCategory());
        txtPublisher.setText(b.getPublishingCompany());
        txtYear.setText(b.getPublishingYear() == null ? "" : String.valueOf(b.getPublishingYear().getValue()));
        txtNumber.setText(String.valueOf(b.getNumberOfBooks()));
    }

    private void refreshTableBook() {
        bookList.setAll(bookService.getAllBooks());
    }

    private void handleAddBook() {
        if (txtId.getText().isBlank() || txtName.getText().isBlank()) {
            showAlert("Thiếu thông tin", "ID và Tên sách là bắt buộc.");
            return;
        }
        Year year = parseYearOrNull(txtYear.getText().trim());
        int number = parseIntOrZero(txtNumber.getText().trim());

        Book b = new Book(
                txtId.getText().trim(),
                txtName.getText().trim(),
                txtAuthor.getText().trim(),
                txtCategory.getText().trim(),
                txtPublisher.getText().trim(),
                year,
                number
        );

        bookService.addBook(b);     // hoặc bookService.add(b) tuỳ service của bạn
        refreshTableBook();
        clearFormBook();
    }

    private void handleUpdateBook() {
        Book sel = tableBooks.getSelectionModel().getSelectedItem();
        if (sel == null) { showAlert("Không có hàng được chọn", "Chọn 1 cuốn sách để sửa."); return; }

        Map<String, Object> changes = new HashMap<>();
        changes.put("nameBook", txtName.getText().trim());
        changes.put("author", txtAuthor.getText().trim());
        changes.put("category", txtCategory.getText().trim());
        changes.put("publishingCompany", txtPublisher.getText().trim());
        changes.put("publishingYear", parseYearOrNull(txtYear.getText().trim()));
        changes.put("numberOfBooks", parseIntOrZero(txtNumber.getText().trim()));

        try {
            bookService.update(sel.getIdBook(), changes); // service phải chấp nhận các key trên
            refreshTableBook();
            clearFormBook();
        } catch (Exception ex) {
            showAlert("Lỗi cập nhật", ex.getMessage());
        }
    }

    private void handleDeleteBook() {
        Book sel = tableBooks.getSelectionModel().getSelectedItem();
        if (sel == null) { showAlert("Không có hàng được chọn", "Chọn 1 cuốn sách để xóa."); return; }
        bookService.delete(sel.getIdBook());
        refreshTableBook();
        clearFormBook();
    }

    private void clearFormBook() {
        txtId.clear(); txtName.clear(); txtAuthor.clear(); txtCategory.clear(); txtPublisher.clear(); txtYear.clear(); txtNumber.clear();
        txtId.setDisable(false);
        tableBooks.getSelectionModel().clearSelection();
    }

    private Year parseYearOrNull(String s) {
        if (s == null || s.isBlank()) return null;
        try { return Year.of(Integer.parseInt(s)); }
        catch (NumberFormatException e) { return null; }
    }

    private int parseIntOrZero(String s) {
        if (s == null || s.isBlank()) return 0;
        try { return Integer.parseInt(s); } catch (NumberFormatException e) { return 0; }
    }

    private void showAlert(String title, String content) {
        Alert a = new Alert(Alert.AlertType.INFORMATION);
        a.setTitle(title);
        a.setHeaderText(null);
        a.setContentText(content);
        a.showAndWait();
    }
}
