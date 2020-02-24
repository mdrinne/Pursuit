package com.example.pursuit;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class LandingActivity extends AppCompatActivity {

    Button logOutBtn;
    Button aboutPursuitBtn;
    Button myProfileBtn;
    Button viewCompaniesBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing);

        logOutBtn = findViewById(R.id.logOutBtn);
        aboutPursuitBtn = findViewById(R.id.aboutPursuitBtn);
        myProfileBtn = findViewById(R.id.myProfileBtn);
        viewCompaniesBtn = findViewById(R.id.viewCompaniesBtn);

        logOutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
                Intent i = new Intent(LandingActivity.this, myProfileActivity.class);
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

}
