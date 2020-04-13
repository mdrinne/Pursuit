package com.example.pursuit;

import android.os.Build;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pursuit.models.Message;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

@RequiresApi(api = Build.VERSION_CODES.O)
public class ReceivedMessageHolder extends RecyclerView.ViewHolder {

    private TextView messageText, timeText, nameText;
    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("H:m a");

    public ReceivedMessageHolder(@NonNull View itemView) {
        super(itemView);
        messageText = itemView.findViewById(R.id.text_message_body);
        timeText = itemView.findViewById(R.id.text_message_time);
        nameText = itemView.findViewById(R.id.text_message_name);
    }

    public void bind(Message message) {
        messageText.setText(message.getMessageText());
        timeText.setText(ZonedDateTime.parse(message.getCreatedAt()).format(formatter));
        nameText.setText(message.getSenderId());
    }
}
