# The Library Management Example Project

## Introduction

This project is a sample implementation of a library management system. It includes management methods for books, members, and loans. The system allows users to add, remove, and update books, members, borrowing and returning status.

The system is designed to be simple and easy to understand, making it a great starting point for learning about object-oriented.

## The Structures

The project structures includes five packages, including raw data structures and logic for working wwith library's data.

The data is stored in different files, whose columns are separated using different delimiters. Since the overall target is for object oriented programming instead of other elements such as data structures, the data is stored in simple text files.

There are different logics for working with data, for example: reading data from files, writing data to files, searching data in files. Specifically, the project includes the following structures:

- com.example.libraryoop.model: This package contains the classes that represent the data in the library, including Book, Reader, Staff, and BorrowCard
- com.example.libraryoop.util: includes the ID generator that can be used as the common ID structure throughout the program.
- com.example.libraryoop.service: This package contains the classes that implement the business logic of the library, such as adding, editing, or removing.
- com.example.libraryoop.validate: This package contains the classes that implement the validation of reader's personal data format, including phong number and email.
- com.example.libraryoop.controller: This package contains the classes that implement the user interface of the library.

Besides the packages with different usages, there are two classes that are outside these packages, including Main and Launcher.

- Main class is the class to call the javafx interface by calling different fxml files.
- Launcher is the main runner of the program since it includes the main void that call the Main class to render the GUI of a completed program.

## The Interface

