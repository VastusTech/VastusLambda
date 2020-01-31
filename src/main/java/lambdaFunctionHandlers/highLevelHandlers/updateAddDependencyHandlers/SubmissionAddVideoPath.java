package main.java.lambdaFunctionHandlers.highLevelHandlers.updateAddDependencyHandlers;

import java.util.ArrayList;
import java.util.List;

import main.java.databaseOperations.exceptions.PermissionsException;
import main.java.logic.Constants;
import main.java.databaseObjects.Submission;
import main.java.databaseOperations.DatabaseAction;
import main.java.databaseOperations.databaseActionBuilders.SubmissionDatabaseActionBuilder;

/**
 * Adds a video path to an existing Submission, making sure that there aren't already too many.
 */
public class SubmissionAddVideoPath {
    public static List<DatabaseAction> getActions(String fromID, String submissionID, String videoPath) throws Exception {
        List<DatabaseAction> databaseActions = new ArrayList<>();

        Submission submission = Submission.readSubmission(submissionID);

        if (fromID == null || (!fromID.equals(submission.by) && !Constants.isAdmin(fromID))) {
            throw new PermissionsException("You can only update a submission you own!");
        }

        if (submission.videoPaths != null && submission.videoPaths.size() >= Constants.postVideoPathsLimit) {
            throw new Exception("This post is already filled with the max number of pictures!");
        }

        // Get all the actions for this process
        databaseActions.add(SubmissionDatabaseActionBuilder.updateAddVideoPath(submissionID, videoPath));

        return databaseActions;
    }
}
