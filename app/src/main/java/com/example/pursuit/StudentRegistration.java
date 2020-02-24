package com.example.pursuit;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

public class StudentRegistration extends AppCompatActivity {

    EditText firstName;
    EditText lastName;
    EditText universityName;
    EditText studentMajor;
    EditText studentMinor;
    EditText studentGPA;
    EditText studentBio;
    Button btnFinish;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_registration);
    }
}
