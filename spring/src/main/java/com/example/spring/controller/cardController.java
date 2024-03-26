package com.example.spring.controller;

import com.example.spring.entities.Borrow;
import com.example.spring.entities.Card;
import com.example.spring.queries.ApiResult;
import com.example.spring.service.ICardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "http://localhost:8080")
@RestController
@RequestMapping("/card")
public class cardController {
    @Autowired
    ICardService card_service;
    List<Borrow> borrows;
    @GetMapping("/list")
    public List<Card> listCards() {
        return card_service.listCards();
    }
    @GetMapping("/histories")
    public List<Borrow> showHistory() {
        return borrows;
    }
    @PostMapping("/insert")
    public ApiResult insertCard(@RequestBody Card card){
        return card_service.registerCard(card);
    }
    @PostMapping("/postid")
    public void getHistory(@RequestBody Map<String, Integer> card){
        int id = card.get("cardId");
        borrows=card_service.showBorrowHistory(id);
        showHistory();
    }
    @PostMapping ("/del")
    public ApiResult deleteBook(@RequestBody Map<String, Integer> carddel){
        int id = carddel.get("cardId");
        return card_service.removeCard(id);
    }
}
