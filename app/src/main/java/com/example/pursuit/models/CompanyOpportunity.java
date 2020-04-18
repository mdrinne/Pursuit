package com.example.pursuit.models;

import com.google.firebase.database.IgnoreExtraProperties;

import java.util.ArrayList;

@IgnoreExtraProperties
public class CompanyOpportunity {

    public String id;
    public String companyID;
    public String position;
    public String withWho;
    public String description;
    public String city;
    public String state;
    public String requirements;
    public ArrayList<String> keywords;
    public Integer approved;
    public String timeStamp;


    public CompanyOpportunity() {
        this.id = "";
    }

    public CompanyOpportunity(String id, String companyID, String position, String withWho, String description, String city, String state, String requirements, ArrayList<String> keywords, Integer approved, String timeStamp) {
        this.id = id;
        this.companyID = companyID;
        this.position = position;
        this.withWho = withWho;
        this.description = description;
        this.city = city;
        this.state = state;
        this.requirements = requirements;
        this.keywords = keywords;
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

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getRequirements() {
        return requirements;
    }

    public void setRequirements(String requirements) {
        this.requirements = requirements;
    }

    public ArrayList<String> getKeywords() {
        return keywords;
    }

    public void setKeywords(ArrayList<String> keywords) {
        this.keywords = keywords;
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

    public String getCompanyID() {
        return companyID;
    }

    public void setCompanyID(String companyID) {
        this.companyID = companyID;
    }
}
