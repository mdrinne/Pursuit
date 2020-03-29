package com.example.pursuit;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;
// import android.widget.ImageView;
// import android.widget.LinearLayout;

import com.example.pursuit.models.Student;

public class StudentProfileActivity extends AppCompatActivity {

    TextView studentFullName;
    TextView studentUniversity;
    TextView studentMajor;
    TextView studentMinor;
    TextView studentGPA;
    TextView studentBio;
    // ImageView profilePhoto;
    Student currentStudent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_profile);

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


    }

    private void initializeCurrentStudent() {
        currentStudent = ((PursuitApplication) this.getApplication()).getCurrentStudent();
    }
}