package com.example.pursuit;

import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pursuit.models.Company;
import com.example.pursuit.models.Employee;
import com.google.android.material.bottomnavigation.BottomNavigationItemView;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;

public class ViewOpportunity extends AppCompatActivity {

    TextView opportunityPosition, opportunityWith, opportunityCity, opportunityState;
    TextView opportunityDescription, opportunityRequirements;

    BottomNavigationItemView bottomNavigation;

    Company currentCompany;
    Employee currentEmployee;
    String currentRole;

    private DatabaseReference dbref;

    private ArrayList<String> keywords;
    private RecyclerView opportunityKeywords;

}
