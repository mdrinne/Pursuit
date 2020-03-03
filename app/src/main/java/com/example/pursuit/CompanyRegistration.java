package com.example.pursuit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.view.View;
import android.content.Intent;
import android.widget.Toast;

import com.example.pursuit.models.Company;
import com.example.pursuit.RandomKeyGenerator;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.Query;

import java.util.ArrayList;

public class CompanyRegistration extends AppCompatActivity {

    private static final String TAG = "CompanyRegistration";

    private DatabaseReference mRef;
    private ArrayList<Company> matchedEmails;
    private View view;
    private boolean match;
    private String checkEmail;

    EditText companyName;
    EditText companyEmail;
    EditText companyPassword;
    EditText companyReEnterPassword;
    EditText companyField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company_registration);

        mRef = FirebaseDatabase.getInstance().getReference();

        matchedEmails = new ArrayList<>();
    }

    /* ********DATABASE******** */

    ValueEventListener emailListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            matchedEmails = new ArrayList<>();
            Log.d(TAG, "in emailListener onDataChange");
            if (dataSnapshot.exists()) {
                Log.d(TAG, "Snapshot exists");
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Log.d(TAG, "looping in snapshot");
                    Company company = snapshot.getValue(Company.class);
                    if (company == null) {
                        Log.d(TAG, "company is null");
                    } else {
                        Log.d(TAG, "company exists: " + company.getEmail());
                    }
                    matchedEmails.add(company);
                }
            }
            processRequest();
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    };

    private void writeNewCompany(String name, String email, String password, String field) {
        String id = RandomKeyGenerator.randomAlphaNumeric(16);
        Company newCompany = new Company(id, name, email, password, field);
        mRef.child("Companies").child(id).setValue(newCompany);
    }

    /* ******END DATABASE****** */

    private boolean emailExists(String email) {
        Log.d(TAG, "In emailExists; email: " + email);

        String size = String.valueOf(matchedEmails.size());

        Log.d(TAG, "matchedEmails size: " + size);

        if (matchedEmails.size() > 0) {
            match = true;
            return true;
        } else {
            return false;
        }
    }

    public String toString(EditText text) {
        return text.getText().toString();
    }

    // Checks If Password And Re-Entered Password Match
    public boolean passwordMatch() {
        if (toString(companyPassword).equals(toString(companyReEnterPassword)))
            return true;
        Toast.makeText(view.getContext(), "Passwords Did Not Match", Toast.LENGTH_SHORT).show();
        return false;
    }

    boolean isEmail(EditText text) {
        CharSequence email = text.getText().toString();
        if (!(!TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches())) {
            Toast.makeText(view.getContext(), "Invalid Email", Toast.LENGTH_SHORT).show();
            return false;
        } else
            return true;
    }

    boolean isEmpty(EditText text) {
        CharSequence str = text.getText().toString();
        return TextUtils.isEmpty(str);
    }

    public boolean checkForEmpties() {
        if (isEmpty(companyName) || isEmpty(companyEmail) || isEmpty(companyField) || isEmpty(companyPassword)) {
            Toast.makeText(view.getContext(), "All Fields are Required", Toast.LENGTH_SHORT).show();
            return true;
        }
        return false;
    }

    public void processRequest() {
        match = false;

        if (!checkForEmpties()) {
            Log.d(TAG, "no empty fields");

            if (isEmail(companyEmail)) {
                Log.d(TAG, "email is valid");

                if (passwordMatch()) {
                    Log.d(TAG, "passwords match");

                    if (!emailExists(toString(companyEmail))) {
                        Log.d(TAG, "email not taken");

                        writeNewCompany(toString(companyName), toString(companyEmail), toString(companyPassword),
                                toString(companyField));
                        Intent intent = new Intent(this, LandingActivity.class);
                        startActivity(intent);
                    } else {
                        Log.d(TAG, "email is already taken");
                        Toast.makeText(view.getContext(), "Email is Already Taken", Toast.LENGTH_LONG).show();
                    }
                }
            }
        }

        if (match == true) {
            Log.d(TAG, "RESULT: true");

        } else {
            Log.d(TAG, "RESULT: false");
        }
    }

    public void register(View v) {
        view = v;
        companyName = findViewById(R.id.companyName);
        companyEmail = findViewById(R.id.companyEmail);
        companyPassword = findViewById(R.id.companyPassword);
        companyReEnterPassword = findViewById(R.id.companyReEnterPassword);
        companyField = findViewById(R.id.companyField);

        checkEmail = toString(companyEmail);

        Query emailQuery = mRef.child("Companies").orderByChild("email").equalTo(checkEmail);

        if (emailQuery == null) {
            Log.d(TAG, "Email query is null");
        } else {
            Log.d(TAG, "Email query is not null");
        }

        emailQuery.addListenerForSingleValueEvent(emailListener);
    }

}
