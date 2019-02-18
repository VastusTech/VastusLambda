package main.java.lambdaFunctionHandlers.highLevelHandlers.createDependencyHandlers;

import java.util.ArrayList;
import java.util.List;

import main.java.Logic.Constants;
import main.java.Logic.ItemType;
import main.java.databaseObjects.Challenge;
import main.java.databaseObjects.Group;
import main.java.databaseOperations.DatabaseActionCompiler;
import main.java.databaseOperations.DynamoDBHandler;
import main.java.databaseOperations.databaseActionBuilders.ChallengeDatabaseActionBuilder;
import main.java.databaseOperations.databaseActionBuilders.GroupDatabaseActionBuilder;
import main.java.databaseOperations.databaseActionBuilders.PostDatabaseActionBuilder;
import main.java.databaseOperations.databaseActionBuilders.UserDatabaseActionBuilder;
import main.java.lambdaFunctionHandlers.requestObjects.CreatePostRequest;

public class CreatePost {
    public static List<DatabaseActionCompiler> getCompilers(String fromID, CreatePostRequest createPostRequest, boolean ifWithCreate) throws Exception {
        if (createPostRequest != null) {
            // Check required fields
            if (createPostRequest.by != null && createPostRequest.description != null) {
                // Create the database action list for the transaction to complete
                List<DatabaseActionCompiler> compilers = new ArrayList<>();
                DatabaseActionCompiler databaseActionCompiler = new DatabaseActionCompiler();

                if (!(fromID.equals(createPostRequest.by)) && !fromID.equals(Constants.adminKey)) {
                    throw new Exception("PERMISSIONS ERROR: You can only create posts as yourself!");
                }

                if (createPostRequest.access != null) {
                    if (!createPostRequest.access.equals("private")
                            && !createPostRequest.access.equals("public")) {
                        throw new Exception("The access must be either \"public\" or \"private\"!!!");
                    }
                }
                else if (createPostRequest.group != null) {
                    createPostRequest.access = Group.readGroup(createPostRequest.group).access;
                }
                else {
                    throw new Exception("Create post access must either be set or inherit from a group");
                }

                // Create post (with createPostRequest)
                databaseActionCompiler.add(PostDatabaseActionBuilder.create(createPostRequest, ifWithCreate));

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

                            databaseActionCompiler.getNotificationHandler().addAddNotification(
                                    createPostRequest.about, "submissions", "", true
                            );
                            databaseActionCompiler.getNotificationHandler().setCreateFlag(createPostRequest.about);
                        }
                    }
                }

                // Make sure the post isn't too much
                if ((createPostRequest.picturePaths != null && createPostRequest.picturePaths.length > Constants
                        .postPicturePathsLimit) || (createPostRequest.videoPaths != null && createPostRequest
                        .videoPaths.length > Constants.postVideoPathsLimit)) {
                    throw new Exception("That post has too many pictures and/or videos on it!");
                }

                // Add the post to the by's
                String by = createPostRequest.by;
                String byItemType = ItemType.getItemType(by);
                databaseActionCompiler.add(UserDatabaseActionBuilder.updateAddPost(by, byItemType, null, true));

                // Add to the post's group
                if (createPostRequest.group != null) {
                    databaseActionCompiler.add(GroupDatabaseActionBuilder.updateAddPost(createPostRequest.group,
                            null, true));
                }

                compilers.add(databaseActionCompiler);

                return compilers;
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
