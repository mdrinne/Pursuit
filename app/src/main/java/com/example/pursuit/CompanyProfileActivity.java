package com.example.pursuit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.view.View;
import android.widget.Toast;


import com.example.pursuit.adapters.OpportunityAdapter;
import com.example.pursuit.models.Company;
import com.example.pursuit.models.CompanyOpportunity;
import com.example.pursuit.models.Employee;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageException;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;

public class CompanyProfileActivity extends AppCompatActivity{

    private static final String TAG = "CompanyProfileActivity";

    TextView companyName;
    TextView companyField;
    TextView companyDescription;
    Company currentCompany;
    Employee currentEmployee;
    BottomNavigationView bottomNavigation;
    ImageView companyProfilePic;
    Uri filePath;

    private DatabaseReference dbref;

    FirebaseStorage storage;
    StorageReference storageReference;

    private final int PICK_IMAGE_REQUEST = 22;

    String currentRole;
    int hasPicture;

    private ArrayList<CompanyOpportunity> companyOpportunities;
    private RecyclerView allOpportunities;
    private OpportunityAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    Dialog deleteDialog;

    CompanyProfileActivity THIS;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company_profile);
        bottomNavigation = findViewById(R.id.bottom_navigation);
        bottomNavigation.setOnNavigationItemSelectedListener(navigationItemSelectedListener);

        setCurrentUser();
        deleteDialog = new Dialog(this);

        populateTextFields();

        dbref = FirebaseDatabase.getInstance().getReference();
        storageReference = FirebaseStorage.getInstance().getReference();
        storage = FirebaseStorage.getInstance();

        companyProfilePic = findViewById(R.id.imgCompanyProfilePic);
        loadCompanyProfilePicture();

        companyOpportunities = currentCompany.getOpportunities();
        buildRecyclerView();

//        Query opportunityQuery = dbref.child("CompanyOpportunities").child(currentCompany.getId()).orderByChild("timestamp");
//
//        opportunityQuery.addListenerForSingleValueEvent(companyOpportunityListener);

    }

    /* ********DATABASE******** */

    ValueEventListener companyOpportunityListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            companyOpportunities = new ArrayList<>();
            companyOpportunities.clear();

            if (dataSnapshot.exists()) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    CompanyOpportunity opportunity = snapshot.getValue(CompanyOpportunity.class);
                    companyOpportunities.add(opportunity);
                }
            }
            buildRecyclerView();
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    };

    ValueEventListener companyHasProfilePictureListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            hasPicture = 0;
            if (dataSnapshot.exists()) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    hasPicture = 1;
                }
            }
            postCompanyHasProfilePictureListener();
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    };

    private void downloadCompanyProfilePicture() {
        StorageReference profilePic = storageReference.child("images").child("CompanyProfilePictures").child(currentCompany.getId());
        profilePic.getDownloadUrl()
                .addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Log.d(TAG, uri.toString());
                        Picasso.get().load(uri.toString()).into(companyProfilePic);

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        int errorCode = ((StorageException) e).getErrorCode();
                        if (errorCode == StorageException.ERROR_OBJECT_NOT_FOUND) {
                            Log.e(TAG, "Company does not have profile picture");
                        }
                    }
                });
    }

    public void uploadImage()
    {

        Log.d(TAG, filePath.toString());
        if (filePath != null) {

            final ProgressDialog progressDialog
                    = new ProgressDialog(this);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();

            StorageReference ref
                    = storageReference
                    .child(
                            "images/CompanyProfilePictures/"
                                    + currentCompany.getId());

            ref.putFile(filePath)
                    .addOnSuccessListener(
                            new OnSuccessListener<UploadTask.TaskSnapshot>() {

                                @Override
                                public void onSuccess(
                                        UploadTask.TaskSnapshot taskSnapshot)
                                {
                                    progressDialog.dismiss();
                                }
                            })

                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e)
                        {
                            progressDialog.dismiss();
                            Log.e(TAG, e.getMessage());
                        }
                    })
                    .addOnProgressListener(
                            new OnProgressListener<UploadTask.TaskSnapshot>() {

                                // Progress Listener for loading
                                // percentage on the dialog box
                                @Override
                                public void onProgress(
                                        UploadTask.TaskSnapshot taskSnapshot)
                                {
                                    double progress
                                            = (100.0
                                            * taskSnapshot.getBytesTransferred()
                                            / taskSnapshot.getTotalByteCount());
                                    progressDialog.setMessage(
                                            "Uploaded "
                                                    + (int)progress + "%");
                                }
                            });
        }

        updateProfilePicDatabase();
    }

    private void updateProfilePicDatabase() {
        dbref.child("ProfilePicture").child("Companies").child(currentCompany.getId()).setValue(1);
    }

    private void approveOpportunity(Integer position) {
        CompanyOpportunity opportunity = companyOpportunities.get(position);
        opportunity.setApproved(1);
        companyOpportunities.set(position, opportunity);
        dbref.child("CompanyOpportunities").child(currentCompany.getId()).child(opportunity.getId()).child("approved").setValue(1);
        mAdapter.notifyItemChanged(position);
    }

    private void deleteOpportunity(final int position) {

        final TextView deleteMessage;
        Button cancel, confirm;
        deleteDialog.setContentView(R.layout.delete_opportunity_pop_up);

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
                CompanyOpportunity opportunity = companyOpportunities.get(position);
                dbref.child("CompanyOpportunities").child(opportunity.getId()).removeValue();
                companyOpportunities.remove(opportunity);
                currentCompany.setOpportunities(companyOpportunities);
                ((PursuitApplication) THIS.getApplication()).setCurrentCompany(currentCompany);
                mAdapter.notifyItemRemoved(position);
                deleteDialog.dismiss();
            }
        });

        String message = "Are you sure you want to delete " + companyOpportunities.get(position).getPosition() + "?";
        deleteMessage.setText(message);
        deleteDialog.show();

    }

    /* ******END DATABASE****** */

    private void buildRecyclerView() {
        allOpportunities = findViewById(R.id.rcycOpportunities);
        allOpportunities.setHasFixedSize(false);
        if (companyOpportunities == null) {
            companyOpportunities = new ArrayList<>();
        }
        mLayoutManager = new LinearLayoutManager(this);
        mAdapter = new OpportunityAdapter(companyOpportunities);

        allOpportunities.setLayoutManager(mLayoutManager);
        allOpportunities.setAdapter(mAdapter);

        mAdapter.setOpportunityOnItemClickListener(new OpportunityAdapter.OpportunityOnItemClickListener() {
            @Override
            public void onApproveClick(int position) {
                if (currentRole.equals("Company") || (currentRole.equals("Employee") && currentEmployee.admin == 1)) {
                    approveOpportunity(position);
                } else {
                    Toast.makeText(getApplicationContext(), "Non-Admin: Access Restricted", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onDeleteClick(int position) {
                deleteOpportunity(position);
            }

            @Override
            public void onCardClick(int position) {
                viewOpportunity(position);
            }
        });
    }

    private void loadCompanyProfilePicture() {
        Query companyHasProfilePictureQuery = dbref.child("ProfilePicture").orderByChild(currentCompany.getId()).equalTo(1);

        companyHasProfilePictureQuery.addListenerForSingleValueEvent(companyHasProfilePictureListener);
    }

    private void postCompanyHasProfilePictureListener() {
        if (hasPicture == 1) {
            downloadCompanyProfilePicture();
        } else {
            companyProfilePic.setImageDrawable(getResources().getDrawable(R.drawable.ic_add_a_photo));
        }
    }

    BottomNavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    switch (item.getItemId()) {
                        case R.id.navigation_home:
                            Intent i0 = new Intent(CompanyProfileActivity.this, LandingActivity.class);
                            startActivity(i0);
                            finish();
                            return true;
                        case R.id.navigation_messages:
                            Intent i1 = new Intent(CompanyProfileActivity.this, ConversationsActivity.class);
                            startActivity(i1);
                            finish();
                            return true;
                        case R.id.navigation_profile:
                            return true;
                    }
                    return false;
                }
            };

    private void setCurrentUser() {
        initializeCurrentCompany();
        initializeCurrentRole();
        if (currentRole.equals("Employee")) {
            currentEmployee = ((PursuitApplication) this.getApplicationContext()).getCurrentEmployee();
        }
    }

    private void initializeCurrentCompany() {
        Log.d(TAG, "initializing company");
        currentCompany = ((PursuitApplication) this.getApplicationContext()).getCurrentCompany();
    }

    private void initializeCurrentRole() {
        currentRole = ((PursuitApplication) this.getApplicationContext()).getRole();
    }

    private void populateTextFields() {
        companyName = findViewById(R.id.txtCompanyName);
        companyName.setText(currentCompany.getName());

        companyField = findViewById(R.id.txtCompanyField);
        companyField.setText(currentCompany.getField());

        companyDescription = findViewById(R.id.txtCompanyDescription);
        companyDescription.setText(currentCompany.getDescription());
    }

    public void inviteEmployee(View v) {
        Log.d(TAG, "inviting");

        if (currentRole.equals("Employee")) {
            Toast.makeText(v.getContext(), "Only Admin Has Access To Invite", Toast.LENGTH_SHORT).show();
        } else {
            Intent intent = new Intent(this, InviteEmployeeActivity.class);
            startActivity(intent);
        }
    }

    public void viewInvites(View v) {
        Intent intent = new Intent(this, viewCompanyEmployeeInvites.class);
        startActivity(intent);
    }

    public void editPicture(View v) {
        if (currentRole.equals("Employee")) {
            Toast.makeText(v.getContext(), "Only Admin Has Profile Picture Edit Rights", Toast.LENGTH_SHORT).show();
        } else {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, "Select Image from here..."), PICK_IMAGE_REQUEST);
        }
    }

    @Override
    protected void onActivityResult(int requestCode,
                                    int resultCode,
                                    Intent data)
    {

        super.onActivityResult(requestCode,
                resultCode,
                data);

        if (requestCode == PICK_IMAGE_REQUEST
                && resultCode == RESULT_OK
                && data != null
                && data.getData() != null) {

            // Get the Uri of data
            filePath = data.getData();
            try {

                // Setting image on image view using Bitmap
                Bitmap bitmap = MediaStore
                        .Images
                        .Media
                        .getBitmap(
                                getContentResolver(),
                                filePath);
                companyProfilePic.setImageBitmap(bitmap);
                uploadImage();
            }

            catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void addOpportunity(View v) {
        Intent intent = new Intent(this, CreateOpportunity.class);
        startActivity(intent);
    }

    public void viewOpportunity(int position) {
        Intent intent = new Intent(this, ViewOpportunity.class);
        intent.putExtra("EXTRA_OPPORTUNITY_ID", companyOpportunities.get(position).getId());
        startActivity(intent);
    }

    public void viewEmployees(View v) {
        Intent intent = new Intent(this, EmployeeManagement.class);
        startActivity(intent);
    }

}