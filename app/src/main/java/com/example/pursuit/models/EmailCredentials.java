package com.example.pursuit.models;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class EmailCredentials {
    public String id;
    public String email;
    public String password;

    public EmailCredentials() {
        this.id = "";
    }

    public EmailCredentials(String id, String emailUsername, String emailPassword) {
        this.id = id;
        this.email = emailUsername;
        this.password = emailPassword;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String emailUsername) {
        this.email = emailUsername;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String emailPassword) {
        this.password = emailPassword;
    }
}
