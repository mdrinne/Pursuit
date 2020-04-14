package com.example.pursuit;

import android.content.Context;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.pursuit.models.Message;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MessageListAdapter extends RecyclerView.Adapter {

    private static final String TAG = "MESSAGE_LIST_ADAPTER";

    private static final int VIEW_TYPE_MESSAGE_SENT = 1;
    private static final int VIEW_TYPE_MESSAGE_RECEIVED = 2;

    private Context context;
    private ArrayList<Message> messageList;
    private String currentUserId;

    public MessageListAdapter(Context context, ArrayList<Message> messageList, String currentUserId) {
        this.context = context;
        this.messageList = messageList;
        this.currentUserId = currentUserId;
    }

    @Override
    public int getItemViewType(int position) {
        Message message = messageList.get(position);
        Log.d(TAG, "getting itemviewtype");
        if (message == null) {
            Log.d(TAG, "message is null");
        }
        Log.d("LIST_SIZE", Integer.toString(messageList.size()));
        Log.d("POSITION", Integer.toString(position));
        String id = message.getId();
        if (id == null) {
            Log.d(TAG, "id is NULL");
        } else {
            Log.d(TAG, id);
        }

        if (message.getSenderId().equals(currentUserId)) {
            return VIEW_TYPE_MESSAGE_SENT;
        } else {
            return VIEW_TYPE_MESSAGE_RECEIVED;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;

        if (viewType == VIEW_TYPE_MESSAGE_SENT) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_message_sent, parent, false);
            return new SentMessageHolder(view);
        } else {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_message_received, parent, false);
            return new ReceivedMessageHolder(view);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Message message = messageList.get(position);

        if (message.getSenderId().equals(currentUserId)) {
            // the current user sent this message
            ((SentMessageHolder) holder).bind(message);
        } else {
            // the other user sent this message
            ((ReceivedMessageHolder) holder).bind(message);
        }
    }

    @Override
    public int getItemCount() {
        Log.d("ITEM_COUNT", Integer.toString(messageList.size()));
        return messageList.size();
    }

}
