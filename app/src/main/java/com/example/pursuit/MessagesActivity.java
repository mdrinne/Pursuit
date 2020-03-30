package com.example.pursuit;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.Query;

import androidx.appcompat.app.AppCompatActivity;
import androidx.annotation.NonNull;

import com.example.pursuit.models.Company;
import com.example.pursuit.models.Student;

public class MessagesActivity extends AppCompatActivity {
  
    BottomNavigationView bottomNavigation;
    private DatabaseReference dbRef;

    TextView newConversationUsername;
    Button createConversationButton;

    Student currentStudent = null;
    Company currentCompany = null;
    String  currentRole = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messages);
        bottomNavigation = findViewById(R.id.bottom_navigation);
        bottomNavigation.setOnNavigationItemSelectedListener(navigationItemSelectedListener);

        findAndSetCurrentUser();
        dbRef = FirebaseDatabase.getInstance().getReference();

    }

    public void showCreateConversation(View v) {
        newConversationUsername = findViewById(R.id.newConversationUsername);
        createConversationButton = findViewById(R.id.createConversationButton);

        if (newConversationUsername.getVisibility() == View.INVISIBLE && createConversationButton.getVisibility() == View.INVISIBLE) {
            newConversationUsername.setVisibility(View.VISIBLE);
            createConversationButton.setVisibility(View.VISIBLE);
        } else {
            newConversationUsername.setVisibility(View.INVISIBLE);
            createConversationButton.setVisibility(View.INVISIBLE);
        }

//        Intent newConversation = new Intent(MessagesActivity.this, newConversationActivity.class);
//        startActivity(newConversation);
//        finish();
    }

    BottomNavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener =
      new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
            case R.id.navigation_home:
                Intent home = new Intent(MessagesActivity.this, LandingActivity.class);
                startActivity(home);
                finish();
                return true;
            case R.id.navigation_messages:
                return true;
            case R.id.navigation_profile:
                if (currentStudent != null) {
                    Intent i = new Intent(MessagesActivity.this, StudentProfileActivity.class);
                    startActivity(i);
                    finish();
                } else {
                    Intent profile = new Intent(MessagesActivity.this, CompanyProfileActivity.class);
                    startActivity(profile);
                    finish();
                }
              return true;
          }
          return false;
        }
      };

    private void findAndSetCurrentUser() {
        if (((PursuitApplication) this.getApplication()).getCurrentStudent() != null) {
            currentStudent = ((PursuitApplication) this.getApplication()).getCurrentStudent();
        } else {
            currentCompany = ((PursuitApplication) this.getApplication()).getCurrentCompany();
        }
        currentRole = ((PursuitApplication) this.getApplication()).getRole();
    }

}