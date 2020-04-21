package com.example.pursuit;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class DiscoverActivity extends AppCompatActivity {
    private final String TAG = "DiscoverActivity";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_discover);
        ottomNavigation = findViewById(R.id.bottom_navigation);
        bottomNavigation.setOnNavigationItemSelectedListener(navigationItemSelectedListener);
    }

}
