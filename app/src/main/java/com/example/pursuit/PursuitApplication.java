package com.example.pursuit;

import android.app.Application;
import com.example.pursuit.models.Company;
import com.example.pursuit.models.Student;

public class PursuitApplication extends Application {
    private Student currentStudent;
    private Company currentCompany;
    private String currentRole;

    public Student getCurrentStudent() {
        return currentStudent;
    }

    public void setCurrentStudent(Student currentStudent) {
        this.currentStudent = currentStudent;
        setCurrentRole("Student");
    }

    public Company getCurrentCompany() {
        return currentCompany;
    }

    public void setCurrentCompany(Company currentCompany) {
        this.currentCompany = currentCompany;
        setCurrentRole("Company");
    }

    public String getRole() {return currentRole; }

    public void setCurrentRole(String currentRole) { this.currentRole = currentRole; }
}