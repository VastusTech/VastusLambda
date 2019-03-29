package main.java.lambdaFunctionHandlers.highLevelHandlers.updateAddDependencyHandlers;

import java.util.ArrayList;
import java.util.List;

import main.java.databaseOperations.databaseActionBuilders.StreakDatabaseActionBuilder;
import main.java.logic.Constants;
import main.java.databaseObjects.Streak;
import main.java.databaseOperations.DatabaseAction;
import main.java.logic.TimeHelper;

/**
 * TODO
 */
public class StreakAddN {
    public static List<DatabaseAction> getActions(String fromID, String streakID) throws Exception {
        List<DatabaseAction> databaseActions = new ArrayList<>();

        Streak streak = Streak.readStreak(streakID);
        if (!fromID.equals(streak.owner) && !fromID.equals(Constants.adminKey)) {
            throw new Exception("PERMISSIONS ERROR: You can only update a streak as yourself!");
        }

        // Update the streak
        // If you still have a streak and it is still valid, update the N and the

        int numSpansPassed;
        switch (streak.updateSpanType) {
            case daily:
                numSpansPassed = TimeHelper.midnightsPassed(streak.lastUpdated);
                break;
            default:
                throw new Exception("Unhandled streak span type!");
        }
        // We update the last updated, to show that we've just updated it now.
        databaseActions.add(StreakDatabaseActionBuilder.updateLastUpdated(streakID,
                TimeHelper.nowString()));

        if (numSpansPassed < 2 * streak.updateInterval) {
            // Then the current streak is still viable
            int currentN = streak.currentN;
            if (numSpansPassed >= streak.updateInterval) {
                // Then this is also in a new span, so we reset currentN
                currentN = 0;
            }
            // Then we check to see if the currentN is where it needs to be to update the N.
            if (currentN + 1 == streak.streakN) {
                // Then, we know that the streak has been accomplished and can be updated.
                databaseActions.add(StreakDatabaseActionBuilder.updateAddN(streakID));
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
            }
        }
        else {
            // Then this is already passed the expiration limit and the whole streak resets.
            // Then we reset the streak and everything
            databaseActions.add(StreakDatabaseActionBuilder.resetNToOne(streakID));
            databaseActions.add(StreakDatabaseActionBuilder.resetCurrentNToOne(streakID));
        }

        return databaseActions;
    }
}
