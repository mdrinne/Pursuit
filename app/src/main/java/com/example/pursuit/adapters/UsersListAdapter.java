package com.example.pursuit.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pursuit.UserHolder;
import com.example.pursuit.R;
import com.example.pursuit.models.Student;

import java.util.ArrayList;

public class UsersListAdapter extends RecyclerView.Adapter implements Filterable {
    private static final String TAG = "UsersListAdapter";

    private Context context;
    private ArrayList<Student> usersList;
    private ArrayList<Student> originalList;

    public UsersListAdapter(Context context, ArrayList<Student> usersList) {
        this.context = context;
        this.usersList = usersList;
        this.originalList = usersList;
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

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults filterResults = new FilterResults();
                ArrayList<Student> temp = new ArrayList<>();
                constraint = constraint.toString().toLowerCase();

                if (constraint.toString().length() == 0) {
                    temp = originalList;
                } else {
                    for (int i=0; i<usersList.size(); i++) {
                        Student tempStudent = usersList.get(i);
                        String fullName = tempStudent.getFirstName().toLowerCase() + " " + tempStudent.getLastName().toLowerCase();
                        String username = tempStudent.getUsername().toLowerCase();
                        String university = tempStudent.getUniversity().toLowerCase();
                        String major = tempStudent.getMajor().toLowerCase();
                        if (fullName.contains(constraint.toString()) || username.contains(constraint.toString()) ||
                                university.contains(constraint.toString()) || major.contains(constraint.toString())) {
                            temp.add(tempStudent);
                        }
                    }
                }

                filterResults.count = temp.size();
                filterResults.values = temp;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                usersList = (ArrayList<Student>) results.values;
                notifyDataSetChanged();
            }
        };
    }
}
