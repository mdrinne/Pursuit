package com.example.pursuit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;
import android.view.View;


import com.example.pursuit.models.Company;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class CompanyProfileActivity extends AppCompatActivity{
    private static final String TAG = "CompanyProfileActivity";

    TextView companyName;
    Company currentCompany;
    BottomNavigationView bottomNavigation;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company_profile);
        bottomNavigation = findViewById(R.id.bottom_navigation);
        bottomNavigation.setOnNavigationItemSelectedListener(navigationItemSelectedListener);

        initializeCurrentCompany();

        companyName = findViewById(R.id.txtCompanyName);
        companyName.setText(currentCompany.getName());
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

    public void inviteEmployee(View v) {
        Log.d(TAG, "inviting");
        Intent intent = new Intent(this, InviteEmployeeActivity.class);
        startActivity(intent);
    }

    public void viewInvites(View v) {
        Intent intent = new Intent(this, viewCompanyEmployeeInvites.class);
        startActivity(intent);
    }
}