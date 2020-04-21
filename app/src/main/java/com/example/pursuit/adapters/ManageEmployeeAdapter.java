package com.example.pursuit.adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pursuit.R;
import com.example.pursuit.models.Employee;
import com.google.firebase.database.annotations.NotNull;

import java.util.ArrayList;

public class ManageEmployeeAdapter extends RecyclerView.Adapter<ManageEmployeeAdapter.ManageEmployeeViewHolder> {

    ArrayList<Employee> companyEmployees;
    Employee currentEmployee;

    private ManageEmployeeOnItemClickListener mListener;

    public interface ManageEmployeeOnItemClickListener {
        void onDeleteClick(int position);
        void makeAdmin(int position);
        void revokeAdmin(int position);
    }

    public void setManageEmployeeOnItemClickListener(ManageEmployeeOnItemClickListener listener) {
        mListener = listener;
    }

    public class ManageEmployeeViewHolder extends RecyclerView.ViewHolder {
        TextView employeeName, employeePosition, employeeEmail;
        ImageView deleteEmployee;
        Button makeAdmin, revokeAdmin;

        public ManageEmployeeViewHolder(@NotNull View itemView, final ManageEmployeeOnItemClickListener listener) {
            super(itemView);
            employeeName = itemView.findViewById(R.id.txtEmployeeName);
            employeePosition = itemView.findViewById(R.id.txtEmployeePosition);
            employeeEmail = itemView.findViewById(R.id.txtEmployeeEmail);
            deleteEmployee = itemView.findViewById(R.id.imgDeleteEmployee);
            makeAdmin = itemView.findViewById(R.id.btnMakeAdmin);
            revokeAdmin = itemView.findViewById(R.id.btnRevokeAdmin);

            deleteEmployee.setOnClickListener( new View.OnClickListener() {
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

            makeAdmin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.makeAdmin(position);
                        }
                    }
                }
            });

            revokeAdmin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.revokeAdmin(position);
                        }
                    }
                }
            });
        }
    }

    public ManageEmployeeAdapter(ArrayList<Employee> employees, Employee currEmployee) {
        companyEmployees = employees;
        currentEmployee = currEmployee;
    }

    @NonNull
    @Override
    public ManageEmployeeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.manage_emplyee_rows, parent, false);
        ManageEmployeeViewHolder mevh = new ManageEmployeeViewHolder(v, mListener);
        return mevh;
    }

    @Override
    public void onBindViewHolder(@NonNull ManageEmployeeViewHolder holder, int position) {
        String employeeFullName = companyEmployees.get(position).getFirstName() + " " + companyEmployees.get(position).getLastName();
        holder.employeeName.setText(employeeFullName);
        holder.employeePosition.setText(companyEmployees.get(position).getPosition());
        holder.employeeEmail.setText(companyEmployees.get(position).getEmail());
        holder.makeAdmin.setVisibility(View.GONE);
        holder.revokeAdmin.setVisibility(View.GONE);
        if (currentEmployee != null && currentEmployee.getAdmin() == 0) {
            holder.deleteEmployee.setVisibility(View.GONE);
        } else {
            if (companyEmployees.get(position).getAdmin() == 1) {
                holder.revokeAdmin.setVisibility(View.VISIBLE);
            } else {
                holder.makeAdmin.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public int getItemCount() {
        return companyEmployees.size();
    }

}