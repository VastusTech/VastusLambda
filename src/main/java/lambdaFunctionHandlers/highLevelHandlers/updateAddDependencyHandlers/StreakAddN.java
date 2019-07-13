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

        // Get the information for how long has passed since the User contributed to the Streak.
        int numSpansPassed = streak.updateSpansPassed(now);
        int streakN = streak.streakN;
        int currentN = streak.currentN;

        // We update the last updated, to show that we've just updated it now.
        databaseActions.add(StreakDatabaseActionBuilder.updateLastUpdated(streakID,
                TimeHelper.isoString(now)));

        // If the Streak has expired, then it can't update
        if (!streak.hasExpired(numSpansPassed)) {
            // Then the current streak is still viable
            if (streak.isInNewSpan(numSpansPassed)) {
                // Then this is also in a new span, so we reset currentN
                currentN = 0;

                // The new attempt was started, so update the last attempt started value
                databaseActions.add(StreakDatabaseActionBuilder.updateLastAttemptStarted(streakID,
                        TimeHelper.isoString(streak.getNextAttemptStarted())));
            }

            // Check to see if the currentN is where it needs to be to update the N.
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
