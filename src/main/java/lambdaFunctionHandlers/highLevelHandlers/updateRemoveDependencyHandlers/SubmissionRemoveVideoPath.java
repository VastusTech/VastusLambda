package main.java.lambdaFunctionHandlers.highLevelHandlers.updateRemoveDependencyHandlers;

import java.util.ArrayList;
import java.util.List;

import main.java.databaseOperations.exceptions.PermissionsException;
import main.java.logic.Constants;
import main.java.databaseObjects.Submission;
import main.java.databaseOperations.DatabaseAction;
import main.java.databaseOperations.databaseActionBuilders.SubmissionDatabaseActionBuilder;

/**
 * Removes a video path from the Submission.
 */
public class SubmissionRemoveVideoPath {
    public static List<DatabaseAction> getActions(String fromID, String submissionID, String videoPath) throws Exception {
        List<DatabaseAction> databaseActions = new ArrayList<>();

        Submission submission = Submission.readSubmission(submissionID);

        if (fromID == null || (!fromID.equals(submission.by) && !Constants.isAdmin(fromID))) {
            throw new PermissionsException("You can only update a submission you own!");
        }

        // Get all the actions for this process
        databaseActions.add(SubmissionDatabaseActionBuilder.updateRemoveVideoPath(submissionID, videoPath));

        return databaseActions;
    }
}
