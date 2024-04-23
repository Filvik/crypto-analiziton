package com.example.crypto.analiziton.helper_time;

import java.sql.Timestamp;
import java.util.Calendar;

public class TimestampAdjuster {
    public static Timestamp addMillisecondsToTimestamp(Timestamp originalTimestamp, int milliseconds) {
        if (originalTimestamp == null) {
            return null;
        }
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(originalTimestamp.getTime());
        cal.add(Calendar.MILLISECOND, milliseconds);
        return new Timestamp(cal.getTimeInMillis());
    }
}

