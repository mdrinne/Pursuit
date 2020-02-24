package com.example.pursuit;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.ToggleButton;

public class RegistrationPage extends AppCompatActivity {

    EditText emailAddress;
    EditText userPassword;
    EditText confirmedPassword;
    EditText txtRole;
    ToggleButton toggleUserType;
    Button btnContinue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration_page);

        emailAddress = findViewById(R.id.emailAddress);

        btnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkDataEntered();
            }
        });
    }

    boolean isEmail(EditText text) {
        CharSequence email = text.getText().toString();
        return (!TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches());
    }

    boolean isEmpty(EditText text) {
        CharSequence str = text.getText().toString();
        return TextUtils.isEmpty(str);
    }


    void checkDataEntered() {
        boolean isValid = true;
        if (!isEmail(emailAddress)) {
            emailAddress.setError("You must enter a valid email address!");
            isValid = false;
        }
        if (isEmpty(userPassword)) {
            userPassword.setError("You must enter password to login!");
            isValid = false;
        }
        if (userPassword.getText().toString().length() < 4) {
            userPassword.setError("Password must be at least 4 chars long!");
            isValid = false;
        }
        if (!userPassword.getText().toString().equals(confirmedPassword.getText().toString())) {
            userPassword.setError("Passwords do not match!");
            confirmedPassword.setError("Passwords do not match!");
            isValid = false;
        }
        if (isValid) {
            if (toggleUserType.isChecked()) {
                Intent i = new Intent(RegistrationPage.this, StudentRegistration.class);
                startActivity(i);
            }
            else {
                Intent i = new Intent(RegistrationPage.this, CompanyRegistration.class);
                startActivity(i);
            }
        }
    }
}
