package main.java.lambdaFunctionHandlers.highLevelHandlers.deleteDependencyHandlers;

import main.java.databaseObjects.Trainer;
import main.java.databaseOperations.DatabaseAction;
import main.java.databaseOperations.databaseActionBuilders.GymDatabaseActionBuilder;
import main.java.databaseOperations.databaseActionBuilders.TrainerDatabaseActionBuilder;

import java.util.ArrayList;
import java.util.List;

public class DeleteTrainer {
    public static List<DatabaseAction> getActions(String fromID, String trainerID) throws Exception {
        List<DatabaseAction> databaseActions = new ArrayList<>();

        if (!fromID.equals(trainerID) && !fromID.equals("admin")) {
            throw new Exception("PERMISSIONS ERROR: You can only delete a trainer if it's yourself!");
        }

        Trainer trainer = Trainer.readTrainer(trainerID);

        // Remove all reviews in reviews by and reviews about
        for (String reviewID : trainer.reviewsBy) {
            databaseActions.addAll(DeleteReview.getActions(fromID, reviewID));
        }
        for (String reviewID : trainer.reviewsAbout) {
            databaseActions.addAll(DeleteReview.getActions(fromID, reviewID));
        }

        // Remove all workouts in scheduled workouts and completed workouts (Cancel them)
        for (String workoutID : trainer.scheduledWorkouts) {
            databaseActions.addAll(DeleteWorkout.getActions(fromID, workoutID));
        }
        for (String workoutID : trainer.completedWorkouts) {
            databaseActions.addAll(DeleteWorkout.getActions(fromID, workoutID));
        }

        // Remove from gym's trainers field
        databaseActions.add(GymDatabaseActionBuilder.updateRemoveTrainer(trainer.gym, trainerID));

        // Delete the Trainer
        databaseActions.add(TrainerDatabaseActionBuilder.delete(trainerID));

        return databaseActions;
    }
}
