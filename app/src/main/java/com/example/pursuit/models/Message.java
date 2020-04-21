package com.example.pursuit.models;

import com.google.firebase.database.IgnoreExtraProperties;
import java.time.LocalDateTime;

@IgnoreExtraProperties
public class Message {

    public String id;
    public String senderId;
    public String senderUsername;
    public String recipientId;
    public String recipientUsername;
    public String messageText;
    public String createdAt;

    public Message() {
        // default constructor required for calls to
        // DataSnapshot.getValue(Company.class)
    }

    public Message(String id, String senderId, String senderUsername, String recipientId, String recipientUsername, String messageText, String createdAt) {
        this.id = id;
        this.senderId = senderId;
        this.senderUsername = senderUsername;
        this.recipientId = recipientId;
        this.recipientUsername = recipientUsername;
        this.messageText = messageText;
        this.createdAt = createdAt;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public void setSenderUsername(String senderUsername) { this.senderUsername = senderUsername; }

    public void setRecipientId(String recipientId) {
        this.recipientId = recipientId;
    }

    public void setRecipientUsername(String recipientUsername) { this.recipientUsername = recipientUsername; }

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

    public String getSenderUsername() { return senderUsername; }

    public String getRecipientId() {
        return recipientId;
    }

    public String getRecipientUsername() { return recipientUsername; }

    public String getMessageText() {
        return messageText;
    }

    public String getCreatedAt() {
        return createdAt;
    }

}
