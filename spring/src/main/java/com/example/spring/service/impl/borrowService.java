package com.example.spring.service.impl;

import com.example.spring.LibraryManagementSystem;
import com.example.spring.LibraryManagementSystemImpl;
import com.example.spring.entities.Borrow;
import com.example.spring.queries.ApiResult;
import com.example.spring.service.IBorrowService;
import com.example.spring.utils.ConnectConfig;
import com.example.spring.utils.DatabaseConnector;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class borrowService implements IBorrowService {
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
    public List<Borrow> listBorrows(){
        initConnect();
        return library.showBorrow();
    }

    @Override
    public ApiResult insertBorrow(Borrow borrow){
        initConnect();
        return library.borrowBook(borrow);
    }

    @Override
    public ApiResult returnBook(Borrow borrow){
        initConnect();;
        return library.returnBook(borrow);
    }
}
