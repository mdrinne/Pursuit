package com.example.pursuit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.pursuit.models.Keyword;
import com.example.pursuit.models.Student;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.Query;

import java.util.ArrayList;

public class StudentRegistration extends AppCompatActivity {

    private static final String TAG = "StudentRegistration";

    private DatabaseReference mRef;
    private ArrayList<Student> matchedUsers;
    private ArrayList<Student> matchedEmails;
    private View view;
    private boolean match;
    private String checkEmail;

    EditText firstName, lastName, university, major, minor, gpa, bio, email, username;
    EditText studentPassword, studentReEnterPassword, interestKeywords;

    private ArrayList<String> interestKeywordArrayList;
    private int keywordParser;
    private String currentKeyword;

    Student newStudent;
    String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_registration);

        // Get Database Reference
        mRef = FirebaseDatabase.getInstance().getReference();

        matchedUsers = new ArrayList<>();
        matchedEmails = new ArrayList<>();
    }

    /* ********DATABASE******** */

    // Listener For Username Query, Calls Email Query At End
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

            // SELECT * FROM Students WHERE email = ?
            Query emailQuery = mRef.child("Students").orderByChild("email").equalTo(checkEmail);

            if (emailQuery == null) {
                Log.d(TAG, "Email query is null");
            } else {
                Log.d(TAG, "Email query is not null");
            }

            // Call To Email EventListener
            emailQuery.addListenerForSingleValueEvent(emailListener);
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {
            Log.e(TAG, databaseError.toString());
        }
    };

    // Listener For Email Query
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

            // Continue To processRequest After Queries Are Complete
            processRequest();
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {
            Log.e(TAG, databaseError.toString());
        }
    };

    private void writeNewStudent(String fname, String lname, String university, String major, String minor, String gpa,
                                 String bio, String email, String username, String password, ArrayList<String> interests) {
        id = RandomKeyGenerator.randomAlphaNumeric(16);
        newStudent = new Student(id, fname, lname, university, major, minor, gpa, bio, email, username, password, interests);

        mRef.child("Students").child(id).setValue(newStudent);
    }

    ValueEventListener keywordListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            Keyword keyword = null;
            if (dataSnapshot.exists()) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    keyword = snapshot.getValue(Keyword.class);
                }
            }

            if (keyword == null) {
                Log.d(TAG, currentKeyword + "does not exist");
                writeNeKeyword();
            } else {
                Log.d(TAG, currentKeyword + " exists, updating");
                updateKeyword(keyword);
            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    };

    private void updateKeyword(Keyword keyword) {
        ArrayList<String> temp = keyword.getStudents();
        if (temp == null) {
            temp = new ArrayList<>();
        }
        temp.add(id);
        mRef.child("Keywords").child(keyword.getId()).child("students").setValue(temp);
        keywordParser++;
        addKeywordToDB();
    }

    private void writeNeKeyword() {
        ArrayList<String> students = new ArrayList<>();
        students.add(id);
        String keywordID = RandomKeyGenerator.randomAlphaNumeric(16);
        Keyword keyword = new Keyword(keywordID, currentKeyword, null, students);
        mRef.child("Keywords").child(keywordID).setValue(keyword);
        keywordParser++;
        addKeywordToDB();
    }

    /* ******END DATABASE****** */

    // Checks If Given Username Already Exists
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

    // Checks If Given Email Already Exists
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

    // Converts EditText Type To String
    public String toString(EditText text) {
        return text.getText().toString();
    }

    // Checks If Password And Re-Entered Password Match
    public boolean passwordMatch() {
        if (toString(studentPassword).equals(toString(studentReEnterPassword)))
            return true;
        Toast.makeText(view.getContext(), "Passwords Did Not Match", Toast.LENGTH_SHORT).show();
        return false;
    }

    // Checks If Given String Is A Valid Email
    boolean isEmail(EditText text) {
        CharSequence email = text.getText().toString();
        if (!(!TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches())) {
            Toast.makeText(view.getContext(), "Invalid Email", Toast.LENGTH_SHORT).show();
            return false;
        } else
            return true;
    }

    // Checks If TextField Is Empty
    boolean isEmpty(EditText text) {
        CharSequence str = text.getText().toString();
        return TextUtils.isEmpty(str);
    }

    // Checks For Any Empty TextFields
    public boolean checkForEmpties() {
        if (isEmpty(firstName) || isEmpty(lastName) || isEmpty(university) || isEmpty(major) || isEmpty(minor)
                || isEmpty(gpa) || isEmpty(bio) || isEmpty(email) || isEmpty(username) || isEmpty(studentPassword)
                || isEmpty(studentReEnterPassword)) {
            Toast.makeText(view.getContext(), "All Fields are Required", Toast.LENGTH_SHORT).show();
            return true;
        }
        return false;
    }

    private void addKeywordToDB() {
        if (keywordParser < interestKeywordArrayList.size()) {
            currentKeyword = interestKeywordArrayList.get(keywordParser);
            Log.d(TAG, "adding " + currentKeyword);
            Query keywordQuery = mRef.child("Keywords").orderByChild("text").equalTo(currentKeyword);
            keywordQuery.addListenerForSingleValueEvent(keywordListener);
        }
    }

    // Completes Necessary Checks For Registering A New Student
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
                            String[] interestKeywordArray = toString(interestKeywords).split(",");
                            interestKeywordArrayList = new ArrayList<>();
                            if (!toString(interestKeywords).equals("")) {
                                for (int i = 0; i < interestKeywordArray.length; i++) {
                                    String word = interestKeywordArray[i].trim().toLowerCase();
                                    if (!interestKeywordArrayList.contains(word)) {
                                        interestKeywordArrayList.add(word);
                                    }
                                }
                            }
                            writeNewStudent(toString(firstName), toString(lastName), toString(university),
                                    toString(major), toString(minor), toString(gpa), toString(bio), toString(email),
                                    toString(username), toString(studentPassword), interestKeywordArrayList);

                            keywordParser = 0;
                            addKeywordToDB();

                            // set the Current User
                            ((PursuitApplication) this.getApplication()).setCurrentStudent(newStudent);

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

    // Called By onClick, Attempts To Register A Student
    public void registerStudent(View v) {
        view = v;
        firstName = findViewById(R.id.txtFirstName);
        lastName = findViewById(R.id.txtLastName);
        university = findViewById(R.id.txtUniversity);
        major = findViewById(R.id.txtMajor);
        minor = findViewById(R.id.txtMinor);
        gpa = findViewById(R.id.txtStudentGPA2);
        bio = findViewById(R.id.txtBio);
        email = findViewById(R.id.txtEmail);
        username = findViewById(R.id.txtStudentUsername);
        studentPassword = findViewById(R.id.txtPassword);
        studentReEnterPassword = findViewById(R.id.txtReEnterPassword);
        interestKeywords = findViewById(R.id.txtInterests);

        checkEmail = toString(email);

        // SELECT * FROM Students WHERE username = ?
        Query usernameQuery = mRef.child("Students").orderByChild("username").equalTo(toString(username));

        if (usernameQuery == null) {
            Log.d(TAG, "Username query is null");
        } else {
            Log.d(TAG, "Username query is not null");
        }

        // Call To Username Event Listener
        usernameQuery.addListenerForSingleValueEvent(usernameListener);
    }

}
