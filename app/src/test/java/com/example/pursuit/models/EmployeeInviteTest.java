package com.example.pursuit.models;

import org.junit.Test;

import static org.junit.Assert.*;

public class EmployeeInviteTest {

    EmployeeInvite newInvite = new EmployeeInvite("1", "2");

    @Test
    public void getCodeTest() {
        assert(newInvite.getCode().equals("1"));
    }

    @Test
    public void setCodeTest() {
        newInvite.setCode("newCode");
        assert(newInvite.getCode().equals("newCode"));
    }

    @Test
    public void getEmployeeEmailTest() {
        assert(newInvite.getEmployeeEmail().equals("2"));
    }

    @Test
    public void setEmployeeEmailTest() {
        newInvite.setEmployeeEmail("newEmail");
        assert(newInvite.getEmployeeEmail().equals("newEmail"));
    }
}