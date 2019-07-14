package main.java.lambdaFunctionHandlers.highLevelHandlers.deleteDependencyHandlers;

import main.java.databaseOperations.exceptions.PermissionsException;
import main.java.logic.Constants;
import main.java.databaseObjects.Gym;
import main.java.databaseOperations.DatabaseAction;

import java.util.ArrayList;
import java.util.List;

/**
 * Deletes a Gym from the database as well as most dependencies on its Gym ID.
 */
public class DeleteGym {
    public static List<DatabaseAction> getActions(String fromID, String gymID) throws Exception {
        // TODO This is defintely annoying for people, but the only thing to figure out is deleting users from pools
        // TODO SEE IF WE CAN GO INTO AWS COGNITO AND DELETE USERS FROM A USER POOL FOR THIS
        List<DatabaseAction> databaseActions = new ArrayList<>();
        Gym gym = Gym.readGym(gymID);

        if (fromID == null || (!fromID.equals(gymID) && !Constants.isAdmin(fromID))) {
            throw new PermissionsException("You can only delete a gym you own!");
        }

        databaseActions.addAll(DeleteUser.getActions(fromID, gym));

        // Remove all trainers in trainers
        // TODO We'll have to do something in this case, but I'm not sure if we should just delete the trainers
//        for (String trainerID : gym.trainerIDs) {
//            databaseActions.addAll(DeleteTrainer.getActions(fromID, trainerID));
//        }
        // TODO REvisit

        return databaseActions;

    }
}
