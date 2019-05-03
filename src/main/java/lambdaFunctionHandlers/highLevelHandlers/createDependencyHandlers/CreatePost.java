package main.java.lambdaFunctionHandlers.highLevelHandlers.createDependencyHandlers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import main.java.databaseObjects.DatabaseObject;
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
    public static List<DatabaseActionCompiler> getCompilers(String fromID, CreatePostRequest createPostRequest, int depth, String aboutIdentifier) throws Exception {
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
                if (depth == 0) {
                    databaseActionCompiler.add(PostDatabaseActionBuilder.create(createPostRequest, null));
                }
                else {
                    Map<String, String> passoverIdentifiers = new HashMap<>();
                    passoverIdentifiers.put("about", aboutIdentifier);
                    databaseActionCompiler.add(PostDatabaseActionBuilder.create(createPostRequest, passoverIdentifiers));
                }

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
                        if (depth == 0 && ifNewType) {
                            throw new Exception("The User cannot directly create a new Item Post");
                        }
                        if (!ifNewType) {
                            if (createPostRequest.about == null) {
                                throw new Exception("PostType of " + postType + " missing the \"about\" section!");
                            } else {
                                DatabaseObject.readDatabaseObject(createPostRequest.about, postType);
                            }
                        }
                    }
                }
                else if (createPostRequest.about != null) {
                    throw new Exception("Post with about must have a non-null post type!");
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

                if (byItemType.equals("Client") && createPostRequest.access.equals("public")) {
                    throw new Exception("Clients cannot create public posts!");
                }

                databaseActionCompiler.add(UserDatabaseActionBuilder.updateAddPost(by, byItemType, null, true));

                // Add to the post's group
                if (createPostRequest.group != null) {
                    if (!Group.readGroup(createPostRequest.group).members.contains(createPostRequest.by)) {
                        throw new Exception("Cannot create a post for a Group you are not a part of");
                    }
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
