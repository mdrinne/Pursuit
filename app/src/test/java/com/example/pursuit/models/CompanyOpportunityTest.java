package com.example.pursuit.models;

import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class CompanyOpportunityTest {

    ArrayList<String> x = new ArrayList<String>(0);
    private CompanyOpportunity newOp = new CompanyOpportunity("1", "2", "3", "4", "5", "6", "7", "8", x, 1, "9");
    private Company newCompany = new Company("id", "name", "email", "pw", "field", "description");

    @Test
    public void getIdTest() {
        assert(newOp.getId().equals("1"));
    }

    @Test
    public void setIdTest() {
        newOp.setId("newId");
        assert(newOp.getId().equals("newId"));
    }

    @Test
    public void getPositionTest() {
        assert(newOp.getPosition().equals("3"));
    }

    @Test
    public void setPositionTest() {
        newOp.setPosition(("newPosition"));
        assert(newOp.getPosition().equals("newPosition"));
    }

    @Test
    public void getCompanyIDTest() {
        newOp.setCompanyID(newCompany.getId());
        assert(newOp.getCompanyID().equals("id"));
    }
}