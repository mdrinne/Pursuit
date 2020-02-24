package com.example.pursuit;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class aboutPursuitActivity extends AppCompatActivity {

    Button backToLandingbtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_pursuit);

        backToLandingbtn = findViewById(R.id.backToLandingBtn);

        backToLandingbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(aboutPursuitActivity.this, LandingActivity.class);
                startActivity(i);
                finish();
            }
        });
    }
}
