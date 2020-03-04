package com.example.pursuit;

import android.app.Application;
import com.example.pursuit.models.Company;
import com.example.pursuit.models.Student;

public class PursuitApplication extends Application {
    private Student currentStudent;
    private Company currentCompany;

    public Student getCurrentStudent() {
        return currentStudent;
    }

    public void setCurrentStudent(Student currentStudent) {
        this.currentStudent = currentStudent;
    }

    public Company getCurrentCompany() {
        return currentCompany;
    }

    public void setCurrentCompany(Company currentCompany) {
        this.currentCompany = currentCompany;
    }
}