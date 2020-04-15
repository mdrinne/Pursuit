package com.example.pursuit;

import android.os.Build;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pursuit.models.Message;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.TimeZone;

@RequiresApi(api = Build.VERSION_CODES.O)
public class ReceivedMessageHolder extends RecyclerView.ViewHolder {

    private TextView messageText, timeText;
    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("h:mm a");
    private ZoneId zoneId = TimeZone.getDefault().toZoneId();

    public ReceivedMessageHolder(@NonNull View itemView) {
        super(itemView);
        messageText = itemView.findViewById(R.id.text_message_body);
        timeText = itemView.findViewById(R.id.text_message_time);
    }

    public void bind(Message message) {
        messageText.setText(message.getMessageText());
        ZonedDateTime utc = ZonedDateTime.parse(message.getCreatedAt()).withZoneSameLocal(zoneId);
        LocalDateTime local = utc.toLocalDateTime();
        timeText.setText(local.format(formatter));
    }
}
