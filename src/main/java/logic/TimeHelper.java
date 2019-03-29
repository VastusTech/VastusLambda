package main.java.logic;

import org.joda.time.DateTime;
import org.joda.time.Days;

/**
 * This class to handle all of our time specific logic for our app that does not have to do with
 * time intervals.
 */
public class TimeHelper {
    /**
     * Finds how many midnights have passed between the "from" and the "to" variables.
     *
     * @param from The start time
     * @param to
     * @return
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
        return midnightsBetween(since, new DateTime());
    }

    /**
     * Calculates the properly formatted ISO string for the current date and time.
     *
     * @return The ISO 8601 String for the current date and time.
     */
    public static String nowString() {
        return isoString(new DateTime());
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
}
