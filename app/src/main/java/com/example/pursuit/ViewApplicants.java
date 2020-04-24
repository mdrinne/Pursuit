package com.example.pursuit;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pursuit.adapters.StudentAdapter;
import com.example.pursuit.models.CompanyOpportunity;
import com.example.pursuit.models.Student;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ViewApplicants extends AppCompatActivity {

    private CompanyOpportunity currentOpportunity;

    private ArrayList<String> appIDs;

    private ArrayList<Student> applicants;
    private RecyclerView viewApps;
    private StudentAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private DatabaseReference dbref;

    int studentParser;
    String currentStudentID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_applicants);

        dbref = FirebaseDatabase.getInstance().getReference();

        Query opportunityQuery = dbref.child("CompanyOpportunities").orderByKey().equalTo(getIntent().getStringExtra("EXTRA_OPPORTUNITY_ID"));
        opportunityQuery.addListenerForSingleValueEvent(opportunityListener);
    }

    private void continueOnCreate() {
        appIDs = currentOpportunity.getApplicants();
        if (appIDs != null) {
            studentParser = 0;
            applicants = new ArrayList<>();
            getStudents();
        }
    }

    private void getStudents() {
        if (studentParser < appIDs.size()) {
            currentStudentID = appIDs.get(studentParser);
            Query studentQuery = dbref.child("Students").orderByKey().equalTo(currentStudentID);
            studentQuery.addListenerForSingleValueEvent(studentListener);
        } else {
            buildRecyclerView();
        }
    }

    private void buildRecyclerView() {
        viewApps = findViewById(R.id.rcycApplicants);
        viewApps.setHasFixedSize(false);
        mLayoutManager = new LinearLayoutManager(this);
        mAdapter = new StudentAdapter(applicants);

        viewApps.setLayoutManager(mLayoutManager);
        viewApps.setAdapter(mAdapter);

        mAdapter.setStudentOnItemClickListener(new StudentAdapter.StudentOnItemClickListener() {
            @Override
            public void onCardClick(int position) {
                cardClick(position);
            }
        });
    }

    ValueEventListener opportunityListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            if (dataSnapshot.exists()) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    currentOpportunity = snapshot.getValue(CompanyOpportunity.class);
                }
            }
            continueOnCreate();
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    };

    ValueEventListener studentListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            if (dataSnapshot.exists()) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Student student = snapshot.getValue(Student.class);
                    applicants.add(student);
                }
            }
            studentParser++;
            getStudents();
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    };

    private void cardClick(int position) {
        Intent intent = new Intent(this, NonOwnerViewStudent.class);
        intent.putExtra("EXTRA_STUDENT_ID", applicants.get(position).getId());
        startActivity(intent);
    }
}
