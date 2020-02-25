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

    EditText companyName;
    EditText companyPassword;
    EditText companyEmail;
    EditText companyField;
//    Button register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company_registration);

        companyPassword = findViewById(R.id.companyPassword);
        companyName = findViewById(R.id.companyName);
        companyEmail = findViewById(R.id.companyEmail);
        companyField = findViewById(R.id.companyField);
    }

    public void register(View view) {
        Log.d(getClass().getSimpleName(), "in checkIfEmpty");

        String companyNameString = companyName.getText().toString();
        Log.d(getClass().getSimpleName(), companyNameString);

        String companyPasswordString = companyPassword.getText().toString();
        Log.d(getClass().getSimpleName(), companyPasswordString);

        String companyEmailString = companyEmail.getText().toString();
        Log.d(getClass().getSimpleName(), companyEmailString);

        String companyFieldString = companyField.getText().toString();
        Log.d(getClass().getSimpleName(), companyFieldString);

    }
}
