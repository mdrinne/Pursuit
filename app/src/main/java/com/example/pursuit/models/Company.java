package com.example.pursuit.models;

import com.google.firebase.database.IgnoreExtraProperties;

import java.util.ArrayList;

@IgnoreExtraProperties
public class Company {

    public String id;
    public String name;
    public String email;
    public String password;
    public String field;
    public String description;
    public ArrayList<CompanyOpportunity> opportunities;

    public Company() {
        this.id = "";
        // default constructor required for calls to
        // DataSnapshot.getValue(Company.class)
    }

    public Company(String id, String name, String email, String password, String field, String description, ArrayList<CompanyOpportunity> opportunities) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.field = field;
        this.description = description;
        this.opportunities = opportunities;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {return email; }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ArrayList<CompanyOpportunity> getOpportunities() {
        return opportunities;
    }

    public void setOpportunities(ArrayList<CompanyOpportunity> opportunities) {
        this.opportunities = opportunities;
    }
}
