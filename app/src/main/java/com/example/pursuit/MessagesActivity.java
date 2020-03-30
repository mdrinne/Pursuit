package com.example.pursuit;

import com.example.pursuit.models.Employee;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

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

import java.util.ArrayList;

public class MessagesActivity extends AppCompatActivity {

    private static final String TAG = "MessagesActivity";
  
    BottomNavigationView bottomNavigation;
    private DatabaseReference dbRef;

    private View view;
    EditText newConversationUsername;
    Button createConversationButton;

    Student currentStudent = null;
    Company currentCompany = null;
    String  currentRole = null;

    private Student matchedStudentUsername;
    private Employee matchedEmployeeUsername;
    private String checkUsername;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messages);
        bottomNavigation = findViewById(R.id.bottom_navigation);
        bottomNavigation.setOnNavigationItemSelectedListener(navigationItemSelectedListener);

        findAndSetCurrentUser();
        dbRef = FirebaseDatabase.getInstance().getReference();

    }


    ValueEventListener studentUsernameListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            matchedStudentUsername = null;
            Log.d(TAG, "In usernameListener onDataChange");
            if (dataSnapshot.exists()) {
                Log.d(TAG, "Snapshot exists");
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Log.d(TAG, "looping in snapshot");
                    Student student = snapshot.getValue(Student.class);
                    if (student == null) {
                        Log.d(TAG, "student is null");
                    } else {
                        Log.d(TAG, "student exists: " + student.getUsername());
                    }
                    matchedStudentUsername = student;
                }
            }

            postStudentUsernameListener();
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {
            Log.e(TAG, databaseError.toString());
        }
    };

    public void postStudentUsernameListener() {
        if (matchedStudentUsername != null) {
            // create new conversation with this student
            Toast.makeText(view.getContext(), "This is a student username!", Toast.LENGTH_LONG).show();
        } else {
            Query employeeUsernameQuery = dbRef.child("Employees").orderByChild("username").equalTo(checkUsername);
            employeeUsernameQuery.addListenerForSingleValueEvent(employeeUsernameListener);
        }
    }

    ValueEventListener employeeUsernameListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            matchedEmployeeUsername = null;
            if (dataSnapshot.exists()) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Employee employee = snapshot.getValue(Employee.class);
                    matchedEmployeeUsername = employee;
                }
            }
            postEmployeeUsernameListener();
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    };

    public void postEmployeeUsernameListener() {
        if (matchedEmployeeUsername != null) {
            // create new conversation with this employee
            Toast.makeText(view.getContext(), "This is an employee username!", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(view.getContext(), "This username does not exist!", Toast.LENGTH_LONG).show();
        }
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

    }

    public void createConversation(View v) {
        newConversationUsername = findViewById(R.id.newConversationUsername);

        view = v;
        checkUsername = toString(newConversationUsername);

        Query studentUsernameQuery = dbRef.child("Students").orderByChild("username").equalTo(checkUsername);
        studentUsernameQuery.addListenerForSingleValueEvent(studentUsernameListener);
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

    // Converts EditText Type To String
    public String toString(EditText text) {
        return text.getText().toString();
    }

}