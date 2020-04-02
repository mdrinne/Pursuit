package com.example.pursuit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;
import android.view.View;
import android.widget.Toast;
import android.widget.Button;


import com.example.pursuit.models.Company;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class CompanyProfileActivity extends AppCompatActivity{
    private static final String TAG = "CompanyProfileActivity";

    TextView companyName;
    Company currentCompany;
    BottomNavigationView bottomNavigation;

    String currentRole;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company_profile);
        bottomNavigation = findViewById(R.id.bottom_navigation);
        bottomNavigation.setOnNavigationItemSelectedListener(navigationItemSelectedListener);

        initializeCurrentCompany();
        initializeCurrentRole();

        companyName = findViewById(R.id.txtCompanyName);
        companyName.setText(currentCompany.getName());

        Button viewOpps = findViewById(R.id.btnViewOpportunities);
        viewOpps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(CompanyProfileActivity.this, ViewOpportunities.class);
                startActivity(i);

            }
        });
    }

    BottomNavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    switch (item.getItemId()) {
                        case R.id.navigation_home:
                            Intent i0 = new Intent(CompanyProfileActivity.this, LandingActivity.class);
                            startActivity(i0);
                            finish();
                            return true;
                        case R.id.navigation_messages:
                            Intent i1 = new Intent(CompanyProfileActivity.this, MessagesActivity.class);
                            startActivity(i1);
                            finish();
                            return true;
                        case R.id.navigation_profile:
                            Intent i2 = new Intent(CompanyProfileActivity.this, CompanyProfileActivity.class);
                            startActivity(i2);
                            finish();
                            return true;
                    }
                    return false;
                }
            };

    private void initializeCurrentCompany() {
        Log.d(TAG, "initializing company");
        currentCompany = ((PursuitApplication) this.getApplicationContext()).getCurrentCompany();
    }

    private void initializeCurrentRole() {
        currentRole = ((PursuitApplication) this.getApplicationContext()).getRole();
    }

    public void inviteEmployee(View v) {
        Log.d(TAG, "inviting");

        if (currentRole.equals("Employee")) {
            Toast.makeText(v.getContext(), "Only Admin Has Access To Invite", Toast.LENGTH_SHORT).show();
        } else {
            Intent intent = new Intent(this, InviteEmployeeActivity.class);
            startActivity(intent);
        }
    }

    public void viewInvites(View v) {
        Intent intent = new Intent(this, viewCompanyEmployeeInvites.class);
        startActivity(intent);
    }
}