package com.testcase.entity;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "messages")
public class MessageEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String msgId;

    private String content;

    private Instant receivedAt;

    public MessageEntity() {}

    public MessageEntity(String msgId, String payload, Instant receivedAt) {
        this.msgId = msgId;
        this.content = payload;
        this.receivedAt = receivedAt;
    }

    public Long getId() {
        return id;
    }

    public String getMsgId() {
        return msgId;
    }

    public void setMsgId(String msgId) {
        this.msgId = msgId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Instant getReceivedAt() {
        return receivedAt;
    }

    public void setReceivedAt(Instant receivedAt) {
        this.receivedAt = receivedAt;
    }
}