package com.example.pursuit;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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

public class StudentProfileActivity extends AppCompatActivity {

    private static final String TAG = "StudentProfileActivity";

    TextView studentFullName;
    TextView studentUniversity;
    TextView studentMajor;
    TextView studentMinor;
    TextView studentGPA;
    TextView studentBio;
    ImageView studentProfilePic;

    private DatabaseReference dbref;
    Uri filePath;
    final int PICK_IMAGE_REQUEST = 22;
    FirebaseStorage storage;
    StorageReference storageReference;
    BottomNavigationView bottomNavigation;
    Student currentStudent;
    private static int RESULT_LOAD_IMAGE = 1;

    int hasPicture;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_profile);
        bottomNavigation = findViewById(R.id.bottom_navigation);
        bottomNavigation.setOnNavigationItemSelectedListener(navigationItemSelectedListener);

        initializeCurrentStudent();

        String studentFullNameString = currentStudent.getFirstName() + " " + currentStudent.getLastName();
        studentFullName = findViewById(R.id.studentFullName);
        studentFullName.setText(studentFullNameString);

        studentUniversity = findViewById(R.id.studentUniversity);
        studentUniversity.setText("University: " + currentStudent.getUniversity());

        studentMajor = findViewById(R.id.studentMajor);
        studentMajor.setText("Major: " + currentStudent.getMajor());

        studentMinor = findViewById(R.id.txtStudentMinor);
        studentMinor.setText("Minor: " + currentStudent.getMinor());

        studentGPA = findViewById(R.id.txtStudentGPA);
        studentGPA.setText("GPA: " + currentStudent.getGpa());

        studentBio = findViewById(R.id.txtStudentBio);
        studentBio.setText(currentStudent.getBio());

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

    /* ******END DATABASE****** */

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
}