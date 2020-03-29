package com.example.pursuit;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pursuit.models.EmployeeInvite;

import java.util.ArrayList;

public class inviteAdapter extends RecyclerView.Adapter<inviteAdapter.MyViewHolder> {

    ArrayList<EmployeeInvite> employeeInvites;
    Context context;

    public inviteAdapter(Context ct, ArrayList<EmployeeInvite> invites) {
    context = ct;
    employeeInvites = invites;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.invite_rows, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.employeeEmail.setText(employeeInvites.get(position).getEmployeeEmail());
        holder.code.setText(employeeInvites.get(position).getCode());
    }

    @Override
    public int getItemCount() {
        Log.d("getItemCount", "invite count: " + employeeInvites.size());
        return employeeInvites.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        TextView employeeEmail, code;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            employeeEmail = itemView.findViewById(R.id.txtEmployeeEmail);
            code = itemView.findViewById(R.id.txtCode);
        }
    }
}
