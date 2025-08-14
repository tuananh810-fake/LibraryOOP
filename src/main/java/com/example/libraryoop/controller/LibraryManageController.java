package com.example.libraryoop.controller;

import java.time.Year;
import java.util.HashMap;
import java.util.Map;

import com.example.libraryoop.model.Book;
import com.example.libraryoop.service.BookManagementService;
import com.example.libraryoop.util.IdGenerator;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

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
    @FXML private Button btnBorrowBook;
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
        btnBorrowBook.setOnAction(e -> handleBorrowBook());
        btnAddBook.setOnAction(e -> handleAddBook());
        btnUpdateBook.setOnAction(e -> handleUpdateBook());
        btnDeleteBook.setOnAction(e -> handleDeleteBook());
        generateAndShowNewId();
        btnClearBook.setOnAction(e -> {clearFormBook();
            generateAndShowNewId();
        });
        txtId.setDisable(true);

        refreshTableBook();
    }

    private void fillForm(Book b) {
        if (b == null) { clearFormBook(); return; }
        txtId.setDisable(true); // khóa id khi edit (model không có setIdReader)
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
        if ( txtName.getText().isBlank()) {
            showAlert("Thiếu thông tin", "Tên sách là bắt buộc.");
            return;
        }
        // Validate Year
        Year year = parseYearOrNull(txtYear.getText().trim());
        if (year == null && !txtYear.getText().trim().isEmpty()) {
            showAlert("Lỗi", "Năm xuất bản không hợp lệ. Vui lòng nhập số nguyên.");
            return;
        }

        // Validate Number of Books
        int number = parseIntOrZero(txtNumber.getText().trim());
        if (number == 0 && !txtNumber.getText().trim().isEmpty()) {
            showAlert("Lỗi", "Số lượng sách không hợp lệ. Vui lòng nhập số nguyên dương.");
            return;
        }

        Book b = new Book(
                txtId.getText(),  // sử dụng id được tạo sẵn
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
        // Thêm alert xác nhận trước khi xóa
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Xác nhận xóa");
        confirm.setHeaderText(null);
        confirm.setContentText("Bạn có chắc chắn muốn xóa cuốn sách này?");
        if (confirm.showAndWait().orElse(null) != javafx.scene.control.ButtonType.OK) {
            return;
        }
        bookService.delete(sel.getIdBook());
        refreshTableBook();
        clearFormBook();
    }

    private void handleBorrowBook() {
        Book selectedBook = tableBooks.getSelectionModel().getSelectedItem();
        if (selectedBook == null) {
            showAlert("Thông báo", "Vui lòng chọn sách trước khi mượn!");
            return;
        }
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/libraryoop/borrow-card.fxml"));
            Parent root = loader.load();
            BorrowCardController controller = loader.getController();
            controller.setBook(selectedBook); // Truyền sách sang borrow card
            Stage dialog = new Stage();
            dialog.setTitle("Mượn sách");
            dialog.setScene(new Scene(root));
            dialog.initModality(javafx.stage.Modality.APPLICATION_MODAL);
            dialog.showAndWait();
            refreshTableBook(); // Làm mới bảng sách sau khi mượn
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void clearFormBook() {
        txtId.clear();
        txtName.clear();
        txtAuthor.clear();
        txtCategory.clear();
        txtPublisher.clear();
        txtYear.clear();
        txtNumber.clear();
        // txtId.setDisable(false); id luôn bị khóa
        generateAndShowNewId(); // Tạo ID mới khi clear form
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

    private void generateAndShowNewId() {
        String newId;
        do {
            newId = IdGenerator.generateId("B"); // Use "B" prefix for books
        } while (bookService.getBookById(newId) != null);  // Assuming you have getBookById in your service

        txtId.setText(newId);
    }
}
