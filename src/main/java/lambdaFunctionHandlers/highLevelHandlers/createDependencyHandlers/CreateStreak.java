package main.java.lambdaFunctionHandlers.highLevelHandlers.createDependencyHandlers;

import java.util.ArrayList;
import java.util.List;

import main.java.databaseObjects.Streak;
import main.java.databaseOperations.DatabaseActionCompiler;
import main.java.databaseOperations.databaseActionBuilders.ChallengeDatabaseActionBuilder;
import main.java.databaseOperations.databaseActionBuilders.StreakDatabaseActionBuilder;
import main.java.databaseOperations.databaseActionBuilders.UserDatabaseActionBuilder;
import main.java.lambdaFunctionHandlers.requestObjects.CreateStreakRequest;
import main.java.logic.Constants;
import main.java.logic.ItemType;

/**
 * TODO
 */
public class CreateStreak {
    public static List<DatabaseActionCompiler> getCompilers(String fromID, CreateStreakRequest createStreakRequest, boolean ifWithCreate) throws Exception {
        if (createStreakRequest != null) {
            // Create Streak
            if (createStreakRequest.owner != null && createStreakRequest.about != null
                    && createStreakRequest.streakType != null
                    && createStreakRequest.updateInterval != null
                    && createStreakRequest.streakN != null) {
                List<DatabaseActionCompiler> compilers = new ArrayList<>();
                DatabaseActionCompiler databaseActionCompiler = new DatabaseActionCompiler();

                if (!fromID.equals(createStreakRequest.owner) && !fromID.equals(Constants.adminKey)) {
                    throw new Exception("PERMISSIONS ERROR: You can only create streaks you're going to own!");
                }

                // Check to see if the request features are well formed (i.e not invalid date or time)
                try {
                    Streak.StreakType.valueOf(createStreakRequest.streakType);
                }
                catch (IllegalArgumentException e) {
                    throw new Exception("Could not understand streakType: " + createStreakRequest.streakType);
                }
                try {
                    switch (Streak.UpdateSpanType.valueOf(createStreakRequest.updateSpanType)) {
                        case daily:
                            break;
                        default:
                            throw new Exception("Update Span Type: " + createStreakRequest.updateSpanType
                                    + ", not implemented yet!");
                    }
                }
                catch (IllegalArgumentException e) {
                    throw new Exception("Could not understand updateSpanType: " +
                            createStreakRequest.updateSpanType);
                }
                try {
                    Integer.parseInt(createStreakRequest.updateInterval);
                }
                catch (NumberFormatException e) {
                    throw new Exception("Could not read updateInterval: "
                            + createStreakRequest.updateInterval);
                }
                try {
                    Integer.parseInt(createStreakRequest.streakN);
                }
                catch (NumberFormatException e) {
                    throw new Exception("Could not read streakN: " + createStreakRequest.streakN);
                }

                // Add the create statement
                databaseActionCompiler.add(StreakDatabaseActionBuilder.create(createStreakRequest,
                        ifWithCreate));

                // Add to owner and about
                String ownerItemType = ItemType.getItemType(createStreakRequest.owner);
                databaseActionCompiler.add(UserDatabaseActionBuilder.updateAddStreak(
                        createStreakRequest.owner, ownerItemType, null, true));

                String aboutItemType = ItemType.getItemType(createStreakRequest.about);
                if (aboutItemType.equals("Challenge")) {
                    databaseActionCompiler.add(ChallengeDatabaseActionBuilder.updateAddStreak(
                            createStreakRequest.about, null, true));
                }
                else {
                    throw new Exception("About item type not recognized for a Streak! Type: " + aboutItemType);
                }

                compilers.add(databaseActionCompiler);

                return compilers;
            }
            else {
                throw new Exception("createStreakRequest is missing required fields!");
            }
        }
        else {
            throw new Exception("createStreakRequest not initialized for CREATE statement!");
        }
    }
}
