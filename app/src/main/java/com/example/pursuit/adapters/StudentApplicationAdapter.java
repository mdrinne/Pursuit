package com.example.pursuit.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pursuit.R;
import com.example.pursuit.models.CompanyOpportunity;
import com.google.firebase.database.annotations.NotNull;

import java.util.ArrayList;

public class StudentApplicationAdapter extends RecyclerView.Adapter<StudentApplicationAdapter.StudentApplicationViewHolder> {

    ArrayList<CompanyOpportunity> opportunities;

    private StudentApplicationOnItemClickListener mListener;

    public interface StudentApplicationOnItemClickListener {
        void onDeleteClick(int position);
        void onCardClick(int position);
    }

    public class StudentApplicationViewHolder extends RecyclerView.ViewHolder {
        TextView companyName, position, description, with, city, state;
        ImageView imgDelete;
        CardView card;

        public StudentApplicationViewHolder(@NotNull View itemView, final StudentApplicationOnItemClickListener listener) {
            super(itemView);
            companyName = itemView.findViewById(R.id.txtUniversity);
            position = itemView.findViewById(R.id.txtMajor);
            description = itemView.findViewById(R.id.txtDescription);
            with = itemView.findViewById(R.id.txtWith);
            imgDelete = itemView.findViewById(R.id.imgDelete);
            city = itemView.findViewById(R.id.txtKeyword);
            state = itemView.findViewById(R.id.txtMinimumGPA);
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

            imgDelete.setOnClickListener(new View.OnClickListener() {
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

    public StudentApplicationAdapter(ArrayList<CompanyOpportunity> opps) {
        opportunities = opps;
    }

    @NotNull
    @Override
    public StudentApplicationViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.student_application_row, parent, false);
        return new StudentApplicationViewHolder(v, mListener);
    }

    @Override
    public void onBindViewHolder(@NotNull StudentApplicationViewHolder holder, int position) {
        CompanyOpportunity currentOpportunity = opportunities.get(position);
        holder.companyName.setText(currentOpportunity.getCompanyName());
        holder.position.setText(currentOpportunity.getPosition());
        holder.city.setText(currentOpportunity.getCity() + ", ");
        holder.state.setText(currentOpportunity.getState());
        if (!currentOpportunity.getWithWho().equals("")) {
            holder.with.setText("With: " + currentOpportunity.getWithWho());
        }
        holder.description.setText(currentOpportunity.getDescription());
    }

    @Override
    public int getItemCount() {
        return opportunities.size();
    }
}
