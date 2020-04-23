package com.example.pursuit;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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
    TextView opportunityDescription, opportunityRequirements, txtKeywords, txtRequirements;
    EditText editPosition, editWith, editCity, editState, editDescription, editRequirements;
    Button approveBtn, editProfileBtn, confirmChangesBtn, cancelBtn;
    ImageView imgLocation;

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

    ArrayList<String> currentOpportunityKeywords;

    private ArrayList<String> keywordsArrayList;
    private int keywordParser;
    private String currentKeyword;

    Dialog addKeywordsDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_opportunity);
        bottomNavigation = findViewById(R.id.bottom_navigation);
        bottomNavigation.setOnNavigationItemSelectedListener(navigationItemSelectedListener);

        txtKeywords = findViewById(R.id.txtKeywords);
        txtRequirements = findViewById(R.id.txtRequirements);
        imgLocation = findViewById(R.id.imgLocationIcon);

        editPosition = findViewById(R.id.editPosition);
        editPosition.setVisibility(View.GONE);

        editWith = findViewById(R.id.editWith);
        editWith.setVisibility(View.GONE);

        editCity = findViewById(R.id.editCity);
        editCity.setVisibility(View.GONE);

        editState = findViewById(R.id.editState);
        editState.setVisibility(View.GONE);

        editDescription = findViewById(R.id.editDescription);
        editDescription.setVisibility(View.GONE);

        editRequirements = findViewById(R.id.editOpportunityRequirements);
        editRequirements.setVisibility(View.GONE);

        editProfileBtn = findViewById(R.id.btnEditOpportunity);

        confirmChangesBtn = findViewById(R.id.btnConfirm);
        confirmChangesBtn.setVisibility(View.GONE);

        cancelBtn = findViewById(R.id.btnCancel);
        cancelBtn.setVisibility(View.GONE);

        setCurrentUser();
        addKeywordsDialog = new Dialog(this);

        if (currentRole.equals("Employee") && currentEmployee.getAdmin() == 0) editProfileBtn.setVisibility(View.GONE);

        dbref = FirebaseDatabase.getInstance().getReference();

        Query getOpportunityQuery = dbref.child("CompanyOpportunities").orderByKey().equalTo(getIntent().getStringExtra("EXTRA_OPPORTUNITY_ID"));
        getOpportunityQuery.addListenerForSingleValueEvent(getOpportunityListener);

    }

    private void continueOnCreate() {

        approveBtn = findViewById(R.id.btnApprove);
        if (currentOpportunity.getApproved() == 1) {
            approveBtn.setVisibility(View.GONE);
        } else {
            Button btnPotentialCandidate = findViewById(R.id.btnPotentialCandidates);
            btnPotentialCandidate.setVisibility(View.GONE);
        }


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

    public void approveOpportunity(View v) {
        currentOpportunity.setApproved(1);
        dbref.child("CompanyOpportunities").child(currentCompany.getId()).child(currentOpportunity.getId()).child("approved").setValue(1);
        approveBtn.setVisibility(View.GONE);
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

    ValueEventListener keywordListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            Keyword keyword = null;
            if (dataSnapshot.exists()) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    keyword = snapshot.getValue(Keyword.class);
                }
            }
            if (keyword == null) {
                writeNewKeyword();
            } else {
                updateKeyword(keyword);
            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    };

    private void updateKeyword(Keyword keyword) {
        ArrayList<String> temp = keyword.getOpportunities();
        if (temp == null) {
            temp = new ArrayList<>();
        }
        temp.add(currentOpportunity.getId());
        dbref.child("Keywords").child(keyword.getId()).child("opportunities").setValue(temp);
        currentOpportunityKeywords.add(keyword.getText());
        keywordParser++;
        addKeywordsToDB();
    }

    private void writeNewKeyword() {
        ArrayList<String> opportunities = new ArrayList<>();
        opportunities.add(currentOpportunity.getId());
        String keywordID = RandomKeyGenerator.randomAlphaNumeric(16);
        Keyword keyword = new Keyword(keywordID, keywordsArrayList.get(keywordParser), opportunities, null);
        dbref.child("Keywords").child(keywordID).setValue(keyword);
        currentOpportunityKeywords.add(keywordsArrayList.get(keywordParser));
        keywordParser++;
        addKeywordsToDB();
    }

    /* ******END DATABASE****** */

    private void buildRecyclerView() {
        if (keywords == null) {
            keywords = new ArrayList<>();
        }
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
        final EditText txtAddKeywords;
        Button cancel, confirm;

        addKeywordsDialog.setContentView(R.layout.add_keywords_pop_up);

        cancel = addKeywordsDialog.findViewById(R.id.btnCancel);
        confirm = addKeywordsDialog.findViewById(R.id.btnConfirm);
        txtAddKeywords = addKeywordsDialog.findViewById(R.id.txtAddInterests);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addKeywordsDialog.dismiss();
            }
        });

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                currentOpportunityKeywords = currentOpportunity.getKeywords();
                String[] keywordsArray = txtAddKeywords.getText().toString().split(",");
                keywordsArrayList = new ArrayList<>();
                for (int i=0; i<keywordsArray.length; i++) {
                    String word = keywordsArray[i].trim().toLowerCase();
                    if (!keywordsArrayList.contains(word) && !currentOpportunity.getKeywords().contains(word)) {
                        keywordsArrayList.add(word);
                    }
                }

                keywordParser = 0;
                addKeywordsToDB();
                addKeywordsDialog.dismiss();
            }
        });

        addKeywordsDialog.show();
    }

    private void addKeywordsToDB() {
        if (keywordParser < keywordsArrayList.size()) {
            currentKeyword = keywordsArrayList.get(keywordParser);
            Query keywordQuery = dbref.child("Keywords").orderByChild("text").equalTo(currentKeyword);
            keywordQuery.addListenerForSingleValueEvent(keywordListener);
        } else {
            dbref.child("CompanyOpportunities").child(currentCompany.getId()).child(currentOpportunity.getId()).child("keywords").setValue(currentOpportunityKeywords);
            mAdapter.notifyDataSetChanged();
            return;
        }
    }

    public void potentialCandidates(View v) {
        Intent intent = new Intent(this, OpportunityMatchedStudents.class);
        intent.putExtra("EXTRA_OPPORTUNITY_ID", currentOpportunity.getId());
        startActivity(intent);
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
                            Intent i1 = new Intent(ViewOpportunity.this, ConversationsActivity.class);
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

    public void editOpportunity(View view) {
        editPosition.setVisibility(View.VISIBLE);
        editWith.setVisibility(View.VISIBLE);
        editCity.setVisibility(View.VISIBLE);
        editState.setVisibility(View.VISIBLE);
        editDescription.setVisibility(View.VISIBLE);
        editRequirements.setVisibility(View.VISIBLE);
        confirmChangesBtn.setVisibility(View.VISIBLE);
        cancelBtn.setVisibility(View.VISIBLE);

        //Rewrite constraints
        ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) txtKeywords.getLayoutParams();
        params.topToBottom = cancelBtn.getId();
        txtKeywords.setLayoutParams(params);

        params = (ConstraintLayout.LayoutParams) txtRequirements.getLayoutParams();
        params.topToBottom = editDescription.getId();
        txtRequirements.setLayoutParams(params);

        params = (ConstraintLayout.LayoutParams) imgLocation.getLayoutParams();
        params.topToBottom = editPosition.getId();
        imgLocation.setLayoutParams(params);

        editProfileBtn.setVisibility(View.GONE);
        opportunityRequirements.setVisibility(View.GONE);
        opportunityPosition.setVisibility(View.GONE);
        opportunityState.setVisibility(View.GONE);
        opportunityDescription.setVisibility(View.GONE);
        opportunityWith.setVisibility(View.GONE);
        opportunityCity.setVisibility(View.GONE);

        editPosition.setText(currentOpportunity.getPosition());
        editWith.setText(currentOpportunity.getWithWho());
        editCity.setText(currentOpportunity.getCity());
        editState.setText(currentOpportunity.getState());
        editDescription.setText(currentOpportunity.getDescription());
        editRequirements.setText(currentOpportunity.getRequirements());
    }

    public void submitChanges(View view) {
        //Update currentOpportunity fields
        currentOpportunity.setPosition(editPosition.getText().toString());
        currentOpportunity.setWithWho(editWith.getText().toString());
        currentOpportunity.setCity(editCity.getText().toString());
        currentOpportunity.setState(editState.getText().toString());
        currentOpportunity.setDescription(editDescription.getText().toString());
        currentOpportunity.setRequirements(editRequirements.getText().toString());

        //Submit changes to firebase
        dbref.child("CompanyOpportunities").child(currentOpportunity.getId()).setValue(currentOpportunity);

        exitOpportunityEditor(view);
    }

    public void exitOpportunityEditor(View view) {
        opportunityCity.setVisibility(View.VISIBLE);
        opportunityWith.setVisibility(View.VISIBLE);
        opportunityDescription.setVisibility(View.VISIBLE);
        opportunityState.setVisibility(View.VISIBLE);
        opportunityPosition.setVisibility(View.VISIBLE);
        opportunityRequirements.setVisibility(View.VISIBLE);
        editProfileBtn.setVisibility(View.VISIBLE);

        //Rewrite constraints
        ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) txtKeywords.getLayoutParams();
        params.topToBottom = editProfileBtn.getId();
        txtKeywords.setLayoutParams(params);

        params = (ConstraintLayout.LayoutParams) txtRequirements.getLayoutParams();
        params.topToBottom = opportunityDescription.getId();
        txtRequirements.setLayoutParams(params);

        params = (ConstraintLayout.LayoutParams) imgLocation.getLayoutParams();
        params.topToBottom = opportunityPosition.getId();
        imgLocation.setLayoutParams(params);

        cancelBtn.setVisibility(View.GONE);
        confirmChangesBtn.setVisibility(View.GONE);
        editRequirements.setVisibility(View.GONE);
        editState.setVisibility(View.GONE);
        editDescription.setVisibility(View.GONE);
        editWith.setVisibility(View.GONE);
        editCity.setVisibility(View.GONE);
        editPosition.setVisibility(View.GONE);

        //Update TextViews
        //currentOpportunity = ((PursuitApplication) this.getApplication()).getCurrentOpportunity();
        opportunityDescription.setText(currentOpportunity.getDescription());
        opportunityRequirements.setText(currentOpportunity.getRequirements());
        opportunityWith.setText(currentOpportunity.getWithWho());
        opportunityState.setText(currentOpportunity.getState());
        opportunityCity.setText(currentOpportunity.getCity());
        opportunityPosition.setText(currentOpportunity.getPosition());
    }
}
