package com.example.demo.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.Deal;
import com.example.demo.services.DealService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/deals")
@RequiredArgsConstructor
public class DealController {

    private final DealService dealService;

    @PostMapping
    public Deal createDeal(@RequestBody Deal deal) {
        return dealService.createDeal(deal);
    }

    @PutMapping("/{id}/stage")
    public ResponseEntity<Deal> updateStage(
            @PathVariable String id,
            @RequestParam String stage) {

        Deal updatedDeal = dealService.updateDealStage(id, stage);
        return ResponseEntity.ok(updatedDeal);
    }
}

