package com.example.libraryoop.controller;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class MainController {

    @FXML
    private AnchorPane contentPane;

    @FXML
    public void initialize() {
        // Khi khởi động, tự load Dashboard
        loadPage("/com/example/libraryoop/dashboard.fxml");
    }

    @FXML
    private void onDashboard() {
        loadPage("/com/example/libraryoop/dashboard.fxml");
    }

    @FXML
    private void onLibraryManage() {
        loadPage("/com/example/libraryoop/library-manage.fxml");
    }

    @FXML
    private void onUserManage() {
        loadPage("/com/example/libraryoop/reader-manage.fxml");
    }

    @FXML
    private void onBorrowCardManage() {
        loadPage("/com/example/libraryoop/borrow-card-manage.fxml");
    }

    @FXML
    void onLogout(ActionEvent event) {
        // Hiện cảnh báo
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Xác nhận đăng xuất");
        alert.setHeaderText(null);
        alert.setContentText("Bạn có chắc chắn muốn đăng xuất?");
        if (alert.showAndWait().orElse(null) != ButtonType.OK) {
            return; // Nếu không chọn OK, thoát khỏi phương thức
        }

        // Xử lý sự kiện đăng xuất ở đây
        try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/libraryoop/login.fxml"));
                Parent root = loader.load();
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                stage.setScene(new Scene(root, 350, 500));
                stage.setTitle("Login!");
            } catch (Exception e) {
                e.printStackTrace();
            }

    }

    /** Load một file FXML con và đặt vào contentPane */
    private void loadPage(String fxmlFile) {
        try {
            // Load FXML con từ cùng package
            Node page = FXMLLoader.load(
                    getClass().getResource(fxmlFile)
            );
            // Thay thế toàn bộ children
            contentPane.getChildren().setAll(page);

            // Kéo full kích thước:
            AnchorPane.setTopAnchor(page, 0.0);
            AnchorPane.setBottomAnchor(page, 0.0);
            AnchorPane.setLeftAnchor(page, 0.0);
            AnchorPane.setRightAnchor(page, 0.0);

        } catch (IOException e) {
            e.printStackTrace();
            // Bạn có thể show Alert ở đây
        }
    }
}
