package com.example.pursuit;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.view.View;
import android.content.Intent;
import android.widget.Toast;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import com.example.pursuit.database.DatabaseHelper;
import com.example.pursuit.database.models.Company;

public class CompanyRegistration extends AppCompatActivity {

    EditText companyName;
    EditText companyEmail;
    EditText companyPassword;
    EditText companyField;
    private DatabaseHelper db;
    // Button register;

    // private final String DB_NAME = "pursuit.db";
    // SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company_registration);

        companyPassword = findViewById(R.id.companyPassword);
        companyName = findViewById(R.id.companyName);
        companyEmail = findViewById(R.id.companyEmail);
        companyField = findViewById(R.id.companyField);

        db = new DatabaseHelper(this);
    }

    public void register(View view) {
        boolean valid = true;
        Log.d(getClass().getSimpleName(), "in checkIfEmpty");

        String companyNameString = companyName.getText().toString();
        Log.d(getClass().getSimpleName(), companyNameString);

        String companyEmailString = companyEmail.getText().toString();
        Log.d(getClass().getSimpleName(), companyEmailString);

        String companyPasswordString = companyPassword.getText().toString();
        Log.d(getClass().getSimpleName(), companyPasswordString);

        String companyFieldString = companyField.getText().toString();
        Log.d(getClass().getSimpleName(), companyFieldString);

        if (companyNameString.isEmpty() || companyEmailString.isEmpty() || companyPasswordString.isEmpty()) {
            Toast.makeText(view.getContext(), "Please fill out all required fields.", 2).show();
            valid = false;
        }

        if (valid) {
            long res = db.insertCompany(companyNameString, companyEmailString, companyPasswordString, companyFieldString);
            Log.d(getClass().getSimpleName(), Long.toString(res));
            Intent intent = new Intent(this, LandingActivity.class);
            startActivity(intent);
        }

        // if (!companyAlreadyExists(companyEmailString, companyPasswordString)) {
        //
        // }
    }

}
