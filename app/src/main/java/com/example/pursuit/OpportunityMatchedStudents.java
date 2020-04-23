package com.example.pursuit;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;

public class OpportunityMatchedStudents extends AppCompatActivity {

    private DatabaseReference dbref;

    TextView noStudents;
    //btnClearFilter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_opportunity_matched_students);
    }
}
