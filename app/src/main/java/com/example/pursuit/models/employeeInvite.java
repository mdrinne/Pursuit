package com.example.pursuit.models;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class employeeInvite {

    public String code;
    public String companyName;
    public String employeeEmail;

    public employeeInvite() {

    }

    public employeeInvite(String code, String companyName, String employeeEmail) {
        this.code = code;
        this.companyName = companyName;
        this.employeeEmail = employeeEmail;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getEmployeeEmail() {
        return employeeEmail;
    }

    public void setEmployeeEmail(String employeeEmail) {
        this.employeeEmail = employeeEmail;
    }
}
