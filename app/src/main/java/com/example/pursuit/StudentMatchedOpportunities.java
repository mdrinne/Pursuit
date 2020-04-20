package com.example.pursuit;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pursuit.adapters.StudentOpportunityAdapter;
import com.example.pursuit.models.CompanyOpportunity;
import com.example.pursuit.models.Student;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;

public class StudentMatchedOpportunities extends AppCompatActivity {

    Student currentStudent;

    private DatabaseReference dbref;

    private ArrayList<CompanyOpportunity> allMatchedOpportunities;
    private RecyclerView viewMatchedEmployees;
    private StudentOpportunityAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

//    @Override void onCreate

}
