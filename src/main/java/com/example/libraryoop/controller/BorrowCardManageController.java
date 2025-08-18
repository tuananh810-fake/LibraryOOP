package com.example.libraryoop.controller;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

import com.example.libraryoop.model.BorrowCard;
import com.example.libraryoop.service.BorrowCardManagementService;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

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

    @FXML
    private TextField txtSearch;

    @FXML
    private Button btnAdd;

    @FXML
    private Button btnReturn;

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
                return new SimpleStringProperty(cellData.getValue().getBook().getNameBook());
            }
            return new SimpleStringProperty("");
        });
        colReaderId.setCellValueFactory(cellData -> {
            if (cellData.getValue().getReader() != null) {
                return new SimpleStringProperty(cellData.getValue().getReader().getIdReader());
            }
            return new SimpleStringProperty("");
        });
        colQuantity.setCellValueFactory(cellData ->
                new SimpleIntegerProperty(cellData.getValue().getQuantity()).asObject()
        );
        colLoanDay.setCellValueFactory(cellData -> {
            if (cellData.getValue().getBookLoanDay() != null) {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                return new SimpleStringProperty(cellData.getValue().getBookLoanDay().format(formatter));
            }
            return new SimpleStringProperty("");
        });

        // Thiết lập tìm kiếm
        setupSearch();

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
        tableBorrowCard.setItems(FXCollections.observableArrayList(service.getAllBorrowCards()));
    }

    private void setupSearch() {
        // Tìm kiếm khi gõ
        txtSearch.textProperty().addListener((_, _, _) -> performSearch());
    }

    private void performSearch() {
        String searchText = txtSearch.getText().trim().toLowerCase();

        if (searchText.isEmpty()) {
            loadBorrowCards();
            return;
        }

        List<BorrowCard> allCards = service.getAllBorrowCards();
        List<BorrowCard> searchResults = allCards.stream()
                .filter(card ->
                        (card.getIdBorrowCard() != null &&
                                card.getIdBorrowCard().toLowerCase().contains(searchText)) ||
                                (card.getBook() != null &&
                                        card.getBook().getNameBook() != null &&
                                        card.getBook().getNameBook().toLowerCase().contains(searchText)) ||
                                (card.getReader() != null &&
                                        card.getReader().getIdReader() != null &&
                                        card.getReader().getIdReader().toLowerCase().contains(searchText)))
                .collect(Collectors.toList());

        tableBorrowCard.setItems(FXCollections.observableArrayList(searchResults));

        if (searchResults.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Kết quả tìm kiếm");
            alert.setHeaderText(null);
            alert.setContentText("Không tìm thấy phiếu mượn nào phù hợp với từ khóa: " + searchText);
            alert.show();
        }
    }
}
