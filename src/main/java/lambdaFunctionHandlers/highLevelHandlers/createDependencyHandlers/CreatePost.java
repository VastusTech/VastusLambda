package main.java.lambdaFunctionHandlers.highLevelHandlers.createDependencyHandlers;

import main.java.Logic.Constants;
import main.java.Logic.ItemType;
import main.java.databaseObjects.Challenge;
import main.java.databaseOperations.DatabaseActionCompiler;
import main.java.databaseOperations.DynamoDBHandler;
import main.java.databaseOperations.databaseActionBuilders.ChallengeDatabaseActionBuilder;
import main.java.databaseOperations.databaseActionBuilders.PostDatabaseActionBuilder;
import main.java.databaseOperations.databaseActionBuilders.UserDatabaseActionBuilder;
import main.java.lambdaFunctionHandlers.requestObjects.CreatePostRequest;

public class CreatePost {
    public static String handle(String fromID, CreatePostRequest createPostRequest) throws Exception {
        if (createPostRequest != null) {
            // Check required fields
            if (createPostRequest.by != null && createPostRequest.description != null && createPostRequest
                    .access != null) {
                // Create the database action list for the transaction to complete
                DatabaseActionCompiler databaseActionCompiler = new DatabaseActionCompiler();

                // Create post (with createPostRequest)
                databaseActionCompiler.add(PostDatabaseActionBuilder.create(createPostRequest));

                // Check to see if the request features are well formed (i.e not empty string or invalid date)
                String postType = createPostRequest.postType;
                if (postType != null) {
                    boolean ifType = ItemType.ifItemType(postType);
                    boolean ifNewType = (postType.substring(0, 3).equals("new")) && ItemType.ifItemType(postType.substring(3));
                    boolean ifSubmission = postType.equals("submission");
                    if (!ifType && !ifNewType && !ifSubmission) {
                        throw new Exception("postType must either be empty, \"new\" + <item_type>, just " +
                                "<item_type>, or \"submission\"!!");
                    }
                    else {
                        if (createPostRequest.about == null) {
                            throw new Exception("PostType of " + postType + " missing the \"about\" section!");
                        }
                        if (ifSubmission) {
                            Challenge challenge = Challenge.readChallenge(createPostRequest.about);
                            if (!challenge.members.contains(createPostRequest.by)) {
                                throw new Exception("You must be a part of the challenge to add a submission!");
                            }
//                            if (createPostRequest.picturePaths.length == 0 && createPostRequest.videoPaths.length == 0) {
//                                throw new Exception("Submissions must have at least one photo or video!");
//                            }
                            databaseActionCompiler.add(ChallengeDatabaseActionBuilder.updateAddSubmission
                                    (createPostRequest.about, null, true));
                        }
                    }
                }

                // Make sure the post isn't too much
                if (createPostRequest.picturePaths.length > Constants.postPicturePathsLimit || createPostRequest
                        .videoPaths.length > Constants.postVideoPathsLimit) {
                    throw new Exception("That post has too many pictures and/or videos on it!");
                }


                // Add the post to the by's
                String by = createPostRequest.by;
                String byItemType = ItemType.getItemType(by);
                databaseActionCompiler.add(UserDatabaseActionBuilder.updateAddPost(by, byItemType, null, true));

                return DynamoDBHandler.getInstance().attemptTransaction(databaseActionCompiler);
            }
            else {
                throw new Exception("Required fields missing in createPostRequest!");
            }
        }
        else {
            throw new Exception("createPostRequest not initialized for CREATE statement!");
        }
    }
}
