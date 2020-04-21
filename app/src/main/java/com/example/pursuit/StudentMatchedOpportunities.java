package com.example.pursuit;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pursuit.adapters.StudentOpportunityAdapter;
import com.example.pursuit.models.CompanyOpportunity;
import com.example.pursuit.models.Student;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.ArrayList;

public class StudentMatchedOpportunities extends AppCompatActivity {

    Student currentStudent;

    private DatabaseReference dbref;

    TextView noInterests;
    RecyclerView opportunitiesRecycler;

    private ArrayList<CompanyOpportunity> allMatchedOpportunities;
    private RecyclerView viewMatchedEmployees;
    private StudentOpportunityAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_matched_opportunities);

        currentStudent = ((PursuitApplication) this.getApplication()).getCurrentStudent();

        noInterests = findViewById(R.id.txtNoInterests);
        opportunitiesRecycler = findViewById(R.id.rcycOpportunities);

        if (currentStudent.getInterestKeywords() == null) {
            opportunitiesRecycler.setVisibility(View.GONE);
            noInterests.setText("No available opportunities match your interests");
        } else {
            dbref = FirebaseDatabase.getInstance().getReference();
        }
    }

}
