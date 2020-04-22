package com.example.pursuit;

import android.annotation.SuppressLint;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import com.example.pursuit.models.Student;

public class UserHolder extends RecyclerView.ViewHolder {

    private TextView fullName, username, major, university, bio;
    private ImageButton toggleFollow;

    public UserHolder(@NonNull View itemView) {
        super(itemView);
        fullName = itemView.findViewById(R.id.full_name);
        username = itemView.findViewById(R.id.username);
        university = itemView.findViewById(R.id.university);
        major = itemView.findViewById(R.id.major);
        bio = itemView.findViewById(R.id.bio);
        toggleFollow = itemView.findViewById(R.id.toggle_follow_btn);
    }

    @SuppressLint("SetTextI18n")
    public void bind(Student student, String currentUserId, String currentUserRole) {
        fullName.setText(student.getFirstName() + " " + student.getLastName());
        username.setText(student.getUsername());
        university.setText("University: " + student.getUniversity());
        major.setText("Major: " + student.getMajor());
        bio.setText(student.getBio());
        checkFollowStatus(student, currentUserId, currentUserRole);
        toggleFollow.setTag(student);
    }

    private void checkFollowStatus(Student student, String currentUserId, String currentUserRole) {
        Query checkFollowStatusQuery;
        if (currentUserRole.equals("Student")) {
            checkFollowStatusQuery = FirebaseDatabase.getInstance().getReference()
                    .child("Students").child(currentUserId).child("Following").child("Students")
                    .orderByChild("id").equalTo(student.getId());
        } else {
            checkFollowStatusQuery = FirebaseDatabase.getInstance().getReference()
                    .child("Companies").child(currentUserId).child("Following").child("Students")
                    .orderByChild("id").equalTo(student.getId());
        }

        checkFollowStatusQuery.addListenerForSingleValueEvent(checkFollowStatusListener);
    }

    ValueEventListener checkFollowStatusListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            if (dataSnapshot.exists()) {
                updateToggleBtnBackground();
            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) { }
    };

    private void updateToggleBtnBackground() {
        toggleFollow.setBackgroundResource(R.drawable.ic_check_black_24dp);
    }
}
