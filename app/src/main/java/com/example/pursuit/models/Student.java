package com.example.pursuit.models;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class Student {
    public int id;
    public String fname;
    public String lname;
    public String university;
    public String major;
    public String minor;
    public String gpa;
    public String bio;
    public String email;
    public String username;
    public String password;

    public Student() {
        // default constructor required for calls to DataSnapshot.getValue(Company.class)
    }

    // constructor
    public Student(int id, String fname, String lname, String university, String major, String minor, String gpa, String bio, String email, String username, String password) {
        this.id = id;
        this.fname = fname;
        this.lname = lname;
        this.university = university;
        this.major = major;
        this.minor = minor;
        this.gpa = gpa;
        this.bio = bio;
        this.email = email;
        this.username = username;
        this.password = password;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLname() {
        return lname;
    }

    public void setLname(String lname) {
        this.lname = lname;
    }

    public String getUniversity() {
        return university;
    }

    public void setUniversity(String university) {
        this.university = university;
    }

    public String getMajor() {
        return major;
    }

    public void setMajor(String major) {
        this.major = major;
    }

    public String getMinor() {
        return minor;
    }

    public void setMinor(String minor) {
        this.minor = minor;
    }

    public String getGpa() {
        return gpa;
    }

    public void setGpa(String gpa) {
        this.gpa = gpa;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
