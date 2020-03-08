package com.example.pursuit;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.example.pursuit.models.Company;

public class CompanyProfileActivity  extends AppCompatActivity{

    Company currentCompany;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company_profile);

        initializeCurrentCompany();
    }

    private void initializeCurrentCompany() {
        currentCompany = ((PursuitApplication) this.getApplicationContext()).getCurrentCompany();
    }
}
