package com.example.spring.service;

import com.example.spring.entities.Borrow;
import com.example.spring.queries.ApiResult;

import java.util.List;

public interface IBorrowService {
    List<Borrow> listBorrows();
    ApiResult insertBorrow(Borrow borrow);
    ApiResult returnBook(Borrow borrow);
}
