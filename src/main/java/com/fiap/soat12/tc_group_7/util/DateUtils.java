package com.fiap.soat12.tc_group_7.util;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;

public abstract class DateUtils {

	public static String TIMEZONE_OFFSET_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSSZ";

	public static String TIMEZONE_OFFSET_FORMAT_PTBR = "dd/MM/yyyy HH:mm:ss Z";

	public static Timestamp getCurrentTimestamp() {

		return new Timestamp(Calendar.getInstance().getTimeInMillis());

	}

	public static java.sql.Date toSQLDate(Date date) {

		if (date == null) {
			return null;
		}

		try {
			return new java.sql.Date(date.getTime());
		} catch (Exception e) {
			return null;
		}

	}

	public static LocalDateTime toLocalDateTime(Date value) {
		try {
			return value.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
		} catch (Exception e) {
			return null;
		}
	}
	
	
	public static LocalDate toLocalDate(Date value) {
		try {
			return value.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
		} catch (Exception e) {
			return null;
		}
	}
	

	public static Date toDate(LocalDateTime localDateTime) {

		return localDateTime == null ? null : java.util.Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());

	}

	public static Date getEndOfDay(Date day) {

		if (day == null)
			day = new Date();

		Calendar cal = Calendar.getInstance();

		cal.setTime(day);

		cal.set(Calendar.HOUR_OF_DAY, cal.getMaximum(Calendar.HOUR_OF_DAY));
		cal.set(Calendar.MINUTE, cal.getMaximum(Calendar.MINUTE));
		cal.set(Calendar.SECOND, cal.getMaximum(Calendar.SECOND));
		cal.set(Calendar.MILLISECOND, cal.getMaximum(Calendar.MILLISECOND));

		return cal.getTime();

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

	public static Date millisencodToDate(Integer millis) {

		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(millis);

		return calendar.getTime();

	}

	public static Date minuteToDate(Long minute) {
		return (minute == null ? null : minuteToDate(minute.intValue()));
	}

	public static String minuteToHour(Integer minute) {

		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.HOUR_OF_DAY, minute / 60);
		calendar.set(Calendar.MINUTE, minute % 60);

		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");

		return simpleDateFormat.format(calendar.getTime());

	}

	public static Date getCurrentDateTrucate() {

		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);

		return calendar.getTime();

	}

	public static Date truncate(Date date) {

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);

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

	public static Integer getCurrentHourInMinute() {

		Calendar calendar = Calendar.getInstance();

		return calendar.get(Calendar.HOUR_OF_DAY) * 60 + calendar.get(Calendar.MINUTE);

	}

	public static Date getCurrentDate() {

		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.MILLISECOND, 0);
		return calendar.getTime();

	}

	public static String getCurrentExtensiveDate() {

		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd 'de' MMMMM 'de' yyyy");

		return simpleDateFormat.format(getCurrentDate());

	}

	public static String formatDate(Date data, String format) {

		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);

		return data == null ? "" : simpleDateFormat.format(data);

	}

	public static String formatDate(Date data) {

		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");

		return data == null ? "" : simpleDateFormat.format(data);

	}

	public static String formatDateHour(Date data) {

		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

		return data == null ? "" : simpleDateFormat.format(data);

	}

	public static int minutesDiff(Date earlierDate, Date laterDate) {
		if (earlierDate == null || laterDate == null)
			return 0;

		return (int) ((earlierDate.getTime() / 60000) - (laterDate.getTime() / 60000));
	}

	public static long daysDiff(Date earlierDate, Date laterDate) {

		long startTime = laterDate.getTime();

		long endTime = earlierDate.getTime();

		long diffTime = endTime - startTime;

		long diffDays = diffTime / (1000 * 60 * 60 * 24);

		return diffDays;

	}

	public static Date toDate(String date) throws ParseException {

		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");

		return date == null ? null : simpleDateFormat.parse(date);

	}

	public static Date toDate(String date, String format) throws ParseException {

		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);

		return date == null ? null : simpleDateFormat.parse(date);

	}

	public static Integer toMinute(String hour) {

		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");

		try {
			return hour == null ? 0 : dateToMinute(simpleDateFormat.parse(hour));
		} catch (ParseException e) {
			return 0;
		}

	}

	public static Date toDateTime(Date data, Integer hora) {

		Calendar calendar = Calendar.getInstance();

		calendar.setTime(data);

		calendar.set(Calendar.MINUTE, hora);

		return calendar.getTime();

	}

	public static Integer getMaxHourInMinute() {
		return 1439;
	}

	public static Date _1900() {

		Calendar calendar = Calendar.getInstance();
		calendar.set(1900, 11, 31, 0, 0, 0);
		calendar.set(Calendar.MILLISECOND, 0);

		return calendar.getTime();

	}

	public static Date addDays(Date date, int amount) {
		return org.apache.commons.lang3.time.DateUtils.addDays(date, amount);
	}

	public static void main(String[] args) {

		try {
			System.out.println( DateUtils.toDate("2019-06-11T09:45:32.000-0000", "yyyy-MM-dd'T'HH:mm:ss.SSSZ") );
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	
	public static Date toDate(LocalDate localDate) {
		
		return localDate == null ? null : java.util.Date.from(localDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
		
	}
	
	
	public static String getCurrentDateTimeWithZone() {
		
		return ZonedDateTime.now().format(DateTimeFormatter.ofPattern( TIMEZONE_OFFSET_FORMAT ));
		
	}
	
	

}
