package com.example.pursuit.adapters;

import android.annotation.SuppressLint;
import android.os.Build;
import android.view.LayoutInflater;
import android.widget.ArrayAdapter;
import android.content.Context;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.TextView;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.RequiresApi;

import com.example.pursuit.R;
import com.example.pursuit.models.Conversation;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.TimeZone;

@RequiresApi(api = Build.VERSION_CODES.O)
public class ConversationAdapter extends ArrayAdapter<Conversation> implements Filterable {
    private String TAG = "ConversationAdapter";

    private ArrayList<Conversation> conversations = new ArrayList<>();
    private ArrayList<Conversation> storedConversations;

    private ZoneId zoneId = TimeZone.getDefault().toZoneId();
    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");

    public ConversationAdapter(Context context, ArrayList<Conversation> conversations) {
        super(context, R.layout.conversation, conversations);
        this.storedConversations = conversations;
    }

    public void add(Conversation conversation) {
        this.conversations.add(conversation);
        notifyDataSetChanged();
    }

    public void addAll(ArrayList<Conversation> conversations) {
        this.conversations.addAll(conversations);
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

    @RequiresApi(api = Build.VERSION_CODES.O)
    @SuppressLint("SetTextI18n")
    @Override
    public View getView(int i, View convertView, ViewGroup parent) {
        Conversation conversation = getItem(i);

        ConversationViewHolder viewHolder;

        if (convertView == null) {
            viewHolder = new ConversationViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.conversation, parent, false);
            viewHolder.conversationTitle = convertView.findViewById(R.id.conversation_title);
            viewHolder.conversationUpdatedAt = convertView.findViewById(R.id.conversation_updated_at);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ConversationViewHolder) convertView.getTag();
        }

        assert conversation != null;
        viewHolder.conversationTitle.setText(conversation.getOtherUserRole() + ": " + conversation.getOtherUserUsername());

        ZonedDateTime utc = ZonedDateTime.parse(conversation.getUpdatedAt()).withZoneSameLocal(zoneId);
        LocalDateTime local = utc.toLocalDateTime();
        viewHolder.conversationUpdatedAt.setText(local.format(formatter));

        ImageButton deleteBtn = convertView.findViewById(R.id.deleteConversation);
        deleteBtn.setTag(conversation.getId());

        TextView title = convertView.findViewById(R.id.conversation_title);
        title.setTag(conversation.getId());

        return convertView;
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults filterResults = new FilterResults();
                ArrayList<Conversation> temp = new ArrayList<>();
                constraint = constraint.toString().toLowerCase();

                if (constraint.toString().length() == 0) {
                    temp = storedConversations;
                } else {
                    for (int i = 0; i < conversations.size(); i++) {
                        String conversationUsername = conversations.get(i).getOtherUserUsername();
                        if (conversationUsername.contains(constraint.toString())) {
                            temp.add(conversations.get(i));
                        }
                    }
                }

                filterResults.count = temp.size();
                filterResults.values = temp;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                conversations = (ArrayList<Conversation>) results.values;
                notifyDataSetChanged();
            }
        };
    }
}

class ConversationViewHolder {
    public TextView conversationTitle;
    public TextView conversationUpdatedAt;
}
