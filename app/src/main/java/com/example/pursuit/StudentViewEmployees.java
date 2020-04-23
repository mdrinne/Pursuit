package com.example.pursuit;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pursuit.adapters.StudentEmployeeAdapter;
import com.example.pursuit.models.Employee;
import com.example.pursuit.models.Student;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class StudentViewEmployees extends AppCompatActivity {

    BottomNavigationView bottomNavigation;
    Student currentStudent;

    private DatabaseReference dbref;

    private ArrayList<Employee> employees;
    private RecyclerView viewEmployees;
    private StudentEmployeeAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emploee_management);
        bottomNavigation = findViewById(R.id.bottom_navigation);
        bottomNavigation.setOnNavigationItemSelectedListener(navigationItemSelectedListener);

        currentStudent = ((PursuitApplication) this.getApplicationContext()).getCurrentStudent();

        dbref = FirebaseDatabase.getInstance().getReference();

        employees = new ArrayList<>();
        Query employeeQuery = dbref.child("Employees").orderByChild("companyID").equalTo(getIntent().getStringExtra("EXTRA_COMPANY_ID"));
        employeeQuery.addListenerForSingleValueEvent(employeeListener);
    }

    private void buildRecyclerView() {
        viewEmployees = findViewById(R.id.rcycCompanyEmployees);
        viewEmployees.setHasFixedSize(false);
        mLayoutManager = new LinearLayoutManager(this);
        mAdapter = new StudentEmployeeAdapter(employees);

        viewEmployees.setLayoutManager(mLayoutManager);
        viewEmployees.setAdapter(mAdapter);

        mAdapter.setStudentEmployeeOnItemClickListener(new StudentEmployeeAdapter.StudentEmployeeOnItemClickListener() {

        });

    }

    /* ********DATABASE******** */

    ValueEventListener employeeListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            if (dataSnapshot.exists()) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    employees.add(snapshot.getValue(Employee.class));
                }
            }
            buildRecyclerView();
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    };

    /* ******END DATABASE****** */

    BottomNavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    switch (item.getItemId()) {
                        case R.id.navigation_home:
                            Intent i0 = new Intent(StudentViewEmployees.this, LandingActivity.class);
                            startActivity(i0);
                            finish();
                            return true;
                        case R.id.navigation_messages:
                            Intent i1 = new Intent(StudentViewEmployees.this, MessagesActivity.class);
                            startActivity(i1);
                            finish();
                            return true;
                        case R.id.navigation_profile:
                            Intent i2 = new Intent(StudentViewEmployees.this, StudentProfileActivity.class);
                            startActivity(i2);
                            finish();
                            return true;
                    }
                    return false;
                }
            };
}
