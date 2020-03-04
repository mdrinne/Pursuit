package com.example.pursuit;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.android.material.tabs.TabLayout;

public class RegistrationActivity extends AppCompatActivity {

    private static final String TAG = "RegistrationActivity";

    private RegistrationPageAdapter registrationPageAdapter;
    private ViewPager registrationViewPager;

    public static final String UI_TAB_STUDENTREGISTRATION = "STUDENTREGISTRATION";
    public static final String UI_TAB_COMPANYREGISTRATION = "COMPANYREGISTRATION";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "RegistrationActivity onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        registrationPageAdapter = new RegistrationPageAdapter(getSupportFragmentManager());
        Log.d(TAG, "registrationPageAdapter created");

        // Set Up The ViewPager With The Sections Adapter
        registrationViewPager = findViewById(R.id.viewPagerRegistration);
        setupViewPager(registrationViewPager);
        Log.d(TAG, "registrationViewPager created");

        TabLayout registrationTabLayout = findViewById(R.id.tabsRegistration);
        registrationTabLayout.setupWithViewPager(registrationViewPager);
    }

    private void  setupViewPager(ViewPager viewPager) {
        Log.d(TAG, "start setupViewPager");
        RegistrationPageAdapter adapter = new RegistrationPageAdapter(getSupportFragmentManager());
        adapter.addFragment(new StudentRegistration(), UI_TAB_STUDENTREGISTRATION);
        adapter.addFragment(new CompanyRegistration(), UI_TAB_COMPANYREGISTRATION);
        viewPager.setAdapter(adapter);
        Log.d(TAG, "end setupViewPager");
    }

//    public void continueToRegistration(View view) {
//        final RadioGroup radGroup = (RadioGroup) findViewById(R.id.studentOrCompany);
//        if (radGroup.getCheckedRadioButtonId() == -1) {
//            Toast.makeText(this, "Please make a selection.", Toast.LENGTH_SHORT).show();
//        } else {
//            int selectedID = radGroup.getCheckedRadioButtonId();
//            if (selectedID == findViewById(R.id.radioBtnStudent).getId()) {
//                Intent intent = new Intent(this, StudentRegistration.class);
//                startActivity(intent);
//            } else {
//                Intent intent = new Intent(this, CompanyRegistration.class);
//                startActivity(intent);
//            }
//
//        }
//    }

    // public void getSelectedRadioButton(View view) {
    // final RadioGroup radGroup = (RadioGroup) findViewById(R.id.studentOrCompany);
    // int radioID = radGroup.getCheckedRadioButtonId();
    // RadioButton singleButton = (RadioButton) findViewById(radioID);

    // }
}
