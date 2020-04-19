package com.example.pursuit;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.ArrayList;

public class UsernameAdapter extends ArrayAdapter<String> implements Filterable {
    private String TAG = "UsernameAdapter";

    private ArrayList<String> usernames = new ArrayList<>();
    private ArrayList<String> storedUsernames;


    public UsernameAdapter(Context context, ArrayList<String> usernames) {
        super(context, R.layout.username, usernames);
        Log.d(TAG, "in UsernameAdapter constructor");
        Log.d("SIZE", Integer.toString(usernames.size()));
        this.storedUsernames = usernames;
    }

    public void add(String username) {
        this.usernames.add(username);
        notifyDataSetChanged();
    }

    public void addAll(ArrayList<String> usernames) {
        this.usernames.addAll(usernames);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return usernames.size();
    }

    @Override
    public String getItem(int i) {
        return usernames.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup parent) {
        String username = getItem(i);
        Log.d(TAG, username);

        UsernameViewHolder viewHolder;

        if (convertView == null) {
            viewHolder = new UsernameViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.username, parent, false);
            viewHolder.username = convertView.findViewById(R.id.username);
            convertView.setTag(viewHolder);

        } else {
            viewHolder = (UsernameViewHolder) convertView.getTag();
        }

        assert username != null;

        viewHolder.username.setText(username);
        @SuppressLint("CutPasteId") TextView usernameTextView = convertView.findViewById(R.id.username);
        usernameTextView.setTag(username);

        return convertView;
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults filterResults = new FilterResults();
                ArrayList<String> temp = new ArrayList<>();
                constraint = constraint.toString().toLowerCase();

                if (constraint.toString().length() == 0) {
                    temp = storedUsernames;
                } else {
                    for (int i = 0; i < usernames.size(); i++) {
                        String current = usernames.get(i);
                        if (current.contains(constraint.toString())) {
                            temp.add(usernames.get(i));
                        }
                    }
                }

                filterResults.count = temp.size();
                filterResults.values = temp;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                usernames = (ArrayList<String>) results.values;
                notifyDataSetChanged();
            }
        };
    }
}

class UsernameViewHolder {
    public TextView username;
}
