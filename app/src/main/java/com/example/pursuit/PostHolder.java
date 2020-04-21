package com.example.pursuit;

import android.annotation.SuppressLint;
import android.os.Build;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pursuit.models.Share;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.ZoneId;
import java.time.ZoneOffset;
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

    @SuppressLint("SetTextI18n")
    public void bind(Share share) {
        userFullName.setText(share.getUserFullName());
        userUsername.setText(share.getUserUsername());
        if (share.getSubject().isEmpty()) {
            subject.setVisibility(View.GONE);
        } else {
            subject.setText(share.getSubject());
        }
        message.setText(share.getMessage());

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            ZonedDateTime now = ZonedDateTime.now(ZoneOffset.UTC);
            ZonedDateTime approvedTime = ZonedDateTime.parse(share.getCreatedAt());
            Period ymdDiff = Period.between(approvedTime.toLocalDate(), now.toLocalDate());
            Duration hmsDiff = Duration.between(approvedTime.toLocalDateTime(), now.toLocalDateTime());
            Long hmsSeconds = hmsDiff.getSeconds();
            if (hmsSeconds < 60) {
                date.setText(hmsSeconds.toString() + "s");
            } else if (hmsSeconds < 3600){
                Long minutes = hmsSeconds / 60;
                date.setText(minutes.toString() + "m");
            } else if (hmsSeconds < 86400) {
                Long hours = hmsSeconds / 3600;
                date.setText(hours.toString() + "h");
            } else if (ymdDiff.getYears() > 0) {
                date.setText(ymdDiff.getYears() + "Y");
            } else if (ymdDiff.getMonths() > 0) {
                date.setText(ymdDiff.getMonths() + "M");
            } else {
                date.setText(ymdDiff.getDays() + "D");
            }
        }
    }
}
