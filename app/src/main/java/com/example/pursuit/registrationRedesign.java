package com.example.pursuit;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.pursuit.models.Company;
import com.example.pursuit.models.CompanyOpportunity;
import com.example.pursuit.models.Employee;
import com.example.pursuit.models.EmployeeInvite;
import com.example.pursuit.models.Keyword;
import com.example.pursuit.models.Student;

import com.google.android.material.tabs.TabLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class registrationRedesign extends FragmentActivity {   //AppCompatActivity
    //variables for adapter
    private static final String TAG = "registrationRedesign";
    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;
    //student variables
    private DatabaseReference mRef;
    private ArrayList<Student> matchedStudents; //matchedUsers
    private ArrayList<Student> matchedStudentEmails;    //matchedEmails
    private ArrayList<Company> matchedCompanyEmails;    //matchedEmails
    private View view;
    private boolean match;
    private String checkEmail;
    EditText firstName, lastName, university, major, minor, gpa, bio, email, username;
    EditText studentPassword, studentReEnterPassword, interestKeywords;
    private ArrayList<String> interestKeywordArrayList;
    private int keywordParser;
    private String currentKeyword;
    Student newStudent;
    String id;

    //company variables
    EditText companyName;
    EditText companyEmail;
    EditText companyPassword;
    EditText companyReEnterPassword;
    EditText companyField;
    EditText companyDescription;
    Company newCompany;

    Button employeeRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration_redesign);
        //for ui
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = findViewById(R.id.view_pager);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        mRef = FirebaseDatabase.getInstance().getReference();
        matchedStudentEmails = new ArrayList<>();
        matchedCompanyEmails = new ArrayList<>();
        matchedStudents = new ArrayList<>();
        employeeRegister = findViewById(R.id.btnContinue);
    }

    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_registration_redesign, container, false);
            TextView textView = (TextView) rootView.findViewById(R.id.section_label);
            textView.setText(getString(R.string.project_id, getArguments().getInt(ARG_SECTION_NUMBER)));
            return rootView;
        }
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {
        private Fragment mCurrentFragment;
        public Fragment getCurrentFragment() {
            return mCurrentFragment;
        }

        @Override
        public void setPrimaryItem(ViewGroup container, int position, Object object) {
            if (getCurrentFragment() != object) {
                mCurrentFragment = ((Fragment) object);
            }
            super.setPrimaryItem(container, position, object);
        }

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            Fragment fragment = null;
            switch (position) {
                case 0:
                    fragment = new studentFragment();
                    break;
                case 1:
                    fragment = new employeeFragment();
                    break;
                case 2:
                    fragment = new companyFragment();
                    break;
            }
            return fragment;
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    setTitle("Student");
                    return "Student";
                case 1:
                    setTitle("Employee");
                    return "Employee";
                case 2:
                    setTitle("Company");
                    return "Company";
            }
            return null;
        }
    }
    /* ********DATABASE******** */

    ValueEventListener companyEmailListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            matchedCompanyEmails = new ArrayList<>();
            Log.d(TAG, "in companyEmailListener onDataChange");
            if (dataSnapshot.exists()) {
                Log.d(TAG, "Snapshot exists");
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Log.d(TAG, "looping in snapshot");
                    Company company = snapshot.getValue(Company.class);
                    if (company == null) {
                        Log.d(TAG, "company is null");
                    } else {
                        Log.d(TAG, "company exists: " + company.getEmail());
                    }
                    matchedCompanyEmails.add(company);
                }
            }
            processCompanyRequest();
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    };

    private void writeNewCompany(String name, String email, String password, String field, String description) {
        String id = RandomKeyGenerator.randomAlphaNumeric(16);
        newCompany = new Company(id, name, email, password, field, description, new ArrayList<CompanyOpportunity>());
        mRef.child("Companies").child(id).setValue(newCompany);
    }

    /* ******END DATABASE****** */

    private boolean emailExists(String email, ArrayList list) {
        Log.d(TAG, "In emailExists; email: " + email);

        String size = String.valueOf(list.size());

        Log.d(TAG, "matchedEmails size: " + size);

        if (list.size() > 0) {
            match = true;
            return true;
        } else {
            return false;
        }
    }

    public String toString(EditText text) {
        return text.getText().toString();
    }

    // Checks If Password And Re-Entered Password Match
    public boolean passwordMatchCompany() {
        if (toString(companyPassword).equals(toString(companyReEnterPassword)))
            return true;
        Toast.makeText(view.getContext(), "Passwords Did Not Match", Toast.LENGTH_SHORT).show();
        return false;
    }

    public boolean passwordMatchStudent() {
        if (toString(studentPassword).equals(toString(studentReEnterPassword)))
            return true;
        Toast.makeText(view.getContext(), "Passwords Did Not Match", Toast.LENGTH_SHORT).show();
        return false;
    }

    boolean isEmail(EditText text) {
        CharSequence email = text.getText().toString();
        if (!(!TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches())) {
            Toast.makeText(view.getContext(), "Invalid Email", Toast.LENGTH_SHORT).show();
            return false;
        } else
            return true;
    }

    boolean isEmpty(EditText text) {
        if (text != null) {
            CharSequence str = text.getText().toString();
            return TextUtils.isEmpty(str);
        }
        return true;
    }

    public boolean checkForEmptiesCompany() {
        if (isEmpty(companyName) || isEmpty(companyEmail) || isEmpty(companyField) || isEmpty(companyPassword)
                || isEmpty(companyReEnterPassword) || isEmpty(companyDescription)) {
            Toast.makeText(view.getContext(), "All Fields are Required", Toast.LENGTH_SHORT).show();
            return true;
        }
        return false;
    }

    public boolean checkForEmptiesStudent() {
        if (isEmpty(firstName) || isEmpty(lastName) || isEmpty(university) || isEmpty(major) || isEmpty(minor)
                || isEmpty(gpa) || isEmpty(bio) || isEmpty(email) || isEmpty(username) || isEmpty(studentPassword)
                || isEmpty(studentReEnterPassword)) {
            Toast.makeText(view.getContext(), "All Fields are Required", Toast.LENGTH_SHORT).show();
            return true;
        }
        return false;
    }

    public void processCompanyRequest() {
        match = false;

        if (!checkForEmptiesCompany()) {
            Log.d(TAG, "no empty fields");

            if (isEmail(companyEmail)) {
                Log.d(TAG, "email is valid");

                if (passwordMatchCompany()) {
                    Log.d(TAG, "passwords match");

                    if (!emailExists(toString(companyEmail), matchedCompanyEmails)) {
                        Log.d(TAG, "email not taken");

                        writeNewCompany(toString(companyName), toString(companyEmail), toString(companyPassword),
                                toString(companyField), toString(companyDescription));

                        // set the currentCompany
                        ((PursuitApplication) this.getApplication()).setCurrentCompany(newCompany);

                        Intent intent = new Intent(this, LandingActivity.class);
                        startActivity(intent);
                    } else {
                        Log.d(TAG, "email is already taken");
                        Toast.makeText(view.getContext(), "Email is Already Taken", Toast.LENGTH_LONG).show();
                    }
                }
            }
        }

        if (match == true) {
            Log.d(TAG, "RESULT: true");

        } else {
            Log.d(TAG, "RESULT: false");
        }
    }

    public void register(View v) {
        view = v;
        companyName = findViewById(R.id.companyName);
        companyEmail = findViewById(R.id.companyEmail);
        companyPassword = findViewById(R.id.companyPassword);
        companyReEnterPassword = findViewById(R.id.companyReEnterPassword);
        companyField = findViewById(R.id.companyField);
        companyDescription = findViewById(R.id.companyDescription);

        checkEmail = toString(companyEmail);

        Query emailQuery = mRef.child("Companies").orderByChild("email").equalTo(checkEmail);

        if (emailQuery == null) {
            Log.d(TAG, "Email query is null");
        } else {
            Log.d(TAG, "Email query is not null");
        }

        emailQuery.addListenerForSingleValueEvent(companyEmailListener);
    }

    private DatabaseReference dbref;

    EditText companyCode;
    EditText invitationCode;

    Employee newEmployee;
    Company matchedCompany;

    EmployeeInvite matchedInvite;

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


    public void checkInvite(View v) {
        //xview = v;
        Log.d(TAG, "INIT OF CHECK INVITE\n\n\n");
        companyCode = findViewById(R.id.txtCompanyCode);
        invitationCode = findViewById(R.id.txtInviteCode);

        if (isEmpty(companyCode) || isEmpty(invitationCode)) {
            Toast.makeText(view.getContext(), "All Fields are Required", Toast.LENGTH_SHORT).show();
        } else {

            Query companyCodeQuery = mRef.child("Companies").orderByChild("id").equalTo(toString(companyCode));

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

            Query inviteCodeQuery = mRef.child("EmployeeInvites").child(toString(companyCode)).orderByChild("code").equalTo(toString(invitationCode));

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
                    null,null,null,matchedCompany.getName(), matchedCompany.getId(),
                    toString(invitationCode));

            ((PursuitApplication) this.getApplication()).setCurrentCompany(matchedCompany);
            ((PursuitApplication) this.getApplication()).setCurrentEmployee(newEmployee);

            Intent intent = new Intent(this, EmployeeRegistration2.class);
            startActivity(intent);
        }
    }
    /* ********DATABASE******** */

    // Listener For Username Query, Calls Email Query At End
    ValueEventListener usernameListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            matchedStudents = new ArrayList<>();
            Log.d(TAG, "In usernameListener onDataChange");
            if (dataSnapshot.exists()) {
                Log.d(TAG, "Snapshot exists");
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Log.d(TAG, "looping in snapshot");
                    Student student = snapshot.getValue(Student.class);
                    if (student == null) {
                        Log.d(TAG, "student is null");
                    } else {
                        Log.d(TAG, "student exists: " + student.getUsername());
                    }
                    matchedStudents.add(student);
                }
            }

            Log.d(TAG, "email is: " + email.toString());

            // SELECT * FROM Students WHERE email = ?
            Query emailQuery = mRef.child("Students").orderByChild("email").equalTo(checkEmail);

            if (emailQuery == null) {
                Log.d(TAG, "Email query is null");
            } else {
                Log.d(TAG, "Email query is not null");
            }

            // Call To Email EventListener
            emailQuery.addListenerForSingleValueEvent(studentEmailListener);
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {
            Log.e(TAG, databaseError.toString());
        }
    };

    // Listener For Email Query
    ValueEventListener studentEmailListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            matchedStudentEmails = new ArrayList<>();
            Log.d(TAG, "in studentEmailListener onDataChange");
            if (dataSnapshot.exists()) {
                Log.d(TAG, "Snapshot exists");
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Log.d(TAG, "looping in snapshot");
                    Student student = snapshot.getValue(Student.class);
                    if (student == null) {
                        Log.d(TAG, "student is null");
                    } else {
                        Log.d(TAG, "student exists: " + student.getEmail());
                    }
                    matchedStudentEmails.add(student);
                }
            }

            // Continue To processRequest After Queries Are Complete
            processStudentRequest();
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {
            Log.e(TAG, databaseError.toString());
        }
    };

    private void writeNewStudent(String fname, String lname, String university, String major, String minor, String gpa,
                                 String bio, String email, String username, String password, ArrayList<String> interests) {
        id = RandomKeyGenerator.randomAlphaNumeric(16);
        newStudent = new Student(id, fname, lname, university, major, minor, gpa, bio, email, username, password, interests);

        mRef.child("Students").child(id).setValue(newStudent);
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
                Log.d(TAG, currentKeyword + "does not exist");
                writeNeKeyword();
            } else {
                Log.d(TAG, currentKeyword + " exists, updating");
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
        temp.add(id);
        mRef.child("Keywords").child(keyword.getId()).child("students").setValue(temp);
        keywordParser++;
        addKeywordToDB();
    }

    private void writeNeKeyword() {
        ArrayList<String> students = new ArrayList<>();
        students.add(id);
        String keywordID = RandomKeyGenerator.randomAlphaNumeric(16);
        Keyword keyword = new Keyword(keywordID, currentKeyword, null, students);
        mRef.child("Keywords").child(keywordID).setValue(keyword);
        keywordParser++;
        addKeywordToDB();
    }

    /* ******END DATABASE****** */

    // Checks If Given Username Already Exists
    private boolean usernameExists(String username) {
        Log.d(TAG, "In usernameExists; username: " + username);

        String size = String.valueOf(matchedStudents.size());

        Log.d(TAG, "matchedUsers size: " + size);

        if (matchedStudents.size() > 0) {
            match = true;
            return true;
        } else {
            return false;
        }
    }

    private void addKeywordToDB() {
        if (keywordParser < interestKeywordArrayList.size()) {
            currentKeyword = interestKeywordArrayList.get(keywordParser);
            Log.d(TAG, "adding " + currentKeyword);
            Query keywordQuery = mRef.child("Keywords").orderByChild("text").equalTo(currentKeyword);
            keywordQuery.addListenerForSingleValueEvent(keywordListener);
        }
    }

    // Completes Necessary Checks For Registering A New Student
    public void processStudentRequest() {
        match = false;
        if (!checkForEmptiesStudent()) {
            Log.d(TAG, "no empty fields");

            if (isEmail(email)) {
                Log.d(TAG, "email is valid");

                if (passwordMatchStudent()) {
                    Log.d(TAG, "passwords match");

                    if (!usernameExists(toString(username))) {
                        Log.d(TAG, "username not taken");
                        if (!emailExists(toString(email), matchedStudentEmails)) {
                            Log.d(TAG, "email not taken");
                            String[] interestKeywordArray = toString(interestKeywords).split(",");
                            interestKeywordArrayList = new ArrayList<>();
                            if (!toString(interestKeywords).equals("")) {
                                for (int i = 0; i < interestKeywordArray.length; i++) {
                                    String word = interestKeywordArray[i].trim().toLowerCase();
                                    if (!interestKeywordArrayList.contains(word)) {
                                        interestKeywordArrayList.add(word);
                                    }
                                }
                            }
                            writeNewStudent(toString(firstName), toString(lastName), toString(university),
                                    toString(major), toString(minor), toString(gpa), toString(bio), toString(email),
                                    toString(username), toString(studentPassword), interestKeywordArrayList);

                            keywordParser = 0;
                            addKeywordToDB();

                            // set the Current User
                            ((PursuitApplication) this.getApplication()).setCurrentStudent(newStudent);

                            Intent intent = new Intent(this, LandingActivity.class);
                            startActivity(intent);
                        } else {
                            Log.d(TAG, "email is already taken");
                            Toast.makeText(view.getContext(), "Email is Already Taken", Toast.LENGTH_LONG).show();
                        }
                    } else {
                        Log.d(TAG, "username is already taken");
                        Toast.makeText(view.getContext(), "Username Already Exists", Toast.LENGTH_LONG).show();
                    }
                }
            }
        }
        if (match == true) {
            Log.d(TAG, "RESULT: true");

        } else {
            Log.d(TAG, "RESULT: false");
        }

    }

    // Called By onClick, Attempts To Register A Student
    public void registerStudent(View v) {
        view = v;
        firstName = findViewById(R.id.txtFirstName);
        lastName = findViewById(R.id.txtLastName);
        university = findViewById(R.id.txtUniversity);
        major = findViewById(R.id.txtMajor);
        minor = findViewById(R.id.txtMinor);
        gpa = findViewById(R.id.txtStudentGPA2);
        bio = findViewById(R.id.txtBio);
        email = findViewById(R.id.txtEmail);
        username = findViewById(R.id.txtStudentUsername);
        studentPassword = findViewById(R.id.txtPassword);
        studentReEnterPassword = findViewById(R.id.txtReEnterPassword);
        interestKeywords = findViewById(R.id.txtInterests);

        checkEmail = toString(email);

        // SELECT * FROM Students WHERE username = ?
        Query usernameQuery = mRef.child("Students").orderByChild("username").equalTo(toString(username));

        if (usernameQuery == null) {
            Log.d(TAG, "Username query is null");
        } else {
            Log.d(TAG, "Username query is not null");
        }

        // Call To Username Event Listener
        usernameQuery.addListenerForSingleValueEvent(usernameListener);
    }

}