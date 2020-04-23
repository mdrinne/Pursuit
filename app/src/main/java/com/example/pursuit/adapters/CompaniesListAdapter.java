package com.example.pursuit.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pursuit.CompanyHolder;
import com.example.pursuit.R;
import com.example.pursuit.UserHolder;
import com.example.pursuit.models.Company;
import com.example.pursuit.models.Student;

import java.util.ArrayList;

public class CompaniesListAdapter extends RecyclerView.Adapter implements Filterable {
    private static final String TAG = "CompaniesAdapter";

    private Context context;
    private ArrayList<Company> companiesList;
    private ArrayList<Company> originalList;
    private String currentUserId;
    private String currentUserRole;

    public CompaniesListAdapter(Context context, ArrayList<Company> companiesList, String currentUserId, String currentUserRole) {
        this.context = context;
        this.companiesList = companiesList;
        this.originalList = companiesList;
        this.currentUserId = currentUserId;
        this.currentUserRole = currentUserRole;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_company, parent, false);
        return new CompanyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Company company = companiesList.get(position);
        ((CompanyHolder) holder).bind(company, currentUserId, currentUserRole);
    }

    @Override
    public int getItemCount() {
        return companiesList.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults filterResults = new FilterResults();
                ArrayList<Company> temp = new ArrayList<>();
                constraint = constraint.toString().toLowerCase();

                if (constraint.toString().length() == 0) {
                    temp = originalList;
                } else {
                    for (int i=0; i<companiesList.size(); i++) {
                        Company tempCompany = companiesList.get(i);
                        String name = tempCompany.getName();
                        String field = tempCompany.getField();
                        if (name.contains(constraint.toString()) || field.contains(constraint.toString())) {
                            temp.add(tempCompany);
                        }
                    }
                }

                filterResults.count = temp.size();
                filterResults.values = temp;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                companiesList = (ArrayList<Company>) results.values;
                notifyDataSetChanged();
            }
        };
    }
}
