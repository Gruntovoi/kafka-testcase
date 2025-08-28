package com.testcase.consumer;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
public class KafkaConsumerService {
    @KafkaListener(topics = "postedmessages")
    public void receiveMessage(String message) {
        System.out.println("Received message: " + message);
    }
}