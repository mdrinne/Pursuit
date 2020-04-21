package com.example.pursuit;

import com.example.pursuit.adapters.StudentInterestAdapter;
import com.example.pursuit.models.Keyword;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pursuit.models.Student;
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

public class StudentProfileActivity extends AppCompatActivity {

    private static final String TAG = "StudentProfileActivity";

    TextView studentFullName;
    TextView studentUniversity;
    TextView studentMajor;
    TextView studentMinor;
    TextView studentGPA;
    TextView studentBio;
    ImageView studentProfilePic;
    TextView hardcodedUniversity;
    TextView hardcodedMajor;
    TextView hardcodedMinor;
    TextView hardcodedGPA;
    EditText editStudentUniversity;
    EditText editStudentMajor;
    EditText editStudentMinor;
    EditText editStudentGPA;
    EditText editStudentBio;
    Button btnEditProfile;
    Button btnSubmitChanges;
    Button btnCancel;
    TextView txtInterests;

    private DatabaseReference dbref;
    Uri filePath;
    final int PICK_IMAGE_REQUEST = 22;
    FirebaseStorage storage;
    StorageReference storageReference;
    BottomNavigationView bottomNavigation;
    Student currentStudent;
    private static int RESULT_LOAD_IMAGE = 1;

    private ArrayList<String> interests;
    private RecyclerView studentInterests;
    private StudentInterestAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    int hasPicture;
    
    Dialog addInterestsDialog;
    private int interestsParser;
    private ArrayList<String> interestsArrayList;
    private String currentInterest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_profile);
        bottomNavigation = findViewById(R.id.bottom_navigation);
        bottomNavigation.setOnNavigationItemSelectedListener(navigationItemSelectedListener);

        initializeCurrentStudent();
        addInterestsDialog = new Dialog(this);

        String studentFullNameString = currentStudent.getFirstName() + " " + currentStudent.getLastName();
        studentFullName = findViewById(R.id.studentFullName);
        studentFullName.setText(studentFullNameString);

        studentUniversity = findViewById(R.id.studentUniversity);
        studentUniversity.setText(currentStudent.getUniversity());

        studentMajor = findViewById(R.id.studentMajor);
        studentMajor.setText(currentStudent.getMajor());

        studentMinor = findViewById(R.id.txtStudentMinor);
        studentMinor.setText(currentStudent.getMinor());

        studentGPA = findViewById(R.id.txtStudentGPA);
        studentGPA.setText(currentStudent.getGpa());

        studentBio = findViewById(R.id.txtStudentBio);
        studentBio.setText(currentStudent.getBio());

        btnEditProfile = findViewById(R.id.btnEditProfile);

        //Hardcoded TextViews
        hardcodedUniversity = findViewById(R.id.studentUniversityHardcode);
        hardcodedMajor = findViewById(R.id.studentMajorHardcode);
        hardcodedMinor = findViewById(R.id.txtStudentMinorHardcode);
        hardcodedGPA = findViewById(R.id.txtStudentGPAHardcode);

        // Edit profile EditTexts
        editStudentBio= findViewById(R.id.editStudentBio);
        editStudentBio.setVisibility(View.GONE);

        btnCancel = findViewById(R.id.btnCancel);
        btnCancel.setVisibility(View.GONE);

        btnSubmitChanges = findViewById(R.id.btnConfirm);
        btnSubmitChanges.setVisibility(View.GONE);

        editStudentGPA = findViewById(R.id.editStudentGPA);
        editStudentGPA.setVisibility(View.GONE);

        editStudentMinor = findViewById(R.id.editStudentMinor);
        editStudentMinor.setVisibility(View.GONE);

        editStudentMajor = findViewById(R.id.editStudentMajor);
        editStudentMajor.setVisibility(View.GONE);

        editStudentUniversity = findViewById(R.id.editStudentUniversity);
        editStudentUniversity.setVisibility(View.GONE);

        //Interests TextView
        txtInterests = findViewById(R.id.txtInterests);

        interests = currentStudent.getInterestKeywords();
        buildRecyclerView();

        dbref = FirebaseDatabase.getInstance().getReference();
        storageReference = FirebaseStorage.getInstance().getReference();
        storage = FirebaseStorage.getInstance();

        studentProfilePic = findViewById(R.id.imgStudentProfilePic);
        loadStudentProfilePicture();

    }

    /* ********DATABASE******** */

    ValueEventListener studentHasProfilePictureListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            hasPicture = 0;
            if (dataSnapshot.exists()) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    hasPicture = 1;
                }
            }
            postStudentHasProfilePictureListener();
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    };

    private void downloadStudentProfilePicture() {
        StorageReference profilePic = storageReference.child("images").child("StudentProfilePictures").child(currentStudent.getId());
        profilePic.getDownloadUrl()
                .addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Log.d(TAG, uri.toString());
                        Picasso.get().load(uri.toString()).into(studentProfilePic);

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
                            "images/StudentProfilePictures/"
                                    + currentStudent.getId());

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
        dbref.child("ProfilePicture").child("Students").child(currentStudent.getId()).setValue(1);
    }
    
    ValueEventListener deleteFromKeywordListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            if (dataSnapshot.exists()) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Keyword key = snapshot.getValue(Keyword.class);
                    ArrayList<String> studentsArray = key.getStudents();
                    studentsArray.remove(currentStudent.getId());
                    dbref.child("Keywords").child(key.getId()).child("students").setValue(studentsArray);
                }
            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    };
    
    private void deleteKeyword(int position) {
        String str = interests.get(position);
        interests.remove(str);
        dbref.child("Students").child(currentStudent.getId()).child("interestKeywords").setValue(interests);
        Query deleteFromKeywordQuery = dbref.child("Keywords").orderByChild("text").equalTo(str);
        deleteFromKeywordQuery.addListenerForSingleValueEvent(deleteFromKeywordListener);
        mAdapter.notifyItemRemoved(position);
    }
    
    ValueEventListener keywordListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            Keyword keyword = null;
            if (dataSnapshot.exists()) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    keyword = snapshot.getValue(Keyword.class);
                }
            }
            
            if (keyword == null) {
                writeNewKeyword();
            } else {
                updateKeyword(keyword);
            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    };
    
    private void updateKeyword(Keyword keyword) {
        ArrayList<String> temp = keyword.getStudents();
        if (temp == null) {
            temp = new ArrayList<>();
        }
        temp.add(currentStudent.getId());
        dbref.child("Keywords").child(keyword.getId()).child("students").setValue(temp);
        interests.add(keyword.getText());
        interestsParser++;
        addInterestsToDB();
    }
    
    private void writeNewKeyword() {
        ArrayList<String> students = new ArrayList<>();
        students.add(currentStudent.getId());
        String keywordID = RandomKeyGenerator.randomAlphaNumeric(16);
        Keyword keyword = new Keyword(keywordID, interestsArrayList.get(interestsParser), null, students);
        dbref.child("Keywords").child(keywordID).setValue(keyword);
        interests.add(interestsArrayList.get(interestsParser));
        interestsParser++;
        addInterestsToDB();
    }

    /* ******END DATABASE****** */

    private void buildRecyclerView() {
        if (interests == null) {
            interests = new ArrayList<>();
        }
        studentInterests = findViewById(R.id.rcycStudentInterests);
        studentInterests.setHasFixedSize(false);
        mLayoutManager = new LinearLayoutManager(this);
        mAdapter = new StudentInterestAdapter(interests);
        
        studentInterests.setLayoutManager(mLayoutManager);
        studentInterests.setAdapter(mAdapter);
        
        mAdapter.setStudentInterestOnItemClickListener(new StudentInterestAdapter.StudentInterestOnItemClickListener() {
            @Override
            public void onDeleteClick(int position) {
                deleteKeyword(position);
            }
        });
    }

    private void loadStudentProfilePicture() {
        Query studentHasProfilePictureQuery = dbref.child("ProfilePicture").orderByChild(currentStudent.getId()).equalTo(1);

        studentHasProfilePictureQuery.addListenerForSingleValueEvent(studentHasProfilePictureListener);
    }

    private void postStudentHasProfilePictureListener() {
        if( hasPicture == 1) {
            downloadStudentProfilePicture();
        } else {
            studentProfilePic.setImageDrawable(getResources().getDrawable(R.drawable.ic_add_a_photo));
        }
    }

    public void editPicture(View v) {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Image from here..."), PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

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
                studentProfilePic.setImageBitmap(bitmap);
                uploadImage();
            }

            catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    BottomNavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener =
      new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    Intent landing = new Intent(StudentProfileActivity.this, LandingActivity.class);
                    startActivity(landing);
                    finish();
                    return true;
                case R.id.navigation_messages:
                    Intent messages = new Intent(StudentProfileActivity.this, ConversationsActivity.class);
                    startActivity(messages);
                    finish();
                    return true;
                case R.id.navigation_profile:
                    return true;
          }
          return false;
        }
      };

    private void initializeCurrentStudent() {
        currentStudent = ((PursuitApplication) this.getApplication()).getCurrentStudent();
    }
    
    public void addInterests(View v) {
        final EditText txtAddInterests;
        Button cancel, confirm;
        
        addInterestsDialog.setContentView(R.layout.add_interests_pop_up);
        
        cancel = addInterestsDialog.findViewById(R.id.btnCancel);
        confirm = addInterestsDialog.findViewById(R.id.btnConfirm);
        txtAddInterests = addInterestsDialog.findViewById(R.id.txtAddInterests);
        
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addInterestsDialog.dismiss();
            }
        });
        
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String[] interestsArray = txtAddInterests.getText().toString().split(",");
                interestsArrayList = new ArrayList<>();
                for (int i=0; i<interestsArray.length; i++) {
                    String word = interestsArray[i].trim().toLowerCase();
                    if (!interestsArrayList.contains(word) && !interests.contains(word)) {
                        interestsArrayList.add(word);
                    }
                }
                
                interestsParser = 0;
                addInterestsToDB();
                addInterestsDialog.dismiss();
            }
        });
        
        addInterestsDialog.show();
    }
    
    private void addInterestsToDB() {
        if (interestsParser < interestsArrayList.size()) {
            currentInterest = interestsArrayList.get(interestsParser);
            Query keywordQuery = dbref.child("Keywords").orderByChild("text").equalTo(currentInterest);
            keywordQuery.addListenerForSingleValueEvent(keywordListener);
        } else {
            dbref.child("Students").child(currentStudent.getId()).child("interestKeywords").setValue(interests);
            mAdapter.notifyDataSetChanged();
            return;
        }
    }


    private void editProfile(View v) {
        editStudentUniversity.setVisibility(View.VISIBLE);
        editStudentMajor.setVisibility(View.VISIBLE);
        editStudentMinor.setVisibility(View.VISIBLE);
        editStudentGPA.setVisibility(View.VISIBLE);
        editStudentBio.setVisibility(View.VISIBLE);
        btnSubmitChanges.setVisibility(View.VISIBLE);
        btnCancel.setVisibility(View.VISIBLE);

        //Rewrite constraints here
        

        studentBio.setVisibility(View.GONE);
        btnEditProfile.setVisibility(View.GONE);
        studentGPA.setVisibility(View.GONE);
        studentMinor.setVisibility(View.GONE);
        studentMajor.setVisibility(View.GONE);
        studentUniversity.setVisibility(View.GONE);
    }

    private void submitChanges(View v) {
        //Enter data into firebase

        exitProfileEditor(v);
    }

    private void exitProfileEditor(View v) {
        studentUniversity.setVisibility(View.VISIBLE);
        studentMajor.setVisibility(View.VISIBLE);
        studentMinor.setVisibility(View.VISIBLE);
        studentGPA.setVisibility(View.VISIBLE);
        btnEditProfile.setVisibility(View.VISIBLE);
        studentBio.setVisibility(View.VISIBLE);

        //Rewrite constraints here

        btnCancel.setVisibility(View.GONE);
        btnSubmitChanges.setVisibility(View.GONE);
        editStudentBio.setVisibility(View.GONE);
        editStudentGPA.setVisibility(View.GONE);
        editStudentMinor.setVisibility(View.GONE);
        editStudentMajor.setVisibility(View.GONE);
        editStudentUniversity.setVisibility(View.GONE);
    }

}