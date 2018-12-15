package main.java.databaseObjects;

import main.java.Logic.Constants;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class TimeInterval {

    public String isotime;
    public String fromiso;
    public String toiso;

    public DateTime fromDateTime;
    public DateTime toDateTime;

    public int fromYear, fromMonth, fromDay, fromWeekday;
    public int toYear, toMonth, toDay, toWeekday;
    public int fromHour, fromMinute, toHour, toMinute;

    // These are just 60 * Hour + Minute
     public long fromTotalMillis, toTotalMillis;

     public DateTimeZone timeZone;

    // fromYear, fromMonth, fromDay, fromHour, fromMinute, toYear, toMonth, toDay, toHour, toMinute
    // Length == 10
    public String[] stringTimeArray;

    Float timeFittingValue = 0.0f;

    /**
     * This is the constructor that will parse times from the database. These are the iso strings as they should be
     * in the DB.
     *
     * @param isotime The concatenated ISO 8601 compliant strings (by a "_") that dictate the time interval.
     * @throws Exception This exception is thrown when the string is improperly formed.
     */
    public TimeInterval(String isotime) throws Exception {
        String[] isotimes = isotime.split("_");
        if (isotimes.length == 2) {
            this.isotime = isotime;
            this.fromiso = isotimes[0];
            this.toiso = isotimes[1];

            fromDateTime = new DateTime(this.fromiso);
            toDateTime = new DateTime(this.toiso);

            // From
            this.fromYear = fromDateTime.getYear();
            this.fromMonth = fromDateTime.getMonthOfYear();
            this.fromDay = fromDateTime.getDayOfMonth();
            this.fromWeekday = fromDateTime.getDayOfWeek();
            this.fromHour = fromDateTime.getHourOfDay();
            this.fromMinute = fromDateTime.getMinuteOfHour();
            this.fromTotalMillis = fromDateTime.getMillis();

            // To
            this.toYear = toDateTime.getYear();
            this.toMonth = toDateTime.getMonthOfYear();
            this.toDay = toDateTime.getDayOfMonth();
            this.toWeekday = toDateTime.getDayOfWeek();
            this.toHour = toDateTime.getHourOfDay();
            this.toMinute = toDateTime.getMinuteOfHour();
            this.toTotalMillis = toDateTime.getMillis();

            stringTimeArray = new String[] {
                    Integer.toString(fromYear),
                    Integer.toString(fromMonth),
                    Integer.toString(fromDay),
                    Integer.toString(fromHour),
                    Integer.toString(fromMinute),
                    Integer.toString(toYear),
                    Integer.toString(toMonth),
                    Integer.toString(toDay),
                    Integer.toString(toHour),
                    Integer.toString(toMinute),
            };

            // Make the string time array look pretty
            int i = 0;
            for (String time : stringTimeArray) {
                if (time.length() == 1 && i != 0) {
                    stringTimeArray[i] = "0" + time;
                }
                i++;
            }

            this.timeZone = fromDateTime.getZone();

            if (!timeZone.equals(toDateTime.getZone())) {
                throw new Exception("The two times time zones are different!");
            }

            // Check for forbidden timeInterval states
            // From to to has to be positive
            // TODO Test
            if (fromDateTime.compareTo(toDateTime) >= 0) {
                throw new Exception("The \"From\" time is at or after the \"to\" time in the time interval! isostring" +
                        " = " + isotime);
            }

            // The time section needs to be a multiple of the time section
            int timeSection = Constants.workoutShortestTimeSectionInterval;
            if (fromMinute % timeSection != 0 || toMinute % timeSection != 0) {
                throw new Exception("Time is offset by an irregular amount");
            }
        }
        else {
            throw new Exception("Incorrectly Formatted ISO 8601 date string inputted: Separation Error");
        }
    }

    /**
     * This uses the total milliseconds since 1970 something... To create a time interval.
     * @param from The total number of milliseconds since 1970-01-01T00:00:00Z for the "from" time.
     * @param to The total number of milliseconds since 1970-01-01T00:00:00Z for the "from" time.
     * @param timeZone The time zone for the time interval.
     * @throws Exception Throws exception if the milliseconds are poorly constructed
     */
    public TimeInterval(long from, long to, DateTimeZone timeZone) throws Exception {
        // Calling another constructor
        this((ISODateTimeFormat.dateTimeNoMillis()).print(new DateTime(from, timeZone)) +
                "_" +
                (ISODateTimeFormat.dateTimeNoMillis()).print(new DateTime(to, timeZone)));
    }

    public TimeInterval(String[] stringTimeArray) throws Exception {
        if (stringTimeArray.length != 10) {
            throw new Exception("String Time Array is incorrectly formatted");
        }

        this.stringTimeArray = stringTimeArray;

        // fromYear, fromMonth, fromDay, fromHour, fromMinute, toYear, toMonth, toDay, toHour, toMinute
        try {
            this.fromYear = Integer.parseInt(stringTimeArray[0]);
            this.fromMonth = Integer.parseInt(stringTimeArray[1]);
            this.fromDay = Integer.parseInt(stringTimeArray[2]);
            this.fromHour = Integer.parseInt(stringTimeArray[3]);
            this.fromMinute = Integer.parseInt(stringTimeArray[4]);
            this.toYear = Integer.parseInt(stringTimeArray[5]);
            this.toMonth = Integer.parseInt(stringTimeArray[6]);
            this.toDay = Integer.parseInt(stringTimeArray[7]);
            this.toHour = Integer.parseInt(stringTimeArray[8]);
            this.toMinute = Integer.parseInt(stringTimeArray[9]);
        }
        catch (Exception e) {
            throw new Exception("One of the strings were improperly formatted");
        }

        fromDateTime = new DateTime(fromYear, fromMonth, fromDay, fromHour, fromMinute);
        toDateTime = new DateTime(toYear, toMonth, toDay, toHour, toMinute);

        this.fromTotalMillis = fromDateTime.getMillis();
        this.toTotalMillis = toDateTime.getMillis();

        this.fromWeekday = fromDateTime.getDayOfWeek();
        this.toWeekday = toDateTime.getDayOfWeek();

        DateTimeFormatter dtf = ISODateTimeFormat.dateTimeNoMillis();

        this.fromiso = dtf.print(fromDateTime);
        this.toiso = dtf.print(toDateTime);

        this.isotime = fromiso + "_" + toiso;

        // Check for forbidden timeInterval states
        // From to to has to be positive
        // TODO Test
        if (fromDateTime.compareTo(toDateTime) >= 0) {
            throw new Exception("From time is at or after the to time");
        }

        // The time section needs to be a multiple of the time section
        int timeSection = Constants.workoutShortestTimeSectionInterval;
        if (fromMinute % timeSection != 0 || toMinute % timeSection != 0) {
            throw new Exception("Time is offset by an irregular amount");
        }
    }

    public static List<TimeInterval> getTimeIntervals(Collection<String> times) throws Exception {
        List<TimeInterval> timeIntervals = new ArrayList<>();
        for (String isotime : times) {
            TimeInterval timeInterval = new TimeInterval(isotime);
            timeIntervals.add(timeInterval);
        }
        return timeIntervals;
    }

    public List<TimeInterval> potentialWorkoutTimes(TimeInterval timeInterval, int[] potentialWorkoutLengths) {
        List<TimeInterval> workoutTimes = new ArrayList<>();

        int sectionLength = Constants.workoutShortestTimeSectionInterval * 60000;

        // Intersection is just max from and min to
        long intersectFrom = Long.max(this.fromTotalMillis, timeInterval.fromTotalMillis);
        long intersectTo = Long.min(this.toTotalMillis, timeInterval.toTotalMillis);

        if (intersectFrom < intersectTo) {
            // Then there is an intersection between the two times
            // Loop it for all the potential lengths of workout that there can be
            for (int length : potentialWorkoutLengths) {
                // This is the first section in the intersection
                long from = intersectFrom;
                long to = intersectFrom + (length * 60000);

                // While the ending time still falls within the time intersection
                while (to <= intersectTo) {
                    // Use from and to as well as the self time to create and insert a new time
                    try {
                        workoutTimes.add(new TimeInterval(from, to, this.timeZone));
                    }
                    catch (Exception e) {
                        System.out.println("ERROR CREATING TIMEINTERVAL USING INTERSECTION VALUES?");
                        e.printStackTrace();
                    }

                    // Go to the next section of time
                    to += sectionLength;
                    from += sectionLength;
                }
            }
        }

        return workoutTimes;
    }

    /**
     * This function will get every smallest time section in the time interval. For instance, if the time interval
     * indicates 10:00 AM - 11:00 AM and the smallest time section is 15 minutes, this function would return
     * [(10:00 AM - 10:15 AM), (10:15 AM - 10:30 AM), (10:30 AM - 10:45 AM), (10:45 AM - 11:00 AM)].
     * @return The time sections in the time interval.
     */
    public List<TimeInterval> getAllTimeSections() {
        List<TimeInterval> timeSections = new ArrayList<>();
        // This is measured in milliseconds
        int sectionLength = Constants.workoutShortestTimeSectionInterval * 60000;

        long from = fromTotalMillis;
        long to = from + sectionLength;

        while (to <= toTotalMillis) {
            try {
                // Use the milliseconds interval constructor
                timeSections.add(new TimeInterval(from, to, this.timeZone));
            }
            catch (Exception e) {
                System.out.println("ERROR CREATING TIMEINTERVAL USING FROM TO VALUES?");
                e.printStackTrace();
            }

            from += sectionLength;
            to += sectionLength;
        }

        return timeSections;
    }

    /**
     * This functions finds out if "this" intersects with the given time interval. Can they be on the same timeline?
     * @param timeInterval The time interval to compare this to.
     * @return If "this" and timeInterval intersect
     */
    public Boolean intersects(TimeInterval timeInterval) {
        // Is there an overlap in the times?
        long intersectFrom = Long.max(this.fromTotalMillis, timeInterval.fromTotalMillis);
        long intersectTo = Long.min(this.toTotalMillis, timeInterval.toTotalMillis);
        return (intersectFrom < intersectTo);
    }

    /**
     * This function finds out if "this" completely encompasses the given time interval. Does the given fit entirely
     * in this?
     * @param timeInterval The time interval to compare this to
     * @return If "this" encompasses timeInterval
     */
    public Boolean encompasses(TimeInterval timeInterval) {
        return (this.fromTotalMillis <= timeInterval.fromTotalMillis && this.toTotalMillis >= timeInterval.toTotalMillis);
    }

    /**
     * This function finds whether the time interval intersects the other, given that `this` is a WEEKLY TIME.
     * This means that instead of checking `isSameDay` we are checking `isSameWeekday`. Then finding if it intersects.
     * @return Whether the time interval intersects this weekly time
     */
//    public Boolean weeklyIntersects(TimeInterval timeInterval) {
//        if (this.weekday == timeInterval.weekday) {
//            // Is there an overlap in the times?
//            int intersectFromTotalMinute = Integer.max(this.fromTotalMinute, timeInterval.fromTotalMinute);
//            int intersectToTotalMinute = Integer.min(this.toTotalMinute, timeInterval.toTotalMinute);
//            if (intersectFromTotalMinute < intersectToTotalMinute) {
//                return true;
//            }
//        }
//        return false;
//    }

    /**
     * This function finds whether the time interval encompasses the other, given that `this` is a WEEKLY TIME.
     * This means that instead of checking `isSameDay` we are checking `isSameWeekday`. Then finding if it is
     * encompassed.
     * @return Whether the time interval intersects this weekly time
     */
//    public Boolean weeklyEncompasses(TimeInterval timeInterval) {
//        if (this.weekday == timeInterval.weekday) {
//            if (this.fromTotalMinute <= timeInterval.fromTotalMinute && this.toTotalMinute >= timeInterval.toTotalMinute) {
//                return true;
//            }
//        }
//        return false;
//    }

    /**
     * This calculates if start time has already passed.
     * @return Whether the time interval has already started
     */
    public Boolean hasAlreadyStarted() {
        return !fromDateTime.isAfterNow();
    }

    /**
     * This calculates if end time has already passed.
     * @return Whether the time interval has already started
     */
    public Boolean hasAlreadyFinished() {
        return !toDateTime.isAfterNow();
    }

    public static Boolean timeHasPassed(DateTime time) {
        return !time.isAfterNow();
    }

//    static public String[] getFromToHourMinute(int fromTotalMinute, int toTotalMinute) {
//        int fromHour = fromTotalMinute / 60;
//        int fromMinute = fromTotalMinute % 60;
//        int toHour = toTotalMinute / 60;
//        int toMinute = toTotalMinute % 60;
//
//        String[] stringTimeArray = { Integer.toString(fromHour), Integer.toString(fromMinute), Integer.toString
//                (toHour), Integer.toString(toMinute) };
//
//        int i = 0;
//        for (String time: stringTimeArray) {
//            if (time.length() == 1) {
//                stringTimeArray[i] = "0" + time;
//            }
//            i++;
//        }
//
//        return stringTimeArray;
//    }

//    public Boolean isSameDay(TimeInterval timeInterval) {
//        return (this.year == timeInterval.year && this.month == timeInterval.month && this.day == timeInterval.day);
//    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof TimeInterval) {
            return ((TimeInterval) obj).isotime.equals(isotime);
        }
        else {
            return false;
        }
    }

    @Override
    public String toString() {
        return isotime;
    }
}
