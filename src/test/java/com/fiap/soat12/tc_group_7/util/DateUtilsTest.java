package com.fiap.soat12.tc_group_7.util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.sql.Timestamp;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

public class DateUtilsTest {

    @Test
    @DisplayName("Deve retornar o Timestamp atual")
    void getCurrentTimestampShouldReturnCurrentTime() {
        Timestamp currentTimestamp = DateUtils.getCurrentTimestamp();
        Timestamp systemTimestamp = new Timestamp(System.currentTimeMillis());
        // Verificamos se a diferença de tempo é mínima (menos de 1 segundo)
        assertTrue(Math.abs(currentTimestamp.getTime() - systemTimestamp.getTime()) < 1000);
    }

    @Test
    @DisplayName("Deve converter Date para LocalDateTime com sucesso")
    void toLocalDateTimeShouldConvertDateCorrectly() {
        Date now = new Date();
        LocalDateTime localDateTime = DateUtils.toLocalDateTime(now);
        assertNotNull(localDateTime);
        assertEquals(now.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime(), localDateTime);
    }

    @Test
    @DisplayName("Deve retornar null ao converter Date nula para LocalDateTime")
    void toLocalDateTimeShouldReturnNullForNullDate() {
        assertNull(DateUtils.toLocalDateTime(null));
    }

    @Test
    @DisplayName("Deve converter LocalDateTime para Date com sucesso")
    void toDateFromLocalDateTimeShouldConvertCorrectly() {
        LocalDateTime now = LocalDateTime.now();
        Date date = DateUtils.toDate(now);
        assertNotNull(date);
        assertEquals(java.util.Date.from(now.atZone(ZoneId.systemDefault()).toInstant()), date);
    }

    @Test
    @DisplayName("Deve retornar null ao converter LocalDateTime nulo para Date")
    void toDateFromLocalDateTimeShouldReturnNullForNullLocalDateTime() {
        assertNull(DateUtils.toDate((LocalDateTime) null));
    }

    @Test
    @DisplayName("Deve converter Date para minutos do dia")
    void dateToMinuteShouldCalculateCorrectly() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 10);
        calendar.set(Calendar.MINUTE, 30);
        Date date = calendar.getTime();
        Integer minutes = DateUtils.dateToMinute(date);
        assertEquals(10 * 60 + 30, minutes);
    }

    @Test
    @DisplayName("Deve converter minutos do dia para Date")
    void minuteToDateShouldConvertCorrectly() {
        Integer minutes = 90; // 1h30min
        Date date = DateUtils.minuteToDate(minutes);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        assertEquals(1, calendar.get(Calendar.HOUR_OF_DAY));
        assertEquals(30, calendar.get(Calendar.MINUTE));
    }

    @Test
    @DisplayName("Deve retornar o início do dia para uma data")
    void firstShouldReturnStartOfDay() {
        Date now = new Date();
        Date firstOfDay = DateUtils.first(now);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(firstOfDay);
        assertEquals(0, calendar.get(Calendar.HOUR_OF_DAY));
        assertEquals(0, calendar.get(Calendar.MINUTE));
        assertEquals(0, calendar.get(Calendar.SECOND));
        assertEquals(0, calendar.get(Calendar.MILLISECOND));
    }

    @Test
    @DisplayName("Deve retornar o final do dia para uma data")
    void lastShouldReturnEndOfDay() {
        Date now = new Date();
        Date lastOfDay = DateUtils.last(now);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(lastOfDay);
        assertEquals(23, calendar.get(Calendar.HOUR_OF_DAY));
        assertEquals(59, calendar.get(Calendar.MINUTE));
        assertEquals(59, calendar.get(Calendar.SECOND));
        assertEquals(0, calendar.get(Calendar.MILLISECOND));
    }

    @Test
    @DisplayName("Deve retornar a data atual com milissegundos zerados")
    void getCurrentDateShouldHaveMillisecondsZeroed() {
        Date currentDate = DateUtils.getCurrentDate();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(currentDate);
        assertEquals(0, calendar.get(Calendar.MILLISECOND));
    }

    @Test
    @DisplayName("Deve calcular a diferença de minutos entre duas datas")
    void minutesDiffShouldCalculateCorrectly() {
        Calendar earlier = Calendar.getInstance();
        earlier.set(2025, Calendar.AUGUST, 12, 10, 0, 0);

        Calendar later = Calendar.getInstance();
        later.set(2025, Calendar.AUGUST, 12, 11, 30, 0);

        long diffInMillis = earlier.getTimeInMillis() - later.getTimeInMillis();
        long expectedDiffInMinutes = TimeUnit.MILLISECONDS.toMinutes(diffInMillis);

        int actualDiff = DateUtils.minutesDiff(earlier.getTime(), later.getTime());
        assertEquals(expectedDiffInMinutes, actualDiff);
    }

    @Test
    @DisplayName("Deve retornar 0 se uma das datas for nula")
    void minutesDiffShouldReturnZeroForNullDates() {
        assertEquals(0, DateUtils.minutesDiff(null, new Date()));
        assertEquals(0, DateUtils.minutesDiff(new Date(), null));
        assertEquals(0, DateUtils.minutesDiff(null, null));
    }

    @Test
    @DisplayName("Deve converter String para Date com sucesso")
    void toDateFromStringShouldParseCorrectly() throws ParseException {
        String dateString = "2025-08-12T09:45:32.000-0000";
        String format = "yyyy-MM-dd'T'HH:mm:ss.SSSZ";
        Date date = DateUtils.toDate(dateString, format);
        assertNotNull(date);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        assertEquals(2025, calendar.get(Calendar.YEAR));
        assertEquals(Calendar.AUGUST, calendar.get(Calendar.MONTH));
        assertEquals(12, calendar.get(Calendar.DAY_OF_MONTH));
    }

    @Test
    @DisplayName("Deve lançar ParseException para formato inválido")
    void toDateFromStringShouldThrowParseException() {
        String dateString = "2025/08/12";
        String format = "yyyy-MM-dd";
        assertThrows(ParseException.class, () -> DateUtils.toDate(dateString, format));
    }

    @Test
    @DisplayName("Deve retornar null ao converter String nula para Date")
    void toDateFromStringShouldReturnNullForNullString() throws ParseException {
        assertNull(DateUtils.toDate(null, "yyyy-MM-dd"));
    }
}