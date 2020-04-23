package com.example.pursuit;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.recyclerview.widget.LinearLayoutManager;
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

    private final String TAG = "StudentMAtchedOpps";

    Student currentStudent;

    private DatabaseReference dbref;

    TextView noInterests;
    RecyclerView opportunitiesRecycler;
    Button btnFilter;
    Button btnClearFilter;
    ConstraintLayout innerLayout;

    ArrayList<String> interests;
    String currentInterest;
    ArrayList<String> opportunityIds;
    String currentOpportunityID;

    private ArrayList<CompanyOpportunity> allMatchedOpportunities;
    private RecyclerView viewMatchedEmployees;
    private StudentOpportunityAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ArrayList<CompanyOpportunity> filteredResults;

    private ArrayList<String> companies;
    private ArrayList<String> positions;
    private ArrayList<String> keywords;
    private ArrayList<String> cities;
    private ArrayList<String> states;

    String companyFilter;
    String positionFilter;
    String keywordFilter;
    String cityFilter;
    String stateFilter;

    int interestsParser;
    int getOpportunitiesParser;

    private Dialog filterDialog;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_matched_opportunities);

        currentStudent = ((PursuitApplication) this.getApplication()).getCurrentStudent();
        interests = currentStudent.getInterestKeywords();

        noInterests = findViewById(R.id.txtNoStudents);
        opportunitiesRecycler = findViewById(R.id.rcycStudents);
        btnFilter = findViewById(R.id.btnFilter);

        btnClearFilter = findViewById(R.id.btnClearFilter);
        btnClearFilter.setVisibility(View.GONE);
        innerLayout = findViewById(R.id.innerLayout);

        allMatchedOpportunities = new ArrayList<>();
        filteredResults = new ArrayList<>();
        companies = new ArrayList<>();
        companies.add("");
        positions = new ArrayList<>();
        positions.add("");
        keywords = new ArrayList<>();
        keywords.add("");
        cities = new ArrayList<>();
        cities.add("");
        states = new ArrayList<>();
        states.add("");

        filterDialog = new Dialog(this);

        if (interests == null) {
            opportunitiesRecycler.setVisibility(View.GONE);
            btnFilter.setVisibility(View.GONE);
            noInterests.setText("No available opportunities match your interests");
        } else {
            dbref = FirebaseDatabase.getInstance().getReference();
            opportunityIds = new ArrayList<>();
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
            if (opportunityIds.size() == 0) {

                opportunitiesRecycler.setVisibility(View.GONE);
                btnFilter.setVisibility(View.GONE);
                noInterests.setText("No available opportunities match your interests");
            } else {
                getOpportunitiesParser = 0;
                getOpportunities();
            }
        }
    }

    public void getOpportunities() {
        if (getOpportunitiesParser < opportunityIds.size()) {
            currentOpportunityID = opportunityIds.get(getOpportunitiesParser);
            Query opportunityQuery = dbref.child("CompanyOpportunities").orderByKey().equalTo(currentOpportunityID);
            opportunityQuery.addListenerForSingleValueEvent(opportunityListener);
        } else {
            buildRecyclerView();
        }
    }

    public void buildRecyclerView() {
        viewMatchedEmployees = findViewById(R.id.rcycStudents);
        viewMatchedEmployees.setHasFixedSize(false);
        mLayoutManager = new LinearLayoutManager(this);
        for (int i=0; i<allMatchedOpportunities.size(); i++) {
            filteredResults.add(allMatchedOpportunities.get(i));
        }
        mAdapter = new StudentOpportunityAdapter(filteredResults);

        viewMatchedEmployees.setLayoutManager(mLayoutManager);
        viewMatchedEmployees.setAdapter(mAdapter);

        mAdapter.setStudentOpportunityOnItemClickListener(new StudentOpportunityAdapter.StudentOpportunityOnItemClickListener() {
            @Override
            public void onCardClick(int position) {
                viewOpportunity(position);
            }
        });
    }

    /* ********DATABASE******** */

    ValueEventListener keywordListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            if (dataSnapshot.exists()) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Keyword keyword = snapshot.getValue(Keyword.class);
                    keywords.add(keyword.getText());
                    ArrayList<String> opportunities = keyword.getOpportunities();
                    if (opportunities == null) {
                        opportunities = new ArrayList<>();
                    }
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

    ValueEventListener opportunityListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            if (dataSnapshot.exists()) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    CompanyOpportunity opportunity = snapshot.getValue(CompanyOpportunity.class);
                    if (opportunity.getApproved() == 1) {
                        allMatchedOpportunities.add(opportunity);
                        if (!companies.contains(opportunity.getCompanyName())) {
                            companies.add(opportunity.getCompanyName());
                        }
                        if (!positions.contains(opportunity.getPosition())) {
                            positions.add(opportunity.getPosition());
                        }
                        if (!cities.contains(opportunity.getCity())) {
                            cities.add(opportunity.getCity());
                        }
                        if (!states.contains(opportunity.getState())) {
                            states.add(opportunity.getState());
                        }
                    }
                }
            }
            getOpportunitiesParser++;
            getOpportunities();
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    };

    /* ******END DATABASE****** */

    public void filter(View v) {
        final Spinner companyName, position, keyword, city, state;
        Button cancel, confirm;

        filterDialog.setContentView(R.layout.student_opportunity_filter_pop_up);
        cancel = filterDialog.findViewById(R.id.btnCancel);
        confirm = filterDialog.findViewById(R.id.btnConfirm);

        companyName = filterDialog.findViewById(R.id.spnCompanyName);
        ArrayAdapter<String> companyNameAdapter = new ArrayAdapter<String>(
                                                this,
                                                        android.R.layout.simple_spinner_item,
                                                        companies);
        companyNameAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        companyName.setAdapter(companyNameAdapter);

        position = filterDialog.findViewById(R.id.spnPosition);
        ArrayAdapter<String> positionAdapter = new ArrayAdapter<String>(
                                                this,
                                                        android.R.layout.simple_spinner_item,
                                                        positions);
        positionAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        position.setAdapter(positionAdapter);

        keyword = filterDialog.findViewById(R.id.spnKeyword);
        ArrayAdapter<String> keywordAdapter = new ArrayAdapter<String>(
                                                this,
                                                        android.R.layout.simple_spinner_item,
                                                        keywords);
        keywordAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        keyword.setAdapter(keywordAdapter);

        city = filterDialog.findViewById(R.id.spnCity);
        ArrayAdapter<String> cityAdapter = new ArrayAdapter<String>(
                                                this,
                                                        android.R.layout.simple_spinner_item,
                                                        cities);
        cityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        city.setAdapter(cityAdapter);

        state = filterDialog.findViewById(R.id.spnState);
        ArrayAdapter<String> stateAdapter = new ArrayAdapter<String>(
                                                this,
                                                        android.R.layout.simple_spinner_item,
                                                        states);
        stateAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        state.setAdapter(stateAdapter);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filterDialog.dismiss();
            }
        });

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                companyFilter = companyName.getSelectedItem().toString();
                positionFilter = position.getSelectedItem().toString();
                keywordFilter = keyword.getSelectedItem().toString();
                cityFilter = city.getSelectedItem().toString();
                stateFilter = state.getSelectedItem().toString();

                for (int i=0; i<allMatchedOpportunities.size(); i++) {
                    if (!filteredResults.contains(allMatchedOpportunities.get(i))) {
                        filteredResults.add(allMatchedOpportunities.get(i));
                    }
                }
                if (!allFiltersEmpty()) {
                    filterOpportunities();
                    btnClearFilter.setVisibility(View.VISIBLE);
                    ConstraintSet clearFilter = new ConstraintSet();
                    clearFilter.clone(innerLayout);
                    clearFilter.connect(R.id.btnClearFilter, ConstraintSet.TOP, R.id.btnFilter, ConstraintSet.BOTTOM, 0);
                    clearFilter.connect(R.id.rcycStudents, ConstraintSet.TOP, R.id.btnClearFilter, ConstraintSet.BOTTOM, 0);
                    clearFilter.applyTo(innerLayout);
                }

                mAdapter.notifyDataSetChanged();
                filterDialog.dismiss();
            }
        });

        filterDialog.show();
    }

    private boolean allFiltersEmpty() {
        if (isEmpty(companyFilter) && isEmpty(positionFilter)
                && isEmpty(keywordFilter) && isEmpty(cityFilter)
                && isEmpty(stateFilter)) {
            return true;
        } else {
            return false;
        }
    }

    private boolean isEmpty(String str) {
        if (str.equals("")) {
            return true;
        } else {
            return false;
        }
    }

    private void filterOpportunities() {
        boolean companyEmpty = isEmpty(companyFilter);
        boolean positionEmpty = isEmpty(positionFilter);
        boolean keywordEmpty = isEmpty(keywordFilter);
        boolean cityEmpty = isEmpty(cityFilter);
        boolean stateEmpty = isEmpty(stateFilter);

        for (int i=0; i<allMatchedOpportunities.size(); i++) {
            CompanyOpportunity temp = allMatchedOpportunities.get(i);
            if (!companyEmpty && filteredResults.contains(temp) && !temp.getCompanyName().equals(companyFilter)) {
                filteredResults.remove(temp);
            }
            if (!positionEmpty && filteredResults.contains(temp) && !temp.getPosition().equals(positionFilter)) {
                filteredResults.remove(temp);
            }
            if (!keywordEmpty && filteredResults.contains(temp) && !temp.getKeywords().contains(keywordFilter)) {
                filteredResults.remove(temp);
            }
            if (!cityEmpty && filteredResults.contains(temp) && !temp.getCity().equals(cityFilter)) {
                filteredResults.remove(temp);
            }
            if (!stateEmpty && filteredResults.contains(temp) && !temp.getState().equals(stateFilter)) {
                filteredResults.remove(temp);
            }
        }
    }

    public void clearFilter(View v) {
        ConstraintSet clear = new ConstraintSet();
        clear.clone(innerLayout);
        clear.connect(R.id.rcycStudents, ConstraintSet.TOP, R.id.btnFilter, ConstraintSet.BOTTOM, 0);
        clear.applyTo(innerLayout);
        btnClearFilter.setVisibility(View.GONE);

        for (int i=0; i<allMatchedOpportunities.size(); i++) {
            if (!filteredResults.contains(allMatchedOpportunities.get(i))) {
                filteredResults.add(allMatchedOpportunities.get(i));
            }
        }

        mAdapter.notifyDataSetChanged();
    }

    public void viewOpportunity(int position) {
        Intent intent = new Intent(this, NonOwnerViewOpportunity.class);
        intent.putExtra("EXTRA_OPPORTUNITY_ID", filteredResults.get(position).getId());
        startActivity(intent);
    }

}
