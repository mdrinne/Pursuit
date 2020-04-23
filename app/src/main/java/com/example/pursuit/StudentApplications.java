package com.example.pursuit;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pursuit.adapters.StudentApplicationAdapter;
import com.example.pursuit.models.CompanyOpportunity;
import com.example.pursuit.models.Student;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class StudentApplications extends AppCompatActivity {

    Student currentStudent;
    String currentOpportunity;


    private ArrayList<String> appliedTo;

    private DatabaseReference dbref;

    private ArrayList<CompanyOpportunity> opportunities;
    private RecyclerView viewApplications;
    private StudentApplicationAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    int appParser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_applications);

        currentStudent = ((PursuitApplication) this.getApplication()).getCurrentStudent();

        dbref = FirebaseDatabase.getInstance().getReference();

        appliedTo = currentStudent.getAppliedTo();
        if (appliedTo == null) {
            TextView noApps = findViewById(R.id.txtNoApplications);
            noApps.setText("You have not applied to any opportunities");
        } else {
            appParser = 0;
            opportunities = new ArrayList<>();
            cycleOpportunities();
        }
    }

    private void cycleOpportunities() {
        if (appParser < appliedTo.size()) {
            currentOpportunity = appliedTo.get(appParser);
            Query opportunityQuery = dbref.child("CompanyOpportunities").orderByKey().equalTo(currentOpportunity);
            opportunityQuery.addListenerForSingleValueEvent(opportunityListener);
        } else {
            buildRecyclerView();
        }
    }

    private void buildRecyclerView() {
        viewApplications = findViewById(R.id.rcycOpportunitites);
        viewApplications.setHasFixedSize(false);
        mLayoutManager = new LinearLayoutManager(this);
        mAdapter = new StudentApplicationAdapter(opportunities);

        viewApplications.setLayoutManager(mLayoutManager);
        viewApplications.setAdapter(mAdapter);

        mAdapter.setStudentApplicationOnItemClickListener(new StudentApplicationAdapter.StudentApplicationOnItemClickListener() {
            @Override
            public void onDeleteClick(int position) {

            }

            @Override
            public void onCardClick(int position) {
                viewOpportunity(position);
            }
        });
    }

    private void viewOpportunity(int position) {
        Intent intent = new Intent(this, NonOwnerViewOpportunity.class);
        intent.putExtra("EXTRA_OPPORTUNITY_ID", opportunities.get(position).getId());
        startActivity(intent);
    }

    ValueEventListener opportunityListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            if (dataSnapshot.exists()) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    CompanyOpportunity opp = snapshot.getValue(CompanyOpportunity.class);
                    opportunities.add(opp);
                }
            }
            appParser++;
            cycleOpportunities();
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    };

    BottomNavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    switch (item.getItemId()) {
                        case R.id.navigation_home:
                            Intent landing = new Intent(StudentApplications.this, LandingActivity.class);
                            startActivity(landing);
                            finish();
                            return true;
                        case R.id.navigation_discover:
                            Intent discover = new Intent(StudentApplications.this, DiscoverActivity.class);
                            startActivity(discover);
                            finish();
                            return true;
                        case R.id.navigation_messages:
                            Intent messages = new Intent(StudentApplications.this, ConversationsActivity.class);
                            startActivity(messages);
                            finish();
                            return true;
                        case R.id.navigation_profile:
                            Intent profile = new Intent(StudentApplications.this, StudentProfileActivity.class);
                            startActivity(profile);
                            finish();
                            return true;
                    }
                    return false;
                }
            };

}
