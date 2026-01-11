package com.example.demo.dealeventConsumer;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class DealEventConsumer {

    @KafkaListener(
        topics = "deal-events",
        groupId = "deal-consumer-group"
    )
    public void consume(String message) {
        log.info("Received Kafka Event: {}", message);
    }
}

