package main.java.lambdaFunctionHandlers.highLevelHandlers.createDependencyHandlers;

import java.util.ArrayList;
import java.util.List;

import main.java.Logic.Constants;
import main.java.Logic.ItemType;
import main.java.databaseObjects.Challenge;
import main.java.databaseOperations.DatabaseActionCompiler;
import main.java.databaseOperations.databaseActionBuilders.ChallengeDatabaseActionBuilder;
import main.java.databaseOperations.databaseActionBuilders.SubmissionDatabaseActionBuilder;
import main.java.databaseOperations.databaseActionBuilders.UserDatabaseActionBuilder;
import main.java.lambdaFunctionHandlers.requestObjects.CreateSubmissionRequest;

public class CreateSubmission {
    public static List<DatabaseActionCompiler> getCompilers(String fromID, CreateSubmissionRequest createSubmissionRequest, boolean ifWithCreate) throws Exception {
        if (createSubmissionRequest != null) {
            // Check required fields
            if (createSubmissionRequest.by != null && createSubmissionRequest.description != null &&
                    createSubmissionRequest.about != null) {
                // Create the database action list for the transaction to complete
                List<DatabaseActionCompiler> compilers = new ArrayList<>();
                DatabaseActionCompiler databaseActionCompiler = new DatabaseActionCompiler();

                if (!(fromID.equals(createSubmissionRequest.by)) && !fromID.equals(Constants.adminKey)) {
                    throw new Exception("PERMISSIONS ERROR: You can only create submissions as yourself!");
                }

                // Create post (with createPostRequest)
                databaseActionCompiler.add(SubmissionDatabaseActionBuilder.create(createSubmissionRequest, ifWithCreate));

                Challenge challenge = Challenge.readChallenge(createSubmissionRequest.about);
                if (!challenge.members.contains(createSubmissionRequest.by)) {
                    throw new Exception("You must be a part of the challenge to add a submission!");
                }
//                if (createSubmissionRequest.picturePaths.length == 0 &&
//                        createSubmissionRequest.videoPaths.length == 0) {
//                    throw new Exception("Submissions must have at least one photo or video!");
//                }
                databaseActionCompiler.add(ChallengeDatabaseActionBuilder.updateAddSubmission
                        (createSubmissionRequest.about, null, true));

                databaseActionCompiler.getNotificationHandler().addAddNotification(
                        createSubmissionRequest.about, "submissions", "", true
                );
                databaseActionCompiler.getNotificationHandler().setCreateFlag(createSubmissionRequest.about);

                // Make sure the post isn't too much
                if ((createSubmissionRequest.picturePaths != null && createSubmissionRequest.picturePaths.length > Constants
                        .postPicturePathsLimit) || (createSubmissionRequest.videoPaths != null && createSubmissionRequest
                        .videoPaths.length > Constants.postVideoPathsLimit)) {
                    throw new Exception("That post has too many pictures and/or videos on it!");
                }

                // Add the post to the by's
                String by = createSubmissionRequest.by;
                String byItemType = ItemType.getItemType(by);
                databaseActionCompiler.add(UserDatabaseActionBuilder.updateAddSubmission(by, byItemType, null, true));

                compilers.add(databaseActionCompiler);

                return compilers;
            }
            else {
                throw new Exception("Required fields missing in createSubmissionRequest!");
            }
        }
        else {
            throw new Exception("createSubmissionRequest not initialized for CREATE statement!");
        }
    }
}
