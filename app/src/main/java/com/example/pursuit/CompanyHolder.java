package com.example.pursuit;

import android.annotation.SuppressLint;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pursuit.models.Company;
import com.example.pursuit.models.Student;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class CompanyHolder extends RecyclerView.ViewHolder {
    private static final String TAG = "CompanyHolder";

//    private TextView fullName, username, major, university, bio;
    private  TextView name, field;
    private ImageButton toggleFollow;

    public CompanyHolder(@NonNull View itemView) {
        super(itemView);
        name = itemView.findViewById(R.id.name);
        field = itemView.findViewById(R.id.field);
//        fullName = itemView.findViewById(R.id.full_name);
//        username = itemView.findViewById(R.id.username);
//        university = itemView.findViewById(R.id.university);
//        major = itemView.findViewById(R.id.major);
//        bio = itemView.findViewById(R.id.bio);
        toggleFollow = itemView.findViewById(R.id.toggle_follow_btn);
    }

    @SuppressLint("SetTextI18n")
    public void bind(Company company, String currentUserId, String currentUserRole) {
//        fullName.setText(company.getFirstName() + " " + company.getLastName());
//        username.setText(company.getUsername());
//        university.setText("University: " + company.getUniversity());
//        major.setText("Major: " + company.company());
//        bio.setText(company.company());
        name.setText(company.getName());
        field.setText(company.getField());
        toggleFollow.setTag(company);
        checkFollowStatus(company, currentUserId, currentUserRole);

    }

    private void checkFollowStatus(Company company, String currentUserId, String currentUserRole) {
        Log.d(TAG, "checkFollowStatus");
//        Log.d("USERNAME", company.getUsername());
//        Log.d("CURRENT_ID", currentUserId);
//        Log.d("CURRENT_ROLE", currentUserRole);
        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference();
        Query checkFollowStatusQuery;
        if (currentUserRole.equals("Student")) {
            checkFollowStatusQuery = dbRef.child("Students").child(currentUserId).child("Following").child("Companies")
                    .orderByChild("id").equalTo(company.getId());
        } else {
            checkFollowStatusQuery = dbRef.child("Companies").child(currentUserId).child("Following").child("Companies")
                    .orderByChild("id").equalTo(company.getId());
        }

        checkFollowStatusQuery.addValueEventListener(checkFollowStatusListener);
    }

    private ValueEventListener checkFollowStatusListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            if (dataSnapshot.exists()) {
                Log.d(TAG, "changing to check");
                updateToggleBtnBackground(1);
            } else {
                Log.d(TAG, "changing to x");
                updateToggleBtnBackground(0);
            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) { }
    };

    private void updateToggleBtnBackground(int exists) {
        if (exists == 1) {
            toggleFollow.setBackgroundResource(R.drawable.ic_check_black_24dp);
        } else {
            toggleFollow.setBackgroundResource(R.drawable.ic_add_black_24dp);
        }
    }
}
