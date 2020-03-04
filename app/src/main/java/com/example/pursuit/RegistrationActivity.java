package com.example.pursuit;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class RegistrationActivity extends AppCompatActivity {

    private static final String TAG = "RegistrationActivity";

    private RegistrationPageAdapter registrationPageAdapter;

    public static final String UI_TAB_STUDENTREGISTRATION = "STUDENTREGISTRATION";
    public static final String UI_TAB_COMPANYREGISTRATION = "COMPANYREGISTRATION";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
    }

    private void  setupViewPager(ViewPager viewPager) {
        RegistrationPageAdapter adapter = new RegistrationPageAdapter(getSupportFragmentManager());
        adapter.addFragment(new StudentRegistration(), UI_TAB_STUDENTREGISTRATION);

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
