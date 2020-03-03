package com.example.pursuit;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
// import android.database.Cursor;
// import android.database.sqlite.SQLiteDatabase;
// import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.text.TextUtils;

import com.example.pursuit.models.Company;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

// import com.example.pursuit.database.DatabaseHelper;
// import com.example.pursuit.database.models.Company;

public class MainActivity extends AppCompatActivity {

    // firebase variables
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mRef;

    EditText username;
    EditText password;
    Button login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // initialize database reference
        mRef = FirebaseDatabase.getInstance().getReference();

        login = findViewById(R.id.button);
        username = findViewById(R.id.username);
        password = findViewById(R.id.password);

//        writeNewCompany(1, "NT", "mr416@ntrs.com", "1234", "banking");

    }

    private void writeNewCompany(String id, String name, String email, String password, String field) {
        Company company = new Company(id, name, email, password, field);

        mRef.child("Users").child("Company").child(name).setValue(company);
    }

    // checks if user input from text field is empty
    // boolean isEmpty(EditText text) {
    // Log.d(getClass().getSimpleName(), "in isEmpty");
    // CharSequence str = text.getText().toString();
    // return TextUtils.isEmpty(str);
    // }
    //
    // // converts user input from text field to a string object
    // String toString(EditText text) {
    // Log.d(getClass().getSimpleName(), "in toString");
    // return text.getText().toString();
    // }

    // checks for empty user input fields and displays appropriate alert
    // returns true if either input field is empty
    // Boolean checkIfEmpty(EditText username, EditText password, View v) {
    // Log.d(getClass().getSimpleName(), "in checkIfEmpty");
    // if (isEmpty(username) && isEmpty(password)) {
    // Log.d(getClass().getSimpleName(), "both empty");
    // Toast.makeText(v.getContext(), "Must Enter Email and Password", 2).show();
    // return true;
    // }
    // else if (isEmpty(username)) {
    // Log.d(getClass().getSimpleName(), "user empty");
    // Toast.makeText(v.getContext(), "Must Enter Email", 2).show();
    // return true;
    //
    // }
    // else if (isEmpty(password)) {
    // Log.d(getClass().getSimpleName(), "password empty");
    // Toast.makeText(v.getContext(), "Must Enter Password", 2).show();
    // return true;
    // }
    // else return false;
    // }

    // compares user input data to database
    // Boolean compareToDB(EditText username, EditText password, View v, Cursor c) {
    // Log.d(getClass().getSimpleName(), "in compareToDb");
    // if (c != null) {
    // if (c.moveToFirst()) {
    // String dbPassword = c.getString(c.getColumnIndex("Password"));
    //
    // if (dbPassword.equals(toString(password))) {
    // return true;
    // }
    // else {
    // Log.d(getClass().getSimpleName(), "password did not match");
    // Toast.makeText(v.getContext(), "Incorrect Password", 2).show();
    // return false;
    // }
    // }
    // else {
    // Log.d(getClass().getSimpleName(), "user does not exist");
    // Toast.makeText(v.getContext(), "User Does Not Exist", 2).show();
    // return false;
    // }
    // }
    // else {
    // Log.e(getClass().getSimpleName(), "Cursor returned null while querying for
    // user at login");
    // return false;
    // }
    // }

    // completes login verification tasks
    // Boolean checkCredentials(EditText username, EditText password, View v) {
    // Log.d(getClass().getSimpleName(), "in checkCredentials");
    // if (!checkIfEmpty(username, password, v)) {
    // Log.d(getClass().getSimpleName(), "fields not empty");
    // Cursor c = db.rawQuery("SELECT Password FROM " + USER_TABLE +
    // " WHERE Username = '" + toString(username) + "'", null);
    // Log.d(getClass().getSimpleName(), "created cursor");
    //
    // return compareToDB(username, password, v, c);
    // }
    // else return false;
    // }

    // function executed upon click on login button
    // public void loginUser(View view) {
    // Log.d(getClass().getSimpleName(), "in loginUser");
    // // username = findViewById(R.id.username);
    // // password = findViewById(R.id.password);

    // // Log.d(getClass().getSimpleName(), toString(username));

    // String loginUsername = username.getText().toString();
    // String loginPassword = password.getText().toString();

    // if (loginUsername.isEmpty() || loginPassword.isEmpty()) {
    // Toast.makeText(view.getContext(), "Please enter username and password.",
    // 2).show();
    // } else {
    // Company c = db.getCompanyForLogin(loginUsername, loginPassword);

    // if (c != null) {
    // Intent intent = new Intent(this, LandingActivity.class);

    // startActivity(intent);
    // } else {
    // Toast.makeText(view.getContext(), "Incorrect username and password.",
    // 2).show();
    // }
    // }

    // if (checkCredentials(username, password, view)) {
    // Intent intent = new Intent(this, LandingActivity.class);
    //
    // startActivity(intent);
    // }
    // }

    public void registerUser(View view) {
        Log.d(getClass().getSimpleName(), "register");
        Intent intent = new Intent(this, RegistrationActivity.class);
        startActivity(intent);
    }
}
