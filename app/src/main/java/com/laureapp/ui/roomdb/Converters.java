package com.laureapp.ui.roomdb;

import androidx.room.TypeConverter;

import java.sql.Timestamp;


public class Converters {
    @TypeConverter
    public static Timestamp fromTimestampString(String value) {
        return value == null ? null : Timestamp.valueOf(value);
    }

    @TypeConverter
    public static String toTimestampString(Timestamp timestamp) {
        return timestamp == null ? null : timestamp.toString();
    }
}