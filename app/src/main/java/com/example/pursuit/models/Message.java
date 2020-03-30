package com.example.pursuit.models;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class Message {

    public String id;
    public String senderId;
    public String recipientId;
    public String subject;
    public String messageText;

    public Message() {
        // default constructor required for calls to
        // DataSnapshot.getValue(Company.class)
    }

    public Message(String id, String senderId, String recipientId, String subject, String messageText) {
        this.id = id;
        this.senderId = senderId;
        this.recipientId = recipientId;
        this.subject = subject;
        this.messageText = messageText;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public void setRecipientId(String recipientId) {
        this.recipientId = recipientId;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public void messageText(String messageText) {
        this.messageText = messageText;
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

    public String getSubject() {
        return subject;
    }

    public String getMessageText() {
        return messageText;
    }

}
