package com.example.spring.controller;

import com.example.spring.entities.Borrow;
import com.example.spring.queries.ApiResult;
import com.example.spring.service.IBorrowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/borrow")
public class borrowController {
    @Autowired
    IBorrowService borrow_service;
    @GetMapping("/list")
    public List<Borrow> listBorrows(){
        return borrow_service.listBorrows();
    }
    @PostMapping("/insert")
    public ApiResult insertBorrow(@RequestBody Borrow borrow){
        return borrow_service.insertBorrow(borrow);
    }
    @PostMapping("/ret")
    public ApiResult returnBook(@RequestBody Borrow borrow){
        return borrow_service.returnBook(borrow);
    }
}
