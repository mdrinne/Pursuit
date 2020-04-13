package com.example.pursuit;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Button;


public class CreateMessageActivity extends AppCompatActivity {

    EditText recipient;
    EditText content;

    Button sendMessageBtn;
    Button cancelBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_message);

        recipient = findViewById(R.id.recipientText);
        content = findViewById(R.id.contentText);
        sendMessageBtn = findViewById(R.id.sendMessageBtn);
        cancelBtn = findViewById(R.id.cancelBtn);

        sendMessageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(CreateMessageActivity.this, ConversationsActivity.class);
                startActivity(i);
                finish();
            }
        });

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(CreateMessageActivity.this, ConversationsActivity.class);
                startActivity(i);
                finish();
            }
        });
    }



}
