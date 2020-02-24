package com.example.pursuit;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void loginUser(View view) {
        username = findViewById(R.id.username);
        password = findViewById(R.id.password);

        if (!username.isEmpty() && !password.isEmpty()) {

        } else {
            Intent intent = new Intent(this, RegistrationActivity.class);
            startActivity(intent);
        }

        Intent intent = new Intent(this, RegistrationActivity.class);

        startActivity(intent);

    }

    public void registerUser(View view) {
        Intent intent = new Intent(this, RegistrationActivity.class);
        startActivity(intent);
    }

}
