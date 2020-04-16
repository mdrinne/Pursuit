package com.example.pursuit;

import com.example.pursuit.models.Conversation;
import com.example.pursuit.models.Employee;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;


import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.Query;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import com.example.pursuit.models.Company;
import com.example.pursuit.models.Student;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Comparator;

public class ConversationsActivity extends AppCompatActivity
        implements NewConversationDialogFragment.NewConversationDialogListener, ConfirmDeleteDialogFragment.ConfirmDeleteDialogListener {

    private Student matchedStudentUsername;
    private Employee matchedEmployeeUsername;
    private String checkUsername;
    private ConversationAdapter conversationAdapter;
    private Conversation newConversation;
    private ListView conversationsView;
    private ArrayList<Conversation> myConversations;

    private static final String TAG = "MessagesActivity";

    private DatabaseReference dbRef;

    private Student currentStudent = null;
    private Employee currentEmployee = null;
    private Company currentCompany = null;
    private String  currentRole = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversations);
        BottomNavigationView bottomNavigation = findViewById(R.id.bottom_navigation);
        bottomNavigation.setOnNavigationItemSelectedListener(navigationItemSelectedListener);

        findAndSetCurrentUser();
        dbRef = FirebaseDatabase.getInstance().getReference();

        getMyConversations();

    }


    ValueEventListener studentUsernameListener = new ValueEventListener() {
        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            matchedStudentUsername = null;
            Log.d(TAG, "In usernameListener onDataChange");
            if (dataSnapshot.exists()) {
                Log.d(TAG, "Snapshot exists");
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Log.d(TAG, "looping in snapshot");
                    matchedStudentUsername = snapshot.getValue(Student.class);
                }
            }

            postStudentUsernameListener();
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {
            Log.e(TAG, databaseError.toString());
        }
    };

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void postStudentUsernameListener() {
        if (matchedStudentUsername != null) {
            Query existingConversationQuery = dbRef.child("Students").child(matchedStudentUsername.getId()).child("Conversations");
            existingConversationQuery.addListenerForSingleValueEvent(existingConversationListener);

            Toast.makeText(this, "Conversation Created!", Toast.LENGTH_LONG).show();
        } else {
            Query employeeUsernameQuery = dbRef.child("Employees").orderByChild("username").equalTo(checkUsername);
            employeeUsernameQuery.addListenerForSingleValueEvent(employeeUsernameListener);
        }
    }

    ValueEventListener employeeUsernameListener = new ValueEventListener() {
        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            matchedEmployeeUsername = null;
            if (dataSnapshot.exists()) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    matchedEmployeeUsername = snapshot.getValue(Employee.class);
                }
            }
            postEmployeeUsernameListener();
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    };

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void postEmployeeUsernameListener() {
        if (matchedEmployeeUsername != null) {
            Query existingConversationQuery = dbRef.child("Employees").child(matchedEmployeeUsername.getId()).child("Conversations");
            existingConversationQuery.addListenerForSingleValueEvent(existingConversationListener);

            Toast.makeText(this, "Conversation Created!", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, "This username does not exist!", Toast.LENGTH_LONG).show();
        }
    }

    ValueEventListener existingConversationListener = new ValueEventListener() {
        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            Conversation existingConversation = null;

            if (dataSnapshot.exists()) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Conversation temp = snapshot.getValue(Conversation.class);
                    assert temp != null;
                    if (currentStudent != null) {
                        if (temp.getOtherUserId().equals(currentStudent.getId())) {
                            existingConversation = temp;
                        }
                    } else {
                        if (temp.getOtherUserId().equals(currentEmployee.getId())) {
                            existingConversation = temp;
                        }
                    }
                }
            }

            postExistingConversationListener(existingConversation);
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    };

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void postExistingConversationListener(Conversation existing) {
        if (existing != null) {
            writeExistingCounterpartConversation(existing);

        } else {
            writeNewConversation();

            conversationAdapter.add(newConversation);
            conversationsView.setSelection(conversationsView.getCount() - 1);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void writeExistingCounterpartConversation(Conversation existing) {
        if (matchedStudentUsername != null) {
            existing.setOtherUserId(matchedStudentUsername.getId());
            existing.setOtherUserUsername(matchedStudentUsername.getUsername());
            existing.setOtherUserRole("Student");
        } else {
            existing.setOtherUserId(matchedEmployeeUsername.getId());
            existing.setOtherUserUsername(matchedEmployeeUsername.getUsername());
            existing.setOtherUserRole("Employee");
        }
        existing.setCreatedAt(ZonedDateTime.of(LocalDateTime.now(), ZoneOffset.UTC).toString());
        existing.setUpdatedAt(ZonedDateTime.of(LocalDateTime.now(), ZoneOffset.UTC).toString());
        if (currentStudent != null) {
            dbRef.child("Students").child(currentStudent.getId()).child("Conversations").child(existing.getId()).setValue(existing);
        } else {
            dbRef.child("Employees").child(currentEmployee.getId()).child("Conversations").child(existing.getId()).setValue(existing);
        }

        Intent messagesActivity = new Intent(ConversationsActivity.this, MessagesActivity.class);
        messagesActivity.putExtra("CONVERSATION_ID", existing.getId());
        startActivity(messagesActivity);
    }

    ValueEventListener myConversationsListener = new ValueEventListener() {
        @RequiresApi(api = Build.VERSION_CODES.O)
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

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void postMyConversationsListener() {
        // sort the conversations, newest first
        myConversations.sort(new Comparator<Conversation>() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public int compare(Conversation o1, Conversation o2) {
                return ZonedDateTime.parse(o2.getUpdatedAt()).compareTo(ZonedDateTime.parse(o1.getUpdatedAt()));
            }
        });

        conversationAdapter = new ConversationAdapter(this, myConversations);
        conversationsView = findViewById(R.id.conversations_view);
        conversationsView.setAdapter(conversationAdapter);
        conversationAdapter.addAll(myConversations);
//        conversationsView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Log.d(TAG, "in onItemClick !");
//                Intent messagesActivity = new Intent(ConversationsActivity.this, MessagesActivity.class);
//                messagesActivity.putExtra("CONVERSATION_ID", myConversations.get(position).getId());
//                startActivity(messagesActivity);
//            }
//        });
    }

    public void openConversation(View v) {
        Intent messagesActivity = new Intent(ConversationsActivity.this, MessagesActivity.class);
        messagesActivity.putExtra("CONVERSATION_ID", (String) v.getTag());
        startActivity(messagesActivity);
    }

    public void showCreateConversation(View v) {
        // open the new conversation fragment
        DialogFragment dialog  = new NewConversationDialogFragment();
        dialog.show(getSupportFragmentManager(), "newConversation");
    }

    public void showDeleteConversation(View v) {
        DialogFragment dialog = new ConfirmDeleteDialogFragment((String) v.getTag());

        dialog.show(getSupportFragmentManager(), (String) v.getTag());
        Log.d("TAG", "can do stuff after");
    }

    public void onDialogPositiveClick(DialogFragment dialog) {
        // create a conversation
        if (dialog.getClass().getSimpleName().equals("NewConversationDialogFragment")) {
            createConversation(dialog);
        } else {
            deleteConversations(dialog.getTag());
        }
    }

    public void onDialogNegativeClick(DialogFragment dialog) {
        // close the dialog
        try {
            dialog.getDialog().cancel();
        } catch (NullPointerException e){
            Log.d(TAG, "Unable to obtain dialog");
        }
    }

    public void createConversation(DialogFragment dialog) {
        EditText newConversationUsername = null;
        try {
            newConversationUsername = dialog.getDialog().findViewById(R.id.newConversationUsername);
        } catch (NullPointerException e) {
            Log.d(TAG, "Failed to obtain editText from newConversationDialog");
        }

        assert newConversationUsername != null;
        checkUsername = toString(newConversationUsername);
        if (currentStudent != null && checkUsername.equals(currentStudent.getUsername())) {
            Toast.makeText(this, "You can't message yourself!", Toast.LENGTH_LONG).show();
        } else if (currentEmployee != null && checkUsername.equals(currentEmployee.getUsername())) {
            Toast.makeText(this, "You can't message yourself!", Toast.LENGTH_LONG).show();
        } else {
            Query studentUsernameQuery = dbRef.child("Students").orderByChild("username").equalTo(checkUsername);
            studentUsernameQuery.addListenerForSingleValueEvent(studentUsernameListener);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void writeNewConversation() {
        String otherUserId;
        String otherUserUsername;
        String otherUserRole;
        String createdAt;
        String updatedAt;

        if (matchedStudentUsername != null) {
            otherUserId = matchedStudentUsername.getId();
            otherUserUsername = matchedStudentUsername.getUsername();
            otherUserRole = "Student";
        } else if (matchedEmployeeUsername != null) {
            otherUserId = matchedEmployeeUsername.getId();
            otherUserUsername = matchedEmployeeUsername.getUsername();
            otherUserRole = "Employee";
        } else {
            Toast.makeText(this, "Something went wrong!", Toast.LENGTH_LONG).show();
            return;
        }

        String id = RandomKeyGenerator.randomAlphaNumeric(16);
        createdAt = ZonedDateTime.of(LocalDateTime.now(), ZoneOffset.UTC).toString();
        updatedAt = ZonedDateTime.of(LocalDateTime.now(), ZoneOffset.UTC).toString();
        newConversation = new Conversation(id, otherUserId, otherUserUsername, otherUserRole, createdAt, updatedAt);

        // Add the newConversation under the Student/Employee
        // Students/Employees > id > Conversations > id > newConversation
        if (currentStudent != null) {
            dbRef.child("Students").child(currentStudent.getId()).child("Conversations").child(id).setValue(newConversation);
        } else {
            dbRef.child("Employees").child(currentEmployee.getId()).child("Conversations").child(id).setValue(newConversation);
        }

        Intent messagesActivity = new Intent(ConversationsActivity.this, MessagesActivity.class);
        messagesActivity.putExtra("CONVERSATION_ID", newConversation.getId());
        startActivity(messagesActivity);
    }

    public void deleteConversations(String conversationId) {
        Log.d(TAG, "in deleteConversation");

        if (currentStudent != null) {
            DatabaseReference conversationRef = dbRef.child("Students").child(currentStudent.getId()).child("Conversations").child(conversationId);

            // remove the conversation
            conversationRef.removeValue();
        } else {
            DatabaseReference conversationRef = dbRef.child("Employees").child(currentEmployee.getId()).child("Conversations").child(conversationId);

            // remove the conversation
            conversationRef.removeValue();
        }
    }

    public void getMyConversations() {
        // get all conversations
        Query conversationsQuery;
        if (currentStudent != null) {
            conversationsQuery = dbRef.child("Students").child(currentStudent.getId()).child("Conversations");
        } else {
            conversationsQuery = dbRef.child("Employees").child(currentEmployee.getId()).child("Conversations");
        }

        conversationsQuery.addValueEventListener(myConversationsListener);
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
                        Intent home = new Intent(ConversationsActivity.this, LandingActivity.class);
                        startActivity(home);
                        finish();
                        return true;
                    case R.id.navigation_messages:
                        return true;
                    case R.id.navigation_profile:
                        if (currentStudent != null) {
                            Intent i = new Intent(ConversationsActivity.this, StudentProfileActivity.class);
                            startActivity(i);
                            finish();
                        } else {
                            Intent profile = new Intent(ConversationsActivity.this, CompanyProfileActivity.class);
                            startActivity(profile);
                            finish();
                        }
                        return true;
                }
                return false;
            }
        };

}