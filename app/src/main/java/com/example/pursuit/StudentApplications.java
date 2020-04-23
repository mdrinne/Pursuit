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
import com.example.pursuit.models.Company;
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
    CompanyOpportunity deleteOpp;
    Company currentCompany;


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
                deleteApplication(position);
            }

            @Override
            public void onCardClick(int position) {
                viewOpportunity(position);
            }
        });
    }

    private void deleteApplication(int position) {
        deleteOpp = opportunities.get(position);
        for (int i=0; i<appliedTo.size(); i++) {
            if (appliedTo.get(i).equals(deleteOpp.getId())) {
                appliedTo.remove(i);
                opportunities.remove(deleteOpp);
                break;
            }
        }
        currentStudent.setAppliedTo(appliedTo);
        dbref.child("Students").child(currentStudent.getId()).setValue(currentStudent);
        ((PursuitApplication) this.getApplication()).setCurrentStudent(currentStudent);

        Query companyQuery = dbref.child("Companies").orderByKey().equalTo(deleteOpp.getCompanyID());
        companyQuery.addListenerForSingleValueEvent(companyListener);
    }

    private void continueDelete() {
        ArrayList<String> applicants = deleteOpp.getApplicants();
        for (int i =0; i<applicants.size(); i++) {
            if (applicants.get(i).equals(currentStudent.getId())) {
                applicants.remove(i);
                break;
            }
        }

        deleteOpp.setApplicants(applicants);

        ArrayList<CompanyOpportunity> companyOpps = currentCompany.getOpportunities();
        for (int i=0; i<companyOpps.size(); i++) {
            if (companyOpps.get(i).getId().equals(deleteOpp.getId())) {
                companyOpps.remove(companyOpps.get(i));
                break;
            }
        }
        companyOpps.add(deleteOpp);
        currentCompany.setOpportunities(companyOpps);

        dbref.child("CompanyOpportunities").child(deleteOpp.getId()).child("applicants").setValue(applicants);
        dbref.child("Companies").child(currentCompany.getId()).setValue(currentCompany);
        mAdapter.notifyDataSetChanged();

    }

    ValueEventListener companyListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            if (dataSnapshot.exists()) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    currentCompany = snapshot.getValue(Company.class);
                }
            }
            continueDelete();
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    };

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
