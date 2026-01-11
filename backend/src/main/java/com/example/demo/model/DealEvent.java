package com.example.demo.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class DealEvent {

    private String eventType;
    private String dealId;
    private String message;
    private String timestamp;
}

