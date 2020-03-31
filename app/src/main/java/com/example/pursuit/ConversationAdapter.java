package com.example.pursuit;

import android.app.Activity;
import android.view.LayoutInflater;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.content.Context;
import android.widget.TextView;
import android.view.View;
import android.view.ViewGroup;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;
import androidx.appcompat.app.AppCompatActivity;

import com.example.pursuit.models.Conversation;

import java.util.ArrayList;

public class ConversationAdapter extends ArrayAdapter<Conversation> {

    ArrayList<Conversation> conversations = new ArrayList<>();
    Context context;

    public ConversationAdapter(Context context, ArrayList<Conversation> conversations) {
        super(context, R.layout.conversation, conversations);
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
    public Conversation getItem(int i) {
        return conversations.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup parent) {
        Conversation conversation = getItem(i);

        ConversationViewHolder viewHolder;

        if (convertView == null) {
            viewHolder = new ConversationViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.conversation, parent, false);
            viewHolder.conversationTitle = (TextView) convertView.findViewById(R.id.conversation_title);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ConversationViewHolder) convertView.getTag();
        }

        viewHolder.conversationTitle.setText(conversation.getUserIds().get(0));

        return convertView;
    }
}

class ConversationViewHolder {
    public TextView conversationTitle;
}
