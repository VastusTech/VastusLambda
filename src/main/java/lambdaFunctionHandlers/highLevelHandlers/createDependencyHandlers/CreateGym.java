package main.java.lambdaFunctionHandlers.highLevelHandlers.createDependencyHandlers;

import main.java.databaseObjects.TimeInterval;
import main.java.databaseOperations.DatabaseAction;
import main.java.databaseOperations.DatabaseActionCompiler;
import main.java.databaseOperations.DynamoDBHandler;
import main.java.databaseOperations.databaseActionBuilders.GymDatabaseActionBuilder;
import main.java.lambdaFunctionHandlers.requestObjects.CreateGymRequest;
import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.List;

/**
 * Creates a Gym in the database and checks the inputs.
 */
public class CreateGym {
    public static List<DatabaseActionCompiler> getCompilers(String fromID, CreateGymRequest createGymRequest, boolean ifWithCreate) throws Exception {
        if (createGymRequest != null) {
            if (createGymRequest.name != null && createGymRequest.foundingDay != null && createGymRequest.email !=
                    null && createGymRequest.username != null && createGymRequest.address != null && createGymRequest
                    .sessionCapacity != null) {
                List<DatabaseActionCompiler> compilers = new ArrayList<>();
                DatabaseActionCompiler databaseActionCompiler = new DatabaseActionCompiler();

                // Check to see if the request features are well formed (i.e invalid date)
                if (createGymRequest.foundingDay != null) { new DateTime(createGymRequest.foundingDay); }
                Integer.parseInt(createGymRequest.sessionCapacity);
                if (createGymRequest.weeklyHours != null) {
                    for (String time : createGymRequest.weeklyHours) {
                        new TimeInterval(time);
                    }
                }
                if (createGymRequest.paymentSplit != null) { Float.parseFloat(createGymRequest.paymentSplit); }


                // Create Gym
                databaseActionCompiler.add(GymDatabaseActionBuilder.create(createGymRequest, ifWithCreate));

                compilers.add(databaseActionCompiler);

                return compilers;
            }
            else {
                throw new Exception("createGymRequest is missing required fields!");
            }
        }
        else {
            throw new Exception("createGymRequest not initialized for CREATE statement!");
        }
    }
}
