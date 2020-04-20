package com.example.pursuit;

import com.example.pursuit.adapters.ConversationAdapter;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ListView;

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
import com.example.pursuit.models.Conversation;
import com.example.pursuit.models.Employee;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Comparator;

public class ConversationsActivity extends AppCompatActivity
        implements ConfirmDeleteDialogFragment.ConfirmDeleteDialogListener {

    private ConversationAdapter conversationAdapter;
    private ArrayList<Conversation> myConversations;

    private static final String TAG = "MessagesActivity";

    private DatabaseReference dbRef;

    private Student currentStudent = null;
    private Employee currentEmployee = null;
    private Company currentCompany = null;
    private String  currentRole = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversations);
        BottomNavigationView bottomNavigation = findViewById(R.id.bottom_navigation);
        bottomNavigation.setOnNavigationItemSelectedListener(navigationItemSelectedListener);

        findAndSetCurrentUser();
        dbRef = FirebaseDatabase.getInstance().getReference();

        getMyConversations();
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
        ListView conversationsView = findViewById(R.id.conversations_view);
        conversationsView.setAdapter(conversationAdapter);
        conversationsView.setTextFilterEnabled(true);
        EditText conversationsFilter = findViewById(R.id.conversations_filter);
        conversationsFilter.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                conversationAdapter.getFilter().filter(s);
            }
        });
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
        Intent newConversationActivity = new Intent(ConversationsActivity.this, NewConversationActivity.class);
        startActivity(newConversationActivity);
    }

    public void showDeleteConversation(View v) {
        DialogFragment dialog = new ConfirmDeleteDialogFragment((String) v.getTag());
        dialog.show(getSupportFragmentManager(), (String) v.getTag());
    }

    public void onDialogPositiveClick(DialogFragment dialog) {
        deleteConversations(dialog.getTag());
    }

    public void onDialogNegativeClick(DialogFragment dialog) {
        // close the dialog
        try {
            dialog.getDialog().cancel();
        } catch (NullPointerException e){
            Log.d(TAG, "Unable to obtain dialog");
        }
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