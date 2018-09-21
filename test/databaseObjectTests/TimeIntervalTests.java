package databaseObjectTests;

import static org.junit.Assert.assertEquals;

import databaseObjects.TimeInterval;

import org.junit.Test;

// TODO EXPECTED (THEORETICALLY) , ACTUAL (TESTED)
public class TimeIntervalTests {
    @Test
    public void TestConstructor1() {
        // September 20th, 2018, 17:30:00 -  September 20th, 2018, 18:30:00
        // String testisotime = new DateTime(2018, 9, 20, 18, 30).toString();
        String isotime = "2018-09-20T17:30:00-04:00_2018-09-20T18:30:00-04:00";
        TimeInterval timeInterval ;

        try {
            timeInterval = new TimeInterval(isotime);
        }
        catch (Exception e) {
            e.printStackTrace();
            return;
        }

        int year = timeInterval.year;
        int month = timeInterval.month;
        int day = timeInterval.day;
        int fromHour = timeInterval.fromHour;
        int fromMinute = timeInterval.fromMinute;
        int toHour = timeInterval.toHour;
        int toMinute = timeInterval.toMinute;
        int fromTotalMinute = timeInterval.fromTotalMinute;
        int toTotalMinute = timeInterval.toTotalMinute;

        // Check each individual component
        assertEquals(2018, year);
        assertEquals(9, month);
        assertEquals(20, day);
        assertEquals(17, fromHour);
        assertEquals(30, fromMinute);
        assertEquals(18, toHour);
        assertEquals(30, toMinute);
        assertEquals((17 * 60) + 30, fromTotalMinute);
        assertEquals((18 * 60) + 30, toTotalMinute);
    }

    @Test
    public void testConstructor2() {
        String year = "2018", month = "10", day = "05", fromHour = "12", fromMinute = "00", toHour = "13", toMinute =
                "00";
        String[] stringTimeArray = {
                year,
                month,
                day,
                fromHour,
                fromMinute,
                toHour,
                toMinute
        };

        String fromiso = "2018-10-05T12:00:00-04:00";
        String toiso = "2018-10-05T13:00:00-04:00";
        String isotime = fromiso + "_" + toiso;

        try {
            TimeInterval timeInterval = new TimeInterval(stringTimeArray);
            assertEquals(fromiso, timeInterval.fromiso);
            assertEquals(toiso, timeInterval.toiso);
            assertEquals(isotime, timeInterval.isotime);
        }
        catch (Exception e) {
            throw new AssertionError();
        }
    }

}
