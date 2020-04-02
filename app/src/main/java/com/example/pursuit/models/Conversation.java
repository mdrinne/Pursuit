package com.example.pursuit.models;

import com.google.firebase.database.IgnoreExtraProperties;
import java.util.ArrayList;

@IgnoreExtraProperties
public class Conversation {

    public String id;
//    public ArrayList<String> userIds;
    public String otherUserId;
    public String otherUserUsername;
    public String otherUserRole;

    public Conversation() {
        // default constructor required for calls to
        // DataSnapshot.getValue(Conversation.class)
    }

//    public Conversation(String id, ArrayList<String> userIds) {
    public Conversation(String id, String otherUserId, String otherUserUsername, String otherUserRole) {
      this.id = id;
      this.otherUserId = otherUserId;
      this.otherUserUsername = otherUserUsername;
      this.otherUserRole = otherUserRole;
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

//    public void setUserIds(ArrayList<String> userIds) {
//        this.userIds = userIds;
//    }
//
//    public ArrayList<String> getUserIds() {
//        return userIds;
//    }

}
