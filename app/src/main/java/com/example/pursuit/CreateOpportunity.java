package com.example.pursuit;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.pursuit.models.Company;
import com.example.pursuit.models.CompanyOpportunity;
import com.example.pursuit.models.Employee;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.ArrayList;

public class CreateOpportunity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private final String TAG = "CreateOpportunity";

    DatabaseReference dbref;

    View view;
    EditText opportunityPosition;
    EditText opportunityWithWho;
    EditText opportunityDescription;
    EditText opportunityCity;
    Spinner opportunityState;
    EditText opportunityRequirements;
    EditText opportunityKeywords;

    String[] keywordArray;
    ArrayList<String> keywordArrayList;
    String selectedState;

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

        opportunityState = findViewById(R.id.spinnerSate);
        ArrayAdapter<CharSequence> stateAdapter = ArrayAdapter.createFromResource(this, R.array.states, android.R.layout.simple_spinner_item);
        stateAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        opportunityState.setAdapter(stateAdapter);
        opportunityState.setOnItemSelectedListener(this);

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
                            Intent i1 = new Intent(CreateOpportunity.this, MessagesActivity.class);
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
        if (isEmpty(opportunityPosition) || isEmpty(opportunityDescription) || isEmpty(opportunityCity) || selectedState.isEmpty()) {
            Toast.makeText(view.getContext(), "Position, Description, City and State Are Required", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void addToDB() {
        String id = RandomKeyGenerator.randomAlphaNumeric(16);
        newOpportunity = new CompanyOpportunity(id, toString(opportunityPosition), toString(opportunityWithWho),
                toString(opportunityDescription), toString(opportunityCity), selectedState,
                toString(opportunityRequirements), keywordArrayList, 0, "");
        if (currentRole.equals("Company") || (currentRole.equals("Employee") && currentEmployee.admin == 1)) {
            newOpportunity.setApproved(1);
                ZonedDateTime now = ZonedDateTime.now(ZoneOffset.UTC);
                Log.d(TAG, now.toString());
                newOpportunity.setTimeStamp(now.toString());
        } else {
            newOpportunity.setTimeStamp("");
        }

        writeNewCompanyOpportunity(id); 

        Intent intent = new Intent(this, CompanyProfileActivity.class);
        startActivity(intent);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void addOpportunity(View v) {
        view = v;
        opportunityPosition = findViewById(R.id.txtPosition);
        opportunityWithWho = findViewById(R.id.txtWith);
        opportunityDescription = findViewById(R.id.txtDescription);
        opportunityCity = findViewById(R.id.txtCity);
        selectedState = opportunityState.getSelectedItem().toString();
        opportunityRequirements = findViewById(R.id.txtRequirements);
        opportunityKeywords = findViewById(R.id.txtKeywords);

        keywordArray = toString(opportunityKeywords).split(",");
        keywordArrayList = new ArrayList<>();
        for (int i=0; i<keywordArray.length; i++) {
            keywordArrayList.add(keywordArray[i].trim());
        }

        if (checkForEmpties()) {
            addToDB();
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//        String text = parent.getItemAtPosition(position).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
