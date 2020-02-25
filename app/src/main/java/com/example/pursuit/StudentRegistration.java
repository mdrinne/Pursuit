package com.example.pursuit;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

public class StudentRegistration extends AppCompatActivity {

    EditText firstName;
    EditText lastName;
    EditText university;
    EditText major;
    EditText minor;
    EditText gpa;
    EditText bio;
    EditText email;
    EditText password1;
    EditText password2;
    Button btnFinish;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_registration);
    }
}
