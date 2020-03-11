package com.example.pursuit;

import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;
import android.os.Bundle;
import android.widget.EditText;

import com.example.pursuit.models.Company;
import com.example.pursuit.models.EmployeeInvite;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class InviteEmployeeActivity extends AppCompatActivity{
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
        String code = RandomKeyGenerator.randomInviteCode(25);
        newInvite = new EmployeeInvite(code, employeeEmail);
        dbRef.child("EmployeeInvites").child(companyName).child(code).setValue(newInvite);

    }
    /* ******END DATABASE****** */

    private void initializeCurrentCompany() {
        Log.d(TAG, "initializing company");
        currentCompany = ((PursuitApplication) this.getApplicationContext()).getCurrentCompany();
    }

    public void generateInvite() {

    }

    public void createEmployeeInvite(View v) {
        employeeEmail = findViewById(R.id.txtEmployeeEmail);
        Log.d(TAG, employeeEmail.getText().toString());

        generateInvite();
    }
}
