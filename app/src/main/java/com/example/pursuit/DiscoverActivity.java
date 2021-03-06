package com.example.pursuit;

import android.content.Intent;
import android.os.Bundle;

import com.example.pursuit.models.Company;
import com.example.pursuit.models.Employee;
import com.example.pursuit.models.Student;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.tabs.TabLayout;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.example.pursuit.ui.main.DiscoverPagerAdapter;


public class DiscoverActivity extends AppCompatActivity {
    private final String TAG = "DiscoverActivity";

    private Student currentStudent;
    private Employee currentEmployee;
    private Company currentCompany;
    private String currentRole;

    private Student toggleFollowingStudent;
    private Company toggleFollowingCompany;

    private DatabaseReference dbRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_discover);

        BottomNavigationView bottomNavigation = findViewById(R.id.bottom_navigation);
        bottomNavigation.setOnNavigationItemSelectedListener(navigationItemSelectedListener);

        findAndSetCurrentUser();

        if (currentRole.equals("Company")) {
            Log.d(TAG, "current role is company");
            bottomNavigation.getMenu().removeItem(R.id.navigation_messages);
            bottomNavigation.getMenu().removeItem(R.id.navigation_discover);
        }

        dbRef = FirebaseDatabase.getInstance().getReference();

        TabLayout tabLayout = findViewById(R.id.tabs);
        tabLayout.addTab(tabLayout.newTab().setText("Users"));
        tabLayout.addTab(tabLayout.newTab().setText("Companies"));
        if (currentRole.equals("Student")) {
            tabLayout.addTab(tabLayout.newTab().setText("Opportunities"));
        }
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        String currentUserId;
        if (currentRole.equals("Student")) {
            currentUserId = currentStudent.getId();
        } else {
            currentUserId = currentEmployee.getCompanyID();
        }

        final ViewPager viewPager = findViewById(R.id.view_pager);
        DiscoverPagerAdapter sectionsPagerAdapter = new DiscoverPagerAdapter(this, getSupportFragmentManager(),
                tabLayout.getTabCount(), currentUserId, currentRole);

        viewPager.setAdapter(sectionsPagerAdapter);

        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) { }

            @Override
            public void onTabReselected(TabLayout.Tab tab) { }
        });
    }

    public void toggleFollowCompany(View view) {
        toggleFollowingCompany = (Company) view.getTag();
        checkCompanyFollowStatus();
    }

    private void checkCompanyFollowStatus() {
        Query checkQuery;
        if (currentRole.equals("Student")) {
            checkQuery = dbRef.child("Students").child(currentStudent.getId()).child("Following").child("Companies")
                    .orderByChild("id").equalTo(toggleFollowingCompany.getId());
        } else {
            checkQuery = dbRef.child("Companies").child(currentCompany.getId()).child("Following").child("Companies")
                    .orderByChild("id").equalTo(toggleFollowingCompany.getId());
        }
        checkQuery.addListenerForSingleValueEvent(checkFollowingCompanyListener);
    }

    ValueEventListener checkFollowingCompanyListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            if (dataSnapshot.exists()) {
                // Following > Students > id exists, meaning that the current
                // user already follows this student => unfollow them
                Log.d(TAG, "current user is following this student");
                unfollowCompany();
            } else {
                // Following > Students > doesn't exist => follow them
                Log.d(TAG, "current user is not following this student");
                followCompany();
            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) { }
    };

    private void unfollowCompany() {
        if (currentRole.equals("Student")) {
            DatabaseReference followRef = dbRef.child("Students").child(currentStudent.getId())
                    .child("Following").child("Companies").child(toggleFollowingCompany.getId());

            followRef.removeValue();
        } else {
            DatabaseReference followRef = dbRef.child("Companies").child(currentCompany.getId())
                    .child("Following").child("Companies").child(toggleFollowingCompany.getId());

            followRef.removeValue();
        }
    }

    private void followCompany() {
        if (currentRole.equals("Student")) {
            dbRef.child("Students").child(currentStudent.getId()).child("Following").child("Companies")
                    .child(toggleFollowingCompany.getId()).setValue(toggleFollowingCompany);
        } else {
            dbRef.child("Companies").child(currentEmployee.getCompanyID()).child("Following").child("Companies")
                    .child(toggleFollowingCompany.getId()).setValue(toggleFollowingCompany);
        }
    }


    public void toggleFollowStudent(View view) {
        Log.d(TAG, "toggling follow student");
        toggleFollowingStudent = (Student) view.getTag();
        checkStudentFollowStatus();
    }

    private void checkStudentFollowStatus() {
        Query checkQuery;
        if (currentRole.equals("Student")) {
            checkQuery = dbRef.child("Students").child(currentStudent.getId()).child("Following").child("Students")
                    .orderByChild("id").equalTo(toggleFollowingStudent.getId());
        } else {
            checkQuery = dbRef.child("Companies").child(currentCompany.getId()).child("Following").child("Students")
                    .orderByChild("id").equalTo(toggleFollowingStudent.getId());
        }
        checkQuery.addListenerForSingleValueEvent(checkFollowingStudentListener);
    }

    ValueEventListener checkFollowingStudentListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            if (dataSnapshot.exists()) {
                // Following > Students > id exists, meaning that the current
                // user already follows this student => unfollow them
                Log.d(TAG, "current user is following this student");
                unFollowStudent();
            } else {
                // Following > Students > doesn't exist => follow them
                Log.d(TAG, "current user is not following this student");
                followStudent();
            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) { }
    };

    private void unFollowStudent() {
        if (currentRole.equals("Student")) {
            DatabaseReference followRef = dbRef.child("Students").child(currentStudent.getId())
                    .child("Following").child("Students").child(toggleFollowingStudent.getId());

            followRef.removeValue();
        } else {
            DatabaseReference followRef = dbRef.child("Companies").child(currentCompany.getId())
                    .child("Following").child("Students").child(toggleFollowingStudent.getId());

            followRef.removeValue();
        }
    }

    private void followStudent() {
        if (currentRole.equals("Student")) {
            dbRef.child("Students").child(currentStudent.getId()).child("Following").child("Students")
                    .child(toggleFollowingStudent.getId()).setValue(toggleFollowingStudent);
        } else {
            dbRef.child("Companies").child(currentEmployee.getCompanyID()).child("Following").child("Students")
                    .child(toggleFollowingStudent.getId()).setValue(toggleFollowingStudent);
        }
    }

    private void findAndSetCurrentUser() {
        if (((PursuitApplication) this.getApplication()).getCurrentStudent() != null) {
            currentStudent = ((PursuitApplication) this.getApplication()).getCurrentStudent();
        } else {
            currentEmployee = ((PursuitApplication) this.getApplication()).getCurrentEmployee();
            currentCompany = ((PursuitApplication) this.getApplication()).getCurrentCompany();
        }
        currentRole = ((PursuitApplication) this.getApplication()).getRole();
    }

    BottomNavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    switch (item.getItemId()) {
                        case R.id.navigation_home:
                            Intent home = new Intent(DiscoverActivity.this, LandingActivity.class);
                            startActivity(home);
                            finish();
                            return true;
                        case R.id.navigation_discover:
                            return true;
                        case R.id.navigation_messages:
                            Intent messages = new Intent(DiscoverActivity.this, ConversationsActivity.class);
                            startActivity(messages);
                            finish();
                            return true;
                        case R.id.navigation_profile:
                            if (currentStudent != null) {
                                Intent profile = new Intent(DiscoverActivity.this, StudentProfileActivity.class);
                                startActivity(profile);
                                finish();
                            } else {
                                Intent profile = new Intent(DiscoverActivity.this, CompanyProfileActivity.class);
                                startActivity(profile);
                                finish();
                            }
                            return true;
                    }
                    return false;
                }
            };
}