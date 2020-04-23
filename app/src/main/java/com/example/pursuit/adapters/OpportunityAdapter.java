package com.example.pursuit.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pursuit.R;
import com.example.pursuit.models.CompanyOpportunity;

import java.time.Duration;
import java.time.Period;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.ArrayList;

public class OpportunityAdapter extends RecyclerView.Adapter<OpportunityAdapter.OpportunityViewHolder> {

    ArrayList<CompanyOpportunity> companyOpportunities;

    private OpportunityOnItemClickListener mListener;

    public interface OpportunityOnItemClickListener {
        void onApproveClick(int position);
        void onDeleteClick(int position);
        void onCardClick(int position);
    }

    public void setOpportunityOnItemClickListener(OpportunityOnItemClickListener listener) {
        mListener = listener;
    }

    public class OpportunityViewHolder extends RecyclerView.ViewHolder {
        TextView opportunityPosition, opportunityDescription, opportunityWith, opportunityTimeStamp;
        TextView opportunityCity, opportunityState;
        ImageView deleteOpportunity;
        Button approve;
        CardView card;

        public OpportunityViewHolder(@NonNull View itemView, final OpportunityOnItemClickListener listener) {
            super(itemView);
            opportunityPosition = itemView.findViewById(R.id.txtMajor);
            opportunityWith = itemView.findViewById(R.id.txtWith);
            opportunityDescription = itemView.findViewById(R.id.txtDescription);
            opportunityTimeStamp = itemView.findViewById(R.id.txtTimeStamp);
            approve = itemView.findViewById(R.id.btnApprove);
            deleteOpportunity = itemView.findViewById(R.id.imgDeleteOpportunity);
            opportunityCity = itemView.findViewById(R.id.txtKeyword);
            opportunityState = itemView.findViewById(R.id.txtMinimumGPA);
            card = itemView.findViewById(R.id.crdStudent);

            approve.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onApproveClick(position);
                        }
                    }
                }
            });

            deleteOpportunity.setOnClickListener(new View.OnClickListener() {
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
        if (companyOpportunities.get(position).getApproved() == 1) {
            holder.approve.setVisibility(View.GONE);
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                ZonedDateTime now = ZonedDateTime.now(ZoneOffset.UTC);
                ZonedDateTime approvedTime = ZonedDateTime.parse(companyOpportunities.get(position).getTimeStamp());
                Period ymdDiff = Period.between(approvedTime.toLocalDate(), now.toLocalDate());
                Duration hmsDiff = Duration.between(approvedTime.toLocalDateTime(), now.toLocalDateTime());
                Long hmsSeconds = hmsDiff.getSeconds();
                if (hmsSeconds < 60) {
                    holder.opportunityTimeStamp.setText(hmsSeconds.toString() + "s");
                } else if (hmsSeconds < 3600){
                    Long minutes = hmsSeconds / 60;
                    holder.opportunityTimeStamp.setText(minutes.toString() + "m");
                } else if (hmsSeconds < 86400) {
                    Long hours = hmsSeconds / 3600;
                    holder.opportunityTimeStamp.setText(hours.toString() + "h");
                } else if (ymdDiff.getYears() > 0) {
                    holder.opportunityTimeStamp.setText(ymdDiff.getYears() + "Y");
                } else if (ymdDiff.getMonths() > 0) {
                    holder.opportunityTimeStamp.setText(ymdDiff.getMonths() + "M");
                } else {
                    holder.opportunityTimeStamp.setText(ymdDiff.getDays() + "D");
                }
            }
        }
        holder.opportunityPosition.setText(companyOpportunities.get(position).getPosition());
        holder.opportunityCity.setText(companyOpportunities.get(position).getCity() + ", ");
        holder.opportunityState.setText(companyOpportunities.get(position).getState());
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
