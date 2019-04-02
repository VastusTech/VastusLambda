package main.java.lambdaFunctionHandlers.highLevelHandlers.createDependencyHandlers;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import main.java.lambdaFunctionHandlers.highLevelHandlers.updateAddDependencyHandlers.StreakAddN;
import main.java.logic.Constants;
import main.java.logic.ItemType;
import main.java.databaseObjects.Challenge;
import main.java.databaseObjects.User;
import main.java.databaseOperations.DatabaseActionCompiler;
import main.java.databaseOperations.databaseActionBuilders.ChallengeDatabaseActionBuilder;
import main.java.databaseOperations.databaseActionBuilders.SubmissionDatabaseActionBuilder;
import main.java.databaseOperations.databaseActionBuilders.UserDatabaseActionBuilder;
import main.java.lambdaFunctionHandlers.requestObjects.CreateSubmissionRequest;

/**
 * Creates a Submission in the database, checks the inputs, adds the Submission to the author and
 * the Challenge it's for, and updates the potential Streak associated with the Submission.
 */
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

                if (challenge.challengeType != null && challenge.challengeType.equals("streak")) {
                    // Then we also update this user's streak!
                    User user = User.readUser(createSubmissionRequest.by);
                    Set<String> intersection = new HashSet<>(challenge.streaks);
                    intersection.retainAll(user.streaks);
                    if (intersection.size() == 0) {
                        throw new Exception("No streak associated with this user, so cannot "
                                + "complete this submission!");
                    }
                    else if (intersection.size() > 1) {
                        throw new Exception("Too many streaks associated with this user, cannot "
                                + "complete this submission!");
                    }
                    else {
                        for (String streakID : intersection) {
                            // INVARIANT: Will only run once whenever it gets here
                            databaseActionCompiler.addAll(StreakAddN.getActions(fromID, streakID));
                        }
                    }
                }

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
