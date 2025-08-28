package com.testcase.controller;

import com.testcase.dto.PostMessageRequest;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.time.Instant;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

@RestController
public class MessageController {

    private AtomicInteger delay;
    private AtomicBoolean modeFlag;

    private final KafkaTemplate<String, String> kafkaTemplate;
    public MessageController(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
        this.modeFlag = new AtomicBoolean();
        this.delay= new AtomicInteger();
    }


    @PostMapping("/post-message")
    public ResponseEntity<?> postMessage(@RequestBody PostMessageRequest request,
                                         HttpServletRequest httpRequest) throws InterruptedException {
        Thread.sleep(delay.get());
            if(modeFlag.get()){
                return ResponseEntity.status(500).body("500");
            }
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
    }
    @PostMapping("/setDelay")
    public ResponseEntity<?> setDelay(@RequestParam int millis){
        delay.set(millis);
        System.out.println("Set delay:" + millis + "ms");
        return ResponseEntity.ok().build();
    }
    @PostMapping("/setModeOff")
    public ResponseEntity<?> setDelay(@RequestParam boolean flagMode){
        modeFlag.set(flagMode);
        System.out.println("setModeOff " + modeFlag);
        return ResponseEntity.ok("Mode 500 " + modeFlag);
    }
}
