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

public class MessagesActivity extends AppCompatActivity {
  
    BottomNavigationView bottomNavigation;

    Student currentStudent = null;
    Company currentCompany = null;
    String  currentRole = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messages);
        bottomNavigation = findViewById(R.id.bottom_navigation);
        bottomNavigation.setOnNavigationItemSelectedListener(navigationItemSelectedListener);

        findAndSetCurrentUser();

    }

    BottomNavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener =
      new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
            case R.id.navigation_home:
                Intent home = new Intent(MessagesActivity.this, LandingActivity.class);
                startActivity(home);
                finish();
                return true;
            case R.id.navigation_messages:
                return true;
            case R.id.navigation_profile:
                if (currentStudent != null) {
                    Intent i = new Intent(MessagesActivity.this, StudentProfileActivity.class);
                    startActivity(i);
                    finish();
                } else {
                    Intent profile = new Intent(MessagesActivity.this, CompanyProfileActivity.class);
                    startActivity(profile);
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

}