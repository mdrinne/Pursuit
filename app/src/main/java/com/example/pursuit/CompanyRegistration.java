package com.example.pursuit;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

public class CompanyRegistration extends AppCompatActivity {

    EditText companyUsername;
    EditText companyPassword;
    EditText companyName;
    EditText companyField;
    EditText companyEmail;
    Button register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company_registration);


    }
}
