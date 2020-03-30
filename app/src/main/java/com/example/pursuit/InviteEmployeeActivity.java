package com.example.pursuit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.os.Bundle;
import android.widget.EditText;
import android.content.Intent;

import com.example.pursuit.models.Company;
import com.example.pursuit.models.EmployeeInvite;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class InviteEmployeeActivity extends AppCompatActivity {
    private static final String TAG = "InviteEmployeeActivity";

    private DatabaseReference dbRef;

    Company currentCompany;
    EditText employeeEmail;

    BottomNavigationView bottomNavigation;


    EmployeeInvite newInvite;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invite_employee);
        bottomNavigation = findViewById(R.id.bottom_navigation);
        bottomNavigation.setOnNavigationItemSelectedListener(navigationItemSelectedListener);

        initializeCurrentCompany();

        dbRef = FirebaseDatabase.getInstance().getReference();
    }

    /* ********DATABASE******** */

    private void writeNewEmployeeInvite(String companyName, String employeeEmail) {
        Log.d(TAG, "in writeNewEmployeeInvite, company: " + companyName + "; email: " + employeeEmail);
        String code = RandomKeyGenerator.randomLowerNumeric(25);
        Log.d(TAG, "code: " + code);
        newInvite = new EmployeeInvite(code, employeeEmail);
        Log.d(TAG, "new invite created");
        dbRef.child("EmployeeInvites").child(currentCompany.getId()).child(code).setValue(newInvite);

        Intent intent = new Intent(this, viewCompanyEmployeeInvites.class);
        startActivity(intent);
    }

    /* ******END DATABASE****** */

    BottomNavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    switch (item.getItemId()) {
                        case R.id.navigation_home:
                            Intent i0 = new Intent(InviteEmployeeActivity.this, LandingActivity.class);
                            startActivity(i0);
                            finish();
                            return true;
                        case R.id.navigation_messages:
                            return true;
                        case R.id.navigation_profile:
                            Intent i2 = new Intent(InviteEmployeeActivity.this, CompanyProfileActivity.class);
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

    public void createEmployeeInvite(View v) {
        employeeEmail = findViewById(R.id.txtCompanyCode);
        Log.d(TAG, employeeEmail.getText().toString());

        writeNewEmployeeInvite(currentCompany.getName(), employeeEmail.getText().toString());
    }
}