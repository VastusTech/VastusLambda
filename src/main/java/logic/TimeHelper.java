package main.java.logic;

import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.Hours;
import org.joda.time.Months;
import org.joda.time.Weeks;
import org.joda.time.Years;

import java.time.DayOfWeek;

/**
 * This class to handle all of our time specific logic for our app that does not have to do with
 * time intervals.
 */
public class TimeHelper {
    /**
     * Returns the now {@link DateTime} object.
     *
     * @return The object representing right now.
     */
    public static DateTime now() {
        return new DateTime();
    }

    /**
     * Finds how many 0 minute marks are in the "from" and the "to" variable interval.
     *
     * @param from The {@link DateTime} object indicating the beginning of the interval.
     * @param to The {@link DateTime} object indicating the end of the interval.
     * @return The amount of 0 minute marks in the interval.
     */
    public static int hourStartsBetween(DateTime from, DateTime to) {
        from = from.withMinuteOfHour(0).withSecondOfMinute(0).withMillisOfSecond(0);
        to = to.withMinuteOfHour(0).withSecondOfMinute(0).withMillisOfSecond(0);
        return Hours.hoursBetween(from, to).getHours();
    }

    /**
     * Finds how many 0 minute marks have passed since a certain time until now.
     *
     * @param since The {@link DateTime} object indicating the beginning of the interval.
     * @return The amount of 0 minute marks since the given time object.
     */
    public static int hourStartsPassed(DateTime since) {
        return hourStartsBetween(since, now());
    }

    /**
     * Finds how many midnights are in the "from" and the "to" variable interval.
     *
     * @param from The {@link DateTime} object indicating the beginning of the interval.
     * @param to The {@link DateTime} object indicating the end of the interval.
     * @return The amount of midnights in the interval.
     */
    public static int midnightsBetween(DateTime from, DateTime to) {
        return Days.daysBetween(from.toLocalDate(), to.toLocalDate()).getDays();
    }

    /**
     * Finds how many midnights have passed since a certain time until now.
     *
     * @param since The {@link DateTime} object indicating the beginning of the interval.
     * @return The amount of midnights since the given time object.
     */
    public static int midnightsPassed(DateTime since) {
        return midnightsBetween(since, now());
    }

    /**
     * Finds how many Mondays are in the "from" and the "to" variable interval.
     *
     * @param from The {@link DateTime} object indicating the beginning of the interval.
     * @param to The {@link DateTime} object indicating the end of the interval.
     * @return The amount of Mondays in the interval.
     */
    public static int mondaysBetween(DateTime from, DateTime to) {
        // Calculated mathematically based on the total days between and the current day of week
        // If we wanted a different day of the week, we simply add however many days from monday it
        // is to the day of week property and modulo it to 7.
        return (midnightsBetween(from, to) + from.dayOfWeek().get() - to.dayOfWeek().get()) / 7;
    }

    /**
     * Finds how many Mondays have passed since a certain time until now.
     *
     * @param since The {@link DateTime} object indicating the beginning of the interval.
     * @return The amount of Mondays since the given time object.
     */
    public static int mondaysPassed(DateTime since) {
        return mondaysBetween(since, now());
    }

    /**
     * Finds how many 1sts of the month are in the "from" and the "to" variable interval.
     *
     * @param from The {@link DateTime} object indicating the beginning of the interval.
     * @param to The {@link DateTime} object indicating the end of the interval.
     * @return The amount of 1st of the month in the interval.
     */
    public static int firstDatesOfMonthBetween(DateTime from, DateTime to) {
        return Months.monthsBetween(from.toLocalDate().withDayOfMonth(1), to.toLocalDate()
                .withDayOfMonth(1)).getMonths();
    }

    /**
     * Finds how many 1sts of the month have passed since a certain time until now.
     *
     * @param since The {@link DateTime} object indicating the beginning of the interval.
     * @return The amount of 1sts of the month since the given time object.
     */
    public static int firstDatesOfMonthPassed(DateTime since) {
        return firstDatesOfMonthBetween(since, now());
    }

    /**
     * Finds how many 1sts of the year are in the "from" and the "to" variable interval.
     *
     * @param from The {@link DateTime} object indicating the beginning of the interval.
     * @param to The {@link DateTime} object indicating the end of the interval.
     * @return The amount of 1sts of the year in the interval.
     */
    public static int firstDatesOfYearBetween(DateTime from, DateTime to) {
        return Years.yearsBetween(from.toLocalDate().withMonthOfYear(1).withDayOfMonth(1),
                to.toLocalDate().withMonthOfYear(1).withDayOfMonth(1)).getYears();
    }

    /**
     * Finds how many 1sts of the year have passed since a certain time until now.
     *
     * @param since The {@link DateTime} object indicating the beginning of the interval.
     * @return The amount of 1sts of the year since the given time object.
     */
    public static int firstDatesOfYearPassed(DateTime since) {
        return firstDatesOfYearBetween(since, now());
    }

    /**
     * Calculates a {@link DateTime} a certain number of hours after the given time.
     *
     * @param time The initial {@link DateTime} object to calculate.
     * @param numberOfHours The number of hours to add to it.
     * @return The {@link DateTime} representing a number of hours after the given time.
     */
    public static DateTime hoursAfter(DateTime time, int numberOfHours) {
        return time.plusHours(numberOfHours);
    }

    /**
     * Calculates a {@link DateTime} a certain number of days after the given time.
     *
     * @param time The initial {@link DateTime} object to calculate.
     * @param numberOfDays The number of days to add to it.
     * @return The {@link DateTime} representing a number of days after the given time.
     */
    public static DateTime daysAfter(DateTime time, int numberOfDays) {
        return time.plusDays(numberOfDays);
    }

    /**
     * Calculates a {@link DateTime} a certain number of weeks after the given time.
     *
     * @param time The initial {@link DateTime} object to calculate.
     * @param numberOfWeeks The number of weeks to add to it.
     * @return The {@link DateTime} representing a number of weeks after the given time.
     */
    public static DateTime weeksAfter(DateTime time, int numberOfWeeks) {
        return time.plusWeeks(numberOfWeeks);
    }

    /**
     * Calculates a {@link DateTime} a certain number of months after the given time.
     *
     * @param time The initial {@link DateTime} object to calculate.
     * @param numberOfMonths The number of months to add to it.
     * @return The {@link DateTime} representing a number of months after the given time.
     */
    public static DateTime monthsAfter(DateTime time, int numberOfMonths) {
        return time.plusMonths(numberOfMonths);
    }

    /**
     * Calculates a {@link DateTime} a certain number of years after the given time.
     *
     * @param time The initial {@link DateTime} object to calculate.
     * @param numberOfYears The number of years to add to it.
     * @return The {@link DateTime} representing a number of years after the given time.
     */
    public static DateTime yearsAfter(DateTime time, int numberOfYears) {
        return time.plusYears(numberOfYears);
    }

    /**
     * Calculates the properly formatted ISO string for the current date and time.
     *
     * @return The ISO 8601 String for the current date and time.
     */
    public static String nowString() {
        return isoString(now());
    }

    /**
     * Gets the properly formatted ISO 8601 String for a date and time.
     *
     * @param dateTime The {@link DateTime} object to represent the date and time.
     * @return The properly formatted ISO 8601 time string.
     */
    public static String isoString(DateTime dateTime) {
        return dateTime.toString();
    }

    /**
     * Gets the {@link DateTime} object a number of hours from the given date.
     *
     * @param time The {@link DateTime} object to find the hours from.
     * @param hours The number of hours to get the object for after.
     * @return The {@link DateTime} object hours after the given time.
     */
    public static DateTime hoursFrom(DateTime time, int hours) {
        return time.plusHours(hours);
    }

    /**
     * Gets the datetime equal to the amount of hours from now.
     *
     * @param hours The number of hours to get the time for after now.
     * @return The {@link DateTime} object hours after now.
     */
    public static DateTime hoursFromNow(int hours) {
        return hoursFrom(now(), hours);
    }
}
