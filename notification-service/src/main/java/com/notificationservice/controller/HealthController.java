package com.notificationservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/health")
public class HealthController {
    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;

    @GetMapping
    public String health() {
        kafkaTemplate.send("test-topic","hello from notification-service");
        return "OK";
    }
}