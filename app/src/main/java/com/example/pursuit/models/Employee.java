package com.example.pursuit.models;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class Employee {
    public String id;
    public String firstName;
    public String lastName;
    public String email;
    public String password;
    public String username;
    public String position;
    public String companyName;
    public String companyID;
    public String inviteCode;
    public Integer admin;

    public Employee() {
        // default constructor required for calls to
        //        // DataSnapshot.getValue(Student.class)
        this.id = "";
    }

    public Employee(String id, String firstName, String lastName, String email, String password, String username, String position, String companyName, String companyID, String inviteCode) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.username = username;
        this.position = position;
        this.companyName = companyName;
        this.companyID = companyID;
        this.inviteCode = inviteCode;
        this.admin = 0;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getCompanyID() {
        return companyID;
    }

    public void setCompanyID(String companyID) {
        this.companyID = companyID;
    }

    public String getInviteCode() {
        return inviteCode;
    }

    public void setInviteCode(String inviteCode) {
        this.inviteCode = inviteCode;
    }

    public Integer getAdmin() {
        return admin;
    }

    public void setAdmin(Integer admin) {
        this.admin = admin;
    }
}
