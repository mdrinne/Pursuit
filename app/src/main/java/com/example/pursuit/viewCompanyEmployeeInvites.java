package com.example.pursuit;

import com.google.android.material.bottomnavigation.BottomNavigationView;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

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
    String currentRole;
    BottomNavigationView bottomNavigation;

    private ArrayList<EmployeeInvite> companyInvites;
    private RecyclerView activeInvites;
    private inviteAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.company_view_employee_invites);
        bottomNavigation = findViewById(R.id.bottom_navigation);
        bottomNavigation.setOnNavigationItemSelectedListener(navigationItemSelectedListener);

        initializeCurrentCompany();
        initializeCurrentRole();

        dbref = FirebaseDatabase.getInstance().getReference();

//        activeInvites = findViewById(R.id.rcycEmployeeInvites);
//        buildRecyclerView();

        Query inviteQuery = dbref.child("EmployeeInvites").child(currentCompany.getId()).orderByKey();

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

    private void initializeCurrentRole() {
        currentRole = ((PursuitApplication) this.getApplicationContext()).getRole();
    }

    public void removeInvite(Integer position) {
        EmployeeInvite invite = companyInvites.get(position);
        dbref.child("EmployeeInvites").child(currentCompany.getId()).child(invite.getCode()).removeValue();
        companyInvites.remove(position);
        mAdapter.notifyItemRemoved(position);
    }


    private void buildRecyclerView() {
//        inviteAdapter myAdapter = new inviteAdapter(this, companyInvites);
//        activeInvites.setAdapter(myAdapter);
//        activeInvites.setLayoutManager(new LinearLayoutManager(this));
        activeInvites = findViewById(R.id.rcycEmployeeInvites);
        activeInvites.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mAdapter = new inviteAdapter(companyInvites);

        activeInvites.setLayoutManager(mLayoutManager);
        activeInvites.setAdapter(mAdapter);

        mAdapter.setOnItemClickListener(new inviteAdapter.OnItemClickListener() {
            @Override
            public void onDeleteClick(int position) {
                removeInvite(position);
            }
        });
    }

    public void inviteEmployee(View v) {
        if (currentRole.equals("Employee")) {
            Toast.makeText(v.getContext(), "Only Admin Has Access To Invite", Toast.LENGTH_SHORT).show();
        } else {
            Intent intent = new Intent(this, InviteEmployeeActivity.class);
            startActivity(intent);
        }
    }
}