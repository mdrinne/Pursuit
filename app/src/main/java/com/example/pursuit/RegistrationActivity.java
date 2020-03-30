package com.example.pursuit;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pursuit.models.Student;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.Query;

public class RegistrationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
    }

    public void continueToRegistration(View view) {
        final RadioGroup radGroup = (RadioGroup) findViewById(R.id.studentOrCompany);
        if (radGroup.getCheckedRadioButtonId() == -1) {
            Toast.makeText(this, "Please make a selection.", Toast.LENGTH_SHORT).show();
        } else {
            int selectedID = radGroup.getCheckedRadioButtonId();
            if (selectedID == findViewById(R.id.radioBtnStudent).getId()) {
                Intent intent = new Intent(this, StudentRegistration.class);
                startActivity(intent);
            } else if (selectedID == findViewById(R.id.radioBtnEmployee).getId()){
                Intent intent = new Intent(this, EmployeeRegistration1.class);
                startActivity(intent);
            } else {
                Intent intent = new Intent(this, CompanyRegistration.class);
                startActivity(intent);
            }

        }
    }

    // public void getSelectedRadioButton(View view) {
    // final RadioGroup radGroup = (RadioGroup) findViewById(R.id.studentOrCompany);
    // int radioID = radGroup.getCheckedRadioButtonId();
    // RadioButton singleButton = (RadioButton) findViewById(radioID);

    // }
}
