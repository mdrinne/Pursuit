package com.example.pursuit;

import android.app.Activity;
import android.view.LayoutInflater;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.content.Context;
import android.widget.TextView;
import android.view.View;
import android.view.ViewGroup;

import com.example.pursuit.models.Conversation;

import java.util.ArrayList;

public class ConversationAdapter extends ArrayAdapter<Conversation> {

    ArrayList<Conversation> conversations = new ArrayList<>();
    Context context;

    public ConversationAdapter(Context context, ArrayList<Conversation> conversations) {
        super(context, 0, conversations);
    }

    public void add(Conversation conversation) {
        this.conversations.add(conversation);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return conversations.size();
    }

    @Override
    public Object getItem(int i) {
        return conversations.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        ConversationViewHolder holder = new ConversationViewHolder();
        LayoutInflater conversationInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        Conversation conversation = conversations.get(i);

        convertView = conversationInflater.inflate(R.layout.conversation, null);
        holder.conversationTitle = convertView.findViewById(R.id.conversation_title);
        convertView.setTag(holder);

        return convertView;
    }
}

class ConversationViewHolder {
    public TextView conversationTitle;
}
