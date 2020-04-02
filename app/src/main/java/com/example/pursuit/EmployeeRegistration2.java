package com.example.pursuit;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.pursuit.models.Company;
import com.example.pursuit.models.Employee;
import com.example.pursuit.models.Student;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class EmployeeRegistration2 extends AppCompatActivity {

    private static final String TAG = "EmployeeRegistration2";

    private DatabaseReference dbref;

    EditText firstName;
    EditText lastName;
    EditText position;
    EditText username;
    EditText password;
    EditText reEnterPassword;
    View view;

    Employee continuedEmployee;
    Company currentCompany;

    Employee employeeUsernameCheck;
    Student studentUsernameCheck;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_registration2);

        dbref = FirebaseDatabase.getInstance().getReference();

        initializeCurrentEmployee();
        initializeCurrentCompany();
    }

    /* ********DATABASE******** */

    ValueEventListener employeeUsernameListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            employeeUsernameCheck = null;
            if (dataSnapshot.exists()) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Employee employee = snapshot.getValue(Employee.class);
                    employeeUsernameCheck = employee;
                }
            }
            postEmployeeUsernameCheck();
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    };

    ValueEventListener studentUsernameListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            studentUsernameCheck = null;
            if (dataSnapshot.exists()) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Student student = snapshot.getValue(Student.class);
                    studentUsernameCheck = student;
                }
            }
            postStudentUsernameCheck();
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    };

    private void writeNewEmployee(Employee employee) {
        dbref.child("Employees").child(employee.getId()).setValue(employee);
    }

    private void deleteEmployeeInvite() {
        dbref.child("EmployeeInvites").child(currentCompany.getId()).child(continuedEmployee.getInviteCode()).removeValue();
    }

    /* ******END DATABASE****** */

    private void initializeCurrentEmployee() {
        continuedEmployee = ((PursuitApplication) this.getApplicationContext()).getCurrentEmployee();
    }

    private void initializeCurrentCompany() {
        currentCompany = ((PursuitApplication) this.getApplicationContext()).getCurrentCompany();
    }

    public String toString(EditText text) {
        return text.getText().toString();
    }

    boolean isEmpty(EditText text) {
        return TextUtils.isEmpty(toString(text));
    }

    boolean checkForEmpties() {
        if (isEmpty(firstName) || isEmpty(lastName) || isEmpty(position) || isEmpty(username)
            || isEmpty(password) || isEmpty(reEnterPassword)) {
            return true;
        }
        return false;
    }

    boolean passwordMatch() {
        if (toString(password).equals(toString(reEnterPassword))) {
            return true;
        }
        return false;
    }

    public void register(View v) {
        view = v;
        firstName = findViewById(R.id.txtFirstName);
        lastName = findViewById(R.id.txtLastName);
        position = findViewById(R.id.txtPosition);
        username = findViewById(R.id.txtUsername);
        password = findViewById(R.id.txtPassword);
        reEnterPassword = findViewById(R.id.txtReEnterPassword);

        if (checkForEmpties()) {
            Toast.makeText(view.getContext(), "All Fields are Required", Toast.LENGTH_SHORT).show();
        } else {
            if (!passwordMatch()) {
                Toast.makeText(view.getContext(), "Passwords Do Not Match", Toast.LENGTH_SHORT).show();
            } else {
                Query employeeUsernameQuery = dbref.child("Employees").orderByChild("username").equalTo(toString(username));
                employeeUsernameQuery.addListenerForSingleValueEvent(employeeUsernameListener);
            }
        }
    }

    private void postEmployeeUsernameCheck() {
        if (employeeUsernameCheck != null) {
            Toast.makeText(view.getContext(), "Username Already Taken", Toast.LENGTH_SHORT).show();
        }
        else {
            Query studentUsernameQuery = dbref.child("Students").orderByChild("username").equalTo(toString(username));
            studentUsernameQuery.addListenerForSingleValueEvent(studentUsernameListener);
        }
    }

    private void postStudentUsernameCheck() {
        if (studentUsernameCheck != null) {
            Toast.makeText(view.getContext(), "Username Already Taken", Toast.LENGTH_SHORT).show();
        }
        else {
            continuedEmployee.setFirstName(toString(firstName));
            continuedEmployee.setLastName(toString(lastName));
            continuedEmployee.setPosition(toString(position));
            continuedEmployee.setUsername(toString(username));
            continuedEmployee.setPassword(toString(password));

            writeNewEmployee(continuedEmployee);
            ((PursuitApplication) this.getApplication()).setCurrentEmployee(continuedEmployee);
            deleteEmployeeInvite();

            Intent intent = new Intent(this, LandingActivity.class);
            startActivity(intent);
        }
    }

}
