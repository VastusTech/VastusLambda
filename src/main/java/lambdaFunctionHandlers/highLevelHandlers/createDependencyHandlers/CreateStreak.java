package main.java.lambdaFunctionHandlers.highLevelHandlers.createDependencyHandlers;

import main.java.Logic.Constants;
import main.java.databaseObjects.Streak;
import main.java.databaseOperations.DatabaseActionCompiler;
import main.java.databaseOperations.DynamoDBHandler;
import main.java.databaseOperations.databaseActionBuilders.StreakDatabaseActionBuilder;
import main.java.lambdaFunctionHandlers.requestObjects.CreateStreakRequest;

public class CreateStreak {
    public static String handle(String fromID, CreateStreakRequest createStreakRequest) throws Exception {
        if (createStreakRequest != null) {
            // Create Streak
            if (createStreakRequest.owner != null && createStreakRequest.about != null && createStreakRequest
                    .streakType != null) {
                DatabaseActionCompiler databaseActionCompiler = new DatabaseActionCompiler();

                // TODO Permissions?

                // Check to see if the request features are well formed (i.e not invalid date or time)
                try {
                    Streak.StreakType.valueOf(createStreakRequest.streakType);
                }
                catch (IllegalArgumentException e) {
                    throw new Exception("Could not understand streakType: " + createStreakRequest.streakType);
                }

                // Add the create statement
                databaseActionCompiler.add(StreakDatabaseActionBuilder.create(createStreakRequest));

                return DynamoDBHandler.getInstance().attemptTransaction(databaseActionCompiler);
            }
            else {
                throw new Exception("createClientRequest is missing required fields!");
            }
        }
        else {
            throw new Exception("createMessageRequest not initialized for CREATE statement!");
        }
    }
}
