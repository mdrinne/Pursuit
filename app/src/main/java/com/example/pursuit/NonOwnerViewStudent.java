package com.example.pursuit;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pursuit.adapters.StudentOpportunityKeywordAdapter;
import com.example.pursuit.models.Student;
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
import com.google.firebase.storage.StorageException;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class NonOwnerViewStudent extends AppCompatActivity {

    TextView name, username, email, university, major, minor, gpa, bio;
    ImageView studentProfilePic;

    private DatabaseReference dbref;
    StorageReference sref;
    BottomNavigationView bottomNavigation;

    final int PICK_IMAGE_REQUEST = 22;

    Student currentStudent;
    String currentRole;

    private ArrayList<String> interests;
    private RecyclerView studentInterests;
    private StudentOpportunityKeywordAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    int hasPicture;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_non_owner_view_student);

        currentRole = ((PursuitApplication) this.getApplication()).getRole();
        bottomNavigation = findViewById(R.id.bottom_navigation);
        bottomNavigation.setOnNavigationItemSelectedListener(navigationItemSelectedListener);

        dbref = FirebaseDatabase.getInstance().getReference();
        sref = FirebaseStorage.getInstance().getReference();

        Query studentQuery = dbref.child("Students").orderByKey().equalTo(getIntent().getStringExtra("EXTRA_STUDENT_ID"));
        studentQuery.addListenerForSingleValueEvent(studentListener);
    }

    private void continueOnCreate() {
        name = findViewById(R.id.studentFullName);
        name.setText(currentStudent.getFirstName() + " " + currentStudent.getLastName());

        username = findViewById(R.id.txtStudentUsername);
        username.setText(currentStudent.getUsername());

        email = findViewById(R.id.txtStudentEmail);
        email.setText(currentStudent.getEmail());

        university = findViewById(R.id.studentUniversity);
        university.setText(currentStudent.getUniversity());

        major = findViewById(R.id.studentMajor);
        major.setText(currentStudent.getMajor());

        minor = findViewById(R.id.txtStudentMinor);
        minor.setText(currentStudent.getMinor());

        gpa = findViewById(R.id.txtStudentGPA);
        gpa.setText(currentStudent.getGpa());

        bio = findViewById(R.id.txtStudentBio);
        bio.setText(currentStudent.getBio());

        interests = currentStudent.getInterestKeywords();
        buildRecyclerView();

        studentProfilePic = findViewById(R.id.imgStudentProfilePic);
        loadStudentProfilePic();
    }

    private void buildRecyclerView() {
        studentInterests = findViewById(R.id.rcycStudentInterests);
        studentInterests.setHasFixedSize(false);
        mLayoutManager = new LinearLayoutManager(this);
        if (interests == null) {
            interests = new ArrayList<>();
        }

        mAdapter = new StudentOpportunityKeywordAdapter(interests);

        studentInterests.setLayoutManager(mLayoutManager);
        studentInterests.setAdapter(mAdapter);
    }

    private void loadStudentProfilePic() {
        Query hasProfilePicQuery = dbref.child("ProfilePicture").orderByChild(currentStudent.getId()).equalTo(1);
        hasProfilePicQuery.addListenerForSingleValueEvent(hasProfilePicListener);
    }

    private void setProfilePicture() {
        if (hasPicture == 1) {
            downloadStudentProfilePic();
        } else {
            studentProfilePic.setImageDrawable(getResources().getDrawable(R.drawable.ic_add_a_photo));
        }
    }

    /* ********DATABASE******** */

    ValueEventListener studentListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            if (dataSnapshot.exists()) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    currentStudent = snapshot.getValue(Student.class);
                }
            }
            continueOnCreate();
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    };

    ValueEventListener hasProfilePicListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            if (dataSnapshot.exists()) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    hasPicture = 1;
                }
            }
            setProfilePicture();
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    };

    private void downloadStudentProfilePic() {
        StorageReference profilePic = sref.child("images").child("StudentProfilePictures").child(currentStudent.getId());
        profilePic.getDownloadUrl()
                .addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Picasso.get().load(uri.toString()).into(studentProfilePic);

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        int errorCode = ((StorageException) e).getErrorCode();
                        if (errorCode == StorageException.ERROR_OBJECT_NOT_FOUND) {
                        }
                    }
                });
    }

    /* ******END DATABASE****** */

    BottomNavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    switch (item.getItemId()) {
                        case R.id.navigation_home:
                            Intent landing = new Intent(NonOwnerViewStudent.this, LandingActivity.class);
                            startActivity(landing);
                            finish();
                            return true;
                        case R.id.navigation_messages:
                            Intent messages = new Intent(NonOwnerViewStudent.this, ConversationsActivity.class);
                            startActivity(messages);
                            finish();
                            return true;
                        case R.id.navigation_profile:
                            Intent profile;
                            if (currentRole.equals("Student")) {
                                profile = new Intent(NonOwnerViewStudent.this, StudentProfileActivity.class);
                            } else {
                                profile = new Intent(NonOwnerViewStudent.this, CompanyProfileActivity.class);
                            }
                            startActivity(profile);
                            finish();
                            return true;
                    }
                    return false;
                }
            };
}
