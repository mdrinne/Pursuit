package com.example.pursuit.models;

import org.junit.Test;

import static org.junit.Assert.*;

public class EmployeeTest {

    Employee newEmployee = new Employee("1", "2", "3", "4", "5", "6", "7", "8", "9");

    @Test
    public void getIdTest() {
        assert(newEmployee.getId().equals("1"));
    }

    @Test
    public void setIdTest() {
        newEmployee.setId("newId");
        assert(newEmployee.getId().equals("newId"));
    }

    @Test
    public void getFirstNameTest() {
        assert(newEmployee.getFirstName().equals("2"));
    }

    @Test
    public void setFirstNameTest() {
        newEmployee.setFirstName("newFirstName");
        assert(newEmployee.getFirstName().equals("newFirstName"));
    }

    @Test
    public void getLastNameTest() {
        assert(newEmployee.getLastName().equals("3"));
    }

    @Test
    public void setLastNameTest() {
        newEmployee.setLastName("newLastName");
        assert(newEmployee.getLastName().equals("newLastName"));
    }

    @Test
    public void getEmailTest() {
        assert(newEmployee.getEmail().equals("4"));
    }

    @Test
    public void setEmailTest() {
        newEmployee.setEmail("newEmail");
        assert(newEmployee.getEmail().equals("newEmail"));
    }

    @Test
    public void getPasswordTest() {
        assert(newEmployee.getPassword().equals("5"));
    }

    @Test
    public void setPasswordTest() {
        newEmployee.setPassword("newPassword");
        assert(newEmployee.getPassword().equals("newPassword"));
    }

    @Test
    public void getUsernameTest() {
        assert(newEmployee.getUsername().equals("6"));
    }

    @Test
    public void setUsernameTest() {
        newEmployee.setUsername("newUsername");
        assert(newEmployee.getUsername().equals("newUsername"));
    }

}
