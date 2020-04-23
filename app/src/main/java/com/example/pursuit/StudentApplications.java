package com.example.pursuit;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pursuit.adapters.StudentApplicationAdapter;
import com.example.pursuit.models.CompanyOpportunity;
import com.example.pursuit.models.Student;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;

public class StudentApplications extends AppCompatActivity {

    BottomNavigationView bottomNavigation;
    Student currentStudent;
    CompanyOpportunity currentOpportunity;

    private DatabaseReference dbref;

    private ArrayList<CompanyOpportunity> opportunities;
    private RecyclerView viewApplications;
    private StudentApplicationAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    
}
