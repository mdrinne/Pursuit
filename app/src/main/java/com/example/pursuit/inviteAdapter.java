package com.example.pursuit;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pursuit.models.EmployeeInvite;

import java.util.ArrayList;

public class inviteAdapter extends RecyclerView.Adapter<inviteAdapter.MyViewHolder> {

    ArrayList<EmployeeInvite> employeeInvites;

    private OnItemClickListener mListener;

    public interface OnItemClickListener {
        void onDeleteClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        TextView employeeEmail, code;
        ImageView deleteInvite;

        public MyViewHolder(@NonNull View itemView, final OnItemClickListener listener) {
            super(itemView);
            employeeEmail = itemView.findViewById(R.id.txtCompanyCode);
            code = itemView.findViewById(R.id.txtCode);
            deleteInvite = itemView.findViewById(R.id.imgDeleteInvite);

            deleteInvite.setOnClickListener(new View.OnClickListener() {
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


    public inviteAdapter(ArrayList<EmployeeInvite> invites) {
    employeeInvites = invites;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.invite_rows, parent, false);
        MyViewHolder mvh = new MyViewHolder(v, mListener);
        return mvh;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.employeeEmail.setText(employeeInvites.get(position).getEmployeeEmail());
        holder.code.setText(employeeInvites.get(position).getCode());
    }

    @Override
    public int getItemCount() {
        return employeeInvites.size();
    }

}