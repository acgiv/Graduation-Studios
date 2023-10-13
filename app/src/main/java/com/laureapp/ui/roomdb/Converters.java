package com.laureapp.ui.roomdb;

import androidx.room.TypeConverter;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


public class Converters {
    @TypeConverter
    public static Timestamp fromTimestampString(String value) {
        return value == null ? null : Timestamp.valueOf(value);
    }

    @TypeConverter
    public static String toTimestampString(Timestamp timestamp) {
        return timestamp == null ? null : timestamp.toString();
    }

    @TypeConverter
    public static Timestamp toTimestamp(Long value) {
        return value == null ? null : new Timestamp(value);
    }

    @TypeConverter
    public static Long toLong(Timestamp timestamp) {
        return timestamp == null ? null : timestamp.getTime();
    }

    /**
     * Questo metodo serve per convertire una stringa nel formato Timestamp
     * @param dateString è la stringa che deve essere convertita
     * @return la string convertita nel tipo Timestamp
     * @throws ParseException se si verifica un errore durante il parsing
     */
    public static Timestamp stringToTimestamp(String dateString) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", new Locale("italian"));
        Date date = sdf.parse(dateString);
        assert date != null;
        return new Timestamp(date.getTime());
    }


    @TypeConverter
    public static Date toDate(Long timestamp) {
        return timestamp == null ? null : new Date(timestamp);
    }

    @TypeConverter
    public static Long toTimestamp(Date date) {
        return date == null ? null : date.getTime();
    }


}