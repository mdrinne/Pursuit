package com.example.pursuit;

import com.example.pursuit.models.Conversation;
import com.example.pursuit.models.Employee;
import com.example.pursuit.ConversationAdapter;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

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

    private Student matchedStudentUsername;
    private Employee matchedEmployeeUsername;
    private String checkUsername;
    private ConversationAdapter conversationAdapter;
    private Conversation newConversation;
    private ListView conversationsView;
    private ArrayList<Conversation> myConversations;

    private static final String TAG = "MessagesActivity";

    private DatabaseReference dbRef;

    private View view;
    private EditText newConversationUsername;

    private Student currentStudent = null;
    private Employee currentEmployee = null;
    private Company currentCompany = null;
    private String  currentRole = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messages);
        BottomNavigationView bottomNavigation = findViewById(R.id.bottom_navigation);
        bottomNavigation.setOnNavigationItemSelectedListener(navigationItemSelectedListener);

        findAndSetCurrentUser();
        dbRef = FirebaseDatabase.getInstance().getReference();

        getMyConversations();

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
            writeNewConversations();

            conversationAdapter.add(newConversation);
            conversationsView.setSelection(conversationsView.getCount() - 1);

            Toast.makeText(view.getContext(), "Conversation Created!", Toast.LENGTH_LONG).show();
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
            writeNewConversations();

            conversationAdapter.add(newConversation);
            conversationsView.setSelection(conversationsView.getCount() - 1);

            Toast.makeText(view.getContext(), "Conversation Created!", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(view.getContext(), "This username does not exist!", Toast.LENGTH_LONG).show();
        }
    }

    ValueEventListener myConversationsListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

            myConversations = new ArrayList<>();

            if (dataSnapshot.exists()) {
                Log.d(TAG, "Snapshot exists for myConversations");
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Log.d(TAG, "Looping in myConversations snapshot");

                    Conversation conversation = snapshot.getValue(Conversation.class);

                    if (conversation == null) {
                        Log.d(TAG, "Conversation is null");
                    } else {
                        Log.d(TAG, "conversation is not null");
                        myConversations.add(conversation);
                    }
                }
            }

            postMyConversationsListener();
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    };

    public void postMyConversationsListener() {
        conversationAdapter = new ConversationAdapter(this, myConversations);
        conversationsView = findViewById(R.id.conversations_view);
        conversationsView.setAdapter(conversationAdapter);
        conversationAdapter.addAll(myConversations);
    }

    public void showCreateConversation(View v) {
        newConversationUsername = findViewById(R.id.newConversationUsername);
        Button createConversationButton = findViewById(R.id.createConversationButton);

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

    public void writeNewConversations() {
        String otherUserId;
        String otherUserUsername;
        String otherUserRole;

        String counterpartOtherUserId;
        String counterpartOtherUserUsername;
        String counterpartOtherUserRole;

        if (matchedStudentUsername != null) {
            otherUserId = matchedStudentUsername.getId();
            otherUserUsername = matchedStudentUsername.getUsername();
            otherUserRole = "Student";
        } else if (matchedEmployeeUsername != null) {
            otherUserId = matchedEmployeeUsername.getId();
            otherUserUsername = matchedEmployeeUsername.getUsername();
            otherUserRole = "Employee";
        } else {
            Toast.makeText(view.getContext(), "Something went wrong!", Toast.LENGTH_LONG).show();
            return;
        }

        String id = RandomKeyGenerator.randomAlphaNumeric(16);
        newConversation = new Conversation(id, otherUserId, otherUserUsername, otherUserRole);

        // Add the newConversation under the Student
        // Students/Employees > id > Conversations > id > newConversation
        if (currentStudent != null) {
            dbRef.child("Students").child(currentStudent.getId()).child("Conversations").child(id).setValue(newConversation);
            counterpartOtherUserId = currentStudent.getId();
            counterpartOtherUserUsername = currentStudent.getUsername();
            counterpartOtherUserRole = "Student";
        } else {
            dbRef.child("Employees").child(currentEmployee.getId()).child("Conversations").child(id).setValue(newConversation);
            counterpartOtherUserId = currentEmployee.getId();
            counterpartOtherUserUsername = currentEmployee.getUsername();
            counterpartOtherUserRole = "Employee";
        }

        // Add the newConversation's counterpart to the user/company it is with
        String counterpartId = RandomKeyGenerator.randomAlphaNumeric(16);
        Conversation counterpart = new Conversation(counterpartId, counterpartOtherUserId, counterpartOtherUserUsername, counterpartOtherUserRole);
        if (matchedStudentUsername != null) {
            dbRef.child("Students").child(matchedStudentUsername.getId()).child("Conversations").child(id).setValue(counterpart);
        } else {
            dbRef.child("Employees").child(matchedEmployeeUsername.getId()).child("Conversations").child(id).setValue(counterpart);
        }

        conversationAdapter.add(newConversation);
    }

    public void getMyConversations() {
        // get all conversations
        DatabaseReference conversationsRef;
        if (currentStudent != null) {
            conversationsRef = dbRef.child("Students").child(currentStudent.getId()).child("Conversations");
        } else {
            conversationsRef = dbRef.child("Employees").child(currentEmployee.getId()).child("Conversations");
        }

        conversationsRef.addValueEventListener(myConversationsListener);
    }

    private void findAndSetCurrentUser() {
        if (((PursuitApplication) this.getApplication()).getCurrentStudent() != null) {
            currentStudent = ((PursuitApplication) this.getApplication()).getCurrentStudent();
        } else if (((PursuitApplication) this.getApplication()).getCurrentEmployee() != null) {
            currentEmployee = ((PursuitApplication) this.getApplication()).getCurrentEmployee();
        } else {
            currentCompany = ((PursuitApplication) this.getApplication()).getCurrentCompany();
        }
        currentRole = ((PursuitApplication) this.getApplication()).getRole();
    }

    // Converts EditText Type To String
    public String toString(EditText text) {
        return text.getText().toString();
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

}