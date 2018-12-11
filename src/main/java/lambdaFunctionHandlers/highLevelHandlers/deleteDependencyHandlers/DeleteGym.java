package main.java.lambdaFunctionHandlers.highLevelHandlers.deleteDependencyHandlers;

import main.java.Logic.Constants;
import main.java.databaseObjects.Gym;
import main.java.databaseOperations.DatabaseAction;
import main.java.lambdaFunctionHandlers.highLevelHandlers.deleteDependencyHandlers.DeleteReview;
import main.java.lambdaFunctionHandlers.highLevelHandlers.deleteDependencyHandlers.DeleteTrainer;

import java.util.ArrayList;
import java.util.List;

public class DeleteGym {
    public static List<DatabaseAction> getActions(String fromID, String gymID) throws Exception {
        // TODO This is defintely annoying for people, but the only thing to figure out is deleting users from pools
        // TODO SEE IF WE CAN GO INTO AWS COGNITO AND DELETE USERS FROM A USER POOL FOR THIS
        List<DatabaseAction> databaseActions = new ArrayList<>();

        if (!fromID.equals(gymID) && !fromID.equals(Constants.adminKey)) {
            throw new Exception("PERMISSIONS ERROR: You can only delete a gym you own!");
        }

        // TODO =======================================================================================================
        // TODO We should be deleting far fewer "dependencies" in order to make sure as little info as possible is lost
        // TODO =======================================================================================================

        Gym gym = Gym.readGym(gymID);

        databaseActions.addAll(DeleteUser.getActions(fromID, gym));

        // Remove all trainers in trainers
        // TODO We'll have to do something in this case, but I'm not sure if we should just delete the trainers
//        for (String trainerID : gym.trainerIDs) {
//            databaseActions.addAll(DeleteTrainer.getActions(fromID, trainerID));
//        }

        return databaseActions;

    }
}
