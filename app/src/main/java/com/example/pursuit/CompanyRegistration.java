package com.example.pursuit;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.view.View;
import android.content.Intent;
import android.widget.Toast;

public class CompanyRegistration extends AppCompatActivity {

    EditText companyUsername;
    EditText companyPassword;
    EditText companyName;
    EditText companyEmail;
    EditText companyField;
//    Button register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company_registration);
    }

    public void register(View view) {
        Log.d(getClass().getSimpleName(), "in checkIfEmpty");

        companyUsername = findViewById(R.id.companyUsername);
        Log.d(getClass().getSimpleName(), companyUsername.getText().toString());

        companyPassword = findViewById(R.id.companyPassword);
        Log.d(getClass().getSimpleName(), companyPassword.getText().toString());

        companyName = findViewById(R.id.companyName);
        Log.d(getClass().getSimpleName(), companyName.getText().toString());

        companyEmail = findViewById(R.id.companyEmail);
        Log.d(getClass().getSimpleName(), companyEmail.getText().toString());

        companyField = findViewById(R.id.companyField);
        Log.d(getClass().getSimpleName(), companyField.getText().toString());

    }
}
