package com.example.pursuit.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pursuit.R;
import com.example.pursuit.models.Student;
import com.google.firebase.database.annotations.NotNull;

import java.util.ArrayList;

public class StudentAdapter extends RecyclerView.Adapter<StudentAdapter.StudentViewHolder> {

    ArrayList<Student> students;

    private StudentOnItemClickListener mListener;

    public interface StudentOnItemClickListener {
        void onCardClick(int position);
    }

    public void setStudentOnItemClickListener(StudentOnItemClickListener listener) {
        mListener = listener;
    }

    public class StudentViewHolder extends RecyclerView.ViewHolder {
        TextView name, username, university, major, minor, gpa;
        CardView card;

        public StudentViewHolder(@NotNull View itemView, final StudentOnItemClickListener listener) {
            super(itemView);
            name = itemView.findViewById(R.id.txtStudentName);
            username = itemView.findViewById(R.id.txtStudentUsername);
            university = itemView.findViewById(R.id.txtStudentUniversity);
            major = itemView.findViewById(R.id.txtSudentMajor);
            minor = itemView.findViewById(R.id.txtStudentMinor);
            gpa = itemView.findViewById(R.id.txtStudentGPA);
            card = itemView.findViewById(R.id.crdStudent);

            card.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onCardClick(position);
                        }
                    }
                }
            });
        }
    }

    public StudentAdapter(ArrayList<Student> studs) {
        students = studs;
    }

    @NotNull
    @Override
    public StudentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.student_row, parent, false);
        return new StudentViewHolder(v, mListener);
    }

    @Override
    public void onBindViewHolder(@NotNull StudentViewHolder holder, int position) {
        Student currentStudent = students.get(position);
        holder.name.setText(currentStudent.getFirstName() + " " + currentStudent.getLastName());
        holder.username.setText(currentStudent.getUsername());
        holder.university.setText(currentStudent.getUniversity());
        holder.major.setText(currentStudent.getMajor());
        holder.minor.setText(currentStudent.getMinor());
        holder.gpa.setText(currentStudent.getGpa());
    }

    @Override
    public int getItemCount() {
        return students.size();
    }

}
