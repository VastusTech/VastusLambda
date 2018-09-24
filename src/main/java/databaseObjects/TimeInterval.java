package main.java.databaseObjects;

import main.java.Logic.Constants;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;

import java.util.ArrayList;
import java.util.List;

public class TimeInterval {

    public String isotime;
    public String fromiso;
    public String toiso;

    public DateTime fromDateTime;
    public DateTime toDateTime;

    public int year, month, day;
    public int weekday;
    public int fromHour, fromMinute, toHour, toMinute;

    // These are just 60 * Hour + Minute
    public int fromTotalMinute, toTotalMinute;

    // year, month, day, fromHour, fromMinute, toHour, toMinute
    public String[] stringTimeArray;

    Float timeFittingValue = 0.0f;

    // TODO Create new exceptions for this class?
    public TimeInterval(String isotime) throws Exception {
        String[] isotimes = isotime.split("_");
        if (isotimes.length == 2) {
            this.isotime = isotime;
            this.fromiso = isotimes[0];
            this.toiso = isotimes[1];

            fromDateTime = new DateTime(this.fromiso);
            toDateTime = new DateTime(this.toiso);

            this.year = fromDateTime.getYear();
            this.month = fromDateTime.getMonthOfYear();
            this.day = fromDateTime.getDayOfMonth();
            this.weekday = fromDateTime.getDayOfWeek();

            // TimeIntervals have to be on the same day
            if (year == toDateTime.getYear() && month == toDateTime.getMonthOfYear() &&
                    day == toDateTime.getDayOfMonth()) {
                this.fromHour = fromDateTime.getHourOfDay();
                this.fromMinute = fromDateTime.getMinuteOfHour();
                this.fromTotalMinute = fromDateTime.getMinuteOfDay();

                this.toHour = toDateTime.getHourOfDay();
                this.toMinute = toDateTime.getMinuteOfHour();
                this.toTotalMinute = toDateTime.getMinuteOfDay();

                stringTimeArray = new String[] {
                        Integer.toString(year),
                        Integer.toString(month),
                        Integer.toString(day),
                        Integer.toString(fromHour),
                        Integer.toString(fromMinute),
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

                // Check for forbidden timeInterval states
                // From to to has to be positive
                if (fromTotalMinute >= toTotalMinute) {
                    throw new Exception("From time is at or after the to time");
                }

                // The time section needs to be a multiple of the time section
                int timeSection = Constants.workoutShortestTimeSectionInterval;
                if (fromTotalMinute % timeSection != 0 || toTotalMinute % timeSection != 0) {
                    throw new Exception("Time is offset by an irregular amount");
                }
            }
            else {
                throw new Exception("Times need to be on the same day!");
            }
        }
        else {
            throw new Exception("Incorrectly Formatted ISO 8601 date string inputted: Separation Error");
        }
    }

    public TimeInterval(String[] stringTimeArray) throws Exception {
        if (stringTimeArray.length != 7) {
            throw new Exception("String Time Array is incorrectly formatted");
        }

        this.stringTimeArray = stringTimeArray;

        try {
            this.year = Integer.parseInt(stringTimeArray[0]);
            this.month = Integer.parseInt(stringTimeArray[1]);
            this.day = Integer.parseInt(stringTimeArray[2]);
            this.fromHour = Integer.parseInt(stringTimeArray[3]);
            this.fromMinute = Integer.parseInt(stringTimeArray[4]);
            this.toHour = Integer.parseInt(stringTimeArray[5]);
            this.toMinute = Integer.parseInt(stringTimeArray[6]);
        }
        catch (Exception e) {
            throw new Exception("One of the strings were improperly formatted");
        }

        this.fromTotalMinute = fromHour * 60 + fromMinute;
        this.toTotalMinute = toHour * 60 + toMinute;

        fromDateTime = new DateTime(year, month, day, fromHour, fromMinute);
        toDateTime = new DateTime(year, month, day, toHour, toMinute);

        this.weekday = fromDateTime.getDayOfWeek();

        DateTimeFormatter dtf = ISODateTimeFormat.dateTimeNoMillis();

        this.fromiso = dtf.print(fromDateTime);
        this.toiso = dtf.print(toDateTime);

        this.isotime = fromiso + "_" + toiso;

        if (fromTotalMinute >= toTotalMinute) {
            throw new Exception("From time is at or after the to time");
        }

        int timeSection = Constants.workoutShortestTimeSectionInterval;
        if (fromTotalMinute % timeSection != 0 || toTotalMinute % timeSection != 0) {
            throw new Exception("Time is offset by an irregular amount");
        }
    }

    public static List<TimeInterval> getTimeIntervals(List<String> times) throws Exception {
        List<TimeInterval> timeIntervals = new ArrayList<>();
        for (String isotime : times) {
            TimeInterval timeInterval = new TimeInterval(isotime);
            timeIntervals.add(timeInterval);
        }
        return timeIntervals;
    }

    public List<TimeInterval> potentialWorkoutTimes(TimeInterval timeInterval, int[] potentialWorkoutLengths) {
        List<TimeInterval> workoutTimes = new ArrayList<>();

        if (!isSameDay(timeInterval)) {
            return workoutTimes;
        }

        // Intersection is just max from and min to
        int intersectFromTotalMinute = Integer.max(this.fromTotalMinute, timeInterval.fromTotalMinute);
        int intersectToTotalMinute = Integer.min(this.toTotalMinute, timeInterval.toTotalMinute);

        if (intersectFromTotalMinute < intersectToTotalMinute) {
            // Then there is an intersection between the two times
            // Loop it for all the potential lengths of workout that there can be
            for (int length : potentialWorkoutLengths) {
                // This is the first section in the intersection
                int from = intersectFromTotalMinute;
                int to = intersectFromTotalMinute + length;

                // While the ending time still falls within the time intersection
                while (to <= intersectToTotalMinute) {
                    // Use from and to as wel as the self time to create and insert a new time
                    // Use the time array initializer
                    String[] fromToHourMinute = TimeInterval.getFromToHourMinute(from, to);

                    try {
                        TimeInterval workoutTime = new TimeInterval(new String[] {
                                stringTimeArray[0],
                                stringTimeArray[1],
                                stringTimeArray[2],
                                fromToHourMinute[0],
                                fromToHourMinute[1],
                                fromToHourMinute[2],
                                fromToHourMinute[3],
                        });
                        workoutTimes.add(workoutTime);
                    }
                    catch (Exception e) {
                        System.out.println("ERROR CREATING TIMEINTERVAL USING INTERSECTION VALUES?");
                        e.printStackTrace();
                    }

                    // Go to the next section of time
                    to += Constants.workoutShortestTimeSectionInterval;
                    from += Constants.workoutShortestTimeSectionInterval;
                }
            }
        }

        return workoutTimes;
    }

    public List<TimeInterval> getAllTimeSections() {
        List<TimeInterval> timeSections = new ArrayList<>();
        int sectionLength = Constants.workoutShortestTimeSectionInterval;

        int from = fromTotalMinute;
        int to = fromTotalMinute + sectionLength;

        while (to <= toTotalMinute) {
            // Use the timeArray initializer
            String[] fromToHourMinute = TimeInterval.getFromToHourMinute(from, to);

            try {
                timeSections.add(new TimeInterval(new String[] {
                        stringTimeArray[0],
                        stringTimeArray[1],
                        stringTimeArray[2],
                        fromToHourMinute[0],
                        fromToHourMinute[1],
                        fromToHourMinute[2],
                        fromToHourMinute[3]
                }));
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

    public Boolean intersects(TimeInterval timeInterval) {
        if (isSameDay(timeInterval)) {
            // Is there an overlap in the times?
            int intersectFromTotalMinute = Integer.max(this.fromTotalMinute, timeInterval.fromTotalMinute);
            int intersectToTotalMinute = Integer.min(this.toTotalMinute, timeInterval.toTotalMinute);
            if (intersectFromTotalMinute < intersectToTotalMinute) {
                return true;
            }
        }
        return false;
    }

    public Boolean encompasses(TimeInterval timeInterval) {
        if (isSameDay(timeInterval)) {
            if (this.fromTotalMinute <= timeInterval.fromTotalMinute && this.toTotalMinute >= timeInterval.toTotalMinute) {
                return true;
            }
        }
        return false;
    }

    /**
     * This function finds whether the time interval intersects the other, given that `this` is a WEEKLY TIME.
     * This means that instead of checking `isSameDay` we are checking `isSameWeekday`. Then finding if it intersects.
     * @return Whether the time interval intersects this weekly time
     */
    public Boolean weeklyIntersects(TimeInterval timeInterval) {
        if (this.weekday == timeInterval.weekday) {
            // Is there an overlap in the times?
            int intersectFromTotalMinute = Integer.max(this.fromTotalMinute, timeInterval.fromTotalMinute);
            int intersectToTotalMinute = Integer.min(this.toTotalMinute, timeInterval.toTotalMinute);
            if (intersectFromTotalMinute < intersectToTotalMinute) {
                return true;
            }
        }
        return false;
    }

    /**
     * This function finds whether the time interval encompasses the other, given that `this` is a WEEKLY TIME.
     * This means that instead of checking `isSameDay` we are checking `isSameWeekday`. Then finding if it is
     * encompassed.
     * @return Whether the time interval intersects this weekly time
     */
    public Boolean weeklyEncompasses(TimeInterval timeInterval) {
        if (this.weekday == timeInterval.weekday) {
            if (this.fromTotalMinute <= timeInterval.fromTotalMinute && this.toTotalMinute >= timeInterval.toTotalMinute) {
                return true;
            }
        }
        return false;
    }

    public Boolean hasAlreadyStarted() {
        return fromDateTime.isAfterNow();
    }

    static public String[] getFromToHourMinute(int fromTotalMinute, int toTotalMinute) {
        int fromHour = fromTotalMinute / 60;
        int fromMinute = fromTotalMinute % 60;
        int toHour = toTotalMinute / 60;
        int toMinute = toTotalMinute % 60;

        String[] stringTimeArray = { Integer.toString(fromHour), Integer.toString(fromMinute), Integer.toString
                (toHour), Integer.toString(toMinute) };

        int i = 0;
        for (String time: stringTimeArray) {
            if (time.length() == 1) {
                stringTimeArray[i] = "0" + time;
            }
            i++;
        }

        return stringTimeArray;
    }

    public Boolean isSameDay(TimeInterval timeInterval) {
        return (this.year == timeInterval.year && this.month == timeInterval.month && this.day == timeInterval.day);
    }

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
