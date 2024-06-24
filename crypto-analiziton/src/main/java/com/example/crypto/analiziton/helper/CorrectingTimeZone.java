package com.example.crypto.analiziton.helper;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.Calendar;

@Component
public class CorrectingTimeZone {

    @Value("${correcting.time}")
    private final int correctingTime = -4;

    public Timestamp correctingTimeByHours(Timestamp originalTimestamp) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(originalTimestamp.getTime());
        calendar.add(Calendar.HOUR_OF_DAY, correctingTime);
        return new Timestamp(calendar.getTimeInMillis());
    }
}
