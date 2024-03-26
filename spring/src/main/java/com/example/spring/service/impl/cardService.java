package com.example.spring.service.impl;

import com.example.spring.LibraryManagementSystem;
import com.example.spring.LibraryManagementSystemImpl;
import com.example.spring.entities.Borrow;
import com.example.spring.entities.Card;
import com.example.spring.queries.ApiResult;
import com.example.spring.service.ICardService;
import com.example.spring.utils.ConnectConfig;
import com.example.spring.utils.DatabaseConnector;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class cardService implements ICardService {
    LibraryManagementSystem library;
    void initConnect(){
        try{
            ConnectConfig conf = new ConnectConfig();
            DatabaseConnector connector = new DatabaseConnector(conf);;
            connector.connect();
            library = new LibraryManagementSystemImpl(connector);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Card> listCards(){
        initConnect();
        return library.showCards();
    }

    @Override
    public ApiResult registerCard(Card card){
        initConnect();
        return library.registerCard(card);
    }

    @Override
    public List<Borrow> showBorrowHistory(int cardId){
        initConnect();
        return library.showBorrowHistory(cardId);
    }

    @Override
    public ApiResult removeCard(int cardId) {
        initConnect();
        return library.removeCard(cardId);
    }
}
