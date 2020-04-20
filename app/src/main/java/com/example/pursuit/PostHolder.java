package com.example.pursuit;

import android.os.Build;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pursuit.models.Share;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.TimeZone;

@RequiresApi(api = Build.VERSION_CODES.O)
public class PostHolder extends RecyclerView.ViewHolder {

    private TextView userFullName, userUsername, subject, message, date;

    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yy");
    private ZoneId zoneId = TimeZone.getDefault().toZoneId();

    public PostHolder(@NonNull View itemView) {
        super(itemView);
        userFullName = itemView.findViewById(R.id.userFullName);
        userUsername = itemView.findViewById(R.id.userUsername);
        subject = itemView.findViewById(R.id.subject);
        message = itemView.findViewById(R.id.message);
        date = itemView.findViewById(R.id.date);
    }

    public void bind(Share share) {
        userFullName.setText(share.getUserFullName());
        userUsername.setText(share.getUserUsername());
        if (share.getSubject().isEmpty()) {
            subject.setVisibility(View.GONE);
        } else {
            subject.setText(share.getSubject());
        }
        message.setText(share.getMessage());
        ZonedDateTime utc = ZonedDateTime.parse(share.getCreatedAt()).withZoneSameLocal(zoneId);
        LocalDateTime local = utc.toLocalDateTime();
        date.setText(local.format(formatter));
    }
}
