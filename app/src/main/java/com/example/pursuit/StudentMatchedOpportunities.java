package com.example.pursuit;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pursuit.adapters.StudentOpportunityAdapter;
import com.example.pursuit.models.CompanyOpportunity;
import com.example.pursuit.models.Keyword;
import com.example.pursuit.models.Student;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class StudentMatchedOpportunities extends AppCompatActivity {

    Student currentStudent;

    private DatabaseReference dbref;

    TextView noInterests;
    RecyclerView opportunitiesRecycler;

    ArrayList<String> interests;
    String currentInterest;
    ArrayList<String> opportunityIds;
    String currentOpportunityID;

    private ArrayList<CompanyOpportunity> allMatchedOpportunities;
    private RecyclerView viewMatchedEmployees;
    private StudentOpportunityAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    int interestsParser;
    int getOpportunitiesParser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_matched_opportunities);

        currentStudent = ((PursuitApplication) this.getApplication()).getCurrentStudent();
        interests = currentStudent.getInterestKeywords();

        noInterests = findViewById(R.id.txtNoInterests);
        opportunitiesRecycler = findViewById(R.id.rcycOpportunities);

        if (interests == null) {
            opportunitiesRecycler.setVisibility(View.GONE);
            noInterests.setText("No available opportunities match your interests");
        } else {
            dbref = FirebaseDatabase.getInstance().getReference();
            interestsParser = 0;
            cycleInterests();
        }
    }

    public void cycleInterests() {
        if (interestsParser < interests.size()) {
            currentInterest = interests.get(interestsParser);
            Query keywordQuery = dbref.child("Keywords").orderByChild("text").equalTo(currentInterest);
            keywordQuery.addListenerForSingleValueEvent(keywordListener);
        } else {
            getOpportunitiesParser = 0;
            getOpportunities();
        }
    }

    public void getOpportunities() {
        if (getOpportunitiesParser < opportunityIds.size()) {
            currentOpportunityID = opportunityIds.get(getOpportunitiesParser);
//            Query opportunityQuery = dbref.child("Comp")
        }
    }

    /* ********DATABASE******** */

    ValueEventListener keywordListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            if (dataSnapshot.exists()) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Keyword keyword = snapshot.getValue(Keyword.class);
                    ArrayList<String> opportunities = keyword.getOpportunities();
                    for (int i=0; i<opportunities.size(); i++) {
                        if (!opportunityIds.contains(opportunities.get(i))) {
                            opportunityIds.add(opportunities.get(i));
                        }
                    }
                }
            }
            interestsParser++;
            cycleInterests();
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    };

    /* ******END DATABASE****** */

}
