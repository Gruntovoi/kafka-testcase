package com.testcase.controller;

import com.testcase.dto.PostMessageRequest;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import java.time.Instant;

@RestController
public class MessageController {

    private final KafkaTemplate<String, String> kafkaTemplate;
    public MessageController(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }


    @PostMapping("/post-message")
    public ResponseEntity<?> postMessage(@RequestBody PostMessageRequest request,
                                         HttpServletRequest httpRequest) {
        try {
            long timestamp = Instant.now().toEpochMilli();

            String message = String.format(
                    "{ \"msg_id\": \"%s\", \"timestamp\": \"%d\", \"method\": \"%s\", \"uri\": \"%s\" }",
                    request.getMsg_id(),
                    timestamp,
                    httpRequest.getMethod(),
                    httpRequest.getRequestURI()
            );

            kafkaTemplate.send("postedmessages", message);

            return ResponseEntity.ok().build();

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Ошибка при записи в Kafka: " + e.getMessage());
        }
    }
}
