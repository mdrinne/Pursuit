package com.example.pursuit;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pursuit.adapters.StudentAdapter;
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

public class OpportunityMatchedStudents extends AppCompatActivity {

    private DatabaseReference dbref;

    private CompanyOpportunity currentOpportunity;

    private TextView noStudents;
    private Button btnClearFilter, btnFilter;
    private ConstraintLayout innerLayout;

    private ArrayList<String> keywords;
    private String currentKeyword;
    private ArrayList<String> studentIDs;
    private String currentStudentID;

    private ArrayList<Student> allMatchedStudents;
    private ArrayList<Student> filterResults;
    private RecyclerView viewMatchedStudents;
    private StudentAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private ArrayList<String> universitiesSpinner;
    private ArrayList<String> majorsSpinner;
    private ArrayList<String> keywordsSpinner;
    private ArrayList<String> minorsSpinner;

    private String universityFilter;
    private String majorFilter;
    private String minorFilter;
    private String gpaFilter;
    private String keywordFilter;

    private int keywordParser;
    private int getStudentsParser;

    private Dialog filterDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_opportunity_matched_students);

        dbref = FirebaseDatabase.getInstance().getReference();

        Query opportunityQuery = dbref.child("CompanyOpportunities").orderByKey().equalTo(getIntent().getStringExtra("EXTRA_OPPORTUNITY_ID"));
        opportunityQuery.addListenerForSingleValueEvent(opportunityListener);
    }

    private void continueOnCreate() {
        keywords = currentOpportunity.getKeywords();

        noStudents = findViewById(R.id.txtNoStudents);
        viewMatchedStudents = findViewById(R.id.rcycStudents);
        btnClearFilter = findViewById(R.id.btnClearFilter);
        btnFilter = findViewById(R.id.btnFilter);
        innerLayout = findViewById(R.id.innerLayout);

        btnClearFilter.setVisibility(View.GONE);

        allMatchedStudents = new ArrayList<>();
        filterResults = new ArrayList<>();
        universitiesSpinner = new ArrayList<>();
        universitiesSpinner.add("");
        majorsSpinner = new ArrayList<>();
        majorsSpinner.add("");
        minorsSpinner = new ArrayList<>();
        minorsSpinner.add("");
        keywordsSpinner = new ArrayList<>();
        keywordsSpinner.add("");

        filterDialog = new Dialog(this);

        if (keywords == null) {
            viewMatchedStudents.setVisibility(View.GONE);
            btnFilter.setVisibility(View.GONE);
            noStudents.setText("No students match the keywords for this opportunity");
        } else {
            studentIDs = new ArrayList<>();
            keywordParser = 0;
            cycleKeywords();
        }
    }

    private void cycleKeywords() {
        if (keywordParser < keywords.size()) {
            currentKeyword = keywords.get(keywordParser);
            Query keywordQuery = dbref.child("Keywords").orderByChild("text").equalTo(currentKeyword);
            keywordQuery.addListenerForSingleValueEvent(keywordListener);
        } else {
            if (studentIDs.size() == 0) {
                viewMatchedStudents.setVisibility(View.GONE);
                btnFilter.setVisibility(View.GONE);
                noStudents.setText("No students match the keywords for this opportunity");
            } else {
                getStudentsParser = 0;
                getStudents();
            }
        }
    }

    private void getStudents() {
        if (getStudentsParser < studentIDs.size()) {
            currentStudentID = studentIDs.get(getStudentsParser);
            Query studentQuery = dbref.child("Students").orderByKey().equalTo(currentStudentID);
            studentQuery.addListenerForSingleValueEvent(studentListener);
        } else {
            buildRecyclerView();
        }
    }

    private void buildRecyclerView() {
        viewMatchedStudents.setHasFixedSize(false);
        mLayoutManager = new LinearLayoutManager(this);
        for (int i=0; i<allMatchedStudents.size(); i++) {
            filterResults.add(allMatchedStudents.get(i));
        }
        mAdapter = new StudentAdapter(filterResults);

        viewMatchedStudents.setLayoutManager(mLayoutManager);
        viewMatchedStudents.setAdapter(mAdapter);

        mAdapter.setStudentOnItemClickListener(new StudentAdapter.StudentOnItemClickListener() {
            @Override
            public void onCardClick(int position) {
                viewStudent(position);
            }
        });
    }

    /* ********DATABASE******** */

    ValueEventListener opportunityListener = new ValueEventListener() {
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

    ValueEventListener keywordListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            if (dataSnapshot.exists()) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Keyword keyword = snapshot.getValue(Keyword.class);
                    keywordsSpinner.add(keyword.getText());
                    ArrayList<String> keywordStudents = keyword.getStudents();
                    if (keywordStudents == null) {
                        keywordStudents = new ArrayList<>();
                    }
                    for (int i=0; i<keywordStudents.size(); i++) {
                        if (!studentIDs.contains(keywordStudents.get(i))) {
                            studentIDs.add(keywordStudents.get(i));
                        }
                    }
                }
            }
            keywordParser++;
            cycleKeywords();
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    };

    ValueEventListener studentListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            if (dataSnapshot.exists()) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Student student = snapshot.getValue(Student.class);
                    allMatchedStudents.add(student);
                    if (!universitiesSpinner.contains(student.getUniversity())) {
                        universitiesSpinner.add(student.getUniversity());
                    }
                    if (!majorsSpinner.contains(student.getMajor())){
                        majorsSpinner.add(student.getMajor());
                    }
                    if (!minorsSpinner.contains(student.getMinor())) {
                        minorsSpinner.add(student.getMinor());
                    }
                }
            }
            getStudentsParser++;
            getStudents();
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    };

    /* ******END DATABASE****** */

    public void filter(View v) {
        final Spinner university, major, minor, keyword;
        final EditText gpa;
        Button confirm, cancel;

        filterDialog.setContentView(R.layout.opportunity_student_filter_pop_up);
        cancel = filterDialog.findViewById(R.id.btnCancel);
        confirm = filterDialog.findViewById(R.id.btnConfirm);

        university = filterDialog.findViewById(R.id.spnUniversity);
        ArrayAdapter<String> universityAdapter = new ArrayAdapter<String>(
                this,
                android.R.layout.simple_spinner_item,
                universitiesSpinner);
        universityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        university.setAdapter(universityAdapter);

        major = filterDialog.findViewById(R.id.spnMajor);
        ArrayAdapter<String> majorAdapter = new ArrayAdapter<String>(
                this,
                android.R.layout.simple_spinner_item,
                majorsSpinner);
        majorAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        major.setAdapter(majorAdapter);

        minor = filterDialog.findViewById(R.id.spnMinor);
        ArrayAdapter<String> minorAdapter = new ArrayAdapter<String>(
                this,
                android.R.layout.simple_spinner_item,
                minorsSpinner);
        minorAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        minor.setAdapter(minorAdapter);

        keyword = filterDialog.findViewById(R.id.spnKeyword);
        ArrayAdapter<String> keywordAdapter = new ArrayAdapter<String>(
                this,
                android.R.layout.simple_spinner_item,
                keywordsSpinner);
        keywordAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        keyword.setAdapter(keywordAdapter);

        gpa = filterDialog.findViewById(R.id.txtGPA);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filterDialog.dismiss();
            }
        });

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                universityFilter = university.getSelectedItem().toString();
                majorFilter = major.getSelectedItem().toString();
                minorFilter = minor.getSelectedItem().toString();
                keywordFilter = keyword.getSelectedItem().toString();
                gpaFilter = gpa.getText().toString();

                for (int i=0; i<allMatchedStudents.size(); i++) {
                    if (!filterResults.contains(allMatchedStudents.get(i))) {
                        filterResults.add(allMatchedStudents.get(i));
                    }
                }
                if (!allFiltersEmpty()) {
                    filterStudents();
                    btnClearFilter.setVisibility(View.VISIBLE);
                    ConstraintSet clearFilter = new ConstraintSet();
                    clearFilter.clone(innerLayout);
                    clearFilter.connect(R.id.btnClearFilter, ConstraintSet.TOP, R.id.btnFilter, ConstraintSet.BOTTOM, 0);
                    clearFilter.connect(R.id.rcycStudents, ConstraintSet.TOP, R.id.btnClearFilter, ConstraintSet.BOTTOM, 0);
                    clearFilter.applyTo(innerLayout);
                } else {
                    ConstraintSet clear = new ConstraintSet();
                    clear.clone(innerLayout);
                    clear.connect(R.id.rcycStudents, ConstraintSet.TOP, R.id.btnFilter, ConstraintSet.BOTTOM, 0);
                    clear.applyTo(innerLayout);
                    btnClearFilter.setVisibility(View.GONE);

                    for (int i=0; i<allMatchedStudents.size(); i++) {
                        if (!filterResults.contains(allMatchedStudents.get(i))) {
                            filterResults.add(allMatchedStudents.get(i));
                        }
                    }

                    mAdapter.notifyDataSetChanged();
                }

                mAdapter.notifyDataSetChanged();
                filterDialog.dismiss();
            }
        });

        filterDialog.show();
    }

    private boolean allFiltersEmpty() {
        if (isEmpty(universityFilter) && isEmpty(majorFilter) && isEmpty(minorFilter) && isEmpty(keywordFilter) && isEmpty(gpaFilter)) {
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

    private void filterStudents() {
        for (int i=0; i<allMatchedStudents.size(); i++) {
            Student temp = allMatchedStudents.get(i);
            if (!isEmpty(universityFilter) && !temp.getUniversity().equals(universityFilter)) {
                filterResults.remove(temp);
            } else if (!isEmpty(majorFilter) && !temp.getMajor().equals(majorFilter)) {
                filterResults.remove(temp);
            } else if (!isEmpty(minorFilter) && !temp.getMinor().equals(minorFilter)) {
                filterResults.remove(temp);
            } else if (!isEmpty(keywordFilter) && !temp.getInterestKeywords().contains(keywordFilter)) {
                filterResults.remove(temp);
            } else if (!isEmpty(gpaFilter) && (Float.parseFloat(gpaFilter) > Float.parseFloat(temp.getGpa()))) {
                filterResults.remove(temp);
            }
        }
    }

    public void clearFilter(View v) {
        ConstraintSet clear = new ConstraintSet();
        clear.clone(innerLayout);
        clear.connect(R.id.rcycStudents, ConstraintSet.TOP, R.id.btnFilter, ConstraintSet.BOTTOM, 0);
        clear.applyTo(innerLayout);
        btnClearFilter.setVisibility(View.GONE);

        for (int i=0; i<allMatchedStudents.size(); i++) {
            if (!filterResults.contains(allMatchedStudents.get(i))) {
                filterResults.add(allMatchedStudents.get(i));
            }
        }

        mAdapter.notifyDataSetChanged();
    }

    private void viewStudent(int position) {

    }

}
