package main.java.lambdaFunctionHandlers.highLevelHandlers.createDependencyHandlers;

import java.util.ArrayList;
import java.util.List;

import main.java.logic.Constants;
import main.java.logic.ItemType;
import main.java.databaseObjects.Group;
import main.java.databaseOperations.DatabaseActionCompiler;
import main.java.databaseOperations.databaseActionBuilders.GroupDatabaseActionBuilder;
import main.java.databaseOperations.databaseActionBuilders.PostDatabaseActionBuilder;
import main.java.databaseOperations.databaseActionBuilders.UserDatabaseActionBuilder;
import main.java.lambdaFunctionHandlers.requestObjects.CreatePostRequest;

/**
 * Creates a Post in the database, checks the inputs, and adds the post to the by's posts and the
 * group's post (if applicable).
 */
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
                    if (!ifType && !ifNewType) {
                        throw new Exception("postType must either be empty, \"new\" + <item_type>, or just " +
                                "<item_type>!!");
                    }
                    else {
                        if (createPostRequest.about == null) {
                            throw new Exception("PostType of " + postType + " missing the \"about\" section!");
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
