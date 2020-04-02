package com.example.pursuit;

import com.example.pursuit.models.Company;
import com.example.pursuit.models.Employee;
import com.example.pursuit.models.Student;

import org.junit.Test;

import static org.junit.Assert.*;

public class PursuitApplicationTest {

    Student testStudent = new Student("1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11");
    Company testCompany = new Company("1", "2", "3", "4", "5");
    Employee testEmployee = new Employee("1", "2", "3", "4", "5", "6", "7", "8", "9");
    String testRole = "role";

    Student testStudentNew = new Student();
    Company testCompanyNew = new Company();
    Employee testEmployeeNew = new Employee();

    PursuitApplication newApp = new PursuitApplication(testStudent, testCompany, testEmployee, testRole);

    @Test
    public void getCurrentStudentTest() {
        assert(newApp.getCurrentStudent().getId().equals("1"));
    }

    @Test
    public void setCurrentStudentTest() {
        newApp.setCurrentStudent(testStudentNew);
        assert(newApp.getCurrentStudent().getId().equals(""));
    }

    @Test
    public void getCurrentCompanyTest() {
        assert(newApp.getCurrentCompany().getId().equals("1"));
    }

    @Test
    public void setCurrentCompanyTest() {
        newApp.setCurrentCompany(testCompanyNew);
        assert(newApp.getCurrentCompany().getId().equals(""));
    }

    @Test
    public void getCurrentEmployeeTest() {
        assert(newApp.getCurrentEmployee().getId().equals("1"));
    }

    @Test
    public void setCurrentEmployeeTest() {
        newApp.setCurrentEmployee(testEmployeeNew);
        assert(newApp.getCurrentEmployee().getId().equals(""));
    }

    @Test
    public void getRoleTest() {
        assert(newApp.getRole().equals("role"));
    }

    @Test
    public void setCurrentRoleTest() {
        newApp.setCurrentRole("newRole");
        assert(newApp.getRole().equals("newRole"));
    }

    @Test
    public void removeCurrentUserTest() {
        newApp.removeCurrentUser();
        assert(newApp.getCurrentStudent() == null);
        assert(newApp.getCurrentCompany() == null);
        assert(newApp.getCurrentEmployee() == null);
        assert(newApp.getRole() == null);

    }
}