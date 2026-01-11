package com.example.demo.services;

import java.time.Instant;
import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import com.example.demo.model.Deal;
import com.example.demo.model.DealEvent;
import com.example.demo.repository.DealRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DealService {

    private final DealRepository dealRepository;
    private final DealEventProducer eventProducer;

    public Deal createDeal(Deal deal) {
        Deal savedDeal = dealRepository.save(deal);

        DealEvent event = new DealEvent(
        	    "DEAL_CREATED",
        	    deal.getId(),
        	    "Deal created successfully",
        	    Instant.now().toString()
        	);


        eventProducer.publishEvent(event);
        return savedDeal;
    }

    public Deal updateDealStage(String dealId, String stage) {
        Deal deal = dealRepository.findById(dealId)
                .orElseThrow(() -> new RuntimeException("Deal not found"));

        deal.setStage(stage);
        Deal updatedDeal = dealRepository.save(deal);

        DealEvent event = new DealEvent(
        	    "DEAL_CREATED",
        	    deal.getId(),
        	    "Deal created successfully",
        	    Instant.now().toString()
        	);


        eventProducer.publishEvent(event);
        return updatedDeal;
    }
}

