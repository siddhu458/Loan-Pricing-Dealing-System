package com.example.demo.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.example.demo.model.DealEvent;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class DealEventProducer {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    @Value("${kafka.topic.deal-events}")
    private String topic;

    public DealEventProducer(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
        this.objectMapper = new ObjectMapper();
    }

    public void publishEvent(DealEvent event) {
        try {
            String payload = objectMapper.writeValueAsString(event);

            kafkaTemplate.send(topic, event.getDealId(), payload)
                .whenComplete((result, ex) -> {
                    if (ex != null) {
                        log.error(" Kafka send failed", ex);
                    } else {
                        log.info(" Kafka event published: {}", payload);
                    }
                });

        } catch (Exception e) {
            log.error("Exception while publishing Kafka event", e);
            throw new RuntimeException("Failed to publish Kafka event", e);
        }
    }
}


