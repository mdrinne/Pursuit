package com.example.pursuit;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pursuit.models.Share;

public class PostHolder extends RecyclerView.ViewHolder {

    private TextView userFullName, userUsername, subject, message;

    public PostHolder(@NonNull View itemView) {
        super(itemView);
        userFullName = itemView.findViewById(R.id.userFullName);
        userUsername = itemView.findViewById(R.id.userUsername);
        subject = itemView.findViewById(R.id.subject);
        message = itemView.findViewById(R.id.message);
    }

    public void bind(Share share) {
        userFullName.setText(share.getUserFullName());
        userUsername.setText(share.getUserUsername());
        subject.setText(share.getSubject());
        message.setText(share.getMessage());
    }
}
