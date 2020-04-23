package com.example.pursuit.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.pursuit.R;
import com.google.firebase.database.annotations.NotNull;

import java.util.ArrayList;

public class StudentOpportunityKeywordAdapter extends RecyclerView.Adapter<StudentOpportunityKeywordAdapter.StudentOpportunityKeywordViewHolder> {
    ArrayList<String> keywords;

    private StudentOpportunityKeywordOnItemClickListener mListener;

    public interface StudentOpportunityKeywordOnItemClickListener {

    }

    public void StudentOpportunityKeywordOnItemClickListener(StudentOpportunityKeywordOnItemClickListener listener) {
        mListener = listener;
    }

    public class StudentOpportunityKeywordViewHolder extends RecyclerView.ViewHolder {
        TextView keyword;
        ImageView deleteIcon;

        public StudentOpportunityKeywordViewHolder(@NotNull View itemView, StudentOpportunityKeywordOnItemClickListener listener) {
            super(itemView);
            keyword = itemView.findViewById(R.id.txtMinor);
            deleteIcon = itemView.findViewById(R.id.imgDeleteIcon);
        }
    }

    public StudentOpportunityKeywordAdapter(ArrayList<String> keys) {
        keywords = keys;
    }

    @NotNull
    @Override
    public StudentOpportunityKeywordViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.keyword_row, parent, false);
        return new StudentOpportunityKeywordViewHolder(v, mListener);
    }

    @Override
    public void onBindViewHolder(@NotNull StudentOpportunityKeywordViewHolder holder, int position) {
        holder.keyword.setText(keywords.get(position));
        holder.keyword.setPadding(0,0,25,0);
        holder.deleteIcon.setVisibility(View.GONE);
    }

    @Override
    public int getItemCount() {
        return keywords.size();
    }

}
