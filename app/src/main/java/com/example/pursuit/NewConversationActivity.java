package com.example.pursuit;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.pursuit.adapters.UsernameAdapter;
import com.example.pursuit.models.Company;
import com.example.pursuit.models.Conversation;
import com.example.pursuit.models.Employee;
import com.example.pursuit.models.Student;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.Query;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Comparator;

public class NewConversationActivity extends AppCompatActivity {
    private static final String TAG = "NewConversationActivity";

    private Student currentStudent = null;
    private Employee currentEmployee = null;
    private Company currentCompany = null;
    private String  currentRole = null;
    private Student matchedStudentUsername;
    private Employee matchedEmployeeUsername;
    private String checkUsername;

    private ArrayList<String> allUsernames;
    private UsernameAdapter usernameAdapter;

    private DatabaseReference dbRef;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_conversation);

        findAndSetCurrentUser();
        dbRef = FirebaseDatabase.getInstance().getReference();

        getAllUsernames();
    }

    public void setUsername(View v) {
        EditText usernameFilter = findViewById(R.id.usernames_filter);
        usernameFilter.setText((String) v.getTag());
    }

    private void getAllUsernames() {
        Query studentsQuery = dbRef.child("Students").orderByChild("id");
        studentsQuery.addListenerForSingleValueEvent(studentUsernamesListener);
    }

    public void createConversation(View v) {
        EditText newConversationUsername = null;
        try {
            newConversationUsername = findViewById(R.id.usernames_filter);
        } catch (NullPointerException e) {
            Log.d(TAG, "Failed to obtain edittext");
        }

        assert newConversationUsername != null;
        checkUsername = newConversationUsername.getText().toString();

        buildConversationQuery(checkUsername);
    }

    public void buildConversationQuery(String username) {
        if (currentStudent != null && username.equals(currentStudent.getUsername())) {
            Toast.makeText(this, "You can't message yourself!", Toast.LENGTH_LONG).show();
        } else if (currentEmployee != null && username.equals(currentEmployee.getUsername())) {
            Toast.makeText(this, "You can't message yourself!", Toast.LENGTH_LONG).show();
        } else {
            Query studentUsernameQuery = dbRef.child("Students").orderByChild("username").equalTo(username);
            studentUsernameQuery.addListenerForSingleValueEvent(checkStudentUsernameListener);
        }
    }

    ValueEventListener checkStudentUsernameListener = new ValueEventListener() {
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

            postCheckStudentUsernameListener();
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {
            Log.e(TAG, databaseError.toString());
        }
    };

    private void postCheckStudentUsernameListener() {
        if (matchedStudentUsername != null) {
            Query existingConversationQuery = dbRef.child("Students").child(matchedStudentUsername.getId()).child("Conversations");
            existingConversationQuery.addListenerForSingleValueEvent(existingConversationListener);

            Toast.makeText(this, "Conversation Created!", Toast.LENGTH_LONG).show();
        } else {
            Query employeeUsernameQuery = dbRef.child("Employees").orderByChild("username").equalTo(checkUsername);
            employeeUsernameQuery.addListenerForSingleValueEvent(checkEmployeeUsernameListener);
        }
    }

    ValueEventListener checkEmployeeUsernameListener = new ValueEventListener() {
        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            matchedEmployeeUsername = null;
            if (dataSnapshot.exists()) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    matchedEmployeeUsername = snapshot.getValue(Employee.class);
                }
            }
            postCheckEmployeeUsernameListener();
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    };

    private void postCheckEmployeeUsernameListener() {
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

        Intent messagesActivity = new Intent(NewConversationActivity.this, MessagesActivity.class);
        messagesActivity.putExtra("CONVERSATION_ID", existing.getId());
        startActivity(messagesActivity);
    }

    ValueEventListener studentUsernamesListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            allUsernames = new ArrayList<>();

            if (dataSnapshot.exists()) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Student student = snapshot.getValue(Student.class);
                    if (student != null) {
                        allUsernames.add(student.getUsername());
                    }
                }
            }

            postStudentUsernamesListener();
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    };

    private void postStudentUsernamesListener() {
        Query employeesQuery = dbRef.child("Employees").orderByChild("id");
        employeesQuery.addListenerForSingleValueEvent(employeesUsernamesListener);
    }

    ValueEventListener employeesUsernamesListener = new ValueEventListener() {
        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            if (dataSnapshot.exists()) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Employee employee = snapshot.getValue(Employee.class);
                    if (employee != null) {
                        allUsernames.add(employee.getUsername());
                    }
                }
            }

            postEmployeeUsernamesListener();
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    };

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void postEmployeeUsernamesListener() {
        allUsernames.sort(new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                return o1.compareToIgnoreCase(o2);
            }
        });

        usernameAdapter = new UsernameAdapter(this, allUsernames);
        ListView usernamesView = findViewById(R.id.usernames_view);
        usernamesView.setAdapter(usernameAdapter);
        EditText usernamesFilter = findViewById(R.id.usernames_filter);
        usernamesFilter.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }

            @Override
            public void afterTextChanged(Editable s) {
                usernameAdapter.getFilter().filter(s);
            }
        });
        usernameAdapter.addAll(allUsernames);
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
        Conversation newConversation = new Conversation(id, otherUserId, otherUserUsername, otherUserRole, createdAt, updatedAt);

        // Add the newConversation under the Student/Employee
        // Students/Employees > id > Conversations > id > newConversation
        if (currentStudent != null) {
            dbRef.child("Students").child(currentStudent.getId()).child("Conversations").child(id).setValue(newConversation);
        } else {
            dbRef.child("Employees").child(currentEmployee.getId()).child("Conversations").child(id).setValue(newConversation);
        }

        Intent messagesActivity = new Intent(NewConversationActivity.this, MessagesActivity.class);
        messagesActivity.putExtra("CONVERSATION_ID", newConversation.getId());
        startActivity(messagesActivity);
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
