package com.laureapp.ui.roomdb;

import androidx.room.TypeConverter;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Questa classe contiene metodi di conversione per convertire tipi di dati
 */
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

    /**
     * Converte un timestamp nel tipo di dati Date
     * @param timestamp da convertire
     * @return date corrispondente al timestamp
     */
    @TypeConverter
    public static Date toDate(Long timestamp) {
        return timestamp == null ? null : new Date(timestamp);
    }

    /**
     * Converte una Data nel tipo di dati timestamp
     * @param date da converire
     * @return Long della data convertita, o null se la data di input è nulla.
     */
    @TypeConverter
    public static Long toTimestamp(Date date) {
        return date == null ? null : date.getTime();
    }


}