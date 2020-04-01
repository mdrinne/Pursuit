package com.example.pursuit;

import org.junit.Test;

import static org.junit.Assert.*;

public class CompanyRegistrationTest {

    CompanyRegistration newComReg = new CompanyRegistration();

    @Test
    public void checkForEmpties() {
        assertFalse(newComReg.checkForEmpties());
    }
}