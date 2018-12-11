package main.java.lambdaFunctionHandlers.highLevelHandlers.createDependencyHandlers;

import main.java.Logic.ItemType;
import main.java.databaseOperations.DatabaseActionCompiler;
import main.java.databaseOperations.DynamoDBHandler;
import main.java.databaseOperations.databaseActionBuilders.PostDatabaseActionBuilder;
import main.java.databaseOperations.databaseActionBuilders.UserDatabaseActionBuilder;
import main.java.lambdaFunctionHandlers.requestObjects.CreatePostRequest;

public class CreatePost {
    public static String handle(String fromID, CreatePostRequest createPostRequest) throws Exception {
        if (createPostRequest != null) {
            // Check required fields
            if (createPostRequest.by != null && createPostRequest.description != null && createPostRequest
                    .access != null && createPostRequest.postType != null) {
                // Create the database action list for the transaction to complete
                DatabaseActionCompiler databaseActionCompiler = new DatabaseActionCompiler();

                // Check to see if the request features are well formed (i.e not empty string or invalid date)

                // Create post (with createPostRequest)
                databaseActionCompiler.add(PostDatabaseActionBuilder.create(createPostRequest));

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
