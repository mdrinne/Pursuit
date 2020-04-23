package com.example.pursuit.ui.main;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.example.pursuit.R;
import com.example.pursuit.adapters.CompaniesListAdapter;
import com.example.pursuit.models.Company;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DiscoverCompaniesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DiscoverCompaniesFragment extends Fragment {
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "currentUserId";
    private static final String ARG_PARAM2 = "currentUserRole";

    private String currentUserId;
    private String currentUserRole;
    private ArrayList<Company> companiesList;

    private DatabaseReference dbRef;

    private View fragmentView;

    public DiscoverCompaniesFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param currentUserId Parameter 1.
     * @param currentUserRole Parameter 2.
     * @return A new instance of fragment DiscoverCompaniesFragment.
     */
    static DiscoverCompaniesFragment newInstance(String currentUserId, String currentUserRole) {
        DiscoverCompaniesFragment fragment = new DiscoverCompaniesFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, currentUserId);
        args.putString(ARG_PARAM2, currentUserRole);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            currentUserId = getArguments().getString(ARG_PARAM1);
            currentUserRole = getArguments().getString(ARG_PARAM2);
        }
        dbRef = FirebaseDatabase.getInstance().getReference();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        fragmentView =  inflater.inflate(R.layout.fragment_discover_companies, container, false);

        getCompanies();

        return fragmentView;
    }

    private void getCompanies() {
        Query companiesQuery = dbRef.child("Companies").orderByChild("id");
        companiesQuery.addValueEventListener(companiesListener);
    }

    private ValueEventListener companiesListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            companiesList = new ArrayList<>();

            if (dataSnapshot.exists()) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Company company = snapshot.getValue(Company.class);

                    if (company != null && !company.getId().equals(currentUserId)) {
                        companiesList.add(company);
                    }
                }
            }

            postCompaniesListener();
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) { }
    };

    private void postCompaniesListener() {
        RecyclerView usersRecycler = fragmentView.findViewById(R.id.companies_recycler);
        final CompaniesListAdapter companiesListAdapter = new CompaniesListAdapter(getContext(), companiesList, currentUserId, currentUserRole);
        usersRecycler.setAdapter(companiesListAdapter);
        usersRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        EditText usersFilter = fragmentView.findViewById(R.id.companies_filter);
        usersFilter.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }

            @Override
            public void afterTextChanged(Editable s) {
                companiesListAdapter.getFilter().filter(s);
            }
        });
    }
}
