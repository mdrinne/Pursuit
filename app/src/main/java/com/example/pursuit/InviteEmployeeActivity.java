package com.example.pursuit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.os.Bundle;
import android.widget.EditText;
import android.content.Intent;
import android.widget.Toast;

import com.example.pursuit.models.Company;
import com.example.pursuit.models.Employee;
import com.example.pursuit.models.EmployeeInvite;
import com.example.pursuit.models.Student;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class InviteEmployeeActivity extends AppCompatActivity {
    private static final String TAG = "InviteEmployeeActivity";

    private DatabaseReference dbRef;

    Company currentCompany;
    EditText employeeEmail;

    Employee matchedEmployeeEmail;
    Company matchedCompanyEmail;
    Student matchedStudentEmail;
    EmployeeInvite matchedEmployeeInviteEmail;

    BottomNavigationView bottomNavigation;

    EmployeeInvite newInvite;

    View view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invite_employee);
        bottomNavigation = findViewById(R.id.bottom_navigation);
        bottomNavigation.setOnNavigationItemSelectedListener(navigationItemSelectedListener);

        initializeCurrentCompany();

        dbRef = FirebaseDatabase.getInstance().getReference();
    }

    /* ********DATABASE******** */

    ValueEventListener employeeEmailListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            matchedEmployeeEmail = null;
            if (dataSnapshot.exists()) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Employee employee = snapshot.getValue(Employee.class);
                    matchedEmployeeEmail = employee;
                }
            }
            postEmployeeEmailListener();
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    };

    ValueEventListener studentEmailListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            matchedStudentEmail = null;
            if (dataSnapshot.exists()) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Student student = snapshot.getValue(Student.class);
                    matchedStudentEmail = student;
                }
            }
            postStudentEmailListener();
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    };

    ValueEventListener companyEmailListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            matchedCompanyEmail = null;
            if (dataSnapshot.exists()) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Company company = snapshot.getValue(Company.class);
                    matchedCompanyEmail = company;
                }
            }
            postCompanyEmailListener();
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    };

    ValueEventListener inviteEmailListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            matchedEmployeeInviteEmail = null;
            if (dataSnapshot.exists()) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    EmployeeInvite invite = snapshot.getValue(EmployeeInvite.class);
                    matchedEmployeeInviteEmail = invite;
                }
            }
            postInviteEmailListener();
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    };

    private void writeNewEmployeeInvite(String companyName, String employeeEmail) {
        Log.d(TAG, "in writeNewEmployeeInvite, company: " + companyName + "; email: " + employeeEmail);
        String code = RandomKeyGenerator.randomLowerNumeric(25);
        Log.d(TAG, "code: " + code);
        newInvite = new EmployeeInvite(code, employeeEmail);
        Log.d(TAG, "new invite created");
        dbRef.child("EmployeeInvites").child(currentCompany.getId()).child(code).setValue(newInvite);

        Intent intent = new Intent(this, viewCompanyEmployeeInvites.class);
        startActivity(intent);
    }

    /* ******END DATABASE****** */

    BottomNavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    switch (item.getItemId()) {
                        case R.id.navigation_home:
                            Intent i0 = new Intent(InviteEmployeeActivity.this, LandingActivity.class);
                            startActivity(i0);
                            finish();
                            return true;
                        case R.id.navigation_messages:
                            return true;
                        case R.id.navigation_profile:
                            Intent i2 = new Intent(InviteEmployeeActivity.this, CompanyProfileActivity.class);
                            startActivity(i2);
                            finish();
                            return true;
                    }
                    return false;
                }
            };

    public String toString(EditText text) {
        return text.getText().toString();
    }

    private void initializeCurrentCompany() {
        Log.d(TAG, "initializing company");
        currentCompany = ((PursuitApplication) this.getApplicationContext()).getCurrentCompany();
    }

    public void createEmployeeInvite(View v) {
        view = v;
        employeeEmail = findViewById(R.id.txtCompanyCode);

        Query employeeEmailQuery = dbRef.child("Employees").orderByChild("email").equalTo(toString(employeeEmail));
        employeeEmailQuery.addListenerForSingleValueEvent(employeeEmailListener);
    }

    private void postEmployeeEmailListener() {
        if (matchedEmployeeEmail != null) {
            Toast.makeText(view.getContext(), "Existing Account Associated With That Email", Toast.LENGTH_SHORT).show();
        } else {
            Query studentEmailQuery = dbRef.child("Students").orderByChild("email").equalTo(toString(employeeEmail));
            studentEmailQuery.addListenerForSingleValueEvent(studentEmailListener);
        }
    }

    private void postStudentEmailListener() {
        if (matchedStudentEmail != null) {
            Toast.makeText(view.getContext(), "Existing Account Associated With That Email", Toast.LENGTH_SHORT).show();
        } else {
            Query companyEmailQuery = dbRef.child("Companies").orderByChild("email").equalTo(toString(employeeEmail));
            companyEmailQuery.addListenerForSingleValueEvent(companyEmailListener);
        }
    }

    private void postCompanyEmailListener() {
        if (matchedCompanyEmail != null) {
            Toast.makeText(view.getContext(), "Existing Account Associated With That Email", Toast.LENGTH_SHORT).show();
        } else {
            Query inviteEmailQuery = dbRef.child("EmployeeInvites").child(currentCompany.getId()).orderByChild("employeeEmail").equalTo(toString(employeeEmail));
            inviteEmailQuery.addListenerForSingleValueEvent(inviteEmailListener);
        }
    }

    private void postInviteEmailListener() {
        if (matchedEmployeeInviteEmail != null) {
            Toast.makeText(view.getContext(), "Email Already Has An Active Invite", Toast.LENGTH_SHORT).show();
        } else {
            writeNewEmployeeInvite(currentCompany.getName(), employeeEmail.getText().toString());
        }
    }

}