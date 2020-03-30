package com.example.pursuit;

import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;
import android.os.Bundle;
import android.widget.EditText;
import android.content.Intent;

import com.example.pursuit.models.Company;
import com.example.pursuit.models.EmployeeInvite;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class InviteEmployeeActivity extends AppCompatActivity {
    private static final String TAG = "InviteEmployeeActivity";

    private DatabaseReference dbRef;

    Company currentCompany;
    EditText employeeEmail;

    EmployeeInvite newInvite;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invite_employee);

        initializeCurrentCompany();

        dbRef = FirebaseDatabase.getInstance().getReference();
    }

    /* ********DATABASE******** */

    private void writeNewEmployeeInvite(String companyName, String employeeEmail) {
        Log.d(TAG, "in writeNewEmployeeInvite, company: " + companyName + "; email: " + employeeEmail);
        String code = RandomKeyGenerator.randomInviteCode(25);
        Log.d(TAG, "code: " + code);
        newInvite = new EmployeeInvite(code, employeeEmail);
        Log.d(TAG, "new invite created");
        dbRef.child("EmployeeInvites").child(companyName).child(code).setValue(newInvite);

        Intent intent = new Intent(this, viewCompanyEmployeeInvites.class);
        startActivity(intent);
    }

    /* ******END DATABASE****** */

    private void initializeCurrentCompany() {
        Log.d(TAG, "initializing company");
        currentCompany = ((PursuitApplication) this.getApplicationContext()).getCurrentCompany();
    }

    public void createEmployeeInvite(View v) {
        employeeEmail = findViewById(R.id.txtEmployeeEmail);
        Log.d(TAG, employeeEmail.getText().toString());

        writeNewEmployeeInvite(currentCompany.getName(), employeeEmail.getText().toString());
    }
}