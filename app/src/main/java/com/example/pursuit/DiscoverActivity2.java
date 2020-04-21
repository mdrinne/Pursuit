package com.example.pursuit;

import android.content.Intent;
import android.os.Bundle;

import com.example.pursuit.models.Company;
import com.example.pursuit.models.Employee;
import com.example.pursuit.models.Student;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.pursuit.ui.main.SectionsPagerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class DiscoverActivity2 extends AppCompatActivity {
    private final String TAG = "DiscoverActivity2";

    private Student currentStudent;
    private Employee currentEmployee;
    private Company currentCompany;
    private String currentRole;

    private DatabaseReference dbRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_discover2);

        BottomNavigationView bottomNavigation = findViewById(R.id.bottom_navigation);
        bottomNavigation.setOnNavigationItemSelectedListener(navigationItemSelectedListener);

        findAndSetCurrentUser();
        dbRef = FirebaseDatabase.getInstance().getReference();

        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
        ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);
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
                            Intent home = new Intent(DiscoverActivity2.this, LandingActivity.class);
                            startActivity(home);
                            finish();
                            return true;
                        case R.id.navigation_discover:
                            return true;
                        case R.id.navigation_messages:
                            Intent messages = new Intent(DiscoverActivity2.this, ConversationsActivity.class);
                            startActivity(messages);
                            finish();
                            return true;
                        case R.id.navigation_profile:
                            if (currentStudent != null) {
                                Intent profile = new Intent(DiscoverActivity2.this, StudentProfileActivity.class);
                                startActivity(profile);
                                finish();
                            } else {
                                Intent profile = new Intent(DiscoverActivity2.this, CompanyProfileActivity.class);
                                startActivity(profile);
                                finish();
                            }
                            return true;
                    }
                    return false;
                }
            };
}