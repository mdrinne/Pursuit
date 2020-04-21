package com.example.pursuit;

import android.annotation.SuppressLint;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pursuit.models.Student;

public class UserHolder extends RecyclerView.ViewHolder {

    private TextView fullName, username, major, university, bio;

    public UserHolder(@NonNull View itemView) {
        super(itemView);
        fullName = itemView.findViewById(R.id.full_name);
        username = itemView.findViewById(R.id.username);
        university = itemView.findViewById(R.id.university);
        major = itemView.findViewById(R.id.major);
        bio = itemView.findViewById(R.id.bio);
    }

    @SuppressLint("SetTextI18n")
    public void bind(Student student) {
        fullName.setText(student.getFirstName() + " " + student.getLastName());
        username.setText(student.getUsername());
        university.setText("University: " + student.getUniversity());
        major.setText("Major: " + student.getMajor());
        bio.setText(student.getBio());
    }
}
