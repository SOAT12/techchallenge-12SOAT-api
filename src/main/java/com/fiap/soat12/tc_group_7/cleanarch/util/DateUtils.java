package com.fiap.soat12.tc_group_7.cleanarch.util;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;

public abstract class DateUtils {

    private DateUtils() {
    }

    public static Timestamp getCurrentTimestamp() {

        return new Timestamp(Calendar.getInstance().getTimeInMillis());

    }

    public static LocalDateTime toLocalDateTime(Date value) {
        try {
            return value.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        } catch (Exception e) {
            return null;
        }
    }

    public static Date toDate(LocalDateTime localDateTime) {

        return localDateTime == null ? null : java.util.Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());

    }

    public static Integer dateToMinute(Date date) {

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        return calendar.get(Calendar.HOUR_OF_DAY) * 60 + calendar.get(Calendar.MINUTE);

    }

    public static Date minuteToDate(Integer minute) {

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, minute / 60);
        calendar.set(Calendar.MINUTE, minute % 60);

        return calendar.getTime();

    }

    public static Date first(Date date) {

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        return calendar.getTime();

    }

    public static Date last(Date date) {

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 0);

        return calendar.getTime();

    }

    public static Date getCurrentDate() {

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();

    }

    public static int minutesDiff(Date earlierDate, Date laterDate) {
        if (earlierDate == null || laterDate == null)
            return 0;

        return (int) ((earlierDate.getTime() / 60000) - (laterDate.getTime() / 60000));
    }

    public static Date toDate(String date, String format) throws ParseException {

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);

        return date == null ? null : simpleDateFormat.parse(date);

    }
}