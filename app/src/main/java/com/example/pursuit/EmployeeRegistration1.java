package com.example.pursuit;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.pursuit.models.Company;
import com.example.pursuit.models.Employee;
import com.example.pursuit.models.EmployeeInvite;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class EmployeeRegistration1 extends AppCompatActivity {

    private static final String TAG = "EmployeeRegistration1";

    private DatabaseReference dbref;

    EditText companyCode;
    EditText invitationCode;
    View view;

    Employee newEmployee;
    Company matchedCompany;

    EmployeeInvite matchedInvite;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_registration1);

        dbref = FirebaseDatabase.getInstance().getReference();
    }

    /* ********DATABASE******** */

    ValueEventListener companyCodeListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            matchedCompany = null;
            if(dataSnapshot.exists()) {
                Log.d(TAG, "snapshot exists");
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Log.d(TAG, "looping snapshot");
                    Company company = snapshot.getValue(Company.class);
                    if (company == null) {
                        Log.d(TAG, "company is null");
                    } else {
                        Log.d(TAG, "company is not null");
                    }
                    matchedCompany = company;
                }
            }
            processCompanyCode();
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    };

    ValueEventListener inviteCodeListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            matchedInvite = null;
            if(dataSnapshot.exists()) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    EmployeeInvite invite = snapshot.getValue(EmployeeInvite.class);
                    if (invite == null) {
                        Log.d(TAG, "invite is null");
                    } else {
                        Log.d(TAG, "invite is not null");
                    }
                    matchedInvite = invite;
                }
            }
            processInviteCode();
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    };

    /* ******END DATABASE****** */

    public String toString(EditText text) {
        return text.getText().toString();
    }

    boolean isEmpty(EditText text) {
        return TextUtils.isEmpty(toString(text));
    }

    public void checkInvite(View v) {
        view = v;
        companyCode = findViewById(R.id.txtCompanyCode);
        invitationCode = findViewById(R.id.txtInviteCode);

        if (isEmpty(companyCode) || isEmpty(invitationCode)) {
            Toast.makeText(view.getContext(), "All Fields are Required", Toast.LENGTH_SHORT).show();
        } else {

            Query companyCodeQuery = dbref.child("Companies").orderByChild("id").equalTo(toString(companyCode));

            if (companyCodeQuery == null) {
                Log.d(TAG, "company query is null");
            } else {
                Log.d(TAG, "company query is not null");
            }

            companyCodeQuery.addListenerForSingleValueEvent(companyCodeListener);
        }

    }

    private void  processCompanyCode() {
        if (matchedCompany == null) {
            Toast.makeText(view.getContext(), "Incorrect Company Code", Toast.LENGTH_SHORT).show();
        } else {
            Log.d(TAG, "Company exists");

            Query inviteCodeQuery = dbref.child("EmployeeInvites").child(toString(companyCode)).orderByChild("code").equalTo(toString(invitationCode));

            inviteCodeQuery.addListenerForSingleValueEvent(inviteCodeListener);
        }
    }

    private void processInviteCode() {
        if (matchedInvite == null) {
            Toast.makeText(view.getContext(), "Incorrect Invitation Code", Toast.LENGTH_SHORT).show();;
        } else {
            Log.d(TAG, "Invite exists");
            String newId = RandomKeyGenerator.randomAlphaNumeric(16);

            newEmployee = new Employee(newId,null,null,matchedInvite.getEmployeeEmail(),
                    null,null,null,matchedCompany.getName(),
                    toString(invitationCode));

            ((PursuitApplication) this.getApplication()).setCurrentCompany(matchedCompany);
            ((PursuitApplication) this.getApplication()).setCurrentEmployee(newEmployee);

            Intent intent = new Intent(this, EmployeeRegistration2.class);
            startActivity(intent);
        }
    }

}
