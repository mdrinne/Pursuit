package com.example.pursuit;

import com.google.android.material.bottomnavigation.BottomNavigationView;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pursuit.models.Company;
import com.example.pursuit.models.EmployeeInvite;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.Query;

import java.util.ArrayList;

public class viewCompanyEmployeeInvites extends AppCompatActivity {
    private static final String TAG = "viewEmployeeInvites";

    private DatabaseReference dbref;

    Company currentCompany;
    BottomNavigationView bottomNavigation;
    private ArrayList<EmployeeInvite> companyInvites;

    RecyclerView activeInvites;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.company_view_employee_invites);
        bottomNavigation = findViewById(R.id.bottom_navigation);
        bottomNavigation.setOnNavigationItemSelectedListener(navigationItemSelectedListener);

        initializeCurrentCompany();
        dbref = FirebaseDatabase.getInstance().getReference();

        activeInvites = findViewById(R.id.rcycEmployeeInvites);

        Query inviteQuery = dbref.child("EmployeeInvites").child(currentCompany.getName()).orderByKey();

        if (inviteQuery == null) {
            Log.d(TAG, "Invite query is null");
        } else {
            Log.d(TAG, "Invite query is not null");
        }

        inviteQuery.addListenerForSingleValueEvent(employeeInviteListener);
    }

    /* ********DATABASE******** */

    ValueEventListener employeeInviteListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            companyInvites = new ArrayList<>();
            Log.d(TAG, "In employeeInviteListener onDataChange");
            if (dataSnapshot.exists()) {
                Log.d(TAG, "Snapshot exists");
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Log.d(TAG, "looping in snapshot");
                    EmployeeInvite invite = snapshot.getValue(EmployeeInvite.class);
                    if (invite == null) {
                        Log.d(TAG, "invite is null");
                    } else {
                        Log.d(TAG, "invite exists");
                    }
                    companyInvites.add(invite);
                }
            }

            postOnCreate();
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
                            Intent i0 = new Intent(viewCompanyEmployeeInvites.this, LandingActivity.class);
                            startActivity(i0);
                            finish();
                            return true;
                        case R.id.navigation_messages:
                            return true;
                        case R.id.navigation_profile:
                            Intent i2 = new Intent(viewCompanyEmployeeInvites.this, CompanyProfileActivity.class);
                            startActivity(i2);
                            finish();
                            return true;
                    }
                    return false;
                }
            };

    private void initializeCurrentCompany() {
        Log.d(TAG, "initializing company");
        currentCompany = ((PursuitApplication) this.getApplicationContext()).getCurrentCompany();
    }

    private void postOnCreate() {
        Log.d(TAG, Integer.toString(companyInvites.size()));
        inviteAdapter myAdapter = new inviteAdapter(this, companyInvites);
        activeInvites.setAdapter(myAdapter);
        activeInvites.setLayoutManager(new LinearLayoutManager(this));
    }

    public void inviteEmployee(View v) {
        Intent intent = new Intent(this, InviteEmployeeActivity.class);
        startActivity(intent);
    }
}