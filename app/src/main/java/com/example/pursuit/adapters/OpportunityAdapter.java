package com.example.pursuit.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pursuit.R;
import com.example.pursuit.models.CompanyOpportunity;

import java.util.ArrayList;

public class OpportunityAdapter extends RecyclerView.Adapter<OpportunityAdapter.OpportunityViewHolder> {

    ArrayList<CompanyOpportunity> companyOpportunities;

    private OpportunityOnItemClickListener mListener;

    public interface OpportunityOnItemClickListener {

    }

    public void setOpportunityOnItemClickListener(OpportunityOnItemClickListener listener) {
        mListener = listener;
    }

    public class OpportunityViewHolder extends RecyclerView.ViewHolder {
        TextView opportunityPosition, opportunityDescription, opportunityWith;
        ImageView deleteOpportunity;
        Button approve;

        public OpportunityViewHolder(@NonNull View itemView, final OpportunityOnItemClickListener listener) {
            super(itemView);
            opportunityPosition = itemView.findViewById(R.id.txtPosition);
            opportunityWith = itemView.findViewById(R.id.txtWith);
            opportunityDescription = itemView.findViewById(R.id.txtDescription);
        }
    }

    public OpportunityAdapter(ArrayList<CompanyOpportunity> opportunities) {
        companyOpportunities = opportunities;
    }

    @NonNull
    @Override
    public OpportunityViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.opportunity_rows, parent, false);
        OpportunityViewHolder ovh = new OpportunityViewHolder(v, mListener);
        return ovh;
    }

    @Override
    public void onBindViewHolder(@NonNull OpportunityViewHolder holder, int position) {
        holder.opportunityPosition.setText(companyOpportunities.get(position).getPosition());
        if (!companyOpportunities.get(position).getWithWho().equals("")) {
            holder.opportunityWith.setText("With: " + companyOpportunities.get(position).getWithWho());
        }
        holder.opportunityDescription.setText(companyOpportunities.get(position).getDescription());
    }

    @Override
    public int getItemCount() {
        return companyOpportunities.size();
    }

}
