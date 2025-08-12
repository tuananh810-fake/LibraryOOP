package com.example.libraryoop.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Iterator;

import com.example.libraryoop.model.BorrowCard;
import com.example.libraryoop.file_handle.BorrowCardCSV;
import com.example.libraryoop.util.IdGenerator;

public class CallCardManagementService {
    private static List<BorrowCard> borrowCardList = new ArrayList<>();

    public void addCallCard(BorrowCard borrowCard) {
        if (borrowCard == null) {
            throw new IllegalArgumentException("Illegal Argument");
        } 

        int quantity = borrowCard.getQuantity();
        int numberOfBooks = boorrowCard.;

        borrowCard.setQuantity(quantity);
        borrowCardList.add(borrowCard);
        BorrowCardCSV.writeCallCardListToFile(borrowCardList);
    }

    public void addNewCallCard(BorrowCard borrowCard) {
        if (borrowCard == null) {
            throw new IllegalArgumentException("Illegal Argument");
        }

        String newID;
        
        do {
            newID = IdGenerator.generateId("C");
        } while (getBorrowCardById(newID) != null);

        borrowCard.setIdCallCard(newID);

        addCallCard(borrowCard);
    }

    public void deleteByIdCallCard(String id) {
        if (findIndexById(id) == -1) {
            throw new IllegalArgumentException();
        }

        Iterator<BorrowCard> iterator = borrowCardList.iterator();

        while (iterator.hasNext()) {
            BorrowCard borrowCard = iterator.next();

            if (borrowCard.getIdCallCard().equals(id)) {
                if (borrowCard.getBook() == null) {
                    throw new IllegalArgumentException("The muon khong co thong tin sach" + id);
                }

                int quantity = boorrowCard.getQuantity();
                int numberOfBooks = boorrowCard.getBook().getNumberOfBooks();
                numberOfBooks -= quantity;
                boorrowCard.getBook().setNumberOfBooks();
                
                iterator.remove();

                return;
            }
        }

        System.err.println("Không tìm thấy");
    }



    public int findIndexById(String id) {
        for (int i = 0; i < borrowCardList.size(); i++) {
            if (borrowCardList.get(i).getIdCallCard().equals(id)) {
                return i;
            }
        }
        return -1;
    }

    public BorrowCard getBorrowCardById(String id) {
        int index = findIndexById(id);
        return index != -1 ? borrowCardList.get(index) : null;
    }
}
