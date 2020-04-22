package com.example.pursuit;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pursuit.adapters.StudentOpportunityAdapter;
import com.example.pursuit.models.Company;
import com.example.pursuit.models.CompanyOpportunity;
import com.example.pursuit.models.Student;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageException;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class StudentViewCompany extends AppCompatActivity {
    TextView companyName, companyField, companyDescription;

    Company viewCompany;
    Student currentStudent;
    BottomNavigationView bottomNavigation;
    ImageView companyProfilePic;
    Uri filePath;

    private DatabaseReference dbref;

//    FirebaseStorage storage;
    StorageReference sref;

    private final int PICK_IMAGE_REQUEST = 22;

    int hasPicture;

    private ArrayList<CompanyOpportunity> opportunities;
    private RecyclerView viewOpportunities;
    private StudentOpportunityAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private ArrayList<CompanyOpportunity> filteredOpportunities;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_non_owner_view_company);
        bottomNavigation = findViewById(R.id.bottom_navigation);
        bottomNavigation.setOnNavigationItemSelectedListener(navigationItemSelectedListener);

        currentStudent = ((PursuitApplication) this.getApplicationContext()).getCurrentStudent();

        dbref = FirebaseDatabase.getInstance().getReference();
        sref = FirebaseStorage.getInstance().getReference();

        Query companyQuery = dbref.child("Companies").orderByKey().equalTo(getIntent().getStringExtra("EXTRA_COMPANY_ID"));
        companyQuery.addListenerForSingleValueEvent(companyListener);
    }

    private void continueOnCreate() {
        populateTextFields();

        companyProfilePic = findViewById(R.id.imgCompanyProfilePic);
        loadCompanyProfilePicture();

        opportunities = viewCompany.getOpportunities();
        buildRecyclerView();
    }

    private void populateTextFields() {
        companyName = findViewById(R.id.txtCompanyName);
        companyName.setText(viewCompany.getName());

        companyField = findViewById(R.id.txtCompanyField);
        companyField.setText(viewCompany.getField());

        companyDescription = findViewById(R.id.txtCompanyDescription);
        companyDescription.setText(viewCompany.getDescription());
    }

    private void loadCompanyProfilePicture() {
        Query companyHasProfilePicQuery = dbref.child("ProfilePicture").orderByChild(viewCompany.getId()).equalTo(1);
        companyHasProfilePicQuery.addListenerForSingleValueEvent(companyHasProfilePicListener);
    }

    private void setProfilePicture() {
        if (hasPicture == 1) {
            downloadCompanyProfilePic();
        } else {
            companyProfilePic.setImageDrawable(getResources().getDrawable(R.drawable.ic_add_a_photo));
        }
    }

    private void buildRecyclerView() {
        viewOpportunities = findViewById(R.id.rcycOpportunities);
        viewOpportunities.setHasFixedSize(false);
        filteredOpportunities = new ArrayList<>();
        if (opportunities != null) {
            for (int i=0; i<opportunities.size(); i++) {
                if (opportunities.get(i).getApproved() == 1) {
                    filteredOpportunities.add(opportunities.get(i));
                }
            }
        }

        mLayoutManager = new LinearLayoutManager(this);
        mAdapter = new StudentOpportunityAdapter(filteredOpportunities);

        viewOpportunities.setLayoutManager(mLayoutManager);
        viewOpportunities.setAdapter(mAdapter);

        mAdapter.setStudentOpportunityOnItemClickListener(new StudentOpportunityAdapter.StudentOpportunityOnItemClickListener() {
            @Override
            public void onCardClick(int position) {
                viewOpportunity(position);
            }
        });

    }

    /* ********DATABASE******** */

    ValueEventListener companyListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            if (dataSnapshot.exists()) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    viewCompany = snapshot.getValue(Company.class);
                }
            }
            continueOnCreate();
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    };

    ValueEventListener companyHasProfilePicListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            if (dataSnapshot.exists()) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    hasPicture = 1;
                }
            }
            setProfilePicture();
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    };

    private void downloadCompanyProfilePic() {
        StorageReference profilePic = sref.child("images").child("CompanyProfilePictures").child(viewCompany.getId());
        profilePic.getDownloadUrl()
                .addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Picasso.get().load(uri.toString()).into(companyProfilePic);

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        int errorCode = ((StorageException) e).getErrorCode();
                        if (errorCode == StorageException.ERROR_OBJECT_NOT_FOUND) {
                        }
                    }
                });
    }

    /* ******END DATABASE****** */

    private void viewOpportunity(int position) {
        Intent intent = new Intent(this, StudentViewOpportunity.class);
        intent.putExtra("EXTRA_OPPORTUNITY_ID", filteredOpportunities.get(position).getId());
        startActivity(intent);
    }

    BottomNavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    switch (item.getItemId()) {
                        case R.id.navigation_home:
                            Intent i0 = new Intent(StudentViewCompany.this, LandingActivity.class);
                            startActivity(i0);
                            finish();
                            return true;
                        case R.id.navigation_messages:
                            Intent i1 = new Intent(StudentViewCompany.this, ConversationsActivity.class);
                            startActivity(i1);
                            finish();
                            return true;
                        case R.id.navigation_profile:
                            Intent i2 = new Intent(StudentViewCompany.this, StudentProfileActivity.class);
                            startActivity(i2);
                            finish();
                            return true;
                    }
                    return false;
                }
            };
}
