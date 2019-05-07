package test.java.logic;

import org.joda.time.DateTime;
import org.junit.Test;

import main.java.logic.TimeHelper;

import static org.junit.Assert.assertEquals;

public class TimeHelperTest {
    @Test
    public void test0HourStartsBetween() {
        DateTime from = new DateTime(2019, 3, 15, 0, 0);
        DateTime to = new DateTime(2019, 3, 15, 0, 59);
        assertEquals(0, TimeHelper.hourStartsBetween(from, to));
    }

    @Test
    public void test1HourStartsBetween() {
        DateTime from = new DateTime(2019, 3, 15, 0, 59);
        DateTime to = new DateTime(2019, 3, 15, 1, 0);
        assertEquals(1, TimeHelper.hourStartsBetween(from, to));
    }

    @Test
    public void test0DaysBetween() {
        DateTime from = new DateTime(2019, 3, 15, 0, 0);
        DateTime to = new DateTime(2019, 3, 15, 23, 59);
        assertEquals(0, TimeHelper.midnightsBetween(from, to));
    }

    @Test
    public void test1DayBetween() {
        DateTime from = new DateTime(2019, 3, 15, 23, 59);
        DateTime to = new DateTime(2019, 3, 16, 0, 0);
        assertEquals(1, TimeHelper.midnightsBetween(from, to));
    }

    @Test
    public void test0MondaysBetween() {
        DateTime from = new DateTime(2019, 5, 13, 0, 0);
        DateTime to = new DateTime(2019, 5, 19, 23, 59);
        assertEquals(0, TimeHelper.mondaysBetween(from, to));
    }

    @Test
    public void test1MondaysBetween() {
        DateTime from = new DateTime(2019, 5, 12, 23, 59);
        DateTime to = new DateTime(2019, 5, 13, 0, 0);
        assertEquals(1, TimeHelper.mondaysBetween(from, to));
    }

    @Test
    public void test0MonthStartsBetween() {
        DateTime from = new DateTime(2019, 5, 1, 0, 0);
        DateTime to = new DateTime(2019, 5, 31, 23, 59);
        assertEquals(0, TimeHelper.firstDatesOfMonthBetween(from, to));
    }

    @Test
    public void test1MonthStartsBetween() {
        DateTime from = new DateTime(2019, 5, 31, 23, 59);
        DateTime to = new DateTime(2019, 6, 1, 0, 0);
        assertEquals(1, TimeHelper.firstDatesOfMonthBetween(from, to));
    }

    @Test
    public void test0YearStartsBetween() {
        DateTime from = new DateTime(2019, 1, 1, 0, 0);
        DateTime to = new DateTime(2019, 12, 31, 23, 59);
        assertEquals(0, TimeHelper.firstDatesOfYearBetween(from, to));
    }

    @Test
    public void test1YearStartsBetween() {
        DateTime from = new DateTime(2019, 12, 31, 23, 59);
        DateTime to = new DateTime(2020, 1, 1, 0, 0);
        assertEquals(1, TimeHelper.firstDatesOfYearBetween(from, to));
    }
}
