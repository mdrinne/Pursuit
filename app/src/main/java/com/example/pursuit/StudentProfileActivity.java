package com.example.pursuit;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.pursuit.models.Student;

public class StudentProfileActivity extends AppCompatActivity {

    TextView studentFullName;
    TextView studentUniversity;
    TextView studentMajor;
    TextView studentMinor;
    TextView studentGPA;
    TextView studentBio;
    BottomNavigationView bottomNavigation;
    Student currentStudent;
    private static int RESULT_LOAD_IMAGE = 1;

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

        // profilePhoto = findViewById(R.id.profilePhoto);
        // int width = 150;
        // int height = 150;
        // LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(width,height);
        // profilePhoto.setLayoutParams(params);

        Button buttonLoadImage = (Button) findViewById(R.id.buttonLoadPicture);
        buttonLoadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                startActivityForResult(i, RESULT_LOAD_IMAGE);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = { MediaStore.Images.Media.DATA };

            Cursor cursor = getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();

            ImageView imageView = (ImageView) findViewById(R.id.imageView6);
            imageView.setImageBitmap(BitmapFactory.decodeFile(picturePath));
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
                    Intent messages = new Intent(StudentProfileActivity.this, LandingActivity.class);
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