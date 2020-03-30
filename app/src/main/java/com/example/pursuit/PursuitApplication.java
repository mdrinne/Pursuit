package com.example.pursuit;

import android.app.Application;
import com.example.pursuit.models.Company;
import com.example.pursuit.models.Employee;
import com.example.pursuit.models.Student;

public class PursuitApplication extends Application {
    private Student currentStudent;
    private Company currentCompany;
    private Employee currentEmployee;
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

    public Employee getCurrentEmployee() {
        return currentEmployee;
    }

    public void setCurrentEmployee(Employee currentEmployee) {
        this.currentEmployee = currentEmployee;
        setCurrentRole("Employee");
    }

    public String getRole() {return currentRole; }

    public void setCurrentRole(String currentRole) { this.currentRole = currentRole; }

    public void removeCurrentUser() {
        this.currentStudent = null;
        this.currentCompany = null;
        this.currentEmployee = null;
        this.currentRole = null;
    }
}