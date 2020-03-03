package com.example.pursuit.models;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class Company {

    public String id;
    public String name;
    public String email;
    public String password;
    public String field;

    public Company() {
        // default constructor required for calls to
        // DataSnapshot.getValue(Company.class)
    }

    public Company(String id, String name, String email, String password, String field) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.field = field;
    }

    public String getEmail() {
        return this.email;
    }

}
