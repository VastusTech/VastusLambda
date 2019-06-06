package test.java.databaseObjects;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import main.java.logic.Constants;
import main.java.databaseObjects.TimeInterval;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

// EXPECTED (THEORETICALLY) , ACTUAL (TESTED)
public class TimeIntervalTest {
    private int i;

    @Before
    public void initConstants() {
        i = Constants.workoutShortestTimeSectionInterval;
        Constants.workoutShortestTimeSectionInterval = 5;
    }

    @After
    public void deinitConstants() {
        Constants.workoutShortestTimeSectionInterval = i;
    }

    @Test
    public void TestConstructor1() {
        // September 20th, 2018, 17:30:00 -  September 20th, 2018, 18:30:00
        // String testisotime = new DateTime(2018, 9, 20, 18, 30).toString();
        String isotime = "2018-09-20T17:30:00Z_2018-09-20T18:30:00Z";
        TimeInterval timeInterval ;

        try {
            timeInterval = new TimeInterval(isotime);
        }
        catch (Exception e) {
            e.printStackTrace();
            throw new AssertionError();
        }

        assertEquals(new DateTime(2018, 9, 20, 17, 30, DateTimeZone.UTC), timeInterval.fromDateTime.withZone(DateTimeZone.UTC));
        assertEquals(new DateTime(2018, 9, 20, 18, 30, DateTimeZone.UTC), timeInterval.toDateTime.withZone(DateTimeZone.UTC));
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
                year,
                month,
                day,
                toHour,
                toMinute
        };

        try {
            TimeInterval timeInterval = new TimeInterval(stringTimeArray);
            assertEquals(new DateTime(2018, 10, 5, 12, 0), timeInterval.fromDateTime);
            assertEquals(new DateTime(2018, 10, 5, 13, 0), timeInterval.toDateTime);
        }
        catch (Exception e) {
            e.printStackTrace();
            throw new AssertionError();
        }
    }

    @Test
    public void intersectsTest1() {
        String year = "2018", month = "10", day = "5";
        // 1: 6:00-7:00, 2: 5:30-6:30, 3: 5:00-6:00
        String fromH1 = "06", fromM1 = "00", toH1 = "07", toM1 = "00";
        String fromH2 = "05", fromM2 = "30", toH2 = "06", toM2 = "30";
        String fromH3 = "05", fromM3 = "00", toH3 = "06", toM3 = "00";
        String fromH4 = "06", fromM4 = "00", toH4 = "07", toM4 = "00";
        String day4 = "06";

        try {
            TimeInterval timeInterval1 = new TimeInterval(new String[]{year, month, day, fromH1, fromM1, year, month, day, toH1, toM1});
            TimeInterval timeInterval2 = new TimeInterval(new String[]{year, month, day, fromH2, fromM2, year, month, day, toH2, toM2});
            TimeInterval timeInterval3 = new TimeInterval(new String[]{year, month, day, fromH3, fromM3, year, month, day, toH3, toM3});
            TimeInterval timeInterval4 = new TimeInterval(new String[]{year, month, day4, fromH4, fromM4, year, month, day4, toH4, toM4});

            assertEquals(true, timeInterval1.intersects(timeInterval2));
            assertEquals(true, timeInterval2.intersects(timeInterval1));
            assertEquals(false, timeInterval1.intersects(timeInterval3));
            assertEquals(false, timeInterval3.intersects(timeInterval1));
            assertEquals(true, timeInterval2.intersects(timeInterval3));
            assertEquals(true, timeInterval3.intersects(timeInterval2));
            assertEquals(true, timeInterval1.intersects(timeInterval1));
            assertEquals(false, timeInterval4.intersects(timeInterval1));
            assertEquals(false, timeInterval4.intersects(timeInterval2));
            assertEquals(false, timeInterval4.intersects(timeInterval3));
            assertEquals(false, timeInterval1.intersects(timeInterval4));
            assertEquals(false, timeInterval2.intersects(timeInterval4));
            assertEquals(false, timeInterval3.intersects(timeInterval4));
        }
        catch (Exception e) {
            e.printStackTrace();
            throw new AssertionError();
        }
    }

    @Test
    public void encompassesTest1() {
        String year = "2018", month = "10", day = "5";
        // 1: 6:00-9:00, 2: 6:00-8:30, 3: 8:00-9:00, 4: 6:00-9:00 (other day), 5: 5:30-8:00, 6: 7:30-9:30
        String fromH1 = "06", fromM1 = "00", toH1 = "09", toM1 = "00";
        String fromH2 = "06", fromM2 = "00", toH2 = "08", toM2 = "30";
        String fromH3 = "08", fromM3 = "00", toH3 = "09", toM3 = "00";
        String fromH4 = "06", fromM4 = "00", toH4 = "09", toM4 = "00";
        String fromH5 = "05", fromM5 = "30", toH5 = "08", toM5 = "00";
        String fromH6 = "07", fromM6 = "30", toH6 = "09", toM6 = "30";
        String day4 = "06";

        try {
            TimeInterval timeInterval1 = new TimeInterval(new String[]{year, month, day, fromH1, fromM1, year, month, day, toH1, toM1});
            TimeInterval timeInterval2 = new TimeInterval(new String[]{year, month, day, fromH2, fromM2, year, month, day, toH2, toM2});
            TimeInterval timeInterval3 = new TimeInterval(new String[]{year, month, day, fromH3, fromM3, year, month, day, toH3, toM3});
            TimeInterval timeInterval4 = new TimeInterval(new String[]{year, month, day4, fromH4, fromM4, year, month, day4, toH4, toM4});
            TimeInterval timeInterval5 = new TimeInterval(new String[]{year, month, day, fromH5, fromM5, year, month, day, toH5, toM5});
            TimeInterval timeInterval6 = new TimeInterval(new String[]{year, month, day, fromH6, fromM6, year, month, day, toH6, toM6});

            assertEquals(true, timeInterval1.encompasses(timeInterval1));
            assertEquals(true, timeInterval2.encompasses(timeInterval2));
            assertEquals(true, timeInterval3.encompasses(timeInterval3));
            assertEquals(true, timeInterval4.encompasses(timeInterval4));
            assertEquals(true, timeInterval5.encompasses(timeInterval5));
            assertEquals(true, timeInterval6.encompasses(timeInterval6));

            assertEquals(true, timeInterval1.encompasses(timeInterval2));
            assertEquals(true, timeInterval1.encompasses(timeInterval3));
            assertEquals(false, timeInterval1.encompasses(timeInterval5));
            assertEquals(false, timeInterval1.encompasses(timeInterval6));
            assertEquals(false, timeInterval2.encompasses(timeInterval1));
            assertEquals(false, timeInterval3.encompasses(timeInterval1));
            assertEquals(false, timeInterval5.encompasses(timeInterval1));
            assertEquals(false, timeInterval6.encompasses(timeInterval1));

            assertEquals(false, timeInterval4.encompasses(timeInterval1));
            assertEquals(false, timeInterval4.encompasses(timeInterval2));
            assertEquals(false, timeInterval4.encompasses(timeInterval3));
            assertEquals(false, timeInterval4.encompasses(timeInterval5));
            assertEquals(false, timeInterval4.encompasses(timeInterval6));
            assertEquals(false, timeInterval1.encompasses(timeInterval4));
            assertEquals(false, timeInterval2.encompasses(timeInterval4));
            assertEquals(false, timeInterval3.encompasses(timeInterval4));
            assertEquals(false, timeInterval5.encompasses(timeInterval4));
            assertEquals(false, timeInterval6.encompasses(timeInterval4));

        }
        catch (Exception e) {
            e.printStackTrace();
            throw new AssertionError();
        }
    }

    @Test
    public void hasStarted1() {
        String isotime = "2018-11-19T01:30:00-05:00";

        try {
            DateTime dateTime = new DateTime(isotime);
            assertTrue(!dateTime.isAfterNow());
        }
        catch (Exception e) {
            e.printStackTrace();
            throw new AssertionError();
        }
    }
}
