package com.example.pursuit.models;

import com.google.firebase.database.IgnoreExtraProperties;

import java.util.ArrayList;

@IgnoreExtraProperties
public class Share {

    public String id;
    public String userId;
    public String userFullName;
    public String userUsername;
    public String userRole;
    public String type;
    public String subject;
    public String message;
    public ArrayList<String> interestKeywords;
    private int likes;
    private String createdAt;

    public Share() {
        // default constructor required for calls to
        // DataSnapshot.getValue(Share.class)
    }

    public Share(String id, String userId, String userFullName, String userUsername, String userRole, String type, String subject, String message, ArrayList<String> interestKeywords, int likes, String createdAt) {
        this.id = id;
        this.userId = userId;
        this.userFullName = userFullName;
        this.userUsername = userUsername;
        this.userRole = userRole;
        this.type = type;
        this.subject = subject;
        this.message = message;
        this.interestKeywords = interestKeywords;
        this.likes = likes;
        this.createdAt = createdAt;
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

    public String getUserFullName() {
        return userFullName;
    }

    public void setUserFullName(String userFullName) {
        this.userFullName = userFullName;
    }

    public String getUserRole() {
        return userRole;
    }

    public void setUserRole(String userRole) {
        this.userRole = userRole;
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

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public String getUserUsername() {
        return userUsername;
    }

    public void setUserUsername(String userUsername) {
        this.userUsername = userUsername;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }
}
