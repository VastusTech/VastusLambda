package main.java.lambdaFunctionHandlers.highLevelHandlers.updateAddDependencyHandlers;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.List;

import main.java.databaseOperations.databaseActionBuilders.StreakDatabaseActionBuilder;
import main.java.logic.Constants;
import main.java.databaseObjects.Streak;
import main.java.databaseOperations.DatabaseAction;
import main.java.logic.TimeHelper;

/**
 * Indicates that a task for a Streak has been completed and that the Streak needs to be updated.
 * Checks that the Streak hasn't been expired yet and that it is still in the correct span to update
 * correctly. Includes sub-task logic, for N tasks in a span of time.
 */
public class StreakAddN {
    public static List<DatabaseAction> getActions(String fromID, String streakID, DateTime now) throws Exception {
        List<DatabaseAction> databaseActions = new ArrayList<>();

        Streak streak = Streak.readStreak(streakID);
        if (!fromID.equals(streak.owner) && !fromID.equals(Constants.adminKey)) {
            throw new Exception("PERMISSIONS ERROR: You can only update a streak as yourself!");
        }

        // Update the streak
        // If you still have a streak and it is still valid, update the N and the

        int currentN = streak.currentN;
        int streakN = streak.streakN;
        int numSpansPassed;
        if (currentN == 0) {
            // Freshly started so it is expired from the gecko
            numSpansPassed = 2 * streak.updateInterval;
        }
        else {
            switch (streak.updateSpanType) {
                case hourly:
                    numSpansPassed = TimeHelper.hourStartsBetween(streak.lastAttemptStarted, now);
                    break;
                case daily:
                    numSpansPassed = TimeHelper.midnightsBetween(streak.lastAttemptStarted, now);
                    break;
                case weekly:
                    numSpansPassed = TimeHelper.mondaysBetween(streak.lastAttemptStarted, now);
                    break;
                case monthly:
                    numSpansPassed = TimeHelper.firstDatesOfMonthBetween(streak.lastAttemptStarted, now);
                    break;
                case yearly:
                    numSpansPassed = TimeHelper.firstDatesOfYearBetween(streak.lastAttemptStarted, now);
                    break;
                default:
                    throw new Exception("Unhandled streak span type!");
            }
        }

        // We update the last updated, to show that we've just updated it now.
        databaseActions.add(StreakDatabaseActionBuilder.updateLastUpdated(streakID,
                TimeHelper.isoString(now)));

        // If either the streak update interval has passed or the last attempt failed, it can't update
        if (numSpansPassed < 2 * streak.updateInterval && !(numSpansPassed >= streak.updateInterval && currentN < streak.streakN)) {
            // Then the current streak is still viable
            if (numSpansPassed >= streak.updateInterval) {
                // Then this is also in a new span, so we reset currentN
                currentN = 0;

                // The new attempt was started, so update the last attempt started value
                databaseActions.add(StreakDatabaseActionBuilder.updateLastAttemptStarted(streakID,
                        TimeHelper.isoString(now)));
            }
            // Then we check to see if the currentN is where it needs to be to update the N.
            if (currentN + 1 == streak.streakN) {
                // Then, we know that the streak has been accomplished and can be updated.
                databaseActions.add(StreakDatabaseActionBuilder.updateAddN(streakID));
                if (currentN == 0) {
                    // If it was reset, just set it to 1
                    databaseActions.add(StreakDatabaseActionBuilder.resetCurrentNToOne(streakID));
                }
                else {
                    // otherwise add to it
                    databaseActions.add(StreakDatabaseActionBuilder.updateAddCurrentN(streakID));
                }
                // Also set the bestN if you're setting the bar higher.
                if (streak.N >= streak.bestN) {
                    databaseActions.add(StreakDatabaseActionBuilder.updateBestN(streakID,
                            Integer.toString(streak.N + 1)));
                }
            }
            else {
                // Otherwise, we just update currentN to what it's supposed to be
                if (currentN == 0) {
                    databaseActions.add(StreakDatabaseActionBuilder.resetCurrentNToOne(streakID));
                }
                else {
                    databaseActions.add(StreakDatabaseActionBuilder.updateAddCurrentN(streakID));
                }

                // We don't update the last updated, because this is simply a currentN update and we
                // want to maintain the time between each Streak updates, not just currentN updates.
            }
        }
        else {
            // Then this is already passed the expiration limit and the whole streak resets.
            // Then we reset the streak and everything
            if (streakN > 1) {
                databaseActions.add(StreakDatabaseActionBuilder.resetNToZero(streakID));
            }
            else {
                databaseActions.add(StreakDatabaseActionBuilder.resetNToOne(streakID));
                if (streak.bestN == 0) {
                    databaseActions.add(StreakDatabaseActionBuilder.updateBestN(streakID, "1"));
                }
            }
            databaseActions.add(StreakDatabaseActionBuilder.resetCurrentNToOne(streakID));

            // The new attempt was started, so update the last attempt started value
            databaseActions.add(StreakDatabaseActionBuilder.updateLastAttemptStarted(streakID,
                    TimeHelper.isoString(now)));
        }

        return databaseActions;
    }
}
