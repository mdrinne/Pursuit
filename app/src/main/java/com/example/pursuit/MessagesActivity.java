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
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import android.content.Context;

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
import com.example.pursuit.models.Employee;
import com.example.pursuit.models.Conversation;

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
  
    BottomNavigationView bottomNavigation;
    private DatabaseReference dbRef;

    private View view;
    EditText newConversationUsername;
    Button createConversationButton;

    Student currentStudent = null;
    Employee currentEmployee = null;
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
            writeNewConversation();

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
            writeNewConversation();

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
                    ArrayList<String> cIds = (ArrayList<String>) snapshot.child("userIds").getValue();
                    conversation.setUserIds(cIds);
                    if (conversation == null) {
                        Log.d(TAG, "Conversation is null");
                    } else {
                        Log.d(TAG, "conversation is not null");

                        ArrayList<String> conversationIds = conversation.getUserIds();
                        if (currentStudent != null) {
                            if (conversationIds.contains(currentStudent.getId())) {
                                Log.d(TAG, "Conversation contains current student id");
                                myConversations.add(conversation);
                            } else {
                                Log.d(TAG, "Conversation DOES NOT current student id");
                            }
                        } else if (currentEmployee != null) {
                            if (conversationIds.contains(currentEmployee.getId())) {
                                Log.d(TAG, "Conversation contains current employee id");
                                myConversations.add(conversation);
                            } else {
                                Log.d(TAG, "Conversation DOES NOT current employee id");
                            }
                        }
                    }
                }
            }

            // TODO: Implement this
            postMyConversationsListener();
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    };

    // TODO: Finish this
    // conversations should be an array of Conversations that actually have userIds set (test this)
    // then, need to figure out attaching to the listview, getting things to show up, etc.
    public void postMyConversationsListener() {
        Log.d("MYCONVO_SIZE", String.valueOf(myConversations.size()));
        conversationAdapter = new ConversationAdapter(this, myConversations);
        conversationsView = findViewById(R.id.conversations_view);
        conversationsView.setAdapter(conversationAdapter);
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

    public void writeNewConversation() {

        ArrayList<String> userIds = new ArrayList<>();
        if (currentRole == "Student") {
            userIds.add(currentStudent.getId());
        } else {
            userIds.add(currentEmployee.getId());
        }

        if (matchedStudentUsername != null) {
            userIds.add(matchedStudentUsername.getId());
        } else if (matchedEmployeeUsername != null) {
            userIds.add(matchedEmployeeUsername.getId());
        } else {
            Toast.makeText(view.getContext(), "Something went wrong!", Toast.LENGTH_LONG).show();
        }

        String id = RandomKeyGenerator.randomAlphaNumeric(16);
        newConversation = new Conversation(id, userIds);
        dbRef.child("Conversations").child(id).setValue(newConversation);
    }

    public void getMyConversations() {
        // get all conversations
        DatabaseReference conversationsRef = dbRef.child("Conversations");
        conversationsRef.addValueEventListener(myConversationsListener);
        Log.d(TAG, "just added the VEL from getMyConversations()");
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

}