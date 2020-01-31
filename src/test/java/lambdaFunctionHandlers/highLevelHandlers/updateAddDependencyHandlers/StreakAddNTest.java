package test.java.lambdaFunctionHandlers.highLevelHandlers.updateAddDependencyHandlers;

import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;

import java.util.Collections;

import main.java.databaseObjects.Streak;
import main.java.databaseOperations.DatabaseActionCompiler;
import main.java.databaseOperations.DynamoDBHandler;
import main.java.lambdaFunctionHandlers.highLevelHandlers.updateAddDependencyHandlers.StreakAddN;
import main.java.logic.Constants;
import main.java.logic.TimeHelper;
import main.java.testing.TestHelper;
import test.java.LocalDynamoDBCreationRule;

import static org.junit.Assert.assertEquals;

public class StreakAddNTest {
    @ClassRule
    public static LocalDynamoDBCreationRule rule = new LocalDynamoDBCreationRule();

    @Before
    public void init() throws Exception {
        TestHelper.reinitTablesFromJSON("table2.json", "table1.json");
    }

    // ===========================================================================================
    // ==                            EXPECTED SUCCESS TESTS                                     ==
    // ===========================================================================================

    // Streak N = 1

    @Test
    public void testAddFirstStreakNImmediately() throws Exception {
        String id = "ST682504643262";
        DateTime lastAttemptStarted = TimeHelper.hoursFrom(Streak.readStreak(id).lastUpdated, 0);
        testStreakAttributes(getStreakAfterUpdates(id, 0),
                1, 1, 1, lastAttemptStarted);
    }

    @Test
    public void testAddFirstStreakNAfterABit() throws Exception {
        String id = "ST682504643262";
        DateTime lastAttemptStarted = TimeHelper.hoursFrom(Streak.readStreak(id).lastUpdated, 24);
        testStreakAttributes(getStreakAfterUpdates(id, 24),
                1, 1, 1, lastAttemptStarted);
    }

    @Test
    public void testAddFirstStreakNAfterALongerWhile() throws Exception {
        String id = "ST682504643262";
        DateTime lastAttemptStarted = TimeHelper.hoursFrom(Streak.readStreak(id).lastUpdated, 48);
        testStreakAttributes(getStreakAfterUpdates(id, 48),
                1, 1, 1, lastAttemptStarted);
    }

    // Early Early
    @Test
    public void testAddStreakNEarlyEarly() throws Exception {
        String id = "ST682504643262";
        DateTime lastAttemptStarted = TimeHelper.hoursFrom(Streak.readStreak(id).lastUpdated, 0);
        testStreakAttributes(getStreakAfterUpdates(id, 0, 1),
                1, 1, 2, lastAttemptStarted);
    }

    // Early On Time
    @Test
    public void testAddStreakNEarlyOnTime() throws Exception {
        String id = "ST682504643262";
        DateTime lastAttemptStarted = TimeHelper.hoursFrom(Streak.readStreak(id).lastUpdated, 24);
        testStreakAttributes(getStreakAfterUpdates(id, 0, 24),
                2, 2, 1, lastAttemptStarted);
    }
    // Early Late
    @Test
    public void testAddStreakNEarlyLate() throws Exception {
        String id = "ST682504643262";
        DateTime lastAttemptStarted = TimeHelper.hoursFrom(Streak.readStreak(id).lastUpdated, 48);
        testStreakAttributes(getStreakAfterUpdates(id, 0, 48),
                1, 1, 1, lastAttemptStarted);
    }
    // On Time Early
    @Test
    public void testAddStreakNOnTimeEarly() throws Exception {
        String id = "ST682504643262";
        DateTime lastAttemptStarted = TimeHelper.hoursFrom(Streak.readStreak(id).lastUpdated, 24);
        testStreakAttributes(getStreakAfterUpdates(id, 24, 25),
                1, 1, 2, lastAttemptStarted);
    }
    // On Time On Time
    @Test
    public void testAddStreakNOnTimeOnTime() throws Exception {
        String id = "ST682504643262";
        DateTime lastAttemptStarted = TimeHelper.hoursFrom(Streak.readStreak(id).lastUpdated, 48);
        testStreakAttributes(getStreakAfterUpdates(id, 24, 48),
                2, 2, 1, lastAttemptStarted);
    }
    // On Time Late
    @Test
    public void testAddStreakNOnTimeLate() throws Exception {
        String id = "ST682504643262";
        DateTime lastAttemptStarted = TimeHelper.hoursFrom(Streak.readStreak(id).lastUpdated, 72);
        testStreakAttributes(getStreakAfterUpdates(id, 24, 72),
                1, 1, 1, lastAttemptStarted);
    }
    @Test
    public void testAddStreakNEarlyEarlyEarly() throws Exception {
        String id = "ST682504643262";
        DateTime lastAttemptStarted = TimeHelper.hoursFrom(Streak.readStreak(id).lastUpdated, 0);
        testStreakAttributes(getStreakAfterUpdates(id, 0, 1, 2),
                1, 1, 3, lastAttemptStarted);
    }
    @Test
    public void testAddStreakNEarlyEarlyOnTime() throws Exception {
        String id = "ST682504643262";
        DateTime lastAttemptStarted = TimeHelper.hoursFrom(Streak.readStreak(id).lastUpdated, 24);
        testStreakAttributes(getStreakAfterUpdates(id, 0, 1, 24),
                2, 2, 1, lastAttemptStarted);
    }
    @Test
    public void testAddStreakNEarlyEarlyLate() throws Exception {
        String id = "ST682504643262";
        DateTime lastAttemptStarted = TimeHelper.hoursFrom(Streak.readStreak(id).lastUpdated, 48);
        testStreakAttributes(getStreakAfterUpdates(id, 0, 1, 48),
                1, 1, 1, lastAttemptStarted);
    }
    @Test
    public void testAddStreakNEarlyOnTimeEarly() throws Exception {
        String id = "ST682504643262";
        DateTime lastAttemptStarted = TimeHelper.hoursFrom(Streak.readStreak(id).lastUpdated, 24);
        testStreakAttributes(getStreakAfterUpdates(id, 0, 24, 25),
                2, 2, 2, lastAttemptStarted);
    }
    @Test
    public void testAddStreakNEarlyOnTimeOnTime() throws Exception {
        String id = "ST682504643262";
        DateTime lastAttemptStarted = TimeHelper.hoursFrom(Streak.readStreak(id).lastUpdated, 48);
        testStreakAttributes(getStreakAfterUpdates(id, 0, 24, 48),
                3, 3, 1, lastAttemptStarted);
    }
    @Test
    public void testAddStreakNEarlyOnTimeLate() throws Exception {
        String id = "ST682504643262";
        DateTime lastAttemptStarted = TimeHelper.hoursFrom(Streak.readStreak(id).lastUpdated, 72);
        testStreakAttributes(getStreakAfterUpdates(id, 0, 24, 72),
                1, 2, 1, lastAttemptStarted);
    }
    @Test
    public void testAddStreakNOnTimeEarlyEarly() throws Exception {
        String id = "ST682504643262";
        DateTime lastAttemptStarted = TimeHelper.hoursFrom(Streak.readStreak(id).lastUpdated, 24);
        testStreakAttributes(getStreakAfterUpdates(id, 24, 25, 26),
                1, 1, 3, lastAttemptStarted);
    }
    @Test
    public void testAddStreakNOnTimeEarlyOnTime() throws Exception {
        String id = "ST682504643262";
        DateTime lastAttemptStarted = TimeHelper.hoursFrom(Streak.readStreak(id).lastUpdated, 48);
        testStreakAttributes(getStreakAfterUpdates(id, 24, 25, 48),
                2, 2, 1, lastAttemptStarted);
    }
    @Test
    public void testAddStreakNOnTimeEarlyLate() throws Exception {
        String id = "ST682504643262";
        DateTime lastAttemptStarted = TimeHelper.hoursFrom(Streak.readStreak(id).lastUpdated, 72);
        testStreakAttributes(getStreakAfterUpdates(id, 24, 25, 72),
                1, 1, 1, lastAttemptStarted);
    }
    @Test
    public void testAddStreakNOnTimeOnTimeEarly() throws Exception {
        String id = "ST682504643262";
        DateTime lastAttemptStarted = TimeHelper.hoursFrom(Streak.readStreak(id).lastUpdated, 48);
        testStreakAttributes(getStreakAfterUpdates(id, 24, 48, 49),
                2, 2, 2, lastAttemptStarted);
    }
    @Test
    public void testAddStreakNOnTimeOnTimeOnTime() throws Exception {
        String id = "ST682504643262";
        DateTime lastAttemptStarted = TimeHelper.hoursFrom(Streak.readStreak(id).lastUpdated, 72);
        testStreakAttributes(getStreakAfterUpdates(id, 24, 48, 72),
                3, 3, 1, lastAttemptStarted);
    }
    @Test
    public void testAddStreakNOnTimeOnTimeLate() throws Exception {
        String id = "ST682504643262";
        DateTime lastAttemptStarted = TimeHelper.hoursFrom(Streak.readStreak(id).lastUpdated, 96);
        testStreakAttributes(getStreakAfterUpdates(id, 24, 48, 96),
                1, 2, 1, lastAttemptStarted);
    }

    // StreakN = 2

    // Early On Time
    // Early Late
    // On Time Early
    // On Time On Time
    // On Time Late
    // Late Early
    // Late On Time
    // Late Late
    // Early Early [Early]

    // Early
    @Test
    public void testAddFirstCurrentNImmediatelyFor2N() throws Exception {
        String id = "ST675218239921";
        DateTime lastAttemptStarted = TimeHelper.hoursFrom(Streak.readStreak(id).lastUpdated, 0);
        testStreakAttributes(getStreakAfterUpdates(id, 0),
                0, 0, 1, lastAttemptStarted);
    }

    // On Time
    @Test
    public void testAddFirstCurrentNAfterABitFor2N() throws Exception {
        String id = "ST675218239921";
        DateTime lastAttemptStarted = TimeHelper.hoursFrom(Streak.readStreak(id).lastUpdated, 25);
        testStreakAttributes(getStreakAfterUpdates(id, 25),
                0, 0, 1, lastAttemptStarted);
    }

    // Late
    @Test
    public void testAddFirstCurrentNAfterALongerWhileFor2N() throws Exception {
        String id = "ST675218239921";
        DateTime lastAttemptStarted = TimeHelper.hoursFrom(Streak.readStreak(id).lastUpdated, 50);
        testStreakAttributes(getStreakAfterUpdates(id, 50),
                0, 0, 1, lastAttemptStarted);
    }

    @Test
    public void testCurrentNThenEarly() throws Exception {
        String id = "ST675218239921";
        DateTime lastAttemptStarted = TimeHelper.hoursFrom(Streak.readStreak(id).lastUpdated, 0);
        testStreakAttributes(getStreakAfterUpdates(id, 0, 1),
                1, 1, 2, lastAttemptStarted);
    }

    @Test
    public void testCurrentNThenOnTime() throws Exception {
        String id = "ST675218239921";
        DateTime lastAttemptStarted = TimeHelper.hoursFrom(Streak.readStreak(id).lastUpdated, 24);
        testStreakAttributes(getStreakAfterUpdates(id, 0, 24),
                0, 0, 1, lastAttemptStarted);
    }
    @Test
    public void testCurrentNThenLate() throws Exception {
        String id = "ST675218239921";
        DateTime lastAttemptStarted = TimeHelper.hoursFrom(Streak.readStreak(id).lastUpdated, 48);
        testStreakAttributes(getStreakAfterUpdates(id, 0, 48),
                0, 0, 1, lastAttemptStarted);
    }
    @Test
    public void testCurrentNThenEarlyEarly() throws Exception {
        String id = "ST675218239921";
        DateTime lastAttemptStarted = TimeHelper.hoursFrom(Streak.readStreak(id).lastUpdated, 0);
        testStreakAttributes(getStreakAfterUpdates(id, 0, 1, 2),
                1, 1, 3, lastAttemptStarted);
    }
    @Test
    public void testCurrentNThenEarlyOnTime() throws Exception {
        String id = "ST675218239921";
        DateTime lastAttemptStarted = TimeHelper.hoursFrom(Streak.readStreak(id).lastUpdated, 24);
        testStreakAttributes(getStreakAfterUpdates(id, 0, 1, 24),
                1, 1, 1, lastAttemptStarted);
    }
    @Test
    public void testCurrentNThenEarlyLate() throws Exception {
        String id = "ST675218239921";
        DateTime lastAttemptStarted = TimeHelper.hoursFrom(Streak.readStreak(id).lastUpdated, 48);
        testStreakAttributes(getStreakAfterUpdates(id, 0, 1, 48),
                0, 1, 1, lastAttemptStarted);
    }
    @Test
    public void testCurrentNThenOnTimeEarly() throws Exception {
        String id = "ST675218239921";
        DateTime lastAttemptStarted = TimeHelper.hoursFrom(Streak.readStreak(id).lastUpdated, 24);
        testStreakAttributes(getStreakAfterUpdates(id, 0, 24, 25),
                1, 1, 2, lastAttemptStarted);
    }
    @Test
    public void testCurrentNThenOnTimeOnTime() throws Exception {
        String id = "ST675218239921";
        DateTime lastAttemptStarted = TimeHelper.hoursFrom(Streak.readStreak(id).lastUpdated, 48);
        testStreakAttributes(getStreakAfterUpdates(id, 0, 24, 48),
                0, 0, 1, lastAttemptStarted);
    }
    @Test
    public void testCurrentNThenOnTimeLate() throws Exception {
        String id = "ST675218239921";
        DateTime lastAttemptStarted = TimeHelper.hoursFrom(Streak.readStreak(id).lastUpdated, 72);
        testStreakAttributes(getStreakAfterUpdates(id, 0, 24, 72),
                0, 0, 1, lastAttemptStarted);
    }
    @Test
    public void testCurrentNThenLateEarly() throws Exception {
        String id = "ST675218239921";
        DateTime lastAttemptStarted = TimeHelper.hoursFrom(Streak.readStreak(id).lastUpdated, 48);
        testStreakAttributes(getStreakAfterUpdates(id, 0, 48, 49),
                1, 1, 2, lastAttemptStarted);
    }
    @Test
    public void testCurrentNThenLateOnTime() throws Exception {
        String id = "ST675218239921";
        DateTime lastAttemptStarted = TimeHelper.hoursFrom(Streak.readStreak(id).lastUpdated, 72);
        testStreakAttributes(getStreakAfterUpdates(id, 0, 48, 72),
                0, 0, 1, lastAttemptStarted);
    }
    @Test
    public void testCurrentNThenLateLate() throws Exception {
        String id = "ST675218239921";
        DateTime lastAttemptStarted = TimeHelper.hoursFrom(Streak.readStreak(id).lastUpdated, 96);
        testStreakAttributes(getStreakAfterUpdates(id, 0, 48, 96),
                0, 0, 1, lastAttemptStarted);
    }
    @Test
    public void testCurrentNThenEarlyEarlyEarly() throws Exception {
        String id = "ST675218239921";
        DateTime lastAttemptStarted = TimeHelper.hoursFrom(Streak.readStreak(id).lastUpdated, 0);
        testStreakAttributes(getStreakAfterUpdates(id, 0, 1, 2, 3),
                1, 1, 4, lastAttemptStarted);
    }
    @Test
    public void testCurrentNThenEarlyEarlyOnTime() throws Exception {
        String id = "ST675218239921";
        DateTime lastAttemptStarted = TimeHelper.hoursFrom(Streak.readStreak(id).lastUpdated, 24);
        testStreakAttributes(getStreakAfterUpdates(id, 0, 1, 2, 24),
                1, 1, 1, lastAttemptStarted);
    }
    @Test
    public void testCurrentNThenEarlyEarlyLate() throws Exception {
        String id = "ST675218239921";
        DateTime lastAttemptStarted = TimeHelper.hoursFrom(Streak.readStreak(id).lastUpdated, 48);
        testStreakAttributes(getStreakAfterUpdates(id, 0, 1, 2, 48),
                0, 1, 1, lastAttemptStarted);
    }
    @Test
    public void testCurrentNThenEarlyOnTimeEarly() throws Exception {
        String id = "ST675218239921";
        DateTime lastAttemptStarted = TimeHelper.hoursFrom(Streak.readStreak(id).lastUpdated, 24);
        testStreakAttributes(getStreakAfterUpdates(id, 0, 1, 24, 25),
                2, 2, 2, lastAttemptStarted);
    }
    @Test
    public void testCurrentNThenEarlyOnTimeOnTime() throws Exception {
        String id = "ST675218239921";
        DateTime lastAttemptStarted = TimeHelper.hoursFrom(Streak.readStreak(id).lastUpdated, 48);
        testStreakAttributes(getStreakAfterUpdates(id, 0, 1, 24, 48),
                0, 1, 1, lastAttemptStarted);
    }
    @Test
    public void testCurrentNThenEarlyOnTimeLate() throws Exception {
        String id = "ST675218239921";
        DateTime lastAttemptStarted = TimeHelper.hoursFrom(Streak.readStreak(id).lastUpdated, 72);
        testStreakAttributes(getStreakAfterUpdates(id, 0, 1, 24, 72),
                0, 1, 1, lastAttemptStarted);
    }
    @Test
    public void testCurrentNThenEarlyLateEarly() throws Exception {
        String id = "ST675218239921";
        DateTime lastAttemptStarted = TimeHelper.hoursFrom(Streak.readStreak(id).lastUpdated, 48);
        testStreakAttributes(getStreakAfterUpdates(id, 0, 1, 48, 49),
                1, 1, 2, lastAttemptStarted);
    }
    @Test
    public void testCurrentNThenEarlyLateOnTime() throws Exception {
        String id = "ST675218239921";
        DateTime lastAttemptStarted = TimeHelper.hoursFrom(Streak.readStreak(id).lastUpdated, 72);
        testStreakAttributes(getStreakAfterUpdates(id, 0, 1, 48, 72),
                0, 1, 1, lastAttemptStarted);
    }
    @Test
    public void testCurrentNThenEarlyLateLate() throws Exception {
        String id = "ST675218239921";
        DateTime lastAttemptStarted = TimeHelper.hoursFrom(Streak.readStreak(id).lastUpdated, 96);
        testStreakAttributes(getStreakAfterUpdates(id, 0, 1, 48, 96),
                0, 1, 1, lastAttemptStarted);
    }
    @Test
    public void testCurrentNThenOnTimeEarlyEarly() throws Exception {
        String id = "ST675218239921";
        DateTime lastAttemptStarted = TimeHelper.hoursFrom(Streak.readStreak(id).lastUpdated, 24);
        testStreakAttributes(getStreakAfterUpdates(id, 0, 24, 25, 26),
                1, 1, 3, lastAttemptStarted);
    }
    @Test
    public void testCurrentNThenOnTimeEarlyOnTime() throws Exception {
        String id = "ST675218239921";
        DateTime lastAttemptStarted = TimeHelper.hoursFrom(Streak.readStreak(id).lastUpdated, 48);
        testStreakAttributes(getStreakAfterUpdates(id, 0, 24, 25, 48),
                1, 1, 1, lastAttemptStarted);
    }
    @Test
    public void testCurrentNThenOnTimeEarlyLate() throws Exception {
        String id = "ST675218239921";
        DateTime lastAttemptStarted = TimeHelper.hoursFrom(Streak.readStreak(id).lastUpdated, 72);
        testStreakAttributes(getStreakAfterUpdates(id, 0, 24, 25, 72),
                0, 1, 1, lastAttemptStarted);
    }
    @Test
    public void testCurrentNThenOnTimeOnTimeEarly() throws Exception {
        String id = "ST675218239921";
        DateTime lastAttemptStarted = TimeHelper.hoursFrom(Streak.readStreak(id).lastUpdated, 48);
        testStreakAttributes(getStreakAfterUpdates(id, 0, 24, 48, 49),
                1, 1, 2, lastAttemptStarted);
    }
    @Test
    public void testCurrentNThenOnTimeOnTimeOnTime() throws Exception {
        String id = "ST675218239921";
        DateTime lastAttemptStarted = TimeHelper.hoursFrom(Streak.readStreak(id).lastUpdated, 72);
        testStreakAttributes(getStreakAfterUpdates(id, 0, 24, 48, 72),
                0, 0, 1, lastAttemptStarted);
    }
    @Test
    public void testCurrentNThenOnTimeOnTimeLate() throws Exception {
        String id = "ST675218239921";
        DateTime lastAttemptStarted = TimeHelper.hoursFrom(Streak.readStreak(id).lastUpdated, 96);
        testStreakAttributes(getStreakAfterUpdates(id, 0, 24, 48, 96),
                0, 0, 1, lastAttemptStarted);
    }
    @Test
    public void testCurrentNThenOnTimeLateEarly() throws Exception {
        String id = "ST675218239921";
        DateTime lastAttemptStarted = TimeHelper.hoursFrom(Streak.readStreak(id).lastUpdated, 72);
        testStreakAttributes(getStreakAfterUpdates(id, 0, 24, 72, 73),
                1, 1, 2, lastAttemptStarted);
    }
    @Test
    public void testCurrentNThenOnTimeLateOnTime() throws Exception {
        String id = "ST675218239921";
        DateTime lastAttemptStarted = TimeHelper.hoursFrom(Streak.readStreak(id).lastUpdated, 96);
        testStreakAttributes(getStreakAfterUpdates(id, 0, 24, 72, 96),
                0, 0, 1, lastAttemptStarted);
    }
    @Test
    public void testCurrentNThenOnTimeLateLate() throws Exception {
        String id = "ST675218239921";
        DateTime lastAttemptStarted = TimeHelper.hoursFrom(Streak.readStreak(id).lastUpdated, 120);
        testStreakAttributes(getStreakAfterUpdates(id, 0, 24, 72, 120),
                0, 0, 1, lastAttemptStarted);
    }
    @Test
    public void testCurrentNThenLateEarlyEarly() throws Exception {
        String id = "ST675218239921";
        DateTime lastAttemptStarted = TimeHelper.hoursFrom(Streak.readStreak(id).lastUpdated, 48);
        testStreakAttributes(getStreakAfterUpdates(id, 0, 48, 49, 50),
                1, 1, 3, lastAttemptStarted);
    }
    @Test
    public void testCurrentNThenLateEarlyOnTime() throws Exception {
        String id = "ST675218239921";
        DateTime lastAttemptStarted = TimeHelper.hoursFrom(Streak.readStreak(id).lastUpdated, 72);
        testStreakAttributes(getStreakAfterUpdates(id, 0, 48, 49, 72),
                1, 1, 1, lastAttemptStarted);
    }
    @Test
    public void testCurrentNThenLateEarlyLate() throws Exception {
        String id = "ST675218239921";
        DateTime lastAttemptStarted = TimeHelper.hoursFrom(Streak.readStreak(id).lastUpdated, 96);
        testStreakAttributes(getStreakAfterUpdates(id, 0, 48, 49, 96),
                0, 1, 1, lastAttemptStarted);
    }
    @Test
    public void testCurrentNThenLateOnTimeEarly() throws Exception {
        String id = "ST675218239921";
        DateTime lastAttemptStarted = TimeHelper.hoursFrom(Streak.readStreak(id).lastUpdated, 72);
        testStreakAttributes(getStreakAfterUpdates(id, 0, 48, 72, 73),
                1, 1, 2, lastAttemptStarted);
    }
    @Test
    public void testCurrentNThenLateOnTimeOnTime() throws Exception {
        String id = "ST675218239921";
        DateTime lastAttemptStarted = TimeHelper.hoursFrom(Streak.readStreak(id).lastUpdated, 96);
        testStreakAttributes(getStreakAfterUpdates(id, 0, 48, 72, 96),
                0, 0, 1, lastAttemptStarted);
    }
    @Test
    public void testCurrentNThenLateOnTimeLate() throws Exception {
        String id = "ST675218239921";
        DateTime lastAttemptStarted = TimeHelper.hoursFrom(Streak.readStreak(id).lastUpdated, 120);
        testStreakAttributes(getStreakAfterUpdates(id, 0, 48, 72, 120),
                0, 0, 1, lastAttemptStarted);
    }
    @Test
    public void testCurrentNThenLateLateEarly() throws Exception {
        String id = "ST675218239921";
        DateTime lastAttemptStarted = TimeHelper.hoursFrom(Streak.readStreak(id).lastUpdated, 96);
        testStreakAttributes(getStreakAfterUpdates(id, 0, 48, 96, 97),
                1, 1, 2, lastAttemptStarted);
    }
    @Test
    public void testCurrentNThenLateLateOnTime() throws Exception {
        String id = "ST675218239921";
        DateTime lastAttemptStarted = TimeHelper.hoursFrom(Streak.readStreak(id).lastUpdated, 120);
        testStreakAttributes(getStreakAfterUpdates(id, 0, 48, 96, 120),
                0, 0, 1, lastAttemptStarted);
    }
    @Test
    public void testCurrentNThenLateLateLate() throws Exception {
        String id = "ST675218239921";
        DateTime lastAttemptStarted = TimeHelper.hoursFrom(Streak.readStreak(id).lastUpdated, 144);
        testStreakAttributes(getStreakAfterUpdates(id, 0, 48, 96, 144),
                0, 0, 1, lastAttemptStarted);
    }

    // StreakN = 2 and updateInterval = 2

    @Test
    public void testAddFirstCurrentNImmediatelyFor2Update() throws Exception {
        String id = "ST008119683625";
        DateTime lastAttemptStarted = TimeHelper.hoursFrom(Streak.readStreak(id).lastUpdated, 0);
        testStreakAttributes(getStreakAfterUpdates(id, 0),
                0, 0, 1, lastAttemptStarted);
    }

    // On Time
    @Test
    public void testAddFirstCurrentNAfterABitFor2Update() throws Exception {
        String id = "ST008119683625";
        DateTime lastAttemptStarted = TimeHelper.hoursFrom(Streak.readStreak(id).lastUpdated, 48);
        testStreakAttributes(getStreakAfterUpdates(id, 48),
                0, 0, 1, lastAttemptStarted);
    }

    // Late
    @Test
    public void testAddFirstCurrentNAfterALongerWhileFor2Update() throws Exception {
        String id = "ST008119683625";
        DateTime lastAttemptStarted = TimeHelper.hoursFrom(Streak.readStreak(id).lastUpdated, 96);
        testStreakAttributes(getStreakAfterUpdates(id, 96),
                0, 0, 1, lastAttemptStarted);
    }

    @Test
    public void test2UpdateCurrentNThenEarly() throws Exception {
        String id = "ST008119683625";
        DateTime lastAttemptStarted = TimeHelper.hoursFrom(Streak.readStreak(id).lastUpdated, 0);
        testStreakAttributes(getStreakAfterUpdates(id, 0, 1),
                1, 1, 2, lastAttemptStarted);
    }

    @Test
    public void test2UpdateCurrentNThenOnTime() throws Exception {
        String id = "ST008119683625";
        DateTime lastAttemptStarted = TimeHelper.hoursFrom(Streak.readStreak(id).lastUpdated, 48);
        testStreakAttributes(getStreakAfterUpdates(id, 0, 48),
                0, 0, 1, lastAttemptStarted);
    }
    @Test
    public void test2UpdateCurrentNThenLate() throws Exception {
        String id = "ST008119683625";
        DateTime lastAttemptStarted = TimeHelper.hoursFrom(Streak.readStreak(id).lastUpdated, 48);
        testStreakAttributes(getStreakAfterUpdates(id, 0, 48),
                0, 0, 1, lastAttemptStarted);
    }
    @Test
    public void test2UpdateCurrentNThenEarlyEarly() throws Exception {
        String id = "ST008119683625";
        DateTime lastAttemptStarted = TimeHelper.hoursFrom(Streak.readStreak(id).lastUpdated, 0);
        testStreakAttributes(getStreakAfterUpdates(id, 0, 1, 2),
                1, 1, 3, lastAttemptStarted);
    }
    @Test
    public void test2UpdateCurrentNThenEarlyOnTime() throws Exception {
        String id = "ST008119683625";
        DateTime lastAttemptStarted = TimeHelper.hoursFrom(Streak.readStreak(id).lastUpdated, 48);
        testStreakAttributes(getStreakAfterUpdates(id, 0, 1, 48),
                1, 1, 1, lastAttemptStarted);
    }
    @Test
    public void test2UpdateCurrentNThenEarlyLate() throws Exception {
        String id = "ST008119683625";
        DateTime lastAttemptStarted = TimeHelper.hoursFrom(Streak.readStreak(id).lastUpdated, 96);
        testStreakAttributes(getStreakAfterUpdates(id, 0, 1, 96),
                0, 1, 1, lastAttemptStarted);
    }
    @Test
    public void test2UpdateCurrentNThenOnTimeEarly() throws Exception {
        String id = "ST008119683625";
        DateTime lastAttemptStarted = TimeHelper.hoursFrom(Streak.readStreak(id).lastUpdated, 48);
        testStreakAttributes(getStreakAfterUpdates(id, 0, 48, 49),
                1, 1, 2, lastAttemptStarted);
    }
    @Test
    public void test2UpdateCurrentNThenOnTimeOnTime() throws Exception {
        String id = "ST008119683625";
        DateTime lastAttemptStarted = TimeHelper.hoursFrom(Streak.readStreak(id).lastUpdated, 96);
        testStreakAttributes(getStreakAfterUpdates(id, 0, 48, 96),
                0, 0, 1, lastAttemptStarted);
    }
    @Test
    public void test2UpdateCurrentNThenOnTimeLate() throws Exception {
        String id = "ST008119683625";
        DateTime lastAttemptStarted = TimeHelper.hoursFrom(Streak.readStreak(id).lastUpdated, 144);
        testStreakAttributes(getStreakAfterUpdates(id, 0, 48, 144),
                0, 0, 1, lastAttemptStarted);
    }
    @Test
    public void test2UpdateCurrentNThenLateEarly() throws Exception {
        String id = "ST008119683625";
        DateTime lastAttemptStarted = TimeHelper.hoursFrom(Streak.readStreak(id).lastUpdated, 96);
        testStreakAttributes(getStreakAfterUpdates(id, 0, 96, 97),
                1, 1, 2, lastAttemptStarted);
    }
    @Test
    public void test2UpdateCurrentNThenLateOnTime() throws Exception {
        String id = "ST008119683625";
        DateTime lastAttemptStarted = TimeHelper.hoursFrom(Streak.readStreak(id).lastUpdated, 144);
        testStreakAttributes(getStreakAfterUpdates(id, 0, 96, 144),
                0, 0, 1, lastAttemptStarted);
    }
    @Test
    public void test2UpdateCurrentNThenLateLate() throws Exception {
        String id = "ST008119683625";
        DateTime lastAttemptStarted = TimeHelper.hoursFrom(Streak.readStreak(id).lastUpdated, 192);
        testStreakAttributes(getStreakAfterUpdates(id, 0, 96, 192),
                0, 0, 1, lastAttemptStarted);
    }
    @Test
    public void test2UpdateCurrentNThenEarlyEarlyEarly() throws Exception {
        String id = "ST008119683625";
        DateTime lastAttemptStarted = TimeHelper.hoursFrom(Streak.readStreak(id).lastUpdated, 0);
        testStreakAttributes(getStreakAfterUpdates(id, 0, 1, 2, 3),
                1, 1, 4, lastAttemptStarted);
    }
    @Test
    public void test2UpdateCurrentNThenEarlyEarlyOnTime() throws Exception {
        String id = "ST008119683625";
        DateTime lastAttemptStarted = TimeHelper.hoursFrom(Streak.readStreak(id).lastUpdated, 48);
        testStreakAttributes(getStreakAfterUpdates(id, 0, 1, 2, 48),
                1, 1, 1, lastAttemptStarted);
    }
    @Test
    public void test2UpdateCurrentNThenEarlyEarlyLate() throws Exception {
        String id = "ST008119683625";
        DateTime lastAttemptStarted = TimeHelper.hoursFrom(Streak.readStreak(id).lastUpdated, 96);
        testStreakAttributes(getStreakAfterUpdates(id, 0, 1, 2, 96),
                0, 1, 1, lastAttemptStarted);
    }
    @Test
    public void test2UpdateCurrentNThenEarlyOnTimeEarly() throws Exception {
        String id = "ST008119683625";
        DateTime lastAttemptStarted = TimeHelper.hoursFrom(Streak.readStreak(id).lastUpdated, 48);
        testStreakAttributes(getStreakAfterUpdates(id, 0, 1, 48, 49),
                2, 2, 2, lastAttemptStarted);
    }
    @Test
    public void test2UpdateCurrentNThenEarlyOnTimeOnTime() throws Exception {
        String id = "ST008119683625";
        DateTime lastAttemptStarted = TimeHelper.hoursFrom(Streak.readStreak(id).lastUpdated, 96);
        testStreakAttributes(getStreakAfterUpdates(id, 0, 1, 48, 96),
                0, 1, 1, lastAttemptStarted);
    }
    @Test
    public void test2UpdateCurrentNThenEarlyOnTimeLate() throws Exception {
        String id = "ST008119683625";
        DateTime lastAttemptStarted = TimeHelper.hoursFrom(Streak.readStreak(id).lastUpdated, 144);
        testStreakAttributes(getStreakAfterUpdates(id, 0, 1, 48, 144),
                0, 1, 1, lastAttemptStarted);
    }
    @Test
    public void test2UpdateCurrentNThenEarlyLateEarly() throws Exception {
        String id = "ST008119683625";
        DateTime lastAttemptStarted = TimeHelper.hoursFrom(Streak.readStreak(id).lastUpdated, 96);
        testStreakAttributes(getStreakAfterUpdates(id, 0, 1, 96, 97),
                1, 1, 2, lastAttemptStarted);
    }
    @Test
    public void test2UpdateCurrentNThenEarlyLateOnTime() throws Exception {
        String id = "ST008119683625";
        DateTime lastAttemptStarted = TimeHelper.hoursFrom(Streak.readStreak(id).lastUpdated, 144);
        testStreakAttributes(getStreakAfterUpdates(id, 0, 1, 96, 144),
                0, 1, 1, lastAttemptStarted);
    }
    @Test
    public void test2UpdateCurrentNThenEarlyLateLate() throws Exception {
        String id = "ST008119683625";
        DateTime lastAttemptStarted = TimeHelper.hoursFrom(Streak.readStreak(id).lastUpdated, 192);
        testStreakAttributes(getStreakAfterUpdates(id, 0, 1, 96, 192),
                0, 1, 1, lastAttemptStarted);
    }
    @Test
    public void test2UpdateCurrentNThenOnTimeEarlyEarly() throws Exception {
        String id = "ST008119683625";
        DateTime lastAttemptStarted = TimeHelper.hoursFrom(Streak.readStreak(id).lastUpdated, 48);
        testStreakAttributes(getStreakAfterUpdates(id, 0, 48, 49, 50),
                1, 1, 3, lastAttemptStarted);
    }
    @Test
    public void test2UpdateCurrentNThenOnTimeEarlyOnTime() throws Exception {
        String id = "ST008119683625";
        DateTime lastAttemptStarted = TimeHelper.hoursFrom(Streak.readStreak(id).lastUpdated, 96);
        testStreakAttributes(getStreakAfterUpdates(id, 0, 48, 49, 96),
                1, 1, 1, lastAttemptStarted);
    }
    @Test
    public void test2UpdateCurrentNThenOnTimeEarlyLate() throws Exception {
        String id = "ST008119683625";
        DateTime lastAttemptStarted = TimeHelper.hoursFrom(Streak.readStreak(id).lastUpdated, 144);
        testStreakAttributes(getStreakAfterUpdates(id, 0, 48, 49, 144),
                0, 1, 1, lastAttemptStarted);
    }
    @Test
    public void test2UpdateCurrentNThenOnTimeOnTimeEarly() throws Exception {
        String id = "ST008119683625";
        DateTime lastAttemptStarted = TimeHelper.hoursFrom(Streak.readStreak(id).lastUpdated, 96);
        testStreakAttributes(getStreakAfterUpdates(id, 0, 48, 96, 97),
                1, 1, 2, lastAttemptStarted);
    }
    @Test
    public void test2UpdateCurrentNThenOnTimeOnTimeOnTime() throws Exception {
        String id = "ST008119683625";
        DateTime lastAttemptStarted = TimeHelper.hoursFrom(Streak.readStreak(id).lastUpdated, 144);
        testStreakAttributes(getStreakAfterUpdates(id, 0, 48, 96, 144),
                0, 0, 1, lastAttemptStarted);
    }
    @Test
    public void test2UpdateCurrentNThenOnTimeOnTimeLate() throws Exception {
        String id = "ST008119683625";
        DateTime lastAttemptStarted = TimeHelper.hoursFrom(Streak.readStreak(id).lastUpdated, 192);
        testStreakAttributes(getStreakAfterUpdates(id, 0, 48, 96, 192),
                0, 0, 1, lastAttemptStarted);
    }
    @Test
    public void test2UpdateCurrentNThenOnTimeLateEarly() throws Exception {
        String id = "ST008119683625";
        DateTime lastAttemptStarted = TimeHelper.hoursFrom(Streak.readStreak(id).lastUpdated, 144);
        testStreakAttributes(getStreakAfterUpdates(id, 0, 48, 144, 145),
                1, 1, 2, lastAttemptStarted);
    }
    @Test
    public void test2UpdateCurrentNThenOnTimeLateOnTime() throws Exception {
        String id = "ST008119683625";
        DateTime lastAttemptStarted = TimeHelper.hoursFrom(Streak.readStreak(id).lastUpdated, 192);
        testStreakAttributes(getStreakAfterUpdates(id, 0, 48, 144, 192),
                0, 0, 1, lastAttemptStarted);
    }
    @Test
    public void test2UpdateCurrentNThenOnTimeLateLate() throws Exception {
        String id = "ST008119683625";
        DateTime lastAttemptStarted = TimeHelper.hoursFrom(Streak.readStreak(id).lastUpdated, 240);
        testStreakAttributes(getStreakAfterUpdates(id, 0, 48, 144, 240),
                0, 0, 1, lastAttemptStarted);
    }
    @Test
    public void test2UpdateCurrentNThenLateEarlyEarly() throws Exception {
        String id = "ST008119683625";
        DateTime lastAttemptStarted = TimeHelper.hoursFrom(Streak.readStreak(id).lastUpdated, 96);
        testStreakAttributes(getStreakAfterUpdates(id, 0, 96, 97, 98),
                1, 1, 3, lastAttemptStarted);
    }
    @Test
    public void test2UpdateCurrentNThenLateEarlyOnTime() throws Exception {
        String id = "ST008119683625";
        DateTime lastAttemptStarted = TimeHelper.hoursFrom(Streak.readStreak(id).lastUpdated, 144);
        testStreakAttributes(getStreakAfterUpdates(id, 0, 96, 97, 144),
                1, 1, 1, lastAttemptStarted);
    }
    @Test
    public void test2UpdateCurrentNThenLateEarlyLate() throws Exception {
        String id = "ST008119683625";
        DateTime lastAttemptStarted = TimeHelper.hoursFrom(Streak.readStreak(id).lastUpdated, 192);
        testStreakAttributes(getStreakAfterUpdates(id, 0, 96, 97, 192),
                0, 1, 1, lastAttemptStarted);
    }
    @Test
    public void test2UpdateCurrentNThenLateOnTimeEarly() throws Exception {
        String id = "ST008119683625";
        DateTime lastAttemptStarted = TimeHelper.hoursFrom(Streak.readStreak(id).lastUpdated, 144);
        testStreakAttributes(getStreakAfterUpdates(id, 0, 96, 144, 145),
                1, 1, 2, lastAttemptStarted);
    }
    @Test
    public void test2UpdateCurrentNThenLateOnTimeOnTime() throws Exception {
        String id = "ST008119683625";
        DateTime lastAttemptStarted = TimeHelper.hoursFrom(Streak.readStreak(id).lastUpdated, 192);
        testStreakAttributes(getStreakAfterUpdates(id, 0, 96, 144, 192),
                0, 0, 1, lastAttemptStarted);
    }
    @Test
    public void test2UpdateCurrentNThenLateOnTimeLate() throws Exception {
        String id = "ST008119683625";
        DateTime lastAttemptStarted = TimeHelper.hoursFrom(Streak.readStreak(id).lastUpdated, 240);
        testStreakAttributes(getStreakAfterUpdates(id, 0, 96, 144, 240),
                0, 0, 1, lastAttemptStarted);
    }
    @Test
    public void test2UpdateCurrentNThenLateLateEarly() throws Exception {
        String id = "ST008119683625";
        DateTime lastAttemptStarted = TimeHelper.hoursFrom(Streak.readStreak(id).lastUpdated, 192);
        testStreakAttributes(getStreakAfterUpdates(id, 0, 96, 192, 193),
                1, 1, 2, lastAttemptStarted);
    }
    @Test
    public void test2UpdateCurrentNThenLateLateOnTime() throws Exception {
        String id = "ST008119683625";
        DateTime lastAttemptStarted = TimeHelper.hoursFrom(Streak.readStreak(id).lastUpdated, 240);
        testStreakAttributes(getStreakAfterUpdates(id, 0, 96, 192, 240),
                0, 0, 1, lastAttemptStarted);
    }
    @Test
    public void test2UpdateCurrentNThenLateLateLate() throws Exception {
        String id = "ST008119683625";
        DateTime lastAttemptStarted = TimeHelper.hoursFrom(Streak.readStreak(id).lastUpdated, 288);
        testStreakAttributes(getStreakAfterUpdates(id, 0, 96, 192, 288),
                0, 0, 1, lastAttemptStarted);
    }

    @Test
    public void test2UpdateCarryAttemptStartedTimeDefense() throws Exception {
        String id = "ST008119683625";
        DateTime lastAttemptStarted = TimeHelper.hoursFrom(Streak.readStreak(id).lastUpdated, 144);
        testStreakAttributes(getStreakAfterUpdates(id, 0, 24, 72, 73, 144),
                0, 2, 1, lastAttemptStarted);
    }

    private void testStreak(String id, int n, int bestN, int currentN, int lastAttemptedHour, int... updateHours) throws Exception {
        DateTime lastAttemptStarted = TimeHelper.hoursFrom(Streak.readStreak(id).lastUpdated, lastAttemptedHour);
        testStreakAttributes(getStreakAfterUpdates(id, updateHours), n, bestN, currentN, lastAttemptStarted);
    }

    private Streak getStreakAfterUpdates(String id, int... updateHours) throws Exception {
        Streak streak = Streak.readStreak(id);
        for (int hours : updateHours) {
            DynamoDBHandler.getInstance().attemptTransaction(Collections.singletonList(
                    new DatabaseActionCompiler(StreakAddN.getActions(Constants.adminKey,
                            streak.id, TimeHelper.hoursFrom(streak.lastUpdated, hours)))));
        }
        return Streak.readStreak(id);
    }

    private void testStreakAttributes(Streak streak, int n, int bestN, int currentN, DateTime lastAttemptStarted) {
        assertEquals(n, streak.N);
        assertEquals(bestN, streak.bestN);
        assertEquals(currentN, streak.currentN);
        assertEquals(lastAttemptStarted, streak.lastAttemptStarted);
    }

    // ===========================================================================================
    // ==                              EXPECTED ERROR TESTS                                     ==
    // ===========================================================================================

    // Permissions error
    // DateTime too early

    // TODO

}
