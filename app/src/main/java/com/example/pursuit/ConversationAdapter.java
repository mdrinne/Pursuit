package com.example.pursuit;

import android.util.Log;
import android.view.LayoutInflater;
import android.widget.ArrayAdapter;
import android.content.Context;
import android.widget.TextView;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.Query;

import com.example.pursuit.models.Conversation;

import java.util.ArrayList;

public class ConversationAdapter extends ArrayAdapter<Conversation> {
    private String TAG = "ConversationAdapter";

    private ArrayList<Conversation> conversations = new ArrayList<>();

    private DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference();

    public ConversationAdapter(Context context, ArrayList<Conversation> conversations) {
        super(context, R.layout.conversation, conversations);
        Log.d(TAG, "in ConversationAdapter constructor");
    }

    public void add(Conversation conversation) {
        Log.d(TAG, "adding to the adapter");
        this.conversations.add(conversation);
        notifyDataSetChanged();
    }

    public void addAll(ArrayList<Conversation> conversations) {
        Log.d(TAG, "adding all");
        for (int i = 0; i < conversations.size(); i++) {
            this.conversations.add(conversations.get(i));
        }
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return conversations.size();
    }

    @Override
    public Conversation getItem(int i) {
        return conversations.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup parent) {
        Log.d(TAG, "in getView");
        Conversation conversation = getItem(i);
        if (conversation != null) {
            Log.d(TAG, "conversation is not null");
        }

        ConversationViewHolder viewHolder;

        if (convertView == null) {
            Log.d(TAG, "convertView is null");
            viewHolder = new ConversationViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.conversation, parent, false);
            viewHolder.conversationTitle = (TextView) convertView.findViewById(R.id.conversation_title);
            convertView.setTag(viewHolder);
        } else {
            Log.d(TAG, "convertView is NOT null");
            viewHolder = (ConversationViewHolder) convertView.getTag();
        }

        viewHolder.conversationTitle.setText(conversation.getOtherUserRole() + ": " + conversation.getOtherUserUsername());

        return convertView;
    }
}

class ConversationViewHolder {
    public TextView conversationTitle;
}
