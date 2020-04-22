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

    private ArrayList<Share> shareList;
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
        dbRef = FirebaseDatabase.getInstance().getReference();

        getMyShares();

        String currentUserNameString;
        if (currentRole.equals("Student")) {
            currentUserNameString = currentStudent.getUsername();
        } else {
            currentUserNameString = currentEmployee.getUsername();
        }

        currentUserNameText = findViewById(R.id.currentUserName);
        currentUserNameText.setText("Welcome, " + currentUserNameString);

        ImageButton newShare = findViewById(R.id.new_share);
        if (currentRole.equals("Employee") && currentEmployee.getAdmin() == 0) {
            newShare.setVisibility(View.GONE);
        }
    }

    public void newShare(View v) {
        Intent newShareActivity;
        newShareActivity = new Intent(LandingActivity.this, NewShareActivity.class);
        this.startActivity(newShareActivity);
    }

    ValueEventListener mySharesListener = new ValueEventListener() {
        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            shareList = new ArrayList<>();

            if (dataSnapshot.exists()) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Share share = snapshot.getValue(Share.class);

                    if (share != null) {
                        shareList.add(0, share);
                    }
                }
            }

            postMySharesListener();
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) { }
    };

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void postMySharesListener() {
//        shareList.sort(new Comparator<Share>() {
//            @RequiresApi(api = Build.VERSION_CODES.O)
//            @Override
//            public int compare(Share o1, Share o2) {
//                return ZonedDateTime.parse(o1.getCreatedAt()).compareTo(ZonedDateTime.parse(o2.getCreatedAt()));
//            }
//        });
//
//        RecyclerView sharesRecycler = findViewById(R.id.shares_recycler);
//        ShareListAdapter shareListAdapter = new ShareListAdapter(this, shareList);
//        sharesRecycler.setAdapter(shareListAdapter);
//        sharesRecycler.setLayoutManager(new LinearLayoutManager(this));

        getFollowingStudentShares();

    }

    private void getMyShares() {
        Query sharesQuery;
        if (currentStudent != null) {
            sharesQuery = dbRef.child("Students").child(currentStudent.getId()).child("Shares").orderByChild("id");
        } else {
            sharesQuery = dbRef.child("Companies").child(currentCompany.getId()).child("Shares").orderByChild("id");
        }

        sharesQuery.addValueEventListener(mySharesListener);
    }

    private void getFollowingStudentShares() {
        Query studentSharesQuery;
        if (currentRole.equals("Student")) {
            studentSharesQuery = dbRef.child("Students").child(currentStudent.getId()).child("Following").child("Students").orderByChild("id");
        } else {
            studentSharesQuery = dbRef.child("Companies").child(currentCompany.getId()).child("Following").child("Students").orderByChild("id");

        }
        studentSharesQuery.addValueEventListener(studentSharesListener);
    }

    ValueEventListener studentSharesListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            if (dataSnapshot.exists()) {
                for (DataSnapshot snapshot : dataSnapshot.child("Shares").getChildren()) {
                    Log.d(TAG, "snapshot exists for company shares");
                    Share share = snapshot.getValue(Share.class);

                    if (share != null) {
                        shareList.add(0, share);
                    }
                }
            }

            getFollowingCompanyShares();
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) { }
    };

    private void getFollowingCompanyShares() {
        Query companySharesQuery;
        if (currentRole.equals("Student")) {
            companySharesQuery = dbRef.child("Students").child(currentStudent.getId()).child("Following").child("Companies").orderByChild("id");
        } else {
            companySharesQuery = dbRef.child("Companies").child(currentCompany.getId()).child("Following").child("Companies").orderByChild("id");
        }
        companySharesQuery.addValueEventListener(companySharesListener);
    }

    ValueEventListener companySharesListener = new ValueEventListener() {
        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            if (dataSnapshot.exists()) {
                for (DataSnapshot snapshot : dataSnapshot.child("Shares").getChildren()) {
                    Log.d(TAG, "snapshot exists for company shares");
//                    Company company = snapshot.getValue(Company.class);
                    Share share = snapshot.getValue(Share.class);

                    if (share != null) {
                        shareList.add(0, share);
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
                return ZonedDateTime.parse(o1.getCreatedAt()).compareTo(ZonedDateTime.parse(o2.getCreatedAt()));
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