package com.example.pursuit;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pursuit.adapters.StudentOpportunityKeywordAdapter;
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

public class StudentViewOpportunity extends AppCompatActivity {
    TextView companyName, position, city, state, with, description, requirements;
    BottomNavigationView bottomNavigation;

    Student currentStudent;
    CompanyOpportunity currentOpportunity;

    private DatabaseReference dbref;

    private ArrayList<String> keywords;
    private RecyclerView opportunityKeywords;
    private StudentOpportunityKeywordAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_non_owner_view_opportunity);
        bottomNavigation = findViewById(R.id.bottom_navigation);
        bottomNavigation.setOnNavigationItemSelectedListener(navigationItemSelectedListener);

        currentStudent = ((PursuitApplication) this.getApplicationContext()).getCurrentStudent();

        dbref = FirebaseDatabase.getInstance().getReference();

        Query getOpportunityQuery = dbref.child("CompanyOpportunities").orderByKey().equalTo(getIntent().getStringExtra("EXTRA_OPPORTUNITY_ID"));
        getOpportunityQuery.addListenerForSingleValueEvent(getOpportunityListener);
    }

    private void continueOnCreate() {
        companyName = findViewById(R.id.txtUniversity);
        companyName.setText(currentOpportunity.getCompanyName());

        position = findViewById(R.id.txtMajor);
        position.setText(currentOpportunity.getPosition());

        city = findViewById(R.id.txtKeyword);
        city.setText(currentOpportunity.getCity() + ", ");

        state = findViewById(R.id.txtMinimumGPA);
        state.setText(currentOpportunity.getState());

        if (!currentOpportunity.getWithWho().equals("")) {
            with = findViewById(R.id.txtWith);
            with.setText("With: " + currentOpportunity.getWithWho());
        }

        description = findViewById(R.id.txtDescription);
        description.setText(currentOpportunity.getDescription());

        requirements = findViewById(R.id.txtOpportunityRequirements);
        if (currentOpportunity.getRequirements().equals("")) {
            ConstraintLayout innerLayout = findViewById(R.id.innerLayout);
            ConstraintSet constraintSet = new ConstraintSet();
            constraintSet.clone(innerLayout);
            constraintSet.connect(R.id.txtKeywords, ConstraintSet.TOP, R.id.txtDescription, ConstraintSet.BOTTOM, 15);
            requirements.setVisibility(View.GONE);
            findViewById(R.id.txtRequirements).setVisibility(View.GONE);
        } else {
            requirements.setText(currentOpportunity.getRequirements());
        }

        if (currentOpportunity.getKeywords() == null) {
            findViewById(R.id.rcycKeywords).setVisibility(View.GONE);
            findViewById(R.id.txtKeywords).setVisibility(View.GONE);
        } else {
            keywords = currentOpportunity.getKeywords();
            buildRecyclerView();
        }
    }

    /* ********DATABASE******** */

    ValueEventListener getOpportunityListener = new ValueEventListener() {
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

    /* ******END DATABASE****** */

    private void buildRecyclerView() {
        opportunityKeywords = findViewById(R.id.rcycKeywords);
        opportunityKeywords.setHasFixedSize(false);
        mLayoutManager = new LinearLayoutManager(this);
        mAdapter = new StudentOpportunityKeywordAdapter(keywords);

        opportunityKeywords.setLayoutManager(mLayoutManager);
        opportunityKeywords.setAdapter(mAdapter);
    }

    public void companyProfile(View v) {
        Intent intent = new Intent(StudentViewOpportunity.this, StudentViewCompany.class);
        intent.putExtra("EXTRA_COMPANY_ID", currentOpportunity.getCompanyID());
        startActivity(intent);
    }

    BottomNavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    switch (item.getItemId()) {
                        case R.id.navigation_home:
                            Intent i0 = new Intent(StudentViewOpportunity.this, LandingActivity.class);
                            startActivity(i0);
                            finish();
                            return true;
                        case R.id.navigation_discover:
                            Intent discover = new Intent(StudentViewOpportunity.this, DiscoverActivity.class);
                            startActivity(discover);
                            finish();
                            return true;
                        case R.id.navigation_messages:
                            Intent i1 = new Intent(StudentViewOpportunity.this, ConversationsActivity.class);
                            startActivity(i1);
                            finish();
                            return true;
                        case R.id.navigation_profile:
                            Intent i2 = new Intent(StudentViewOpportunity.this, StudentProfileActivity.class);
                            startActivity(i2);
                            finish();
                            return true;
                    }
                    return false;
                }
            };
}
