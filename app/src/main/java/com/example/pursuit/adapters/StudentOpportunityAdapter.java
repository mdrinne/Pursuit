package com.example.pursuit.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pursuit.R;
import com.example.pursuit.models.CompanyOpportunity;
import com.google.firebase.database.annotations.NotNull;

import java.time.Duration;
import java.time.Period;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.ArrayList;

public class StudentOpportunityAdapter extends RecyclerView.Adapter<StudentOpportunityAdapter.StudentOpportunityViewHolder> {

    ArrayList<CompanyOpportunity> matchedOpportunities;

    private StudentOpportunityOnItemClickListener mListener;

    public interface StudentOpportunityOnItemClickListener {
        void onCardClick(int position);
    }

    public void setStudentOpportunityOnItemClickListener (StudentOpportunityOnItemClickListener listener) {
        mListener = listener;
    }

    public class StudentOpportunityViewHolder extends RecyclerView.ViewHolder {
        TextView companyName, position, description, with, timeStamp, city, state;
        CardView card;

        public StudentOpportunityViewHolder(@NotNull View itemView, final StudentOpportunityOnItemClickListener listener) {
            super(itemView);
            companyName = itemView.findViewById(R.id.txtCompanyName);
            position = itemView.findViewById(R.id.txtPosition);
            description = itemView.findViewById(R.id.txtDescription);
            with = itemView.findViewById(R.id.txtWith);
            timeStamp = itemView.findViewById(R.id.txtTimeStamp);
            city = itemView.findViewById(R.id.txtCity);
            state = itemView.findViewById(R.id.txtState);
            card = itemView.findViewById(R.id.crdStudentOpportunity);

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

    public StudentOpportunityAdapter(ArrayList<CompanyOpportunity> opportunities) {
        matchedOpportunities = opportunities;
    }

    @NotNull
    @Override
    public StudentOpportunityViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.student_opportunity_row, parent, false);
        return new StudentOpportunityViewHolder(v, mListener);
    }

    @Override
    public void onBindViewHolder(@NotNull StudentOpportunityViewHolder holder, int position) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            ZonedDateTime now = ZonedDateTime.now(ZoneOffset.UTC);
            ZonedDateTime approvedTime = ZonedDateTime.parse(matchedOpportunities.get(position).getTimeStamp());
            Period ymdDiff = Period.between(approvedTime.toLocalDate(), now.toLocalDate());
            Duration hmsDiff = Duration.between(approvedTime.toLocalDateTime(), now.toLocalDateTime());
            Long hmsSeconds = hmsDiff.getSeconds();
            if (hmsSeconds < 60) {
                holder.timeStamp.setText(hmsSeconds.toString() + "s");
            } else if (hmsSeconds < 3600){
                Long minutes = hmsSeconds / 60;
                holder.timeStamp.setText(minutes.toString() + "m");
            } else if (hmsSeconds < 86400) {
                Long hours = hmsSeconds / 3600;
                holder.timeStamp.setText(hours.toString() + "h");
            } else if (ymdDiff.getYears() > 0) {
                holder.timeStamp.setText(ymdDiff.getYears() + "Y");
            } else if (ymdDiff.getMonths() > 0) {
                holder.timeStamp.setText(ymdDiff.getMonths() + "M");
            } else {
                holder.timeStamp.setText(ymdDiff.getDays() + "D");
            }
        }

        CompanyOpportunity currentOpportunity = matchedOpportunities.get(position);
        holder.companyName.setText(currentOpportunity.getCompanyName());
        holder.position.setText(currentOpportunity.getPosition());
        holder.city.setText(currentOpportunity.getCity() + ", ");
        holder.city.setText(currentOpportunity.getState());
        if (!currentOpportunity.getWithWho().equals("")) {
            holder.with.setText("With: " + currentOpportunity.getWithWho());
        }
        holder.description.setText(currentOpportunity.getDescription());
    }

    @Override
    public int getItemCount() {
        return matchedOpportunities.size();
    }

}
