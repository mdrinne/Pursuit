package com.example.pursuit.models;

import com.google.firebase.database.IgnoreExtraProperties;

import java.util.ArrayList;

@IgnoreExtraProperties
public class Share {

    public String id;
    public String userId;
    public String type;
    public String subject;
    public String message;
    public ArrayList<String> interestKeywords;

    public Share() {
        // default constructor required for calls to
        // DataSnapshot.getValue(Share.class)
    }

    public Share(String id, String userId, String type, String subject, String message, ArrayList<String> interestKeywords) {
        this.id = id;
        this.userId = userId;
        this.type = type;
        this.subject = subject;
        this.message = message;
        this.interestKeywords = interestKeywords;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ArrayList<String> getInterestKeywords() {
        return interestKeywords;
    }

    public void setInterestKeywords(ArrayList<String> interestKeywords) {
        this.interestKeywords = interestKeywords;
    }
}
