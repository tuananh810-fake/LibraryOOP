package com.example.libraryoop.controller;

import com.example.libraryoop.model.Staff;
import com.example.libraryoop.service.StaffManagementService;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class LoginController {

    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private TextField passwordVisibleField;
    @FXML private CheckBox showPasswordCheck;
    @FXML private Label errorLabel;

    private final StaffManagementService staffService = new StaffManagementService();

    @FXML
    private void initialize() {
        // Ẩn trường password thường khi chưa chọn
        passwordVisibleField.setManaged(false);
        passwordVisibleField.setVisible(false);

        // Đồng bộ dữ liệu giữa hai trường
        passwordVisibleField.textProperty().bindBidirectional(passwordField.textProperty());

        showPasswordCheck.selectedProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal) {
                passwordVisibleField.setManaged(true);
                passwordVisibleField.setVisible(true);
                passwordField.setManaged(false);
                passwordField.setVisible(false);
            } else {
                passwordVisibleField.setManaged(false);
                passwordVisibleField.setVisible(false);
                passwordField.setManaged(true);
                passwordField.setVisible(true);
            }
        });
    }

    @FXML
    private void handleLogin() {
        String username = usernameField.getText();
        String password = showPasswordCheck.isSelected() ? passwordVisibleField.getText() : passwordField.getText();

        boolean found = false;
        for (Staff staff : staffService.getAllStaff()) {
            if (staff.getUsername().equals(username) && staff.getPassWord().equals(password)) {
                found = true;
                break;
            }
        }

        if (found) {
            errorLabel.setVisible(false);
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/libraryoop/main.fxml"));
                Parent root = loader.load();
                Stage stage = (Stage) usernameField.getScene().getWindow();
                stage.setScene(new Scene(root, 900, 600));
                stage.setTitle("Library Management");
            } catch (Exception e) {
                errorLabel.setText("Không thể chuyển trang: " + e.getMessage());
                errorLabel.setVisible(true);
            }
        } else {
            errorLabel.setText("Sai tên đăng nhập hoặc mật khẩu.");
            errorLabel.setVisible(true);
        }
    }
}
