package com.example.pursuit;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.example.pursuit.models.Company;
import com.example.pursuit.models.Student;

public class LandingActivity extends AppCompatActivity {

    Button logOutBtn;
    Button aboutPursuitBtn;
    Button myProfileBtn;
    Button viewCompaniesBtn;
    TextView currentUserNameText;

    Student currentStudent = null;
    Company currentCompany = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing);

        findAndSetCurrentUser();

        String currentUserNameString;
        if (currentStudent != null) {
            currentUserNameString = currentStudent.getUsername();
        } else {
            currentUserNameString = currentCompany.getName();
        }

        currentUserNameText = findViewById(R.id.currentUserName);
        currentUserNameText.setText("Welcome, " + currentUserNameString);

        logOutBtn = findViewById(R.id.logOutBtn);
        aboutPursuitBtn = findViewById(R.id.aboutPursuitBtn);
        myProfileBtn = findViewById(R.id.myProfileBtn);
        viewCompaniesBtn = findViewById(R.id.viewCompaniesBtn);

        logOutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentStudent != null) {
                    removeCurrentStudent();
                } else {
                    removeCurrentCompany();
                }

                Intent i = new Intent(LandingActivity.this, MainActivity.class);
                startActivity(i);
                finish();
            }
        });

        aboutPursuitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(LandingActivity.this, aboutPursuitActivity.class);
                startActivity(i);
            }
        });

        myProfileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentStudent != null) {
                    Intent i = new Intent(LandingActivity.this, StudentProfileActivity.class);
                    startActivity(i);
                } else {
                    Intent i = new Intent(LandingActivity.this, StudentProfileActivity.class);
                    startActivity(i);
                }

            }
        });

        viewCompaniesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(LandingActivity.this, viewCompaniesActivity.class);
                startActivity(i);
            }
        });

    }

    private void findAndSetCurrentUser() {
        if (((PursuitApplication) this.getApplication()).getCurrentStudent() != null) {
            currentStudent = ((PursuitApplication) this.getApplication()).getCurrentStudent();
        } else {
            currentCompany = ((PursuitApplication) this.getApplication()).getCurrentCompany();
        }
    }

    private void removeCurrentStudent() {
        ((PursuitApplication) this.getApplication()).setCurrentStudent(null);
    }

    private void removeCurrentCompany() {
        ((PursuitApplication) this.getApplication()).setCurrentCompany(null);
    }

}
