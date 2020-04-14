package com.example.pursuit;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.example.pursuit.models.Conversation;
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

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Objects;

public class MessagesActivity extends AppCompatActivity {

    private final String TAG = "MESSAGES_ACTIVITY";

    private Conversation currentConversation;
    private Conversation counterpartConversation;
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

        getCurrentConversations();
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

                    Message message = snapshot.getValue(Message.class);

                    if (message == null) {
                        Log.d(TAG, "Message is null");
                    } else {
                        Log.d(TAG, "Message is not null");
                        messageList.add(0, message);
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
                return ZonedDateTime.parse(o1.getCreatedAt()).compareTo(ZonedDateTime.parse(o2.getCreatedAt()));
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
        messageRecycler.setAdapter(messageListAdapter);
        messageRecycler.setLayoutManager(new LinearLayoutManager(this));
        RecyclerView rv = findViewById(R.id.messages_recycler);
        rv.smoothScrollToPosition(messageList.size()-1);
    }

    ValueEventListener currentConversationListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            currentConversation = null;
            Log.d(TAG, "In currentConversationListener");
            if (dataSnapshot.exists()) {
                Log.d(TAG, "Snapshot exists");
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Log.d(TAG, "Looping in snapshot");
                    currentConversation = snapshot.getValue(Conversation.class);

                    if (currentConversation != null) {
                        Log.d(TAG, "got the current conversation");
                        Log.d("CURRENT_ID", currentConversation.getId());
                    }
                }
            }

            postCurrentConversationListener();
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    };

    private void postCurrentConversationListener() {
        Query counterpartConversationQuery;
        if (currentConversation.otherUserRole.equals("Student")) {
            counterpartConversationQuery = dbRef.child("Students").child(currentConversation.getOtherUserId()).child("Conversations")
                    .orderByChild("id").equalTo(currentConversation.getId());
        } else {
            counterpartConversationQuery = dbRef.child("Employees").child(currentConversation.getOtherUserId()).child("Conversations")
                    .orderByChild("id").equalTo(currentConversation.getId());
        }
        counterpartConversationQuery.addListenerForSingleValueEvent(counterpartConversationListener);
    }

    ValueEventListener counterpartConversationListener = new ValueEventListener() {
        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            counterpartConversation = null;
            if (dataSnapshot.exists()) {
                Log.d(TAG, "Snapshot exists for counterpartConversationListener");
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    // if the counterpart conversation exists, set it
                    counterpartConversation = snapshot.getValue(Conversation.class);
                }
            } else {
                // if the counterpart conversation doesn't already exist, write it
                writeCounterpartConversation();
            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    };

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void writeCounterpartConversation() {
        DatabaseReference counterpartReference;
        String otherUserId;
        String otherUserRole;
        String otherUserUsername;
        String createdAt;
        String updatedAt;

        if (currentConversation.otherUserRole.equals("Student")) {
            counterpartReference = dbRef.child("Students").child(currentConversation.getOtherUserId()).child("Conversations");
        } else {
            counterpartReference = dbRef.child("Employees").child(currentConversation.getOtherUserId()).child("Conversations");
        }
        if (currentStudent != null) {
            otherUserId = currentStudent.getId();
            otherUserRole = "Student";
            otherUserUsername = currentStudent.getUsername();
        } else {
            otherUserId = currentEmployee.getId();
            otherUserRole = "Employee";
            otherUserUsername = currentEmployee.getUsername();
        }

        String id = currentConversation.getId();
        createdAt = ZonedDateTime.now(ZoneOffset.UTC).toString();
        updatedAt = ZonedDateTime.now(ZoneOffset.UTC).toString();

        Conversation counterpart = new Conversation(id, otherUserId, otherUserUsername, otherUserRole, createdAt, updatedAt);
        counterpartReference.child(id).setValue(counterpart);
        counterpartConversation = counterpart;
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

    private void getCurrentConversations() {
        Query conversationQuery;
        if (currentStudent != null) {
            conversationQuery = dbRef.child("Students").child(currentStudent.getId()).child("Conversations")
                    .orderByChild("id").equalTo(getIntent().getStringExtra("CONVERSATION_ID"));
        } else {
            conversationQuery = dbRef.child("Students").child(currentStudent.getId()).child("Conversations")
                    .orderByChild("id").equalTo(getIntent().getStringExtra("CONVERSATION_ID"));
        }
        conversationQuery.addListenerForSingleValueEvent(currentConversationListener);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void sendMessage(View v) {
        Log.d(TAG, "in sendMessage");
        EditText messageEditText = null;
        try {
            messageEditText = findViewById(R.id.message_box);
            Log.d(TAG, "found the message_box");
        } catch (NullPointerException e) {
            Log.d(TAG, "Failed to obtain editText messageText");
        }

        assert messageEditText != null;
        Log.d(TAG, "messageEditText is not null");
        String messageText = messageEditText.getText().toString();
        Log.d("MESSAGE_TEXT", messageText);
        writeNewMessage(messageText);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void writeNewMessage(String message) {
        Log.d(TAG, "in writeNewMessage");
        DatabaseReference newMessageReference;
        String senderId;
        String senderUsername;
        String recipientId;
        String recipientUsername;
        String messageText;
        String createdAt;

        if (currentStudent != null) {
            senderId = currentStudent.getId();
            senderUsername = currentStudent.getUsername();
            newMessageReference = dbRef.child("Students").child(currentStudent.getId());
        } else {
            senderId = currentEmployee.getId();
            senderUsername = currentEmployee.getUsername();
            newMessageReference = dbRef.child("Employees").child(currentEmployee.getId());
        }

        recipientId = currentConversation.getOtherUserId();
        recipientUsername = currentConversation.getOtherUserUsername();
        messageText = message;
        createdAt = ZonedDateTime.now(ZoneOffset.UTC).toString();
        String id = RandomKeyGenerator.randomAlphaNumeric(16);
        Message newMessage = new Message(id, senderId, senderUsername, recipientId, recipientUsername, messageText, createdAt);

        Log.d(TAG, "created the new Message");

        newMessageReference.child("Conversations").child(currentConversation.getId()).child("Messages").child(id).setValue(newMessage);
        Log.d(TAG, "inserted the new message in the db");
        newMessageReference.child("Conversations").child(currentConversation.getId()).child("updatedAt").setValue(createdAt);
        writeCounterpartMessage(newMessage);
    }

    private void writeCounterpartMessage(Message message) {
        Log.d(TAG, "writing the counterpart");
        DatabaseReference counterpartReference;

        if (currentConversation.getOtherUserRole().equals("Student")) {
            counterpartReference = dbRef.child("Students").child(currentConversation.getOtherUserId()).child("Conversations").child(counterpartConversation.getId());
        } else {
            counterpartReference = dbRef.child("Employees").child(currentConversation.getOtherUserId()).child("Conversations").child(counterpartConversation.getId());
        }

        counterpartReference.child("Messages").child(message.getId()).setValue(message);
        counterpartReference.child("updatedAt").setValue(message.getCreatedAt());

//        hideKeyboard(this);

        EditText editText = findViewById(R.id.message_box);
        editText.getText().clear();
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

    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}

