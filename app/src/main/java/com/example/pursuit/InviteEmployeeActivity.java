package com.example.pursuit;

import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;
import android.os.Bundle;
import android.widget.EditText;

import com.example.pursuit.models.Company;

public class InviteEmployeeActivity extends AppCompatActivity{
    private static final String TAG = "InviteEmployeeActivity";

    Company currentCompany;
    EditText employeeEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invite_employee);

        initializeCurrentCompany();
    }

    private void initializeCurrentCompany() {
        Log.d(TAG, "initializing company");
        currentCompany = ((PursuitApplication) this.getApplicationContext()).getCurrentCompany();
    }

    public void createEmployeeInvite(View v) {
        employeeEmail = findViewById(R.id.txtEmployeeEmail);
        Log.d(TAG, employeeEmail.getText().toString());



    }
}
