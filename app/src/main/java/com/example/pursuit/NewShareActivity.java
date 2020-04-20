package com.example.pursuit;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.util.Log;

import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.DatabaseReference;

import com.example.pursuit.models.Company;
import com.example.pursuit.models.Student;
import com.example.pursuit.models.Employee;
import com.example.pursuit.models.Share;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class NewShareActivity extends AppCompatActivity {
    private final String TAG = "NewShareActivity";

    private DatabaseReference dbRef;

    private static Student currentStudent = null;
    private static Employee currentEmployee = null;
    private static Company currentCompany = null;
    private static String  currentRole = null;

    private EditText subject;
    private EditText message;
    private String subjectText;
    private String messageText;
    private Share newShare;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "in onCreate in NewShare");
        setContentView(R.layout.activity_new_share);

        findAndSetCurrentUser();
        dbRef = FirebaseDatabase.getInstance().getReference();
    }

    public void createShare(View v) {
        subject = findViewById(R.id.subject);
        message = findViewById(R.id.message);
        subjectText = subject.getText().toString();
        messageText = message.getText().toString();

        if (currentStudent != null) {
            writeNewStudentShare();
        } else {
            writeNewEmployeeShare();
        }

        Intent landingActivity = new Intent(NewShareActivity.this, LandingActivity.class);
        startActivity(landingActivity);
    }

    private void writeNewStudentShare() {
        String id = RandomKeyGenerator.randomAlphaNumeric(16);
        String userId = currentStudent.getId();
        String userFullName = currentStudent.getFirstName() + " " + currentStudent.getLastName();
        String userRole = "Student";
        String type = "Post";
        String subject = subjectText;
        String message = messageText;
        ArrayList<String> interestKeywords = new ArrayList<>();
        int likes = 0;

        newShare = new Share(id, userId, userFullName, userRole, type, subject, message, interestKeywords, likes);

        dbRef.child("Students").child(userId).child("Shares").child(id).setValue(newShare);
    }

    private void writeNewEmployeeShare() {
        String id = RandomKeyGenerator.randomAlphaNumeric(16);
        String userId = currentEmployee.getId();
        String userFullName = currentEmployee.getFirstName() + " " + currentEmployee.getLastName();
        String userRole = "Employee";
        String type = "Post";
        String subject = subjectText;
        String message = messageText;
        ArrayList<String> interestKeywords = new ArrayList<>();
        int likes = 0;

        newShare = new Share(id, userId, userFullName, userRole, type, subject, message, interestKeywords, likes);

        dbRef.child("Employees").child(userId).child("Shares").child(id).setValue(newShare);
    }

    private void findAndSetCurrentUser() {
        if (((PursuitApplication) this.getApplication()).getCurrentStudent() != null) {
            currentStudent = ((PursuitApplication) this.getApplication()).getCurrentStudent();
        } else if (((PursuitApplication) this.getApplication()).getCurrentEmployee() != null) {
            currentEmployee = ((PursuitApplication) this.getApplication()).getCurrentEmployee();
        } else {
            currentCompany = ((PursuitApplication) this.getApplication()).getCurrentCompany();
        }
        currentRole = ((PursuitApplication) this.getApplication()).getRole();
    }
}
