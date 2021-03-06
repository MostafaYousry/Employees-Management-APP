package com.example.android.employeesmanagementapp.data;


import java.util.Calendar;

import androidx.room.TypeConverter;

/**
 * Type Converter class
 * <p>
 * helps room to know how to convert a calendar object to a timestamp for SQLite
 * and vice versa.
 */
public class CalendarTypeConverter {

    @TypeConverter
    public static Calendar toCalendar(Long timestamp) {
        if (timestamp == null)
            return null;

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timestamp);
        return calendar;
    }

    @TypeConverter
    public static Long toTimestamp(Calendar calendar) {
        return calendar == null ? null : calendar.getTimeInMillis();
    }
}
