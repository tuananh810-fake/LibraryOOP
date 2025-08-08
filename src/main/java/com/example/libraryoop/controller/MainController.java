package com.example.libraryoop.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;

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
    private void onManagerInfo() {
        loadPage("/com/example/libraryoop/manager-info.fxml");
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
