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


public class MainActivity extends AppCompatActivity {
    public static final String NO_USER_PASS_MESSAGE = "com.example.pursuit.NO_USER_PASS_MESSAGE";

    private final String DB_NAME = "pursuitDB";
    private final String USER_TABLE = "Users";
    private final String USER_COL1 = "Username VARCHAR";
    private final String USER_COL2 = "Password VARCHAR";

    SQLiteDatabase db;

    EditText username;
    EditText password;
    Button login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        login = findViewById(R.id.button);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginUser(v);
            }
        });

        try {
            db = this.openOrCreateDatabase(DB_NAME, MODE_PRIVATE, null);

            db.execSQL("CREATE TABLE IF NOT EXISTS " + USER_TABLE + " (" + USER_COL1 + ", " + USER_COL2 + ");");
        } catch (SQLiteException se) {
            Log.e(getClass().getSimpleName(), "Could not create or open database");
        }

    }

    // checks if user input from text field is empty
    boolean isEmpty(EditText text) {
        CharSequence str = text.getText().toString();
        return TextUtils.isEmpty(str);
    }

    // converts user input from text field to a string object
    String toString(EditText text) {
        return text.getText().toString();
    }

    // checks for empty user input fields and displays appropriate alert
    // returns false if either input field is empty
    Boolean checkIfEmpty(EditText username, EditText password, View v) {
        if (isEmpty(username) && isEmpty(password)) {
            Toast.makeText(v.getContext(), "Must Enter Email and Password", 2);
            return true;
        }
        else if (isEmpty(username)) {
            Toast.makeText(v.getContext(), "Must Enter Email", 2);
            return true;

        }
        else if (isEmpty(password)) {
            Toast.makeText(v.getContext(), "Must Enter Password", 2);
            return true;
        }
        else return false;
    }

    // completes login verification tasks
    Boolean checkCredentials(EditText username, EditText password, View v) {
        if (!checkIfEmpty()) {
            Cursor c = db.rawQuery("SELECT Password FROM " + USER_TABLE +
                    " WHERE Username = " + toString(username), null);

            if (c == null) {
                Toast.makeText(view.getContext(), "User Does Not Exist", 2);
                return false;
            }
            else {
                String dbPassword = c.getString(c.getColumnIndex("Password"));


            }
        }
    }

    // function executed upon click on login button
    public void loginUser(View view) {

        username = findViewById(R.id.username);
        password = findViewById(R.id.password);


        if (checkCredentials(username, password, view)) {
            Intent intent = new Intent(this, LandingActivity.class);

            startActivity(intent);
        }
    }

    public void registerUser(View view) {
        Intent intent = new Intent(this, RegistrationActivity.class);
        startActivity(intent);
    }
}
