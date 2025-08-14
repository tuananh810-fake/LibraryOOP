module com.example.libraryoop {
    requires javafx.controls;
    requires javafx.fxml;
    requires transitive javafx.base;
    requires javafx.graphics;


    opens com.example.libraryoop to javafx.fxml;
    exports com.example.libraryoop;

    opens com.example.libraryoop.controller to javafx.fxml;
    exports com.example.libraryoop.controller;
    
    opens com.example.libraryoop.model to javafx.base;
    exports com.example.libraryoop.model;
}