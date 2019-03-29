package main.java.lambdaFunctionHandlers.highLevelHandlers.deleteDependencyHandlers;

import java.util.ArrayList;
import java.util.List;

import main.java.logic.Constants;
import main.java.logic.ItemType;
import main.java.databaseObjects.Submission;
import main.java.databaseOperations.DatabaseAction;
import main.java.databaseOperations.databaseActionBuilders.ChallengeDatabaseActionBuilder;
import main.java.databaseOperations.databaseActionBuilders.SubmissionDatabaseActionBuilder;
import main.java.databaseOperations.databaseActionBuilders.UserDatabaseActionBuilder;

/**
 * TODO
 */
public class DeleteSubmission {
    public static List<DatabaseAction> getActions(String fromID, String submissionID) throws Exception {
        List<DatabaseAction> databaseActions = new ArrayList<>();
        Submission submission = Submission.readSubmission(submissionID);

        if (!fromID.equals(submission.by) && !fromID.equals(Constants.adminKey)) {
            throw new Exception("PERMISSIONS ERROR: You can only delete a submission you created!");
        }

        // TODO =======================================================================================================
        // TODO We should be deleting far fewer "dependencies" in order to make sure as little info as possible is lost
        // TODO =======================================================================================================

        // Remove from posts by field
        String by = submission.by;
        String byItemType = ItemType.getItemType(by);
        databaseActions.add(UserDatabaseActionBuilder.updateRemoveSubmission(by, byItemType, submissionID));

        // Check if we are also removing this post from the challenge submissions
        databaseActions.add(ChallengeDatabaseActionBuilder.updateRemoveSubmission(submission.about, submissionID));

        // Remove from liked
        for (String likedID : submission.likes) {
            String likeItemType = ItemType.getItemType(likedID);
            databaseActions.add(UserDatabaseActionBuilder.updateRemoveLike(likedID, likeItemType, submissionID));
        }

        // Remove from comments
        for (String commentID : submission.comments) {
            databaseActions.addAll(DeleteComment.getActions(fromID, commentID));
        }

        // Delete the submission
        databaseActions.add(SubmissionDatabaseActionBuilder.delete(submissionID));

        return databaseActions;
    }
}
