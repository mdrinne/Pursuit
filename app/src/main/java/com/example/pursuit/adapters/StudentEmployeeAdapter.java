package com.example.pursuit.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.pursuit.R;
import com.example.pursuit.models.Employee;
import com.google.firebase.database.annotations.NotNull;

import java.util.ArrayList;

public class StudentEmployeeAdapter extends RecyclerView.Adapter<StudentEmployeeAdapter.StudentEmployeeViewHolder> {

    ArrayList<Employee> companyEmployees;

    private StudentEmployeeOnItemClickListener mListener;

    public interface StudentEmployeeOnItemClickListener {

    }

    public void setStudentEmployeeOnItemClickListener(StudentEmployeeOnItemClickListener listener) {
        mListener = listener;
    }

    public class StudentEmployeeViewHolder extends RecyclerView.ViewHolder {
        TextView name, username, position, email;

        public StudentEmployeeViewHolder(@NotNull View itemView, final StudentEmployeeOnItemClickListener listener) {
            super(itemView);
            name = itemView.findViewById(R.id.txtEmployeeName);
            username = itemView.findViewById(R.id.txtEmployeeUsername);
            position = itemView.findViewById(R.id.txtEmployeePosition);
            email = itemView.findViewById(R.id.txtEmployeeEmail);
        }

    }

    public StudentEmployeeAdapter(ArrayList<Employee> employees) {
        companyEmployees = employees;
    }

    @NotNull
    @Override
    public StudentEmployeeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.employee_row, parent, false);
        return new StudentEmployeeViewHolder(v, mListener);
    }

    @Override
    public void onBindViewHolder(@NotNull StudentEmployeeViewHolder holder, int position) {
        Employee currentEmp = companyEmployees.get(position);
        holder.name.setText(currentEmp.getFirstName() + " " + currentEmp.getLastName());
        holder.position.setText(currentEmp.getPassword());
        holder.username.setText(currentEmp.getUsername());
        holder.email.setText(currentEmp.getEmail());
    }

    @Override
    public int getItemCount() {
        return companyEmployees.size();
    }
}
