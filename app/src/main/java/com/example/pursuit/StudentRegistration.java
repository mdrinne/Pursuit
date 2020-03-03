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

import com.example.pursuit.models.Student;
import com.example.pursuit.RandomKeyGenerator;
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
    private ArrayList<Student> matchedEmails;
    // private ValueEventListener usernameListener;
    private View view;
    private boolean match;
    private boolean register;
    private String checkEmail;

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
        matchedEmails = new ArrayList<>();
        register = false;

    }

    /* DATABASE */
    ValueEventListener usernameListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            matchedUsers = new ArrayList<>();
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
                    matchedUsers.add(student);
                }
            }

            Log.d(TAG, "email is: " + email.toString());
            Query emailQuery = mRef.child("Students").orderByChild("email").equalTo(checkEmail);

            if (emailQuery == null) {
                Log.d(TAG, "Email query is null");
            } else {
                Log.d(TAG, "Email query is not null");
            }

            emailQuery.addListenerForSingleValueEvent(emailListener);
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    };

    ValueEventListener emailListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            matchedEmails = new ArrayList<>();
            Log.d(TAG, "in emailListener onDataChange");
            if (dataSnapshot.exists()) {
                Log.d(TAG, "Snapshot exists");
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Log.d(TAG, "looping in snapshot");
                    Student student = snapshot.getValue(Student.class);
                    if (student == null) {
                        Log.d(TAG, "student is null");
                    } else {
                        Log.d(TAG, "student exists: " + student.getEmail());
                    }
                    matchedEmails.add(student);
                }
            }
            processRequest();
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    };

    private void writeNewStudent(String fname, String lname, String university, String major, String minor, String gpa,
            String bio, String email, String username, String password) {

        Student student = new Student(RandomKeyGenerator.randomAlphaNumeric(16), fname, lname, university, major, minor,
                gpa, bio, email, username, password);

        mRef.child("Students").child(username).setValue(student);
    }

    private boolean usernameExists(String username) {
        Log.d(TAG, "In usernameExists; username: " + username);

        String size = String.valueOf(matchedUsers.size());

        Log.d(TAG, "matchedUsers size: " + size);

        if (matchedUsers.size() > 0) {
            match = true;
            return true;
        } else {
            return false;
        }
    }

    private boolean emailExists(String email) {

        Log.d(TAG, "In emailExists; email: " + email);

        String size = String.valueOf(matchedEmails.size());

        Log.d(TAG, "matchedEmails size: " + size);

        if (matchedEmails.size() > 0) {
            match = true;
            return true;
        } else {
            return false;
        }

    }

    /* ******** */

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
        match = false;
        if (!checkForEmpties()) {
            Log.d(TAG, "no empty fields");

            if (isEmail(email)) {
                Log.d(TAG, "email is valid");

                if (passwordMatch()) {
                    Log.d(TAG, "passwords match");

                    if (!usernameExists(toString(username))) {
                        Log.d(TAG, "username not taken");
                        if (!emailExists(toString(email))) {
                            Log.d(TAG, "email not taken");
                            writeNewStudent(toString(firstName), toString(lastName), toString(university),
                                    toString(major), toString(minor), toString(gpa), toString(bio), toString(email),
                                    toString(username), toString(password1));
                            // register = true;
                            Intent intent = new Intent(this, LandingActivity.class);
                            startActivity(intent);
                        } else {
                            Log.d(TAG, "email is already taken");
                            Toast.makeText(view.getContext(), "Email is Already Taken", Toast.LENGTH_LONG).show();
                        }
                    } else {
                        Log.d(TAG, "username is already taken");
                        Toast.makeText(view.getContext(), "Username Already Exists", Toast.LENGTH_LONG).show();
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

        checkEmail = toString(email);

        Query usernameQuery = mRef.child("Students").orderByChild("username").equalTo(toString(username));

        if (usernameQuery == null) {
            Log.d(TAG, "Username query is null");
        } else {
            Log.d(TAG, "Username query is not null");
        }

        usernameQuery.addListenerForSingleValueEvent(usernameListener);

        // Query emailQuery =
        // mRef.child("Students").orderByChild("email").equalTo(toString(email));
        //
        // if (emailQuery == null) {
        // Log.d(TAG, "Email query is null");
        // } else {
        // Log.d(TAG, "Email query is not null");
        // }
        //
        // emailQuery.addListenerForSingleValueEvent(emailListener);

        // if (register == true) {
        // Log.d(TAG, "register: true");
        // } else {
        // Log.d(TAG, "register: false");
        // }
        //
        // if (register) {
        // Intent intent = new Intent(this, LandingActivity.class);
        // startActivity(intent);
        // }
    }

}
