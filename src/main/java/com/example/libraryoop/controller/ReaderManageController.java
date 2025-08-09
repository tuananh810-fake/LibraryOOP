package com.example.libraryoop.controller;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import com.example.libraryoop.service.ReaderManagementService;
import com.example.libraryoop.model.Reader;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.CheckBoxTableCell;

public class ReaderManageController {

    // Table + columns
    @FXML private TableView<Reader> tableReaders;
    @FXML private TableColumn<Reader, String> colId;
    @FXML private TableColumn<Reader, String> colName;
    @FXML private TableColumn<Reader, String> colAddress;
    @FXML private TableColumn<Reader, String> colEmail;
    @FXML private TableColumn<Reader, String> colPhone;
    @FXML private TableColumn<Reader, String> colExpiry;
    @FXML private TableColumn<Reader, Boolean> colLock;

    // Form fields
    @FXML private TextField txtId;
    @FXML private TextField txtName;
    @FXML private TextField txtAddress;
    @FXML private TextField txtEmail;
    @FXML private TextField txtPhone;
    @FXML private DatePicker dpExpiry;
    @FXML private CheckBox chkLock;

    // Buttons
    @FXML private Button btnAdd;
    @FXML private Button btnUpdate;
    @FXML private Button btnDelete;
    @FXML private Button btnClear;

    private final ReaderManagementService readerService = new ReaderManagementService();
    private final ObservableList<Reader> readerList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        // column bindings (model dùng getter getIdReader(), getNameReader()...)
        colId.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getIdReader()));
        colName.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getNameReader()));
        colAddress.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getAddressReader()));
        colEmail.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getEmailReader()));
        colPhone.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getPhoneNumber()));
        colExpiry.setCellValueFactory(cell -> {
            LocalDateTime exp = cell.getValue().getExpiry();
            return new SimpleStringProperty(exp == null ? "" : exp.toString());
        });
        colLock.setCellValueFactory(cell -> new SimpleBooleanProperty(cell.getValue().isLock()));
        colLock.setCellFactory(CheckBoxTableCell.forTableColumn(colLock));

        tableReaders.setItems(readerList);
        tableReaders.getSelectionModel().selectedItemProperty().addListener((obs, o, n) -> fillForm(n));

        // button handlers (cần fx:id hoặc có thể gán trong FXML via onAction)
        btnAdd.setOnAction(e -> handleAdd());
        btnUpdate.setOnAction(e -> handleUpdate());
        btnDelete.setOnAction(e -> handleDelete());
        btnClear.setOnAction(e -> handleClear());

        refreshTable();
    }

    private void fillForm(Reader r) {
        if (r == null) {
            handleClear();
            return;
        }
        txtId.setText(r.getIdReader());
        txtId.setDisable(true); // khóa id khi edit (model không có setIdReader)
        txtName.setText(r.getNameReader());
        txtAddress.setText(r.getAddressReader());
        txtEmail.setText(r.getEmailReader());
        txtPhone.setText(r.getPhoneNumber());
        dpExpiry.setValue(r.getExpiry() == null ? null : r.getExpiry().toLocalDate());
        chkLock.setSelected(r.isLock());
    }

    private void refreshTable() {
        try {
            // nếu service có getAllReaders()
            readerList.setAll(readerService.getAllReaders());
        } catch (NoSuchMethodError e) {
            // fallback: dùng findReaderByIdOrName("") nếu service chỉ có hàm đó
            readerList.setAll(readerService.findReaderByIdOrName(""));
        }
    }

    @FXML
    private void handleAdd() {
        if (txtId.getText().isBlank() || txtName.getText().isBlank()) {
            showAlert("Thiếu thông tin", "ID và Name là bắt buộc.");
            return;
        }
        if (!isValidEmail(txtEmail.getText().trim())) {
            showAlert("Lỗi", "Email không hợp lệ.");
            return;
        }
        LocalDateTime expiry = (dpExpiry == null || dpExpiry.getValue() == null)
                ? null
                : dpExpiry.getValue().atStartOfDay();
        Reader r = new Reader(
                txtId.getText().trim(),
                txtName.getText().trim(),
                txtAddress.getText().trim(),
                txtEmail.getText().trim(),
                txtPhone.getText().trim(),
                dpExpiry.getValue() == null ? null : dpExpiry.getValue().atStartOfDay(),
                chkLock.isSelected()
        );

        try {
            readerService.add(r);
        } catch (Exception ex) {
            showAlert("Lỗi", "Không thể thêm độc giả: " + ex.getMessage());
        }
        refreshTable();
        handleClear();
    }

    @FXML
    private void handleUpdate() {
        Reader sel = tableReaders.getSelectionModel().getSelectedItem();
        if (sel == null) {
            showAlert("Không có hàng được chọn", "Hãy chọn 1 độc giả để sửa.");
            return;
        }

        // dùng update(id, Map) để chỉ cập nhật những trường cần thiết
        Map<String, Object> changes = new HashMap<>();
        changes.put("nameReader", txtName.getText().trim());
        changes.put("addressReader", txtAddress.getText().trim());
        changes.put("emailReader", txtEmail.getText().trim());
        changes.put("phoneNumber", txtPhone.getText().trim());
        changes.put("expiry", dpExpiry.getValue() == null ? null : dpExpiry.getValue().atStartOfDay());
        // lưu lock qua key "lock"
        changes.put("lock", chkLock.isSelected());

        try {
            readerService.update(sel.getIdReader(), changes);
            refreshTable();
            handleClear();
        } catch (Exception ex) {
            showAlert("Lỗi cập nhật", ex.getMessage());
        }
    }

    @FXML
    private void handleDelete() {
        Reader sel = tableReaders.getSelectionModel().getSelectedItem();
        if (sel == null) {
            showAlert("Không có hàng được chọn", "Hãy chọn 1 độc giả để xóa.");
            return;
        }
        // Thêm alert xác nhận trước khi xóa
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Xác nhận xóa");
        confirm.setHeaderText(null);
        confirm.setContentText("Bạn có chắc chắn muốn xóa độc giả này?");
        if (confirm.showAndWait().orElse(null) != javafx.scene.control.ButtonType.OK) {
            return;
        }
        readerService.delete(sel.getIdReader());
        refreshTable();
        handleClear();
    }

    @FXML
    private void handleClear() {
        txtId.clear();
        txtName.clear();
        txtAddress.clear();
        txtEmail.clear();
        txtPhone.clear();
        dpExpiry.setValue(null);
        chkLock.setSelected(false);
        txtId.setDisable(false);
        tableReaders.getSelectionModel().clearSelection();
    }

    private void showAlert(String title, String content) {
        Alert a = new Alert(Alert.AlertType.INFORMATION);
        a.setTitle(title);
        a.setHeaderText(null);
        a.setContentText(content);
        a.showAndWait();
    }

    // Simple email validation method
    private boolean isValidEmail(String email) {
        return email != null && email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");
    }
}
