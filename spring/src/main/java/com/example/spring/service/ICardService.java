package com.example.spring.service;

import com.example.spring.entities.Borrow;
import com.example.spring.entities.Card;
import com.example.spring.queries.ApiResult;

import java.util.List;

public interface ICardService {
    List<Card> listCards();
    ApiResult registerCard(Card card);
    List<Borrow> showBorrowHistory(int cardId);
    ApiResult removeCard(int cardId);
}
