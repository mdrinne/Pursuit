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

public class StudentRegistration extends AppCompatActivity {

    private static final String TAG = "StudentRegistration";

    private DatabaseReference mRef;
    private FirebaseDatabase mFirebaseDatabase;

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

        mRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                showData(dataSnapshot);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void showData(DataSnapshot dataSnapshot) {
        for (DataSnapshot ds : dataSnapshot.getChildren()) {
            Student student = new Student();
            student.setUsername(ds.child("Student").child("mdrinne").getValue(Student.class).getUsername());

            Log.d(TAG, "showData: username: " + student.getUsername());
        }
    }

    private void writeNewStudent(int id, String fname, String lname, String university, String major, String minor,
            String gpa, String bio, String email, String username, String password) {
        Student student = new Student(id, fname, lname, university, major, minor, gpa, bio, email, username, password);

        mRef.child("Users").child("Student").child(username).setValue(student);
    }

    boolean isEmail(EditText text, View v) {
        CharSequence email = text.getText().toString();
        if (!(!TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches())) {
            Toast.makeText(v.getContext(), "Invalid Email", 2).show();
            return false;
        } else
            return true;
    }

    boolean isEmpty(EditText text) {
        CharSequence str = text.getText().toString();
        return TextUtils.isEmpty(str);
    }

    String toString(EditText text) {
        return text.getText().toString();
    }

    public boolean checkForEmpties(View v) {
        if (isEmpty(firstName) || isEmpty(lastName) || isEmpty(university) || isEmpty(major) || isEmpty(minor)
                || isEmpty(gpa) || isEmpty(bio) || isEmpty(email) || isEmpty(username) || isEmpty(password1)
                || isEmpty(password2)) {
            Toast.makeText(v.getContext(), "All Fields are Required", 2).show();
            return true;
        }
        return false;
    }

    public boolean passwordMatch(View v) {
        if (toString(password1).equals(toString(password2)))
            return true;
        Toast.makeText(v.getContext(), "Passwords Did Not Match", 2).show();
        return false;
    }

    public boolean usernameTaken(View v) {
        Log.d(getClass().getSimpleName(), "in usernameTaken");

        return false;
    }

    // public boolean emailTaken(View v) {
    // Cursor c = db.rawQuery("SELECT email FROM " + APPLICANT_TABLE + " WHERE email
    // = '"
    // + toString(email) + "';", null);
    //
    // if (c != null) {
    // if (c.moveToFirst()) {
    // Toast.makeText(v.getContext(), "Email is Already Taken", 2).show();
    // return true;
    // }
    // else return false;
    // }
    // Log.e(getClass().getSimpleName(), "Cursor returned null while searching of
    // username is taken");
    // return false;
    // }
    //
    // public boolean insertDB() {
    // try {
    // db.execSQL("INSERT INTO " + APPLICANT_TABLE + " VALUES (" + firstName + ", "
    // + lastName +
    // ", " + university + ", " + major + ", " + minor + ", " + gpa + ", " + bio +
    // email + ", " + username + ", " + password1 + ", " + password2 + ");");
    // Log.d(getClass().getSimpleName(), "inserted user");
    // } catch (SQLiteException se) {
    // Log.e(getClass().getSimpleName(), "Failed to register student");
    // }
    // return true;
    // }

    public boolean processRequest(View v) {
        if (!checkForEmpties(v)) {
            Log.d(getClass().getSimpleName(), "no empty fields");
            if (isEmail(email, v)) {
                Log.d(getClass().getSimpleName(), "email is valid");
                if (passwordMatch(v)) {
                    Log.d(getClass().getSimpleName(), "passwords match");

                    if (!usernameTaken(v)) {
                        Log.d(getClass().getSimpleName(), "username not taken");
                        // if (!emailTaken(v)) {
                        // Log.d(getClass().getSimpleName(), "email not taken");
                        // if (insertDB()) {

                        writeNewStudent(1, toString(firstName), toString(lastName), toString(university),
                                toString(major), toString(minor), toString(gpa), toString(bio), toString(email),
                                toString(username), toString(password1));

                        return true;
                        // }
                        // }
                    }
                    // long res = db.insertStudent(toString(firstName), toString(lastName),
                    // toString(university),
                    // toString(major), toString(minor), toString(gpa),
                    // toString(bio), toString(email), toString(username),
                    // toString(password1));
                    //
                    // Log.d(getClass().getSimpleName(), Long.toString(res));
                    //
                    // if (res == -1) return false;
                    // else return true;
                }
            }
        }
        return false;
    }

    // called by onClick, attempts to register a student
    public void registerStudent(View v) {
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

        if (processRequest(v)) {
            Intent intent = new Intent(this, LandingActivity.class);
            startActivity(intent);
        }
    }

}
