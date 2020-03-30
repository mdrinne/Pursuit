package com.example.pursuit.models;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class EmployeeInvite {
    public String code;
    public String employeeEmail;

    public EmployeeInvite() {
    }

    public EmployeeInvite(String code, String employeeEmail) {
        this.code = code;
        this.employeeEmail = employeeEmail;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getEmployeeEmail() {
        return employeeEmail;
    }

    public void setEmployeeEmail(String employeeEmail) {
        this.employeeEmail = employeeEmail;
    }
}
