package com.example.pursuit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.pursuit.models.Company;
import com.example.pursuit.models.CompanyOpportunity;
import com.example.pursuit.models.Employee;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class CreateOpportunity extends AppCompatActivity {

    DatabaseReference dbref;

    View view;
    EditText opportunityPosition;
    EditText opportunityWithWho;
    EditText opportunityDescription;
    BottomNavigationView bottomNavigation;

    CompanyOpportunity newOpportunity;

    Company currentCompany;
    Employee currentEmployee;
    String currentRole;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_opportunity);

        bottomNavigation = findViewById(R.id.bottom_navigation);
        bottomNavigation.setOnNavigationItemSelectedListener(navigationItemSelectedListener);

        dbref = FirebaseDatabase.getInstance().getReference();
        setCurrentUser();

    }

    /* ********DATABASE******** */

    private void writeNewCompanyOpportunity(String id) {
        dbref.child("CompanyOpportunities").child(currentCompany.getId()).child(id).setValue(newOpportunity);
    }

    /* ******END DATABASE****** */

    BottomNavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    switch (item.getItemId()) {
                        case R.id.navigation_home:
                            Intent i0 = new Intent(CreateOpportunity.this, LandingActivity.class);
                            startActivity(i0);
                            finish();
                            return true;
                        case R.id.navigation_messages:
                            Intent i1 = new Intent(CreateOpportunity.this, ConversationsActivity.class);
                            startActivity(i1);
                            finish();
                            return true;
                        case R.id.navigation_profile:
                            Intent i2 = new Intent(CreateOpportunity.this, CompanyProfileActivity.class);
                            startActivity(i2);
                            finish();
                            return true;
                    }
                    return false;
                }
            };

    private void setCurrentUser() {
        currentRole = ((PursuitApplication) this.getApplicationContext()).getRole();
        currentCompany = ((PursuitApplication) this.getApplicationContext()).getCurrentCompany();
        if (currentRole.equals("Employee")) {
            currentEmployee = ((PursuitApplication) this.getApplicationContext()).getCurrentEmployee();
        }
    }

    public String toString(EditText text) {
        return text.getText().toString();
    }

    boolean isEmpty(EditText text) {
        CharSequence str = text.getText().toString();
        return TextUtils.isEmpty(str);
    }

    boolean checkForEmpties() {
        if (isEmpty(opportunityPosition) || isEmpty(opportunityDescription)) {
            Toast.makeText(view.getContext(), "Position And Description Are Required", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    public void addToDB() {
        String id = RandomKeyGenerator.randomAlphaNumeric(16);
        newOpportunity = new CompanyOpportunity(id, toString(opportunityPosition), toString(opportunityWithWho), toString(opportunityDescription), 0);
        if (currentRole.equals("Company") || (currentRole.equals("Employee") && currentEmployee.admin == 1)) {
            newOpportunity.setApproved(1);
        }

        writeNewCompanyOpportunity(id);

        Intent intent = new Intent(this, CompanyProfileActivity.class);
        startActivity(intent);
    }

    public void addOpportunity(View v) {
        view = v;
        opportunityPosition = findViewById(R.id.txtPosition);
        opportunityWithWho = findViewById(R.id.txtWith);
        opportunityDescription = findViewById(R.id.txtDescription);

        if (checkForEmpties()) {
            addToDB();
        }
    }
}
