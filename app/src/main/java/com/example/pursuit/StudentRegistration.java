package com.example.pursuit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.pursuit.database.DatabaseHelper;
import com.example.pursuit.models.Student;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.Query;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class StudentRegistration extends AppCompatActivity {

    private static final String TAG = "StudentRegistration";

    private DatabaseReference mRef;
    private ArrayList<Student> matchedUsers;
//    private ValueEventListener usernameListener;
    private View view;
    private boolean match = false;

    EditText firstName;
    EditText lastName;
    EditText university;
    EditText major;
    EditText minor;
    EditText gpa;
    EditText bio;
    EditText email;
    EditText username;
    EditText password1;
    EditText password2;
    Button btnFinish;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_registration);

        btnFinish = findViewById(R.id.btnFinish);
        firstName = findViewById(R.id.txtFirstName);
        lastName = findViewById(R.id.txtLastName);
        university = findViewById(R.id.txtUniversity);
        major = findViewById(R.id.txtMajor);
        minor = findViewById(R.id.txtMinor);
        gpa = findViewById(R.id.txtGPA);
        bio = findViewById(R.id.txtBio);
        email = findViewById(R.id.txtEmail);
        username = findViewById(R.id.username);
        password1 = findViewById(R.id.password);
        password2 = findViewById(R.id.txtReEnterPassword);

        mRef = FirebaseDatabase.getInstance().getReference();
        matchedUsers = new ArrayList<>();

    }

    ValueEventListener usernameListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            Log.d(TAG, "In onDataChange");
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
                    matchedUsers.add(student);
                }
            }
            processRequest();
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    };

    /* DATABASE FUNCTIONS */
    private void writeNewStudent(int id, String fname, String lname, String university, String major, String minor,
                                String gpa, String bio, String email, String username, String password) {

        Student student = new Student(id, fname, lname, university, major, minor, gpa, bio, email, username, password);

//        mRef.child("Users").child("Student").child(username).setValue(student);

        mRef.child("Students").child(username).setValue(student);
    }

    private boolean usernameExists(String username) {
        Log.d(TAG, "In usernameExists; username: " + username);

//        Query query = mRef.child("Students").orderByChild("username").equalTo(username);
//
//        if (query == null) {
//            Log.d(TAG, "Query is null");
//        } else {
//            Log.d(TAG, "Query is not null");
//        }
//
//        query.addListenerForSingleValueEvent(usernameListener);

//        usernameListener.onDataChange();

        String size = String.valueOf(matchedUsers.size());

        Log.d(TAG, "matchedUsers size: " + size);

        if (matchedUsers.size() > 0) {
            match = true;
            return true;
        } else {
            return false;
        }
    }
    /* ****************** */

    public String toString(EditText text) {
        return text.getText().toString();
    }

    public boolean passwordMatch() {
        if (toString(password1).equals(toString(password2)))
            return true;
        Toast.makeText(view.getContext(), "Passwords Did Not Match", Toast.LENGTH_SHORT).show();
        return false;
    }

    boolean isEmail(EditText text) {
        CharSequence email = text.getText().toString();
        if (!(!TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches())) {
            Toast.makeText(view.getContext(), "Invalid Email", Toast.LENGTH_SHORT).show();
            return false;
        } else
            return true;
    }

    boolean isEmpty(EditText text) {
        CharSequence str = text.getText().toString();
        return TextUtils.isEmpty(str);
    }

    public boolean checkForEmpties() {
        if (isEmpty(firstName) || isEmpty(lastName) || isEmpty(university) || isEmpty(major) || isEmpty(minor)
                || isEmpty(gpa) || isEmpty(bio) || isEmpty(email) || isEmpty(username) || isEmpty(password1)
                || isEmpty(password2)) {
            Toast.makeText(view.getContext(), "All Fields are Required", Toast.LENGTH_SHORT).show();
            return true;
        }
        return false;
    }

    public void processRequest() {
        if (!checkForEmpties()) {
            Log.d(TAG, "no empty fields");

            if (isEmail(email)) {
                Log.d(TAG, "email is valid");

                if (passwordMatch()) {
                    Log.d(TAG, "passwords match");

                    if (!usernameExists(toString(username))) {
                        Log.d(TAG, "username not taken");

//                        writeNewStudent(1, toString(firstName), toString(lastName), toString(university),
//                                toString(major), toString(minor), toString(gpa), toString(bio), toString(email),
//                                toString(username), toString(password1));

                    }
                }
            }
        }
        if (match == true) {
            Log.d(TAG, "RESULT: true");

        } else {
            Log.d(TAG, "RESULT: false");
        }
    }

    // called by onClick, attempts to register a student
    public void registerStudent(View v) {
        view = v;
        firstName = findViewById(R.id.txtFirstName);
        lastName = findViewById(R.id.txtLastName);
        university = findViewById(R.id.txtUniversity);
        major = findViewById(R.id.txtMajor);
        minor = findViewById(R.id.txtMinor);
        gpa = findViewById(R.id.txtGPA);
        bio = findViewById(R.id.txtBio);
        email = findViewById(R.id.txtEmail);
        username = findViewById(R.id.username);
        password1 = findViewById(R.id.password);
        password2 = findViewById(R.id.txtReEnterPassword);

        Query query = mRef.child("Students").orderByChild("username").equalTo(toString(username));

        if (query == null) {
            Log.d(TAG, "Query is null");
        } else {
            Log.d(TAG, "Query is not null");
        }

        query.addListenerForSingleValueEvent(usernameListener);

//        boolean processResult = processRequest(v);



//        Intent intent = new Intent(this, LandingActivity.class);
//        startActivity(intent);
    }

}
