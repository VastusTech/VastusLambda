package test.java.logicTests;

import org.joda.time.DateTime;
import org.junit.Test;

import main.java.logic.TimeHelper;

import static org.junit.Assert.assertEquals;

public class TimeHelperTest {
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
}
