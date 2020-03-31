package com.example.pursuit;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.annotation.NonNull;

import com.example.pursuit.models.Company;
import com.example.pursuit.models.Student;

public class LandingActivity extends AppCompatActivity {

    Button aboutPursuitBtn;
    Button viewCompaniesBtn;
    TextView currentUserNameText;
    BottomNavigationView bottomNavigation;

    Student currentStudent = null;
    Company currentCompany = null;
    String  currentRole = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing);
        bottomNavigation = findViewById(R.id.bottom_navigation);
        bottomNavigation.setOnNavigationItemSelectedListener(navigationItemSelectedListener);

        findAndSetCurrentUser();

        String currentUserNameString;
        if (currentRole.equals("Student")) {
            currentUserNameString = currentStudent.getUsername();
        } else {
            currentUserNameString = currentCompany.getName();
        }

        currentUserNameText = findViewById(R.id.currentUserName);
        currentUserNameText.setText("Welcome, " + currentUserNameString);

        aboutPursuitBtn = findViewById(R.id.aboutPursuitBtn);
        viewCompaniesBtn = findViewById(R.id.viewCompaniesBtn);


        aboutPursuitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(LandingActivity.this, aboutPursuitActivity.class);
                startActivity(i);
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

    BottomNavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener =
      new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
            case R.id.navigation_home:
              return true;
            case R.id.navigation_messages:
                Intent i1 = new Intent(LandingActivity.this, MessagesActivity.class);
                startActivity(i1);
                finish();
                return true;
            case R.id.navigation_profile:
                if (currentStudent != null) {
                    Intent i = new Intent(LandingActivity.this, StudentProfileActivity.class);
                    startActivity(i);
                    finish();
                } else {
                    Intent i = new Intent(LandingActivity.this, CompanyProfileActivity.class);
                    startActivity(i);
                    finish();
                }
              return true;
          }
          return false;
        }
      };

    private void findAndSetCurrentUser() {
        if (((PursuitApplication) this.getApplication()).getCurrentStudent() != null) {
            currentStudent = ((PursuitApplication) this.getApplication()).getCurrentStudent();
        } else {
            currentCompany = ((PursuitApplication) this.getApplication()).getCurrentCompany();
        }
        currentRole = ((PursuitApplication) this.getApplication()).getRole();
    }

    public void myProfile(View v) {
        if (currentRole.equals("Student")) {
            Intent i = new Intent(LandingActivity.this, StudentProfileActivity.class);
            startActivity(i);
        } else {
            /* ***** HAVING AN ISSUE WITH THIS ***** */
            Intent i = new Intent(LandingActivity.this, CompanyProfileActivity.class);
            startActivity(i);
        }
    }

    private void removeCurrentUser() {
        ((PursuitApplication) this.getApplication()).removeCurrentUser();
    }

    public void logoutCurrentUser(View v) {
        removeCurrentUser();

        Intent i = new Intent(LandingActivity.this, MainActivity.class);
        startActivity(i);
        finish();
    }

}