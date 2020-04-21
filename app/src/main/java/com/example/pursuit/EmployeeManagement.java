package com.example.pursuit;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pursuit.adapters.ManageEmployeeAdapter;
import com.example.pursuit.models.Company;
import com.example.pursuit.models.Employee;
//import com.example.pursuit.popUpWindows.DeleteEmployeePopUp;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class EmployeeManagement extends AppCompatActivity {

    BottomNavigationView bottomNavigation;
    Company currentCompany;
    Employee currentEmployee;
    String currentRole;

    private DatabaseReference dbref;

    private ArrayList<Employee> companyEmployees;
    private RecyclerView viewEmployees;
    private ManageEmployeeAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    Dialog deleteDialog;
    View v;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emploee_management);
        bottomNavigation = findViewById(R.id.bottom_navigation);
        bottomNavigation.setOnNavigationItemSelectedListener(navigationItemSelectedListener);

        setCurrentUser();
        deleteDialog = new Dialog(this);

        dbref = FirebaseDatabase.getInstance().getReference();

        Query employeeQuery = dbref.child("Employees").orderByChild("companyID").equalTo(currentCompany.getId());
        employeeQuery.addListenerForSingleValueEvent(employeeListener);
    }

    /* ********DATABASE******** */

    ValueEventListener employeeListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            companyEmployees = new ArrayList<>();
            companyEmployees.clear();

            if (dataSnapshot.exists()) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Employee employee = snapshot.getValue(Employee.class);
                    companyEmployees.add(employee);
                }
            }

            buildRecyclerView();
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    };

    private void makeEmployeeAdmin(int position) {
        Employee employee = companyEmployees.get(position);
        employee.setAdmin(1);
        companyEmployees.set(position, employee);
        dbref.child("Employees").child(employee.getId()).child("admin").setValue(1);
        mAdapter.notifyItemChanged(position);
    }

    private void revokeEmployeeAdmin(int position) {
        Employee employee = companyEmployees.get(position);
        employee.setAdmin(0);
        companyEmployees.set(position, employee);
        dbref.child("Employees").child(employee.getId()).child("admin").setValue(0);
        mAdapter.notifyItemChanged(position);
    }

    /* ******END DATABASE****** */

    private void buildRecyclerView() {
        viewEmployees = findViewById(R.id.rcycCompanyEmployees);
        viewEmployees.setHasFixedSize(false);
        mLayoutManager = new LinearLayoutManager(this);
        if (currentRole.equals("Company")) {
            mAdapter = new ManageEmployeeAdapter(companyEmployees, null);
        } else {
            mAdapter = new ManageEmployeeAdapter(companyEmployees, currentEmployee);
        }

        viewEmployees.setLayoutManager(mLayoutManager);
        viewEmployees.setAdapter(mAdapter);

        mAdapter.setManageEmployeeOnItemClickListener(new ManageEmployeeAdapter.ManageEmployeeOnItemClickListener() {
            @Override
            public void onDeleteClick(int position) {
                deleteEmployeePopUp(position);
            }

            @Override
            public void makeAdmin(int position) {
                makeEmployeeAdmin(position);
            }

            @Override
            public void revokeAdmin(int position) {
                revokeEmployeeAdmin(position);
            }
        });
    }

    private void deleteEmployeePopUp(final int position) {
        final TextView deleteMessage;
        Button cancel, confirm;
        deleteDialog.setContentView(R.layout.delete_employee_pop_up);

        cancel = deleteDialog.findViewById(R.id.btnCancel);
        confirm = deleteDialog.findViewById(R.id.btnConfirm);
        deleteMessage = deleteDialog.findViewById(R.id.txtDeleteMessage);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteDialog.dismiss();
            }
        });

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Employee employee = companyEmployees.get(position);
                dbref.child("Employees").child(employee.getId()).removeValue();
                companyEmployees.remove(employee);
                mAdapter.notifyItemRemoved(position);
                sendEmail(employee.getEmail());
                deleteDialog.dismiss();
            }
        });

        String fullName = companyEmployees.get(position).getFirstName() + " "
                            + companyEmployees.get(position).getLastName();
        String message = "Are you sure you want to delete " + fullName + "'s ("
                            + companyEmployees.get(position).getEmail()
                            + ") account?";
        deleteMessage.setText(message);
        deleteDialog.show();

    }

    private void sendEmail(final String recievingEmail) {
        final String appEmail = "pursuitappdev@gmail.com";
        final String appPassword = "Cs495spring2020";
        final String body = "Your Pursuit account has been deleted.";
        Thread emailThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    GMailSender sender = new GMailSender(appEmail, appPassword);
                    sender.sendMail("Pursuit Invite <DO NOT REPLY>", body, "<DO_NOT_REPLY>@pursuit.com", recievingEmail);
                } catch (Exception e) {
                    Log.e("SendMail", e.getMessage(), e);
                }
            }
        });

        emailThread.start();
    }

    private void setCurrentUser() {
        currentCompany = ((PursuitApplication) this.getApplicationContext()).getCurrentCompany();
        currentRole = ((PursuitApplication) this.getApplicationContext()).getRole();
        if (currentRole.equals("Employee")) {
            currentEmployee = ((PursuitApplication) this.getApplicationContext()).getCurrentEmployee();
        }
    }

    BottomNavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    switch (item.getItemId()) {
                        case R.id.navigation_home:
                            Intent i0 = new Intent(EmployeeManagement.this, LandingActivity.class);
                            startActivity(i0);
                            finish();
                            return true;
                        case R.id.navigation_messages:
                            Intent i1 = new Intent(EmployeeManagement.this, MessagesActivity.class);
                            startActivity(i1);
                            finish();
                            return true;
                        case R.id.navigation_profile:
                            Intent i2 = new Intent(EmployeeManagement.this, CompanyProfileActivity.class);
                            startActivity(i2);
                            finish();
                            return true;
                    }
                    return false;
                }
            };
}
