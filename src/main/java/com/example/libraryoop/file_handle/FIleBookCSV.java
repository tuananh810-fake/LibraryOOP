package com.example.libraryoop.file_handle;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.Year;
import java.util.ArrayList;
import java.util.List;

import com.example.libraryoop.model.Book;


public class FileBookCSV {

    private static final String CSV_FILE_PATH = "src/main/java/com/example/libraryoop/database/BookData.csv";

    public static List<Book> readBooksFromCSV() {
        List<Book> books = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(CSV_FILE_PATH))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] fields = line.split(",");
                if (fields.length == 7) {
                    String idBook = fields[0];
                    String nameBook = fields[1];
                    String author = fields[2];
                    String category = fields[3];
                    String publishingCompany = fields[4];
                    Year publishingYear = Year.parse(fields[5]);
                    int numberOfBooks = Integer.parseInt(fields[6]);
                    Book book = new Book(idBook, nameBook, author, category, publishingCompany, publishingYear, numberOfBooks);
                    books.add(book);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return books;
    }

    public static void writeBookToCSV(Book book) {
        try (java.io.FileWriter writer = new java.io.FileWriter(CSV_FILE_PATH, true)) {
            writer.append(book.toCSV());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void overwriteBooksToCSV(List<Book> books) {
        try (java.io.FileWriter writer = new java.io.FileWriter(CSV_FILE_PATH, false)) {
            for (Book book : books) {
                writer.write(book.toCSV());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
