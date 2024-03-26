package com.example.spring.controller;


import com.example.spring.entities.Book;
import com.example.spring.queries.ApiResult;
import com.example.spring.queries.BookQueryConditions;
import com.example.spring.service.IBookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "http://localhost:8080")
@RestController
@RequestMapping("/book")
public class bookController {
    @Autowired
    IBookService book_service;
    BookQueryConditions conditions = new BookQueryConditions();
    List<Book> search_res;

    @GetMapping("/list")
    public List<Book> listBooks() {
        search_res = book_service.searchBooks(conditions);
        return search_res;
    }

    @PostMapping("/insert")
    public ApiResult insertBook(@RequestBody Book book){
        return book_service.insertBook(book);
    }

    @PostMapping ("/del")
    public ApiResult deleteBook(@RequestBody Map<String, Integer> bookdel){
        int id = bookdel.get("bookId");
        return book_service.deleteBook(id);
    }

    @PostMapping ("/edit")
    public ApiResult editBook(@RequestBody Book book){
        return book_service.editBook(book);
    }

    @PostMapping("/search")
    public ApiResult searchBooks(@RequestBody BookQueryConditions in_conditions){
        conditions = in_conditions;
        return new ApiResult(true, "Search Success.");
    }
}
