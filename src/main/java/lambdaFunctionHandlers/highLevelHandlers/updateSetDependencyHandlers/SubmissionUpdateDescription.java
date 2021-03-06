package main.java.lambdaFunctionHandlers.highLevelHandlers.updateSetDependencyHandlers;

import java.util.ArrayList;
import java.util.List;

import main.java.databaseOperations.exceptions.PermissionsException;
import main.java.logic.Constants;
import main.java.databaseObjects.Submission;
import main.java.databaseOperations.DatabaseAction;
import main.java.databaseOperations.databaseActionBuilders.SubmissionDatabaseActionBuilder;

/**
 * Update a Submission's description.
 */
public class SubmissionUpdateDescription {
    public static List<DatabaseAction> getActions(String fromID, String submissionID, String description) throws Exception {
        List<DatabaseAction> databaseActions = new ArrayList<>();

        Submission submission = Submission.readSubmission(submissionID);

        if (fromID == null || (!fromID.equals(submission.by) && !Constants.isAdmin(fromID))) {
            throw new PermissionsException("You can only update a submission you own!");
        }

        // Get all the actions for this process
        databaseActions.add(SubmissionDatabaseActionBuilder.updateDescription(submissionID, description));

        return databaseActions;
    }
}
