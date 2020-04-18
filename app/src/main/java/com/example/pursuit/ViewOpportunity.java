package com.example.pursuit;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pursuit.adapters.CompanyKeywordAdapter;
import com.example.pursuit.models.Company;
import com.example.pursuit.models.CompanyOpportunity;
import com.example.pursuit.models.Employee;
import com.example.pursuit.models.Keyword;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ViewOpportunity extends AppCompatActivity {

    TextView opportunityPosition, opportunityWith, opportunityCity, opportunityState;
    TextView opportunityDescription, opportunityRequirements;

    BottomNavigationView bottomNavigation;

    Company currentCompany;
    Employee currentEmployee;
    String currentRole;
    CompanyOpportunity currentOpportunity;

    private DatabaseReference dbref;

    private ArrayList<String> keywords;
    private RecyclerView opportunityKeywords;
    private CompanyKeywordAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    Dialog addKeywordsDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_opportunity);
        bottomNavigation = findViewById(R.id.bottom_navigation);
        bottomNavigation.setOnNavigationItemSelectedListener(navigationItemSelectedListener);

        setCurrentUser();

        dbref = FirebaseDatabase.getInstance().getReference();

        Query getOpportunityQuery = dbref.child("CompanyOpportunities").child(currentCompany.getId()).orderByKey().equalTo(getIntent().getStringExtra("EXTRA_OPPORTUNITY_ID"));
        getOpportunityQuery.addListenerForSingleValueEvent(getOpportunityListener);

    }

    private void continueOnCreate() {
        opportunityPosition = findViewById(R.id.txtPosition);
        opportunityPosition.setText(currentOpportunity.getPosition());

        opportunityCity = findViewById(R.id.txtCity);
        opportunityCity.setText(currentOpportunity.getCity() + ", ");

        opportunityState = findViewById(R.id.txtState);
        opportunityState.setText(currentOpportunity.getState());

        opportunityWith = findViewById(R.id.txtWith);
        if (!currentOpportunity.getWithWho().equals("")) {
            opportunityWith.setText("With: " + currentOpportunity.getWithWho());
        }

        opportunityDescription = findViewById(R.id.txtDescription);
        opportunityDescription.setText(currentOpportunity.getDescription());

        opportunityRequirements = findViewById(R.id.txtOpportunityRequirements);
        TextView txtRequirements = findViewById(R.id.txtRequirements);

        if (currentOpportunity.getRequirements().equals("")) {
            ConstraintLayout innerLayout = findViewById(R.id.innerConstraintLayout);
            ConstraintSet constraintSet = new ConstraintSet();
            constraintSet.clone(innerLayout);
            constraintSet.connect(R.id.txtKeywords, ConstraintSet.TOP, R.id.txtDescription, ConstraintSet.BOTTOM, 15);
            opportunityRequirements.setVisibility(View.GONE);
            txtRequirements.setVisibility(View.GONE);
        } else {
            opportunityRequirements.setText(currentOpportunity.getRequirements());
        }

        keywords = currentOpportunity.getKeywords();
        buildRecyclerView();

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

    ValueEventListener deleteFromKeywordListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            if (dataSnapshot.exists()) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Keyword key = snapshot.getValue(Keyword.class);
                    ArrayList<String> opportunitiesArray = key.getOpportunities();
                    opportunitiesArray.remove(currentOpportunity.getId());
                    dbref.child("Keywords").child(key.id).child("opportunities").setValue(opportunitiesArray);
                }
            }
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
        if (currentRole.equals("Company")) {
            mAdapter = new CompanyKeywordAdapter(keywords, null);
        } else {
            mAdapter = new CompanyKeywordAdapter(keywords, currentEmployee);
        }

        opportunityKeywords.setLayoutManager(mLayoutManager);
        opportunityKeywords.setAdapter(mAdapter);

        mAdapter.setCompanyKeywordOnItemClickListener(new CompanyKeywordAdapter.CompanyKeywordOnItemListener() {
            @Override
            public void onDeleteClick(int position) {
                deleteKeyword(position);
            }
        });
    }

    private void deleteKeyword(int position) {
        String str = keywords.get(position);
        keywords.remove(str);
        dbref.child("CompanyOpportunities").child(currentCompany.getId()).child(currentOpportunity.getId()).child("keywords").setValue(keywords);
        Query deleteFromKeywordQuery = dbref.child("Keywords").orderByChild("text").equalTo(str);
        deleteFromKeywordQuery.addListenerForSingleValueEvent(deleteFromKeywordListener);
        mAdapter.notifyItemRemoved(position);
    }

    private void setCurrentUser() {
        currentCompany = ((PursuitApplication) this.getApplicationContext()).getCurrentCompany();
        currentRole = ((PursuitApplication) this.getApplicationContext()).getRole();
        if (currentRole.equals("Employee")) {
            currentEmployee = ((PursuitApplication) this.getApplicationContext()).getCurrentEmployee();
        }
    }

    public void addKeywords(View v) {

    }

    BottomNavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    switch (item.getItemId()) {
                        case R.id.navigation_home:
                            Intent i0 = new Intent(ViewOpportunity.this, LandingActivity.class);
                            startActivity(i0);
                            finish();
                            return true;
                        case R.id.navigation_messages:
                            Intent i1 = new Intent(ViewOpportunity.this, MessagesActivity.class);
                            startActivity(i1);
                            finish();
                            return true;
                        case R.id.navigation_profile:
                            Intent i2 = new Intent(ViewOpportunity.this, CompanyProfileActivity.class);
                            startActivity(i2);
                            finish();
                            return true;
                    }
                    return false;
                }
            };

}
