package com.example.pursuit;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.view.View;


import com.example.pursuit.models.Company;

public class CompanyProfileActivity extends AppCompatActivity{
    private static final String TAG = "CompanyProfileActivity";

    TextView companyName;
    Company currentCompany;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company_profile);

        initializeCurrentCompany();

        companyName = findViewById(R.id.txtCompanyName);
        companyName.setText(currentCompany.getName());
    }

    private void initializeCurrentCompany() {
        Log.d(TAG, "initializing company");
        currentCompany = ((PursuitApplication) this.getApplicationContext()).getCurrentCompany();
    }

    public void inviteEmployee(View v) {
        Log.d(TAG, "inviting");
        Intent intent = new Intent(this, InviteEmployeeActivity.class);
        startActivity(intent);
    }
}
