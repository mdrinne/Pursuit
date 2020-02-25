package com.example.pursuit;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.print.PrinterId;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class StudentRegistration extends AppCompatActivity {

    private final String DB_NAME = "pursuit";
    private final String APPLICANT_TABLE = "Applicants";
    private final String APPLICANT_COL0 = "id INTEGER PRIMARY KEY";
    private final String APPLICANT_COL1 = "firstname VARCHAR";
    private final String APPLICANT_COL2 = "lastname VARCHAR";
    private final String APPLICANT_COL3 = "university VARCHAR";
    private final String APPLICANT_COL4 = "major VARCHAR";
    private final String APPLICANT_COL5 = "minor VARCHAR";
    private final String APPLICANT_COL6 = "gpa VARCHAR";
    private final String APPLICANT_COL7 = "bio VARCHAR";
    private final String APPLICANT_COL8 = "email VARCHAR";
    private final String APPLICANT_COL9 = "username VARCHAR";
    private final String APPLICANT_COL10 = "password VARCHAR";

    SQLiteDatabase db;

    EditText firstName;
    EditText lastName;
    EditText university;
    EditText major;
    EditText minor;
    EditText gpa;
    EditText bio;
    EditText email;
    EditText username;
    EditText password1;
    EditText password2;
    Button btnFinish;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_registration);

        btnFinish = findViewById(R.id.btnFinish);

        btnFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(getClass().getSimpleName(), "finish click");
                registerStudent(v);
            }
        });

        try {
            db = this.openOrCreateDatabase(DB_NAME, MODE_PRIVATE, null);

            db.execSQL("CREATE TABLE IF NOT EXISTS " + APPLICANT_TABLE + " (" +
                        APPLICANT_COL0 + ", " + APPLICANT_COL1 + ", " +
                        APPLICANT_COL2 + ", " + APPLICANT_COL3 + ", " + APPLICANT_COL4 + ", " +
                        APPLICANT_COL5 + ", " + APPLICANT_COL6 + ", " + APPLICANT_COL7 + ", " +
                        APPLICANT_COL8 + ", " + APPLICANT_COL9 + ", " + APPLICANT_COL10 + ");");
        } catch (SQLiteException se) {
            Log.e(getClass().getSimpleName(), "Student Register: Could not create or open database");
        }
    }

    boolean isEmail(EditText text, View v) {
        CharSequence email = text.getText().toString();
        if (!(!TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches())) {
            Toast.makeText(v.getContext(), "Invalid Email", 2).show();
            return false;
        }
        else return true;
    }

    boolean isEmpty(EditText text) {
        CharSequence str = text.getText().toString();
        return TextUtils.isEmpty(str);
    }

    String toString(EditText text) {
        return text.getText().toString();
    }

    public boolean checkForEmpties(View v) {
        if (isEmpty(firstName)) {
            Toast.makeText(v.getContext(), "All Fields are Required", 2).show();
            return true;
        }
        else if (isEmpty(lastName)) {
            Toast.makeText(v.getContext(), "All Fields are Required", 2).show();
            return true;
        }
        else if (isEmpty(university)) {
            Toast.makeText(v.getContext(), "All Fields are Required", 2).show();
            return true;
        }
        else if (isEmpty(major)) {
            Toast.makeText(v.getContext(), "All Fields are Required", 2).show();
            return true;
        }
        else if (isEmpty(minor)) {
            Toast.makeText(v.getContext(), "All Fields are Required", 2).show();
            return true;
        }
        else if (isEmpty(gpa)) {
            Toast.makeText(v.getContext(), "All Fields are Required", 2).show();
            return true;
        }
        else if (isEmpty(bio)) {
            Toast.makeText(v.getContext(), "All Fields are Required", 2).show();
            return true;
        }
        else if (isEmpty(email)) {
            Toast.makeText(v.getContext(), "All Fields are Required", 2).show();
            return true;
        }
        else if (isEmpty(username)) {
            Toast.makeText(v.getContext(), "All Fields are Required", 2).show();
            return true;
        }
        else if (isEmpty(password1)) {
            Toast.makeText(v.getContext(), "All Fields are Required", 2).show();
            return true;
        }
        else if (isEmpty(password2)) {
            Toast.makeText(v.getContext(), "All Fields are Required", 2).show();
            return true;
        }
        else return false;
    }

    public boolean passwordMatch(View v) {
        if (toString(password1).equals(toString(password2)))
            return true;
        Toast.makeText(v.getContext(), "Passwords Did Not Match", 2).show();
        return false;
    }

    public boolean usernameTaken(View v) {
        Log.d(getClass().getSimpleName(), "in usernameTaken");
        // issue HERE
        Cursor c = db.rawQuery("SELECT username FROM " + APPLICANT_TABLE + " WHERE username = '"
                                    + toString(username) + "'", null);
        Log.d(getClass().getSimpleName(), "cursor retrieved");

        if (c != null) {
            if (c.moveToFirst()) {
                Toast.makeText(v.getContext(), "Username is Already Taken", 2).show();
                return true;
            }
            else return false;
        }
        Log.e(getClass().getSimpleName(), "Cursor returned null while searching of username is taken");
        return false;
    }

    public boolean emailTaken(View v) {
        Cursor c = db.rawQuery("SELECT email FROM " + APPLICANT_TABLE + " WHERE email = '"
                + toString(email) + "';", null);

        if (c != null) {
            if (c.moveToFirst()) {
                Toast.makeText(v.getContext(), "Email is Already Taken", 2).show();
                return true;
            }
            else return false;
        }
        Log.e(getClass().getSimpleName(), "Cursor returned null while searching of username is taken");
        return false;
    }

    public boolean insertDB() {
        try {
            db.execSQL("INSERT INTO " + APPLICANT_TABLE + " VALUES (" + firstName + ", " + lastName +
                    ", " + university + ", " + major + ", " + minor + ", " + gpa + ", " + bio +
                    email + ", " + username + ", " + password1 + ", " + password2 + ");");
            Log.d(getClass().getSimpleName(), "inserted user");
        } catch (SQLiteException se) {
            Log.e(getClass().getSimpleName(), "Failed to register student");
        }
        return true;
    }

    public boolean processRequest(View v) {
        if(!checkForEmpties(v)) {
            Log.d(getClass().getSimpleName(), "no empty fields");
            if (isEmail(email, v)) {
                Log.d(getClass().getSimpleName(), "email is valid");
                if (passwordMatch(v)) {
                    Log.d(getClass().getSimpleName(), "passwords match");
                    if (!usernameTaken(v)) {
                        Log.d(getClass().getSimpleName(), "username not taken");
                        if (!emailTaken(v)) {
                            Log.d(getClass().getSimpleName(), "email not taken");
                            if (insertDB()) {
                                return true;
                            }
                        }
                    }
                }
            }
        }
        return false;
    }

    // called by onClick, attempts to register a student
    public void registerStudent(View v) {
        firstName = findViewById(R.id.txtFirstName);
        lastName = findViewById(R.id.txtLastName);
        university = findViewById(R.id.txtUniversity);
        major = findViewById(R.id.txtMajor);
        minor = findViewById(R.id.txtMinor);
        gpa = findViewById(R.id.txtGPA);
        bio = findViewById(R.id.txtBio);
        email = findViewById(R.id.txtEmail);
        username = findViewById(R.id.txtUsername);
        password1 = findViewById(R.id.txtPassword);
        password2 = findViewById(R.id.txtReEnterPassword);

        if (processRequest(v)) {
//            db.close();

            Intent intent = new Intent(this, LandingActivity.class);
            startActivity(intent);
        }
    }
}
