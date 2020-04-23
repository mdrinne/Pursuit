package com.example.pursuit;

import com.example.pursuit.adapters.ShareListAdapter;
import com.example.pursuit.models.Employee;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.MenuItem;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pursuit.models.Company;
import com.example.pursuit.models.Student;
import com.example.pursuit.models.Share;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Comparator;

public class LandingActivity extends AppCompatActivity {
    private final String TAG = "LandingActivity";

    TextView currentUserNameText;
    BottomNavigationView bottomNavigation;

    private Student currentStudent;
    private Employee currentEmployee;
    private Company currentCompany;
    private String currentRole;

    private ArrayList<Share> shareList = new ArrayList<>();
    private ArrayList<String> followingIds = new ArrayList<>();

    private DatabaseReference dbRef;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing);
        bottomNavigation = findViewById(R.id.bottom_navigation);
        bottomNavigation.setOnNavigationItemSelectedListener(navigationItemSelectedListener);

        findAndSetCurrentUser();

        if (currentRole.equals("Company")) {
            Log.d(TAG, "current role is company");
            bottomNavigation.getMenu().removeItem(R.id.navigation_messages);
            bottomNavigation.getMenu().removeItem(R.id.navigation_discover);
        }

        dbRef = FirebaseDatabase.getInstance().getReference();

        getShares();

        String currentUserNameString;
        if (currentRole.equals("Student")) {
            currentUserNameString = currentStudent.getUsername();
        } else if (currentRole.equals("Employee")) {
            currentUserNameString = currentEmployee.getUsername();
        }
        else {
            currentUserNameString = currentCompany.getName();
        }

        currentUserNameText = findViewById(R.id.currentUserName);
        currentUserNameText.setText("Welcome, " + currentUserNameString);

        ImageButton newShare = findViewById(R.id.new_share);
        if (currentRole.equals("Employee") && currentEmployee.getAdmin() == 0) {
            newShare.setVisibility(View.GONE);
        }
        String newTitle = "Pursuit (" + currentRole + ")";
        setTitle(newTitle);
    }

    public void newShare(View v) {
        Intent newShareActivity;
        newShareActivity = new Intent(LandingActivity.this, NewShareActivity.class);
        this.startActivity(newShareActivity);
    }

    private void getShares() {
        getFollowingStudentIds();
    }

    private void getFollowingStudentIds() {
        Query studentIdsQuery;
        if (currentRole.equals("Student")) {
            studentIdsQuery = dbRef.child("Students").child(currentStudent.getId()).child("Following").child("Students").orderByChild("id");
        } else {
            studentIdsQuery = dbRef.child("Companies").child(currentCompany.getId()).child("Following").child("Students").orderByChild("id");

        }
        studentIdsQuery.addValueEventListener(studentIdsListener);
    }

    ValueEventListener studentIdsListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            if (dataSnapshot.exists()) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Log.d(TAG, "snapshot exists for company shares");
                    Student student = snapshot.getValue(Student.class);

                    if (student != null) {
                        followingIds.add(student.getId());
                    }
                }
            }

            getFollowingCompanyIds();
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) { }
    };

    private void getFollowingCompanyIds() {
        Query companyIdsQuery;
        if (currentRole.equals("Student")) {
            companyIdsQuery = dbRef.child("Students").child(currentStudent.getId()).child("Following").child("Companies").orderByChild("id");
        } else {
            companyIdsQuery = dbRef.child("Companies").child(currentCompany.getId()).child("Following").child("Companies").orderByChild("id");
        }
        companyIdsQuery.addValueEventListener(companyIdsListener);
    }

    ValueEventListener companyIdsListener = new ValueEventListener() {
        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            if (dataSnapshot.exists()) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Log.d(TAG, "snapshot exists for company shares");
                    Company company = snapshot.getValue(Company.class);
                    if (company != null) {
                        followingIds.add(company.getId());
                    }
                }
            }

            getAllShares();
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) { }
    };

    private void getAllShares() {
        Query followingSharesQuery = dbRef.child("Shares").orderByChild("id");
        followingSharesQuery.addValueEventListener(followingSharesListener);
    }

    ValueEventListener followingSharesListener = new ValueEventListener() {
        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            if (dataSnapshot.exists()) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Share share = snapshot.getValue(Share.class);

                    if (share != null) {
                        if (currentRole.equals("Student")) {
                            if (followingIds.contains(share.getUserId()) || share.getUserId().equals(currentStudent.getId())) {
                                shareList.add(share);
                            }
                        } else {
                            if (followingIds.contains(share.getUserId()) || share.getUserId().equals(currentCompany.getId())) {
                                shareList.add(share);
                            }
                        }
                    }
                }
            }

            setRecyclerAdapter();
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) { }
    };

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void setRecyclerAdapter() {
        shareList.sort(new Comparator<Share>() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public int compare(Share o1, Share o2) {
                return ZonedDateTime.parse(o2.getCreatedAt()).compareTo(ZonedDateTime.parse(o1.getCreatedAt()));
            }
        });

        RecyclerView sharesRecycler = findViewById(R.id.shares_recycler);
        ShareListAdapter shareListAdapter = new ShareListAdapter(this, shareList);
        sharesRecycler.setAdapter(shareListAdapter);
        sharesRecycler.setLayoutManager(new LinearLayoutManager(this));
    }

    BottomNavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener =
      new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
            case R.id.navigation_home:
                return true;
            case R.id.navigation_discover:
                    Intent discover = new Intent(LandingActivity.this, DiscoverActivity.class);
                    startActivity(discover);
                    finish();
                    return true;
            case R.id.navigation_messages:
                Intent messages = new Intent(LandingActivity.this, ConversationsActivity.class);
                startActivity(messages);
                finish();
                return true;
            case R.id.navigation_profile:
                if (currentStudent != null) {
                    Intent profile = new Intent(LandingActivity.this, StudentProfileActivity.class);
                    startActivity(profile);
                    finish();
                } else {
                    Intent profile = new Intent(LandingActivity.this, CompanyProfileActivity.class);
                    startActivity(profile);
                    finish();
                }
                return true;
            }
            return false;
        }
      };

    private void findAndSetCurrentUser() {
        if (((PursuitApplication) this.getApplication()).getCurrentStudent() != null) {
            currentStudent = ((PursuitApplication) this.getApplication()).getCurrentStudent();
        } else {
            currentEmployee = ((PursuitApplication) this.getApplication()).getCurrentEmployee();
            currentCompany = ((PursuitApplication) this.getApplication()).getCurrentCompany();
        }
        currentRole = ((PursuitApplication) this.getApplication()).getRole();
    }

    private void removeCurrentUser() {
        ((PursuitApplication) this.getApplication()).removeCurrentUser();
    }

    public void logoutCurrentUser(View v) {
        removeCurrentUser();

        Intent i = new Intent(LandingActivity.this, MainActivity.class);
        startActivity(i);
        finish();
    }

}