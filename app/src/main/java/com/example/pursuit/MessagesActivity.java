package com.example.pursuit;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import com.example.pursuit.models.Message;
import com.example.pursuit.models.Student;
import com.example.pursuit.models.Employee;
import com.example.pursuit.models.Company;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Objects;

public class MessagesActivity extends AppCompatActivity {

    private final String TAG = "MESSAGES_ACTIVITY";

    private Student currentStudent;
    private Employee currentEmployee;
    private Company currentCompany;
    private String currentRole;

    private DatabaseReference dbRef;

    private RecyclerView messageRecycler;
    private MessageListAdapter messageListAdapter;
    private ArrayList<Message> messageList;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messages);

        findAndSetCurrentUser();
        dbRef = FirebaseDatabase.getInstance().getReference();

        getMessages();
    }

    ValueEventListener messagesListener = new ValueEventListener() {
        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

            messageList = new ArrayList<>();

            if (dataSnapshot.exists()) {
                Log.d(TAG, "Snapshot exists for Messages");
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                    Message message = dataSnapshot.getValue(Message.class);

                    if (message == null) {
                        Log.d(TAG, "Message is null");
                    } else {
                        Log.d(TAG, "Message is not null");
                        messageList.add(message);
                    }
                }
            }

            postMessagesListener();
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    };

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void postMessagesListener() {
        messageList.sort(new Comparator<Message>() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public int compare(Message o1, Message o2) {
                return ZonedDateTime.parse(o2.getCreatedAt()).compareTo(ZonedDateTime.parse(o1.getCreatedAt()));
            }
        });

        messageRecycler = findViewById(R.id.messages_recycler);
        String currentUserId;
        if (currentStudent != null) {
            currentUserId = currentStudent.getId();
        } else {
            currentUserId = currentEmployee.getId();
        }
        messageListAdapter = new MessageListAdapter(this, messageList, currentUserId);
        messageRecycler.setLayoutManager(new LinearLayoutManager(this));
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void getMessages() {
        Query messagesQuery;
        if (currentStudent != null) {
            messagesQuery = dbRef.child("Students").child(currentStudent.getId()).child("Conversations")
                    .child(Objects.requireNonNull(getIntent().getStringExtra("CONVERSATION_ID"))).child("Messages");
        } else {
            messagesQuery = dbRef.child("Employees").child(currentEmployee.getId()).child("Conversations")
                    .child(Objects.requireNonNull(getIntent().getStringExtra("CONVERSATION_ID"))).child("Messages");
        }

        messagesQuery.addValueEventListener(messagesListener);
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
}

