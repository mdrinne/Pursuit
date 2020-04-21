package com.example.pursuit.models;

import com.google.firebase.database.IgnoreExtraProperties;


@IgnoreExtraProperties
public class Conversation {

    public String id;
    public String otherUserId;
    public String otherUserUsername;
    public String otherUserRole;
    public String createdAt;
    public String updatedAt;

    public Conversation() {
        // default constructor required for calls to
        // DataSnapshot.getValue(Conversation.class)
    }

//    public Conversation(String id, ArrayList<String> userIds) {
    public Conversation(String id, String otherUserId, String otherUserUsername, String otherUserRole, String createdAt, String updatedAt) {
      this.id = id;
      this.otherUserId = otherUserId;
      this.otherUserUsername = otherUserUsername;
      this.otherUserRole = otherUserRole;
      this.createdAt = createdAt;
      this.updatedAt = updatedAt;
    }

    public String getId() {
        return id;
    }

    public void setOtherUserId(String otherUserId) {
        this.otherUserId = otherUserId;
    }

    public String getOtherUserId() {
        return otherUserId;
    }

    public void setOtherUserUsername(String otherUserUsername) {
        this.otherUserUsername = otherUserUsername;
    }

    public String getOtherUserUsername() {
        return otherUserUsername;
    }

    public void setOtherUserRole(String otherUserRole) {
        this.otherUserRole = otherUserRole;
    }

    public String getOtherUserRole() {
        return otherUserRole;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

}
