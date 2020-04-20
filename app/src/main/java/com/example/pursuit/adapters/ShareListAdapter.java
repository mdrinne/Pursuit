package com.example.pursuit.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pursuit.PostHolder;
import com.example.pursuit.R;
import com.example.pursuit.models.Share;

import java.util.ArrayList;

public class ShareListAdapter extends RecyclerView.Adapter {
    private static final String TAG = "ShareListAdapter";

    private static final int VIEW_TYPE_POST = 1;
    private static final int VIEW_TYPE_SHADOW = 2;

    private Context context;
    private ArrayList<Share> shareList;

    public ShareListAdapter(Context context, ArrayList<Share> shareList) {
        this.context = context;
        this.shareList = shareList;
    }

    @Override
    public int getItemViewType(int position) {
        Share share = shareList.get(position);

        if (share.getType().equals("Post")) {
            return VIEW_TYPE_POST;
        } else {
            return VIEW_TYPE_SHADOW;
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_post, parent, false);
        return new PostHolder(view);

//        if (viewType == VIEW_TYPE_POST) {

//        }
//        else {
//            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_shadow, parent, false);
//            return new ShadowHolder(view);
//        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Share share = shareList.get(position);

        if (share.getType().equals("Post")) {
            ((PostHolder) holder).bind(share);
        }
//        else {
//            ((ShadowHolder) holder).bind(share);
//        }
    }

    @Override
    public int getItemCount() {
        return shareList.size();
    }
}
