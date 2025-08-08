module com.example.libraryoop {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.libraryoop to javafx.fxml;
    exports com.example.libraryoop;
}