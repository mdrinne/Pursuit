package com.example.pursuit;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    public static final String NO_USER_PASS_MESSAGE = "com.example.pursuit.NO_USER_PASS_MESSAGE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void loginUser(View view) {

        EditText userText = (EditText) findViewById(R.id.username);
        String username = userText.getText().toString();

        EditText passwordText = (EditText) findViewById(R.id.password);
        String password = passwordText.getText().toString();

        if (!username.isEmpty() && !password.isEmpty()) {
            if (checkUserame() && checkPassword()) {

            }
        } else {
            Intent intent = new Intent(this, RegistrationActivity.class);
            intent.putExtra(NO_USER_PASS_MESSAGE, "You must enter both a username and password.");
            startActivity(intent);
        }

        Intent intent = new Intent(this, RegistrationActivity.class);
        startActivity(intent);
    }

    public void registerUser(View view) {
        Intent intent = new Intent(this, RegistrationActivity.class);
        startActivity(intent);
    }

}
