package main.java.lambdaFunctionHandlers.highLevelHandlers.deleteDependencyHandlers;

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

        if (!fromID.equals(gymID) && !fromID.equals("admin")) {
            throw new Exception("PERMISSIONS ERROR: You can only delete a gym you own!");
        }

        Gym gym = Gym.readGym(gymID);

        // Remove all reviews in reviews by and reviews about
        for (String reviewID : gym.reviewsBy) {
            databaseActions.addAll(DeleteReview.getActions(fromID, reviewID));
        }
        for (String reviewID : gym.reviewsAbout) {
            databaseActions.addAll(DeleteReview.getActions(fromID, reviewID));
        }

        // Remove all trainers in trainers
        for (String trainerID : gym.trainerIDs) {
            databaseActions.addAll(DeleteTrainer.getActions(fromID, trainerID));
        }

        return databaseActions;

    }
}
