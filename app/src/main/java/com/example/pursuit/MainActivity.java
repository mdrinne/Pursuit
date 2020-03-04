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

import com.example.pursuit.models.Company;
import com.example.pursuit.models.Student;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.Query;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private DatabaseReference dbRef;
    private ArrayList<Company> matchedCompanyEmails;
    private ArrayList<Student> matchedStudentEmails;
    private ArrayList<Student> matchedStudentUsernames;
    private View view;
    private String checkEmail;
    private String checkUsername;
    private String checkPassword;

    EditText loginUsernameEmail;
    EditText loginPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Get Database Reference
        dbRef = FirebaseDatabase.getInstance().getReference();

    }

    /* ********DATABASE******** */

    ValueEventListener studentUsernameListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            matchedStudentUsernames = new ArrayList<>();
            Log.d(TAG, "In studentUsernameListener onDataChange");
            if (dataSnapshot.exists()) {
                Log.d(TAG, "Snapshot exists");
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Log.d(TAG, "looping in snapshot");
                    Student student = snapshot.getValue(Student.class);
                    if (student == null) {
                        Log.d(TAG, "student is null");
                    } else {
                        Log.d(TAG, "student exists: " + student.getId() + "; " + student.getUsername());
                    }
                    matchedStudentUsernames.add(student);
                }
            }

            Log.d(TAG, "finished student username listener");
            postStudentUsernameListener();
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {
            Log.e(TAG, databaseError.toString());
        }
    };

    ValueEventListener studentEmailListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            matchedStudentEmails = new ArrayList<>();
            Log.d(TAG, "In studentEmailListener onDataChange");
            if (dataSnapshot.exists()) {
                Log.d(TAG, "Snapshot exists");
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Log.d(TAG, "looping in snapshot");
                    Student student = snapshot.getValue(Student.class);
                    Log.d(TAG, "got student");
                    if (student == null) {
                        Log.d(TAG, "student is null");
                    } else {
                        Log.d(TAG, "student exists: " + student.getId() + "; " + student.getEmail());
                    }
                    matchedStudentEmails.add(student);
                }
            }

            postStudentEmailListener();
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {
            Log.e(TAG, databaseError.toString());
        }
    };

    ValueEventListener companyEmailListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            matchedCompanyEmails = new ArrayList<>();
            Log.d(TAG, "In companyEmailListener onDataChange");
            if (dataSnapshot.exists()) {
                Log.d(TAG, "Snapshot exists");
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Log.d(TAG, "looping in snapshot");
                    Company company = snapshot.getValue(Company.class);
                    if (company == null) {
                        Log.d(TAG, "company is null");
                    } else {
                        Log.d(TAG, "company exists: " + company.getId() + "; " + company.getEmail());
                    }
                    matchedCompanyEmails.add(company);
                }
            }

            postCompanyEmailListener();
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {
            Log.e(TAG, databaseError.toString());
        }
    };

    /* ******END DATABASE****** */

    public boolean passwordsMatch(String dbPassword, String inputPassword) {
        if (dbPassword.equals(inputPassword)) { return true; }
        else { return false; }
    }

    public void postStudentUsernameListener () {
        int size = matchedStudentUsernames.size();
        Log.d(TAG, "matchedStudentUsernames size: " + size);

        if (size == 1) {
            if (passwordsMatch(matchedStudentUsernames.get(0).getPassword(), checkPassword)) {
                Intent intent = new Intent(this, LandingActivity.class);
                startActivity(intent);
            }
            else {
                Toast.makeText(view.getContext(), "Incorrect Password", Toast.LENGTH_LONG).show();
            }
        }
        else {
            Log.d(TAG, "USERNAME NOT FOUND");
            Toast.makeText(view.getContext(), "Username Does Not Exist", Toast.LENGTH_LONG).show();
        }
    }

    public void postStudentEmailListener() {
        int size = matchedStudentEmails.size();
        Log.d(TAG, "matchedStudentEmails size: " + size);

        if (size == 1) {
            if (passwordsMatch(matchedStudentEmails.get(0).getPassword(), checkPassword)) {
                Intent intent = new Intent(this, LandingActivity.class);
                startActivity(intent);
            }
            else {
                Toast.makeText(view.getContext(), "Incorrect Password", Toast.LENGTH_LONG).show();
            }
        }
        else {
            Log.d(TAG, "EMAIL NOT FOUND");
            Toast.makeText(view.getContext(), "Email Not Found", Toast.LENGTH_LONG).show();
        }
    }

    public void postCompanyEmailListener() {
        int size = matchedCompanyEmails.size();
        Log.d(TAG, "matchedCompanyEmails size: " + size);

        if (size == 1) {
            if (passwordsMatch(matchedCompanyEmails.get(0).getPassword(), checkPassword)) {
                Intent intent = new Intent(this, LandingActivity.class);
                startActivity(intent);
            }
            else {
                Toast.makeText(view.getContext(), "Incorrect Password", Toast.LENGTH_LONG).show();
            }
        }
        else {
            // SELECT * FROM Companies WHERE email = ?;
            Query studentEmailQuery = dbRef.child("Students").orderByChild("email").equalTo(checkEmail);

            // Call Student Email Listener
            studentEmailQuery.addListenerForSingleValueEvent(studentEmailListener);
        }
    }

    public void loginUsername() {
        // SELECT * FROM Students WHERE username = ?
        Query studentUsernameQuery = dbRef.child("Students").orderByChild("username").equalTo(checkUsername);

        // Call Student User Listener
        studentUsernameQuery.addListenerForSingleValueEvent(studentUsernameListener);
    }

    public void loginEmail() {
        // SELECT * FROM Companies WHERE email = ?
        Query companyEmailQuery = dbRef.child("Companies").orderByChild("email").equalTo(checkEmail);

        // Call Company Email Listener
        companyEmailQuery.addListenerForSingleValueEvent(companyEmailListener);
    }

    // Converts EditText Type To String
    public String toString(EditText text) {
        return text.getText().toString();
    }

    // Checks If Given String Is A Valid Email
    boolean isEmail(EditText text) {
        CharSequence email = text.getText().toString();
        if (!(!TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches())) { return false; }
        else { return true; }
    }

    // Checks If TextField Is Empty
    boolean isEmpty(EditText text) {
        CharSequence str = text.getText().toString();
        return TextUtils.isEmpty(str);
    }

    // Checks For Any Empty TextFields
    public boolean checkForEmpties() {
        if (isEmpty(loginUsernameEmail) && isEmpty(loginPassword)) {
            Toast.makeText(view.getContext(), "Username/Email and Password Required ", Toast.LENGTH_SHORT).show();
            return true;
        } else if (isEmpty(loginUsernameEmail)) {
            Toast.makeText(view.getContext(), "Username/Email Required ", Toast.LENGTH_SHORT).show();
            return true;
        } else if (isEmpty(loginPassword)) {
            Toast.makeText(view.getContext(), "Password Required ", Toast.LENGTH_SHORT).show();
            return true;
        }
        return false;
    }

    // Called By onClick, Attempts To Login User
    public void loginUser(View v) {
        Log.d(TAG, "loginUser Called");

        view = v;
        loginUsernameEmail = findViewById(R.id.txtUsernameEmail);
        loginPassword = findViewById(R.id.txtPassword);

        if (!checkForEmpties()) {
            Log.d(TAG, "No empty login fields");

            // Choose Login route based on what is received in Username/Login field
            if (isEmail(loginUsernameEmail)) {
                Log.d(TAG, "attempting EMAIL login");
                checkEmail = toString(loginUsernameEmail);
                checkPassword = toString(loginPassword);

                loginEmail();

            } else {
                Log.d(TAG, "attempting USERNAME login");
                checkUsername = toString(loginUsernameEmail);
                checkPassword = toString(loginPassword);

                loginUsername();
            }
        }
    }

    public void registerUser(View v) {
        Log.d(TAG, "register");

        Intent intent = new Intent(this, RegistrationActivity.class);
        startActivity(intent);
    }
}
