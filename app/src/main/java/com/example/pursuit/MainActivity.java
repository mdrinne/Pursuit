package com.example.pursuit;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.text.TextUtils;

import com.example.pursuit.database.DatabaseHelper;
import com.example.pursuit.database.models.Company;


public class MainActivity extends AppCompatActivity {

    private final String DB_NAME = "pursuit.db";
    private final String USER_TABLE = "Users";
    private final String USER_ID = "id integer primary key";
    private final String USER_COL1 = "Username VARCHAR";
    private final String USER_COL2 = "Password VARCHAR";

    private final String COMPANY_TABLE = "Companies";
    private final String COMPANY_ID = "id integer primary key";
    private final String COMPANY_PSWD = "Password VARCHAR";
    private final String COMPANY_NAME = "CompanyName VARCHAR";
    private final String COMPANY_FIELD = "Field VARCHAR";
    private final String COMPANY_EMAIL = "Email VARCHAR";

    private DatabaseHelper db;

//    SQLiteDatabase db;

    EditText username;
    EditText password;
    Button login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        login = findViewById(R.id.button);
        username = findViewById(R.id.username);
        password = findViewById(R.id.password);

//        login.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Log.d(getClass().getSimpleName(), "in onClick");
//                loginUser(v);
//            }
//        });
    }

    // checks if user input from text field is empty
    boolean isEmpty(EditText text) {
        Log.d(getClass().getSimpleName(), "in isEmpty");
        CharSequence str = text.getText().toString();
        return TextUtils.isEmpty(str);
    }

    // converts user input from text field to a string object
    String toString(EditText text) {
        Log.d(getClass().getSimpleName(), "in toString");
        return text.getText().toString();
    }

    // checks for empty user input fields and displays appropriate alert
    // returns true if either input field is empty
    Boolean checkIfEmpty(EditText username, EditText password, View v) {
        Log.d(getClass().getSimpleName(), "in checkIfEmpty");
        if (isEmpty(username) && isEmpty(password)) {
            Log.d(getClass().getSimpleName(), "both empty");
            Toast.makeText(v.getContext(), "Must Enter Email and Password", 2).show();
            return true;
        }
        else if (isEmpty(username)) {
            Log.d(getClass().getSimpleName(), "user empty");
            Toast.makeText(v.getContext(), "Must Enter Email", 2).show();
            return true;

        }
        else if (isEmpty(password)) {
            Log.d(getClass().getSimpleName(), "password empty");
            Toast.makeText(v.getContext(), "Must Enter Password", 2).show();
            return true;
        }
        else return false;
    }

    // compares user input data to database
    Boolean compareToDB(EditText username, EditText password, View v, Cursor c) {
        Log.d(getClass().getSimpleName(), "in compareToDb");
        if (c != null) {
            if (c.moveToFirst()) {
                String dbPassword = c.getString(c.getColumnIndex("Password"));

                if (dbPassword.equals(toString(password))) {
                    return true;
                }
                else {
                    Log.d(getClass().getSimpleName(), "password did not match");
                    Toast.makeText(v.getContext(), "Incorrect Password", 2).show();
                    return false;
                }
            }
            else {
                Log.d(getClass().getSimpleName(), "user does not exist");
                Toast.makeText(v.getContext(), "User Does Not Exist", 2).show();
                return false;
            }
        }
        else {
            Log.e(getClass().getSimpleName(), "Cursor returned null while querying for user at login");
            return false;
        }
    }

    // completes login verification tasks
//    Boolean checkCredentials(EditText username, EditText password, View v) {
//        Log.d(getClass().getSimpleName(), "in checkCredentials");
//        if (!checkIfEmpty(username, password, v)) {
//            Log.d(getClass().getSimpleName(), "fields not empty");
//            Cursor c = db.rawQuery("SELECT Password FROM " + USER_TABLE +
//                    " WHERE Username = '" + toString(username) + "'", null);
//            Log.d(getClass().getSimpleName(), "created cursor");
//
//            return compareToDB(username, password, v, c);
//        }
//        else return false;
//    }

    // function executed upon click on login button
//    public void loginUser(View view) {
//        Log.d(getClass().getSimpleName(), "in loginUser");
//        username = findViewById(R.id.username);
//        password = findViewById(R.id.password);
//
//        Log.d(getClass().getSimpleName(), toString(username));
//
//
//        if (checkCredentials(username, password, view)) {
//            Intent intent = new Intent(this, LandingActivity.class);
//
//            startActivity(intent);
//        }
//    }

    public void registerUser(View view) {
        Log.d(getClass().getSimpleName(), "register");
        Intent intent = new Intent(this, RegistrationActivity.class);
        startActivity(intent);
    }
}
