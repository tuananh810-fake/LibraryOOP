package com.example.libraryoop.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class LoginController {

    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private Label errorLabel;

    @FXML
    private void handleLogin() {
        String username = usernameField.getText();
        String password = passwordField.getText();

        // Giả lập tài khoản hợp lệ
        if (username.equals("admin") && password.equals("123")) {
            errorLabel.setVisible(false);
            System.out.println("Đăng nhập thành công!");
            // Load dashboard hoặc chuyển scene tại đây
        } else {
            errorLabel.setText("Sai tên đăng nhập hoặc mật khẩu.");
            errorLabel.setVisible(true);
        }
    }
}
