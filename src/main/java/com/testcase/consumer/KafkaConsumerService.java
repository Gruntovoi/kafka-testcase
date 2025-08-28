package com.testcase.consumer;

import com.testcase.entity.MessageEntity;
import com.testcase.repository.MessageRepository;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
public class KafkaConsumerService {

    private final MessageRepository messageRepository;

    public KafkaConsumerService(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    @KafkaListener(topics = "postedmessages")
    public void receiveMessage(String message) {
        System.out.println("Received message: " + message);

        MessageEntity entity = new MessageEntity(
                extractMsgId(message),
                message,
                Instant.now()
        );

        messageRepository.save(entity);
    }
    private String extractMsgId(String json) {
        int start = json.indexOf("\"msg_id\":") + 10;
        if (start < 10) return null;
        int end = json.indexOf("\"", start);
        return json.substring(start, end);
    }
}