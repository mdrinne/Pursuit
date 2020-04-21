package com.example.pursuit.models;

import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class StudentTest {

    ArrayList<String> x;
    Student newStudent = new Student("1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", x);

    @Test
    public void getIdTest() {
        assert(newStudent.getId().equals("1"));
    }

    @Test
    public void setIdTest() {
        newStudent.setId("newId");
        assert(newStudent.getId().equals("newId"));
    }

    @Test
    public void getFirstNameTest() {
        assert(newStudent.getFirstName().equals("2"));
    }

    @Test
    public void setFirstNameTest() {
        newStudent.setFirstName("newFirstName");
        assert(newStudent.getFirstName().equals("newFirstName"));
    }

    @Test
    public void getLastNameTest() {
        assert(newStudent.getLastName().equals("3"));
    }

    @Test
    public void setLastNameTest() {
        newStudent.setLastName("newLastName");
        assert(newStudent.getLastName().equals("newLastName"));
    }

    @Test
    public void getEmailTest() {
        assert(newStudent.getEmail().equals("9"));
    }

    @Test
    public void setEmailTest() {
        newStudent.setEmail("newEmail");
        assert(newStudent.getEmail().equals("newEmail"));
    }

    @Test
    public void getUsernameTest() {
        assert(newStudent.getUsername().equals("10"));
    }

    @Test
    public void setUsernameTest() {
        newStudent.setUsername("newUsername");
        assert(newStudent.getUsername().equals("newUsername"));
    }
}