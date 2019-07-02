package main.java.lambdaFunctionHandlers.highLevelHandlers.updateSetDependencyHandlers;

import java.util.ArrayList;
import java.util.List;

import main.java.databaseObjects.Submission;
import main.java.databaseOperations.DatabaseAction;
import main.java.databaseOperations.databaseActionBuilders.SubmissionDatabaseActionBuilder;
import main.java.lambdaFunctionHandlers.highLevelHandlers.updateAddDependencyHandlers.UserAddNewCredit;
import main.java.logic.Constants;
import main.java.logic.ItemType;

public class SubmissionApprove {
    public static List<DatabaseAction> getActions(String fromID, String submissionID, String approved) throws Exception {
        List<DatabaseAction> databaseActions = new ArrayList<>();

        Submission submission = Submission.readSubmission(submissionID);

        if (!fromID.equals(submission.by) && !fromID.equals(Constants.adminKey)) {
            throw new Exception("PERMISSIONS ERROR: You can only update a submission you own!");
        }

        // Get all the actions for this process
        databaseActions.add(SubmissionDatabaseActionBuilder.updateApproved(submissionID, Boolean.parseBoolean(approved)));

        // Grant the User new credits
        databaseActions.addAll(UserAddNewCredit.getActions(fromID, submission.by, ItemType.getItemType(submission.by), Constants.tokenCreditValue));

        return databaseActions;
    }
}
