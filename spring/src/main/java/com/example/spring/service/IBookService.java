package com.example.spring.service;

import com.example.spring.entities.Book;
import com.example.spring.queries.ApiResult;
import com.example.spring.queries.BookQueryConditions;

import java.util.List;

public interface IBookService {
    ApiResult insertBook(Book book);
    ApiResult deleteBook(int bookId);
    ApiResult editBook(Book book);
    List<Book> searchBooks(BookQueryConditions conditions);
}
