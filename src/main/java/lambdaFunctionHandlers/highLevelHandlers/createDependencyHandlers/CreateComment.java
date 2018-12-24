package main.java.lambdaFunctionHandlers.highLevelHandlers.createDependencyHandlers;

import main.java.Logic.Constants;
import main.java.Logic.ItemType;
import main.java.databaseOperations.DatabaseActionCompiler;
import main.java.databaseOperations.DynamoDBHandler;
import main.java.databaseOperations.databaseActionBuilders.CommentDatabaseActionBuilder;
import main.java.databaseOperations.databaseActionBuilders.PostDatabaseActionBuilder;
import main.java.databaseOperations.databaseActionBuilders.UserDatabaseActionBuilder;
import main.java.lambdaFunctionHandlers.requestObjects.CreateCommentRequest;

public class CreateComment {
    public static String handle(String fromID, CreateCommentRequest createCommentRequest) throws Exception {
        if (createCommentRequest != null) {
            // Create client
            if (createCommentRequest.by != null && createCommentRequest.on != null && createCommentRequest.comment
                    != null) {
                DatabaseActionCompiler databaseActionCompiler = new DatabaseActionCompiler();

                if (!fromID.equals(createCommentRequest.by) && !fromID.equals(Constants.adminKey)) {
                    throw new Exception("PERMISSIONS ERROR: You can only create comments you author!");
                }

                // Create the object
                databaseActionCompiler.add(CommentDatabaseActionBuilder.create(createCommentRequest));

                // Add it to the author's comments
                String byItemType = ItemType.getItemType(createCommentRequest.by);
                databaseActionCompiler.add(UserDatabaseActionBuilder.updateAddComment(createCommentRequest.by,
                        byItemType, null, true));

                // Add the comment to the comments of what you are commenting on
                String onItemType = ItemType.getItemType(createCommentRequest.on);
                if (onItemType.equals("Post")) {
                    databaseActionCompiler.add(PostDatabaseActionBuilder.updateAddComment(createCommentRequest.on,
                            null, true));
                }
                else if (onItemType.equals("Comment")) {
                    databaseActionCompiler.add(CommentDatabaseActionBuilder.updateAddComment(createCommentRequest.on,
                            null, true));
                }
                else {
                    throw new Exception("Cannot comment on an object of type: " + onItemType);
                }

                return DynamoDBHandler.getInstance().attemptTransaction(databaseActionCompiler);
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
