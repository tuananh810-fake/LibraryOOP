package com.example.libraryoop.service;

import java.util.ArrayList;
import java.util.List;
import com.example.libraryoop.model.CallCard;
import com.example.libraryoop.file_handle.CallCardCSV;

public class CallCardManagementService {
    private static List<CallCard> callCardList = new ArrayList<>();

    public void addCallCard(CallCard callCard) {
        if (callCard == null) {
            throw new IllegalArgumentException("Illegal Argument");
        } 

        callCardList.add(callCard);
        CallCardCSV.writeCallCardListToFile(callCardList);
    }

    public void addNewCallCard(CallCard callCard) {
        if (callCard == null) {
            throw new IllegalArgumentException("Illegal Argument");
        }

        String newID;
        
        do

    }
}
