package main.java.lambdaFunctionHandlers.highLevelHandlers.createDependencyHandlers;

import java.util.ArrayList;
import java.util.List;

import main.java.databaseOperations.exceptions.PermissionsException;
import main.java.logic.Constants;
import main.java.logic.ItemType;
import main.java.databaseOperations.DatabaseActionCompiler;
import main.java.databaseOperations.databaseActionBuilders.CommentDatabaseActionBuilder;
import main.java.databaseOperations.databaseActionBuilders.PostDatabaseActionBuilder;
import main.java.databaseOperations.databaseActionBuilders.SubmissionDatabaseActionBuilder;
import main.java.databaseOperations.databaseActionBuilders.UserDatabaseActionBuilder;
import main.java.lambdaFunctionHandlers.requestObjects.CreateCommentRequest;

/**
 * Creates a Comment in the database, checks the inputs, and adds it to the author's comments and
 * the Post/Comment/Submission's comments.
 */
public class CreateComment {
    public static List<DatabaseActionCompiler> getCompilers(String fromID, CreateCommentRequest createCommentRequest, int depth) throws Exception {
        if (createCommentRequest != null) {
            // Create client
            if (createCommentRequest.by != null && createCommentRequest.to != null && createCommentRequest.comment
                    != null) {
                List<DatabaseActionCompiler> compilers = new ArrayList<>();
                DatabaseActionCompiler databaseActionCompiler = new DatabaseActionCompiler();

                if (fromID == null || (!fromID.equals(createCommentRequest.by) && !Constants.isAdmin(fromID))) {
                    throw new PermissionsException("You can only create comments you author!");
                }

                // Create the object
                if (depth == 0) {
                    databaseActionCompiler.add(CommentDatabaseActionBuilder.create(createCommentRequest, null));
                }
                else {
                    // TODO If we ever try to create Comments automatically, figure out which
                    // TODO attributes need which passover Identifiers.
                    databaseActionCompiler.add(CommentDatabaseActionBuilder.create(createCommentRequest, null));
                }

                // Add it to the author's comments
                String byItemType = ItemType.getItemType(createCommentRequest.by);
                databaseActionCompiler.add(UserDatabaseActionBuilder.updateAddComment(createCommentRequest.by,
                        byItemType, null, true));

                // Add the comment to the comments of what you are commenting on
                String toItemType = ItemType.getItemType(createCommentRequest.to);
                if (toItemType.equals("Post")) {
                    databaseActionCompiler.add(PostDatabaseActionBuilder.updateAddComment(createCommentRequest.to,
                            null, true));
                }
                else if (toItemType.equals("Submission")) {
                    databaseActionCompiler.add(SubmissionDatabaseActionBuilder.updateAddComment(createCommentRequest.to,
                            null, true));
                }
                else if (toItemType.equals("Comment")) {
                    databaseActionCompiler.add(CommentDatabaseActionBuilder.updateAddComment(createCommentRequest.to,
                            null, true));
                }
                else {
                    throw new Exception("Cannot comment on an object of type: " + toItemType);
                }

                compilers.add(databaseActionCompiler);

                return compilers;
            }
            else {
                throw new Exception("createCommentRequest is missing required fields!");
            }
        }
        else {
            throw new Exception("createCommentRequest not initialized for CREATE statement!");
        }
    }
}
