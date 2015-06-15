package javaday.datetime;

import javaday.Java8;
import javaday.PriorJava8;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

import static java.time.format.DateTimeFormatter.ofPattern;
import static java.time.temporal.TemporalAdjusters.next;
import static java.util.Calendar.*;

/**
 * DateTimeHacking
 *
 * This class represents set of date/time functions to solve small day to day problems
 * using both old < Java8 API and new shiny JSR-310 Java8 API to be able to compare
 * two approaches
 *
 * @author Oleg Tsal-Tsalko
 */
public class DateTimeFunctions {

    @PriorJava8
    public static Date aDate(int year, int month, int day) {
        return new GregorianCalendar(year, month-1, day).getTime();
    }

    @Java8
    public static LocalDate aLocalDate(int year, int month, int day) {
        return LocalDate.of(year, month, day);
    }

    @PriorJava8
    public static Date stringToDate(String date, String formatPattern) throws ParseException {
        return new SimpleDateFormat(formatPattern).parse(date);
    }

    @Java8
    public static LocalDate stringToLocalDate(String date, String formatPattern) {
        return LocalDate.parse(date, ofPattern(formatPattern));
    }

    @Java8
    public static LocalDateTime stringToDateWithTime(String dateWithTime, String formatPattern) {
        return LocalDateTime.parse(dateWithTime, ofPattern(formatPattern));
    }

    @Java8
    public static LocalTime aNewTime(String time, DateTimeFormatter dateTimeFormatter) throws ParseException {
        return LocalTime.parse(time, dateTimeFormatter);
    }

    @PriorJava8
    public static Calendar toCalendar(Date date){
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        return calendar;
    }

    @PriorJava8
    public static Date truncateDateWithTimeToDateOnly(Date dateWithTime) {
        Calendar calendar = toCalendar(dateWithTime);
        calendar.set(HOUR_OF_DAY, 0);
        calendar.clear(MINUTE);
        calendar.clear(SECOND);
        calendar.clear(MILLISECOND);
        return calendar.getTime();
    }

    @Java8
    public static LocalDate truncateDateWithTimeToDateOnly(LocalDateTime dateWithTime) {
        return dateWithTime.toLocalDate();
    }

    @PriorJava8
    public static Date setTimeToDate(Date date, String time, DateFormat timeFormat) throws ParseException {
        Calendar timeCalendar = toCalendar(timeFormat.parse(time));
        Calendar dateTimeCalendar = toCalendar(date);
        dateTimeCalendar.set(HOUR_OF_DAY, timeCalendar.get(HOUR_OF_DAY));
        dateTimeCalendar.set(MINUTE, timeCalendar.get(MINUTE));
        dateTimeCalendar.set(SECOND, timeCalendar.get(SECOND));
        dateTimeCalendar.set(MILLISECOND, timeCalendar.get(MILLISECOND));
        return dateTimeCalendar.getTime();
    }

    @Java8
    public static LocalDateTime setTimeToDate(LocalDate date, String time, DateTimeFormatter timeFormatter) {
        return date.atTime(LocalTime.parse(time, timeFormatter));
    }

    //It works only for UTC dates. It doesn't take into account daylight savings.
    @PriorJava8
    public static int daysDiff(Date date1, Date date2){
        Date truncatedDate1 = truncateDateWithTimeToDateOnly(date1);
        Date truncatedDate2 = truncateDateWithTimeToDateOnly(date2);
        long diffInMillies = truncatedDate2.getTime() - truncatedDate1.getTime();
        return (int)TimeUnit.DAYS.convert(diffInMillies,TimeUnit.MILLISECONDS);
    }

    @Java8
    public static int daysDiff(LocalDate date1, LocalDate date2){
        //return Period.between(date2, date1).getDays(); //Incorrect solution
        return (int)ChronoUnit.DAYS.between(date1, date2);
    }

    @PriorJava8
    public static int monthOf(Date date){
        Calendar calendar = toCalendar(date);
        return calendar.get(MONTH)+1;
    }

    @Java8
    public static int monthOf(LocalDate date){
        return date.getMonthValue();
    }

    @PriorJava8
    public static Date addDaysToGivenDate(Date date, int numberOfDays){
        Calendar calendar = toCalendar(date);
        calendar.add(DAY_OF_MONTH, numberOfDays);
        return calendar.getTime();
    }

    @Java8
    public static LocalDate addDaysToGivenLocalDate(LocalDate date, int numberOfDays){
        return date.plusDays(numberOfDays);
    }

    @PriorJava8
    public static Date anOldDateTimeInTimeZone(String dateTime, String dateTimeFormat, TimeZone timeZone) throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat(dateTimeFormat);//Formatter is aware of time zone!
        dateFormat.setTimeZone(timeZone);
        return dateFormat.parse(dateTime);
    }

    @PriorJava8
    public static Calendar anOldCalendarInTimeZone(String dateTime, String dateTimeFormat, TimeZone timeZone) throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat(dateTimeFormat);
        dateFormat.setTimeZone(timeZone);//Formatter is aware of time zone!
        Date dateTimeObject = dateFormat.parse(dateTime);
        Calendar calendar = new GregorianCalendar(timeZone);//Second time setUp zone!
        calendar.setTime(dateTimeObject);
        return calendar;
    }

    @Java8
    public static ZonedDateTime aNewZonedDateTime(String dateTime, String dateTimeFormat, ZoneId timeZone) throws ParseException {
        return ZonedDateTime.of(LocalDateTime.parse(dateTime, ofPattern(dateTimeFormat)), timeZone);
    }

    @PriorJava8
    public static Calendar transformCalendarToUTC(Calendar calendar) throws ParseException {
        calendar.setTimeZone(TimeZone.getTimeZone("UTC"));
        return calendar;
    }

    @Java8
    public static ZonedDateTime transformToUTC(ZonedDateTime dateTime) throws ParseException {
        return dateTime.withZoneSameInstant(ZoneId.of("UTC"));
    }

    @PriorJava8
    public static boolean isLeapYear(Date date){
        GregorianCalendar calendar = (GregorianCalendar)toCalendar(date);
        return calendar.isLeapYear(calendar.get(YEAR));//Why it's not static method? And better don't look inside.
    }

    @Java8
    public static boolean isLeapYear(LocalDate date){
        return date.isLeapYear();
    }

    @PriorJava8
    public static int lengthOfMonth(Date date){
        Calendar calendar = toCalendar(date);
        return calendar.getActualMaximum(DAY_OF_MONTH);
    }

    @Java8
    public static int lengthOfMonth(LocalDate date){
        return date.lengthOfMonth();
    }

    @PriorJava8
    public static Calendar adjustCalendarToNextTuesday(Calendar calendar){
        calendar.add(WEEK_OF_MONTH, 1);
        calendar.set(DAY_OF_WEEK, Calendar.TUESDAY);
        return calendar;
    }

    @Java8
    public static LocalDate adjustDateToNextTuesday(LocalDate date){
        return date.with(next(DayOfWeek.TUESDAY));
    }

}
