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

    boolean isEmpty(EditText text) {
        CharSequence str = text.getText().toString();
        return TextUtils.isEmpty(str);
    }

    String toString(EditText text) {
        return text.getText().toString();
    }

    public void loginUser(View view) {

        username = findViewById(R.id.username);
        password = findViewById(R.id.password);


        if (isEmpty(username) && isEmpty(password)) {
            Toast.makeText(view.getContext(), "Must Enter Email and Password", 2);
        }
        else if (isEmpty(username)) {
            Toast.makeText(view.getContext(), "Must Enter Email", 2);
        }
        else if (isEmpty(password)) {
            Toast.makeText(view.getContext(), "Must Enter Password", 2);
        }
        else {
//            Cursor c = db.rawQuery("SELECT Username");
//
//            boolean dbUsername;
        }


        Intent intent = new Intent(this, LandingActivity.class);

        startActivity(intent);
    }

    public void registerUser(View view) {
        Intent intent = new Intent(this, RegistrationActivity.class);
        startActivity(intent);
    }
}
