package com.example.pursuit.models;

import com.google.firebase.database.IgnoreExtraProperties;
import java.time.LocalDateTime;

@IgnoreExtraProperties
public class Message {

    public String id;
    public String senderId;
    public String recipientId;
    public String messageText;
    public String createdAt;

    public Message() {
        // default constructor required for calls to
        // DataSnapshot.getValue(Company.class)
    }

    public Message(String id, String senderId, String recipientId, String messageText, String createdAt) {
        this.id = id;
        this.senderId = senderId;
        this.recipientId = recipientId;
        this.messageText = messageText;
        this.createdAt = createdAt;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public void setRecipientId(String recipientId) {
        this.recipientId = recipientId;
    }

    public void setMessageText(String messageText) {
        this.messageText = messageText;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getId() {
        return id;
    }

    public String getSenderId() {
        return senderId;
    }

    public String getRecipientId() {
        return recipientId;
    }

    public String getMessageText() {
        return messageText;
    }

    public String getCreatedAt() {
        return createdAt;
    }

}
