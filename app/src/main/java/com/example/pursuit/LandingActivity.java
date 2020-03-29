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
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.pursuit.fragments.HomeFragment;
import com.example.pursuit.fragments.MessagesFragment;
import com.example.pursuit.fragments.ProfileFragment;
import com.example.pursuit.models.Company;
import com.example.pursuit.models.Student;

public class LandingActivity extends AppCompatActivity {

    Button logOutBtn;
    Button aboutPursuitBtn;
    Button myProfileBtn;
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
        openFragment(HomeFragment.newInstance("", ""));


        findAndSetCurrentUser();

        String currentUserNameString;
        if (currentRole.equals("Student")) {
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
                removeCurrentUser();

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

    public void openFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    BottomNavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener =
      new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override public boolean onNavigationItemSelected(@NonNull MenuItem item) {
          switch (item.getItemId()) {
            case R.id.navigation_home:
              openFragment(HomeFragment.newInstance("", ""));
              return true;
            case R.id.navigation_messages:
              openFragment(MessagesFragment.newInstance("", ""));
              return true;
            case R.id.navigation_profile:
//              openFragment(ProfileFragment.newInstance("", ""));
                if (currentStudent != null) {
                    Intent i = new Intent(LandingActivity.this, StudentProfileActivity.class);
                    startActivity(i);
                } else {
                    Intent i = new Intent(LandingActivity.this, StudentProfileActivity.class);
                    startActivity(i);
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

    private void removeCurrentUser() {
        ((PursuitApplication) this.getApplication()).removeCurrentUser();
    }

}
