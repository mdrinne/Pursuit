package com.example.pursuit.models;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class CompanyOpportunity {

    public String id;
    public String position;
    public String withWho;
    public String description;
    public Integer approved;
    public String timeStamp;


    public CompanyOpportunity() {
        this.id = "";
    }

    public CompanyOpportunity(String id, String position, String withWho, String description, Integer approved, String timeStamp) {
        this.id = id;
        this.position = position;
        this.withWho = withWho;
        this.description = description;
        this.approved = approved;
        this.timeStamp = timeStamp;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getWithWho() {
        return withWho;
    }

    public void setWithWho(String withWho) {
        this.withWho = withWho;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getApproved() {
        return approved;
    }

    public void setApproved(Integer approved) {
        this.approved = approved;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

}
