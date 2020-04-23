package com.example.pursuit.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.pursuit.R;
import com.example.pursuit.models.Employee;
import com.google.firebase.database.annotations.NotNull;

import java.util.ArrayList;

public class CompanyKeywordAdapter extends RecyclerView.Adapter<CompanyKeywordAdapter.CompanyKeywordViewHolder> {

    ArrayList<String> companyKeywords;
    Employee currentEmployee;

    private CompanyKeywordOnItemListener mListener;

    public interface CompanyKeywordOnItemListener {
        void onDeleteClick(int position);
    }

    public void setCompanyKeywordOnItemClickListener(CompanyKeywordOnItemListener listener) {
        mListener = listener;
    }

    public class CompanyKeywordViewHolder extends RecyclerView.ViewHolder {

        TextView companyKeyword;
        ImageView deleteIcon;

        public CompanyKeywordViewHolder(@NotNull View itemView, final CompanyKeywordOnItemListener listener) {
            super(itemView);
            companyKeyword = itemView.findViewById(R.id.txtMinor);
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

    public CompanyKeywordAdapter(ArrayList<String> keywords, Employee currEmployee) {
        companyKeywords = keywords;
        currentEmployee = currEmployee;
    }

    @NotNull
    @Override
    public CompanyKeywordViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.keyword_row, parent, false);
        return new CompanyKeywordViewHolder(v, mListener);
    }

    @Override
    public void onBindViewHolder(@NotNull CompanyKeywordViewHolder holder, int position) {
        holder.companyKeyword.setText(companyKeywords.get(position));
        if (currentEmployee != null && currentEmployee.getAdmin() == 0) {
            holder.deleteIcon.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return companyKeywords.size();
    }
}
