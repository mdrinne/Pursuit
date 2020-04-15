package com.example.pursuit.models;

import com.google.firebase.database.IgnoreExtraProperties;

import java.util.ArrayList;

@IgnoreExtraProperties
public class Keyword {

    public String id;
    public String text;
    public ArrayList<String> opportunities;
    public ArrayList<String> students;

    public Keyword() {
        this.id = "";
    }

    public Keyword(String id, String text, ArrayList<String> opportunities, ArrayList<String> students) {
        this.id = id;
        this.text = text;
        this.opportunities = opportunities;
        this.students = students;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public ArrayList<String> getOpportunities() {
        return opportunities;
    }

    public void setOpportunities(ArrayList<String> opportunities) {
        this.opportunities = opportunities;
    }

    public ArrayList<String> getStudents() {
        return students;
    }

    public void setStudents(ArrayList<String> students) {
        this.students = students;
    }
}
