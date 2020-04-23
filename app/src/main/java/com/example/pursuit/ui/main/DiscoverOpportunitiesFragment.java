package com.example.pursuit.ui.main;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.pursuit.R;
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

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DiscoverOpportunitiesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DiscoverOpportunitiesFragment extends Fragment {
    private static final String TAG = "DiscoverOpportunity";

    private View fragmentView;

    private Student currentStudent;

    private TextView noInterests;
    private RecyclerView opportunitiesRecycler;
    private Button btnFilter;
    private Button btnClearFilter;
    private ConstraintLayout innerLayout;

    private ArrayList<String> interests;
    private String currentInterest;
    private ArrayList<String> opportunityIds;
    private String currentOpportunityID;

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

    private String companyFilter;
    private String positionFilter;
    private String keywordFilter;
    private String cityFilter;
    private String stateFilter;

    private int interestsParser;
    private int getOpportunitiesParser;

    private Dialog filterDialog;

    private DatabaseReference dbRef;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "currentUserId";

    // TODO: Rename and change types of parameters
    private String currentUserId;

    public DiscoverOpportunitiesFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
//     * @param param1 Parameter 1.
//     * @param param2 Parameter 2.
     * @return A new instance of fragment DiscoverOpportunitiesFragment.
     */
    public static DiscoverOpportunitiesFragment newInstance(String currentUserId) {
        DiscoverOpportunitiesFragment fragment = new DiscoverOpportunitiesFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, currentUserId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            currentUserId = getArguments().getString(ARG_PARAM1);
        }
        dbRef = FirebaseDatabase.getInstance().getReference();
//        getCurrentStudent(currentUserId);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_discover_opportunities, container, false);
        fragmentView = view;

        fragmentView = view;

        noInterests = view.findViewById(R.id.txtNoInterests);
        opportunitiesRecycler = view.findViewById(R.id.rcycOpportunities);
        btnFilter = view.findViewById(R.id.btnFilter);

        btnFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filter(v);
            }
        });

        btnClearFilter = view.findViewById(R.id.btnClearFilter);
        btnClearFilter.setVisibility(View.GONE);
        btnClearFilter.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                clearFilter(v);
            }
        });

        innerLayout = view.findViewById(R.id.innerLayout);

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

        filterDialog = new Dialog(getContext());

        getCurrentStudent(currentUserId);

        return view;
    }

//    @SuppressLint("SetTextI18n")
//    @Override
//    public void onViewCreated(View view, Bundle savedInstanceState) {
//        fragmentView = view;
//
//        noInterests = view.findViewById(R.id.txtNoInterests);
//        opportunitiesRecycler = view.findViewById(R.id.rcycOpportunities);
//        btnFilter = view.findViewById(R.id.btnFilter);
//
//        btnClearFilter = view.findViewById(R.id.btnClearFilter);
//        btnClearFilter.setVisibility(View.GONE);
//        innerLayout = view.findViewById(R.id.innerLayout);
//
//        allMatchedOpportunities = new ArrayList<>();
//        filteredResults = new ArrayList<>();
//        companies = new ArrayList<>();
//        companies.add("");
//        positions = new ArrayList<>();
//        positions.add("");
//        keywords = new ArrayList<>();
//        keywords.add("");
//        cities = new ArrayList<>();
//        cities.add("");
//        states = new ArrayList<>();
//        states.add("");
//
//        filterDialog = new Dialog(getContext());
//
//        getCurrentStudent(currentUserId);
//    }

    private void getCurrentStudent(String currentUserId) {
        Log.d(TAG, "Getting current student");
        Query currentStudentQuery = dbRef.child("Students").orderByChild("id").equalTo(currentUserId);
        currentStudentQuery.addListenerForSingleValueEvent(currentStudentListener);
    }

    private ValueEventListener currentStudentListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            if (dataSnapshot.exists()) {
                currentStudent = dataSnapshot.child(currentUserId).getValue(Student.class);
                if (currentStudent!=null) {
                    Log.d(TAG, "CurrentStudent is not null");
                    Log.d("CURRENT_ID", currentStudent.getId());
                }
                postCurrentStudentListener();
            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) { }
    };

    private void postCurrentStudentListener() {
        Log.d(TAG, "going to get interests");
        interests = currentStudent.getInterestKeywords();
        Log.d(TAG, "Got interests");
        Log.d("INTEREST_SIZE", Integer.toString(interests.size()));
        if (interests == null) {
            Log.d(TAG, "postListener interests is null");
            opportunitiesRecycler.setVisibility(View.GONE);
            btnFilter.setVisibility(View.GONE);
            noInterests.setText("No available opportunities match your interests");
        } else {
            opportunityIds = new ArrayList<>();
            interestsParser = 0;
            cycleInterests();
        }
    }

    @SuppressLint("SetTextI18n")
    private void cycleInterests() {
        Log.d(TAG, "cycling through interests");
        if (interestsParser < interests.size()) {
            currentInterest = interests.get(interestsParser);
            Query keywordQuery = dbRef.child("Keywords").orderByChild("text").equalTo(currentInterest);
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

    private void getOpportunities() {
        if (getOpportunitiesParser < opportunityIds.size()) {
            currentOpportunityID = opportunityIds.get(getOpportunitiesParser);
            Query opportunityQuery = dbRef.child("CompanyOpportunities").orderByKey().equalTo(currentOpportunityID);
            opportunityQuery.addListenerForSingleValueEvent(opportunityListener);
        } else {
            buildRecyclerView();
        }
    }

    private void buildRecyclerView() {
        viewMatchedEmployees = fragmentView.findViewById(R.id.rcycOpportunities);
        viewMatchedEmployees.setHasFixedSize(false);
        mLayoutManager = new LinearLayoutManager(getContext());
        filteredResults.addAll(allMatchedOpportunities);
        mAdapter = new StudentOpportunityAdapter(filteredResults);

        viewMatchedEmployees.setLayoutManager(mLayoutManager);
        viewMatchedEmployees.setAdapter(mAdapter);

        mAdapter.setStudentOpportunityOnItemClickListener(new StudentOpportunityAdapter.StudentOpportunityOnItemClickListener() {
            @Override
            public void onCardClick(int position) {

            }
        });
    }

    /* ********DATABASE******** */

    private ValueEventListener keywordListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            if (dataSnapshot.exists()) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Keyword keyword = snapshot.getValue(Keyword.class);
                    assert keyword != null;
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

    private ValueEventListener opportunityListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            if (dataSnapshot.exists()) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    CompanyOpportunity opportunity = snapshot.getValue(CompanyOpportunity.class);
                    assert opportunity != null;
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
        ArrayAdapter<String> companyNameAdapter = new ArrayAdapter<>(
                getContext(),
                android.R.layout.simple_spinner_item,
                companies);
        companyNameAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        companyName.setAdapter(companyNameAdapter);

        position = filterDialog.findViewById(R.id.spnPosition);
        ArrayAdapter<String> positionAdapter = new ArrayAdapter<>(
                getContext(),
                android.R.layout.simple_spinner_item,
                positions);
        positionAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        position.setAdapter(positionAdapter);

        keyword = filterDialog.findViewById(R.id.spnKeyword);
        ArrayAdapter<String> keywordAdapter = new ArrayAdapter<>(
                getContext(),
                android.R.layout.simple_spinner_item,
                keywords);
        keywordAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        keyword.setAdapter(keywordAdapter);

        city = filterDialog.findViewById(R.id.spnCity);
        ArrayAdapter<String> cityAdapter = new ArrayAdapter<>(
                getContext(),
                android.R.layout.simple_spinner_item,
                cities);
        cityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        city.setAdapter(cityAdapter);

        state = filterDialog.findViewById(R.id.spnState);
        ArrayAdapter<String> stateAdapter = new ArrayAdapter<>(
                getContext(),
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
                    clearFilter.connect(R.id.rcycOpportunities, ConstraintSet.TOP, R.id.btnClearFilter, ConstraintSet.BOTTOM, 0);
                    clearFilter.applyTo(innerLayout);
                }

                mAdapter.notifyDataSetChanged();
                filterDialog.dismiss();
            }
        });

        filterDialog.show();
    }

    private boolean allFiltersEmpty() {
        return isEmpty(companyFilter) && isEmpty(positionFilter)
                && isEmpty(keywordFilter) && isEmpty(cityFilter)
                && isEmpty(stateFilter);
    }

    private boolean isEmpty(String str) {
        return str.equals("");
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
        clear.connect(R.id.rcycOpportunities, ConstraintSet.TOP, R.id.btnFilter, ConstraintSet.BOTTOM, 0);
        clear.applyTo(innerLayout);
        btnClearFilter.setVisibility(View.GONE);

        for (int i=0; i<allMatchedOpportunities.size(); i++) {
            if (!filteredResults.contains(allMatchedOpportunities.get(i))) {
                filteredResults.add(allMatchedOpportunities.get(i));
            }
        }

        mAdapter.notifyDataSetChanged();
    }


}
