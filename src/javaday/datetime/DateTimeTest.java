package javaday.datetime;

import org.junit.Test;

import java.time.*;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import static java.time.format.DateTimeFormatter.ofPattern;
import static javaday.datetime.DateTimeFunctions.*;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

/**
 * DateTimeHacking
 *
 * Each test represents some small date/time related task to be done using
 * both old < Java8 API and new shiny JSR-310 Java8 API
 * to feel the difference and get familiar with new Date&Time API
 *
 * @author Oleg Tsal-Tsalko
 */
public class DateTimeTest {

    @Test
    public void shouldShowCurrentDateInUserFriendlyFormat() throws Exception {
        //Old style
        Date date = new Date();
        System.out.println("Default Date format: "+date);
        Calendar calendar = Calendar.getInstance();
        System.out.println("Default Date format: "+calendar);

        //New style
        LocalDate localDate = LocalDate.now();
        System.out.println("Default LocalDate format: "+localDate);
    }

    @Test
    public void shouldCreateParticularDateObject() throws Exception {
        Date date = aDate(1987, 6, 10);
        LocalDate localDate = aLocalDate(1987, 6, 10);

        assertThat(date, is(new Date(87, 5, 10)));
        assertThat(localDate.toString(), is("1987-06-10"));
    }

    @Test
    public void shouldTruncateDateAndTimeObjectToSimpleDate() throws Exception {
        //No representation of just date before Java 8!
        Date givenOldDateWithTimeObject = stringToDate("10/06/1987 12:00:01", "dd/MM/yyyy HH:mm:ss");
        LocalDateTime givenNewDateWithTimeObject = stringToDateWithTime("10/06/1987 12:00:01", "dd/MM/yyyy HH:mm:ss");

        Date truncatedDate = truncateDateWithTimeToDateOnly(givenOldDateWithTimeObject);
        LocalDate truncatedLocalDate = truncateDateWithTimeToDateOnly(givenNewDateWithTimeObject);

        assertThat(truncatedDate, is(aDate(1987, 6, 10)));
        assertThat(truncatedLocalDate, is(aLocalDate(1987, 6, 10)));
    }

    @Test
    public void shouldCalculateDaysDiffBetweenDates() throws Exception {
        assertThat(daysDiff(aDate(2014, 5, 30), aDate(2014, 6, 10)), is(11));
        assertThat(daysDiff(aDate(2014, 5, 10), aDate(2014, 6, 30)), is(51));
        assertThat(daysDiff(aLocalDate(2014, 5, 30), aLocalDate(2014, 6, 10)), is(11));
        assertThat(daysDiff(aLocalDate(2014, 5, 10), aLocalDate(2014, 6, 30)), is(51));
    }

    @Test
    public void shouldAddParticularNumberOfDaysToGivenDate() throws Exception {
        Date givenDateObject = aDate(1987, 5, 30);
        LocalDate givenLocalDateObject = aLocalDate(1987, 5, 30);

        assertThat(addDaysToGivenDate(givenDateObject, 2), is(aDate(1987, 6, 1)));
        assertThat(addDaysToGivenLocalDate(givenLocalDateObject, 2), is(aLocalDate(1987, 6, 1)));
    }

    @Test
    public void shouldIdentifyLeapYear() throws Exception {
        Date notLeapYearDate = aDate(2014, 6, 10);
        Date leapYearDate = aDate(2012, 6, 10);

        LocalDate notLeapYearLocalDate = aLocalDate(2014, 6, 10);
        LocalDate leapYearLocalDate = aLocalDate(2012, 6, 10);

        assertTrue(isLeapYear(leapYearDate));
        assertFalse(isLeapYear(notLeapYearDate));
        assertTrue(isLeapYear(leapYearLocalDate));
        assertFalse(isLeapYear(notLeapYearLocalDate));
    }

    @Test
    public void shouldCalculateLengthOfCurrentMonth() throws Exception {
        Date notLeapYearDate = aDate(2014, 2, 10);
        Date leapYearDate = aDate(2012, 2, 10);

        LocalDate notLeapYearLocalDate = aLocalDate(2014, 2, 10);
        LocalDate leapYearLocalDate = aLocalDate(2012, 2, 10);

        assertThat(lengthOfMonth(notLeapYearDate), is(28));
        assertThat(lengthOfMonth(leapYearDate), is(29));
        assertThat(lengthOfMonth(notLeapYearLocalDate), is(28));
        assertThat(lengthOfMonth(leapYearLocalDate), is(29));
    }

    @Test
    public void shouldAdjustDateToNextTuesday() throws Exception {
        Calendar calendar = toCalendar(aDate(2014, 12, 29));
        LocalDate localDate = aLocalDate(2014, 6, 24);

        Calendar adjustedCalendar = adjustCalendarToNextTuesday(calendar);
        LocalDate adjustedLocalDate = adjustDateToNextTuesday(localDate);

        assertThat(adjustedCalendar.get(Calendar.DAY_OF_MONTH), is(6));
        assertThat(adjustedCalendar.get(Calendar.MONTH), is(0));
        assertThat(adjustedLocalDate.getDayOfMonth(), is(1));
        assertThat(adjustedLocalDate.getMonth(), is(Month.JULY));
    }

    //No analog of Duration class in old API
    @Test
    public void shouldShiftTimeOnSpecifiedDuration() throws Exception {
        Duration duration = Duration.ofMinutes(30);
        LocalTime localTime = aNewTime("13:30", ofPattern("HH:mm"));
        LocalTime shiftedTime = localTime.plus(duration);
        assertThat(shiftedTime.toString(), is("14:00"));
    }

    //No analog of Period class in old API
    @Test
    public void shouldShiftDateOnSpecifiedPeriod() throws Exception {
        Period period = Period.ofDays(2);
        LocalDate localDate = aLocalDate(1987, 5, 30);
        assertThat(localDate.plus(period), is(aLocalDate(1987, 6, 1)));
    }

    @Test
    public void shouldCreateDateTimeObjectInParticularTimeZone() throws Exception {
        Date dateTime = anOldDateTimeInTimeZone("10/06/1987 13:00", "dd/MM/yyyy HH:mm", TimeZone.getTimeZone("Europe/London"));
        System.out.println("Old Date object representing 1pm London time: "+dateTime);
        System.out.println("1pm London time Date#getHours() = "+dateTime.getHours());
        Calendar calendar = anOldCalendarInTimeZone("10/06/1987 13:00", "dd/MM/yyyy HH:mm", TimeZone.getTimeZone("Europe/London"));
        System.out.println("Calendar object at least doesn't do any stupid normalizations and returns expected time in hours: " + calendar.get(Calendar.HOUR_OF_DAY));
        assertThat(calendar.get(Calendar.HOUR_OF_DAY), is(13));

        ZonedDateTime zonedDateTime = aNewZonedDateTime("10/06/1987 13:00", "dd/MM/yyyy HH:mm", ZoneId.of("Europe/London"));
        System.out.println("New ZonedDateTime object representing 1pm London time: " + zonedDateTime);
        assertThat(zonedDateTime.getHour(), is(13));
    }

    @Test
    public void showsGreatAmbiguityOfTimeZoneRules() throws Exception {

        Date kievTime = anOldDateTimeInTimeZone("10/06/1987 13:00", "dd/MM/yyyy HH:mm", TimeZone.getTimeZone("Europe/Kiev"));
        System.out.println("1pm Kiev time: "+kievTime);
        Date londonTime = anOldDateTimeInTimeZone("10/06/1987 13:00", "dd/MM/yyyy HH:mm", TimeZone.getTimeZone("Europe/London"));
        System.out.println("1pm London time: "+londonTime);

        /*System.out.println("---------------------------------------------------------------------------");

        Date kievTimeNow = anOldDateTimeInTimeZone("10/06/2014 13:00", "dd/MM/yyyy HH:mm", TimeZone.getTimeZone("Europe/Kiev"));
        System.out.println("1pm Kiev time: "+kievTimeNow);
        Date londonTimeNow = anOldDateTimeInTimeZone("10/06/2014 13:00", "dd/MM/yyyy HH:mm", TimeZone.getTimeZone("Europe/London"));
        System.out.println("1pm London time: "+londonTimeNow);*/

        /*System.out.println("---------------------------------------------------------------------------");

        ZonedDateTime kievTimeIn1987 = aNewZonedDateTime("10/06/1987 13:00", "dd/MM/yyyy HH:mm", ZoneId.of("Europe/Kiev"));
        System.out.println("1pm Kiev time in 1987: " + kievTimeIn1987);
        ZonedDateTime kievTimeIn2014 = aNewZonedDateTime("10/06/2014 13:00", "dd/MM/yyyy HH:mm", ZoneId.of("Europe/Kiev"));
        System.out.println("1pm Kiev time in 2014: " + kievTimeIn2014);*/
    }

    @Test
    public void showGreatFragilityOfCalendarImplementation() throws Exception{
        //1pm London time
        Calendar calendar = anOldCalendarInTimeZone("10/06/1987 13:00", "dd/MM/yyyy HH:mm", TimeZone.getTimeZone("Europe/London"));
        System.out.println("Current time in London time zone: "+calendar.get(Calendar.HOUR_OF_DAY));

        /*System.out.println("Lets reset time to same value as it was before - 13pm. Nothing special. It shouldn't affect us, right?");
        calendar.set(Calendar.HOUR_OF_DAY, 13);*/

        //System.out.println("Current time in London time zone: "+calendar.get(Calendar.HOUR_OF_DAY));

        System.out.println("Time after transforming to UTC: " + transformCalendarToUTC(calendar).get(Calendar.HOUR_OF_DAY));
    }

}
