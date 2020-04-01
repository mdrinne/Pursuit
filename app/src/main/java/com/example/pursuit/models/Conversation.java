package com.example.pursuit.models;

import com.google.firebase.database.IgnoreExtraProperties;
import java.util.ArrayList;

@IgnoreExtraProperties
public class Conversation {

    public String id;
    public ArrayList<String> userIds;

    public Conversation() {
        // default constructor required for calls to
        // DataSnapshot.getValue(Conversation.class)
    }

    public Conversation(String id, ArrayList<String> userIds) {
      this.id = id;
      this.userIds = userIds;
    }

    public String getId() {
        return id;
    }

    public void setUserIds(ArrayList<String> userIds) {
        this.userIds = userIds;
    }

    public ArrayList<String> getUserIds() {
        return userIds;
    }

}
