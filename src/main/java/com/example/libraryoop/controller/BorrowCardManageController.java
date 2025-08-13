package com.example.libraryoop.controller;

import java.time.format.DateTimeFormatter;

import com.example.libraryoop.model.BorrowCard;
import com.example.libraryoop.service.BorrowCardManagementService;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

public class BorrowCardManageController {

    @FXML
    private TableView<BorrowCard> tableBorrowCard;

    @FXML
    private TableColumn<BorrowCard, String> colBorrowId;

    @FXML
    private TableColumn<BorrowCard, String> colTitle;

    @FXML
    private TableColumn<BorrowCard, String> colReaderId;

    @FXML
    private TableColumn<BorrowCard, Integer> colQuantity;

    @FXML
    private TableColumn<BorrowCard, String> colLoanDay;

    private BorrowCardManagementService service;

    @FXML
    public void initialize() {
        service = new BorrowCardManagementService();

        // Gán giá trị cho các cột
        colBorrowId.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getIdBorrowCard())
        );
        colTitle.setCellValueFactory(cellData -> {
            if (cellData.getValue().getBook() != null) {
                return javafx.beans.property.SimpleStringProperty.stringExpression(
                        javafx.beans.binding.Bindings.createStringBinding(
                                () -> cellData.getValue().getBook().getNameBook()
                        )
                );
            }
            return javafx.beans.property.SimpleStringProperty.stringExpression(
                    javafx.beans.binding.Bindings.createStringBinding(() -> "")
            );
        });
        colReaderId.setCellValueFactory(cellData -> {
            if (cellData.getValue().getReader() != null) {
                return javafx.beans.property.SimpleStringProperty.stringExpression(
                        javafx.beans.binding.Bindings.createStringBinding(
                                () -> cellData.getValue().getReader().getIdReader()
                        )
                );
            }
            return javafx.beans.property.SimpleStringProperty.stringExpression(
                    javafx.beans.binding.Bindings.createStringBinding(() -> "")
            );
        });
        colQuantity.setCellValueFactory(cellData ->
                new SimpleIntegerProperty(cellData.getValue().getQuantity()).asObject()
        );
        colLoanDay.setCellValueFactory(cellData -> {
            if (cellData.getValue().getBookLoanDay() != null) {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                return javafx.beans.property.SimpleStringProperty.stringExpression(
                        javafx.beans.binding.Bindings.createStringBinding(
                                () -> cellData.getValue().getBookLoanDay().format(formatter)
                        )
                );
            }
            return javafx.beans.property.SimpleStringProperty.stringExpression(
                    javafx.beans.binding.Bindings.createStringBinding(() -> "")
            );
        });

        loadBorrowCards();
    }

    @FXML
    public void handleOnReturn(ActionEvent event) {
        BorrowCard selectedCard = tableBorrowCard.getSelectionModel().getSelectedItem();
        if (selectedCard == null) {
            // Hiển thị thông báo nếu không có thẻ mượn nào được chọn
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Cảnh báo");
            alert.setHeaderText(null);
            alert.setContentText("Vui lòng chọn sách trước khi trả.");
            alert.showAndWait();
            return;
        }

        // Thêm alert xác nhận trước khi xóa
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Xác nhận trả");
        confirm.setHeaderText(null);
        confirm.setContentText("Bạn có chắc chắn muốn trả cuốn sách này?");
        if (confirm.showAndWait().orElse(null) != javafx.scene.control.ButtonType.OK) {
            return;
        }

        // Xử lý trả sách
        service.returnBook(selectedCard);
        loadBorrowCards(); // Làm mới danh sách thẻ mượn sau khi trả sách
    }

    private void loadBorrowCards() {
        ObservableList<BorrowCard> borrowCards =
                FXCollections.observableArrayList(service.getAllBorrowCards());
        tableBorrowCard.setItems(borrowCards);
    }
}
