package com.example.pursuit.models;

import org.junit.Test;
import static org.junit.Assert.*;

public class CompanyTest extends Company {

    private Company company1 = new Company("1", "name", "test@email.com", "testpw", "field", "descript");

    @Test
    public void getIdTest() { assert(company1.getId().equals("1"));
    }

    @Test
    public void setIdTest() {
        company1.setId("2");
        assert(company1.getId().equals("2"));
    }

    @Test
    public void getNameTest() {
        assert(company1.getName().equals("name"));
    }

    @Test
    public void setNameTest() {
        company1.setName("newName");
        assert(company1.getName().equals("newName"));
    }

    @Test
    public void getEmailTest() {
        assert(company1.getEmail().equals("test@email.com"));
    }

    @Test
    public void setEmailTest() {
        company1.setEmail("newEmail");
        assert company1.getEmail().equals("newEmail");
    }

    @Test
    public void getPasswordTest() {
        assert(company1.getPassword().equals("testpw"));
    }

    @Test
    public void setPasswordTest() {
        company1.setPassword("newPass");
        assert(company1.getPassword().equals("newPass"));
    }

    @Test
    public void getFieldTest() {
        assert(company1.getField().equals("field"));
    }

    @Test
    public void setFieldTest() {
        company1.setField("newField");
        assert(company1.getField().equals("newField"));
    }
}