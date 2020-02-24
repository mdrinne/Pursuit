package com.example.pursuit;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class viewCompaniesActivity extends AppCompatActivity {

    Button backBtnCompanies;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_companies);

        backBtnCompanies = findViewById(R.id.backBtnCompanies);

        backBtnCompanies.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(viewCompaniesActivity.this, LandingActivity.class);
                startActivity(i);
                finish();
            }
        });

    }
}
