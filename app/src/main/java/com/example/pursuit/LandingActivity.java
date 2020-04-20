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

//    Button aboutPursuitBtn;
//    Button viewCompaniesBtn;
    TextView currentUserNameText;
    BottomNavigationView bottomNavigation;

    private Student currentStudent;
    private Employee currentEmployee;
    private Company currentCompany;
    private String currentRole;

    private ArrayList<Share> shareList;

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

        getShares();

        String currentUserNameString;
        if (currentRole.equals("Student")) {
            currentUserNameString = currentStudent.getUsername();
        } else {
            currentUserNameString = currentEmployee.getUsername();
        }

        currentUserNameText = findViewById(R.id.currentUserName);
        currentUserNameText.setText("Welcome, " + currentUserNameString);

//        aboutPursuitBtn = findViewById(R.id.aboutPursuitBtn);
//        viewCompaniesBtn = findViewById(R.id.viewCompaniesBtn);


//        aboutPursuitBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent i = new Intent(LandingActivity.this, aboutPursuitActivity.class);
//                startActivity(i);
//            }
//        });
//
//        viewCompaniesBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent i = new Intent(LandingActivity.this, viewCompaniesActivity.class);
//                startActivity(i);
//            }
//        });

    }

    public void newShare(View v) {
        Intent newShareActivity;
        newShareActivity = new Intent(LandingActivity.this, NewShareActivity.class);
        this.startActivity(newShareActivity);
    }

    ValueEventListener sharesListener = new ValueEventListener() {
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

            postSharesListener();
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    };

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void postSharesListener() {
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

    private void getShares() {
        Query sharesQuery;
        if (currentStudent != null) {
            sharesQuery = dbRef.child("Students").child(currentStudent.getId()).child("Shares").orderByChild("id");
        } else {
            sharesQuery = dbRef.child("Employees").child(currentEmployee.getId()).child("Shares").orderByChild("id");
        }

        sharesQuery.addValueEventListener(sharesListener);
    }

    BottomNavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener =
      new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
            case R.id.navigation_home:
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
        } else if (((PursuitApplication) this.getApplication()).getCurrentEmployee() != null) {
            currentEmployee = ((PursuitApplication) this.getApplication()).getCurrentEmployee();
        } else {
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