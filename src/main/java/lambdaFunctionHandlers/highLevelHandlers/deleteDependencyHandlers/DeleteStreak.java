package main.java.lambdaFunctionHandlers.highLevelHandlers.deleteDependencyHandlers;

import java.util.ArrayList;
import java.util.List;

import main.java.databaseObjects.Challenge;
import main.java.databaseObjects.Streak;
import main.java.databaseOperations.DatabaseAction;
import main.java.databaseOperations.databaseActionBuilders.ChallengeDatabaseActionBuilder;
import main.java.databaseOperations.databaseActionBuilders.StreakDatabaseActionBuilder;
import main.java.databaseOperations.databaseActionBuilders.UserDatabaseActionBuilder;
import main.java.logic.Constants;
import main.java.logic.ItemType;

/**
 * Deletes a Streak from the database, as well as any dependencies there may be to its Streak ID.
 */
public class DeleteStreak {
    public static List<DatabaseAction> getActions(String fromID, String streakID) throws Exception {
        List<DatabaseAction> databaseActions = new ArrayList<>();
        Streak streak = Streak.readStreak(streakID);

        // TODO This permission might be bum????????????/
        if (!fromID.equals(streak.owner) && !fromID.equals(Constants.adminKey)) {
            throw new Exception("PERMISSIONS ERROR: You can only update a Streak for yourself!");
        }

        // Remove from the owner
        databaseActions.add(UserDatabaseActionBuilder.updateRemoveStreak(streak.owner,
                ItemType.getItemType(streak.owner), streakID));

        // Remove from the about
        if (streak.about != null && ItemType.getItemType(streak.about).equals("Challenge")) {
            databaseActions.add(ChallengeDatabaseActionBuilder.updateRemoveStreak(streak.about, streakID));
        }

        // Delete the streak
        databaseActions.add(StreakDatabaseActionBuilder.delete(streakID));

        return databaseActions;
    }
}
