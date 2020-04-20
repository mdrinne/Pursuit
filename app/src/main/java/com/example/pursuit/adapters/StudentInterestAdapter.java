package com.example.pursuit.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.pursuit.R;
import com.example.pursuit.models.Student;
import com.google.firebase.database.annotations.NotNull;

import java.util.ArrayList;

public class StudentInterestAdapter extends RecyclerView.Adapter<StudentInterestAdapter.StudentInterestViewHolder> {

    ArrayList<String> studentInterests;
//    Student currentStudent;

    private StudentInterestOnItemClickListener mListener;

    public interface StudentInterestOnItemClickListener {
        void onDeleteClick(int position);
    }

    public void setStudentInterestOnItemClickListener(StudentInterestOnItemClickListener listener) {
        mListener = listener;
    }

    public class StudentInterestViewHolder extends RecyclerView.ViewHolder {
        TextView studentInterest;
        ImageView deleteIcon;

        public StudentInterestViewHolder(@NotNull View itemView, final StudentInterestOnItemClickListener listener) {
            super(itemView);
            studentInterest = itemView.findViewById(R.id.txtKeyword);
            deleteIcon = itemView.findViewById(R.id.imgDeleteIcon);

            deleteIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onDeleteClick(position);
                        }
                    }
                }
            });
        }
    }

    public StudentInterestAdapter(ArrayList<String> interests) {
        studentInterests = interests;
    }

    @NotNull
    @Override
    public StudentInterestViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.keyword_row, parent, false);
        return new StudentInterestViewHolder(v, mListener);
    }

    @Override
    public void onBindViewHolder(@NotNull StudentInterestViewHolder holder, int position) {
        holder.studentInterest.setText(studentInterests.get(position));
    }

    @Override
    public int getItemCount() {
        return studentInterests.size();
    }
}
