package com.example.spring.service.impl;


import com.example.spring.LibraryManagementSystem;
import com.example.spring.LibraryManagementSystemImpl;
import com.example.spring.entities.Book;
import com.example.spring.queries.ApiResult;
import com.example.spring.queries.BookQueryConditions;
import com.example.spring.service.IBookService;
import com.example.spring.utils.ConnectConfig;
import com.example.spring.utils.DatabaseConnector;
import org.springframework.stereotype.Service;


import java.util.List;


@Service
public class bookService implements IBookService {

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
    public ApiResult insertBook(Book book){
        initConnect();
        return library.storeBook(book);
    }

    @Override
    public ApiResult deleteBook(int bookId){
        initConnect();
        return library.removeBook(bookId);
    }

    @Override
    public ApiResult editBook(Book book){
        initConnect();
        return library.modifyBookInfo(book);
    }

    @Override
    public List<Book> searchBooks(BookQueryConditions conditions){
        initConnect();
        return library.queryBook(conditions);
    }
}
