package main.java.logic;

import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.Hours;
import org.joda.time.Months;
import org.joda.time.Weeks;
import org.joda.time.Years;

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

    public static int hourStartsBetween(DateTime from, DateTime to) {
        return Hours.hoursBetween(from.toLocalDate(), to.toLocalDate()).getHours();
    }

    public static int hourStartsPassed(DateTime since) {
        return hourStartsBetween(since, now());
    }

    /**
     * Finds how many midnights have passed between the "from" and the "to" variables.
     *
     * @param from The from time
     * @param to The to time.
     * @return The number of midnights between the two {@link DateTime} objects.
     */
    public static int midnightsBetween(DateTime from, DateTime to) {
        return Days.daysBetween(from.toLocalDate(), to.toLocalDate()).getDays();
    }

    /**
     * How many midnights have passed between now and the since datetime given.
     *
     * @param since The start of this interval that we measure.
     * @return
     */
    public static int midnightsPassed(DateTime since) {
        return midnightsBetween(since, now());
    }

    /**
     * TODO
     *
     * @param from
     * @param to
     * @return
     */
    public static int mondaysBetween(DateTime from, DateTime to) {
        return Weeks.weeksBetween(from.toLocalDate(), to.toLocalDate()).getWeeks();
    }

    /**
     * TODO
     *
     * @param since
     * @return
     */
    public static int mondaysPassed(DateTime since) {
        return mondaysBetween(since, now());
    }

    public static int firstDatesOfMonthBetween(DateTime from, DateTime to) {
        return Months.monthsBetween(from.toLocalDate(), to.toLocalDate()).getMonths();
    }

    public static int firstDatesOfMonthPassed(DateTime since) {
        return firstDatesOfMonthBetween(since, now());
    }

    public static int firstDatesOfYearBetween(DateTime from, DateTime to) {
        return Years.yearsBetween(from.toLocalDate(), to.toLocalDate()).getYears();
    }

    public static int firstDatesOfYearPassed(DateTime since) {
        return firstDatesOfYearBetween(since, now());
    }

    public static DateTime hoursAfter(DateTime time, int numberOfHours) {
        return time.plusHours(numberOfHours);
    }
    public static DateTime daysAfter(DateTime time, int numberOfDays) {
        return time.plusDays(numberOfDays);
    }
    public static DateTime weeksAfter(DateTime time, int numberOfWeeks) {
        return time.plusWeeks(numberOfWeeks);
    }
    public static DateTime monthsAfter(DateTime time, int numberOfMonths) {
        return time.plusMonths(numberOfMonths);
    }
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
