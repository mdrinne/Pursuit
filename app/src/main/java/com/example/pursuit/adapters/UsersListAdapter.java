package com.example.pursuit.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pursuit.UserHolder;
import com.example.pursuit.R;
import com.example.pursuit.models.Student;

import java.util.ArrayList;

public class UsersListAdapter extends RecyclerView.Adapter {
    private static final String TAG = "UsersListAdapter";

    private Context context;
    private ArrayList<Student> usersList;

    public UsersListAdapter(Context context, ArrayList<Student> usersList) {
        this.context = context;
        this.usersList = usersList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user, parent, false);
        return new UserHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Student student = usersList.get(position);
        ((UserHolder) holder).bind(student);
    }

    @Override
    public int getItemCount() {
        return usersList.size();
    }
}
