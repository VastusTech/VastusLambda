package main.java.lambdaFunctionHandlers.highLevelHandlers.createDependencyHandlers;

import main.java.Logic.Constants;
import main.java.Logic.ItemType;
import main.java.databaseOperations.DatabaseActionCompiler;
import main.java.databaseOperations.DynamoDBHandler;
import main.java.databaseOperations.databaseActionBuilders.GroupDatabaseActionBuilder;
import main.java.databaseOperations.databaseActionBuilders.UserDatabaseActionBuilder;
import main.java.lambdaFunctionHandlers.requestObjects.CreateGroupRequest;

import java.util.ArrayList;
import java.util.Arrays;

public class CreateGroup {
    public static String handle(String fromID, CreateGroupRequest createGroupRequest) throws Exception {
        if (createGroupRequest != null) {
            // Create client
            if (createGroupRequest.title != null && createGroupRequest.description != null) {
                DatabaseActionCompiler databaseActionCompiler = new DatabaseActionCompiler();

                if (createGroupRequest.owners == null) {
                    if (fromID.equals(Constants.adminKey)) {
                        throw new Exception("Admin cannot create a owner-less group!");
                    }
                    createGroupRequest.owners = new String[]{fromID};
                }
                else {
                    ArrayList<String> owners = new ArrayList<>(Arrays.asList(createGroupRequest.owners));
                    if (!owners.contains(fromID) && !fromID.equals(Constants.adminKey)) {
                        owners.add(fromID);
                        createGroupRequest.owners = owners.toArray(new String[]{});
                    }
                }
                if (createGroupRequest.members == null) {
                    createGroupRequest.members = createGroupRequest.owners.clone();
                }
                else {
                    ArrayList<String> members = new ArrayList<>(Arrays.asList(createGroupRequest.members));
                    for (String owner : createGroupRequest.owners) {
                        if (!members.contains(owner)) {
                            members.add(owner);
                        }
                    }
                    createGroupRequest.members = members.toArray(new String[]{});
                }

                // Create the group
                databaseActionCompiler.add(GroupDatabaseActionBuilder.create(createGroupRequest));

                // Add to the owners' ownedGroups
                for (String ownerID : createGroupRequest.owners) {
                    String ownerItemType = ItemType.getItemType(ownerID);
                    databaseActionCompiler.add(UserDatabaseActionBuilder.updateAddOwnedGroup(ownerID, ownerItemType,
                            null, true));
                }
                // Add the group to everyone else too
                for (String memberID : createGroupRequest.members) {
                    String memberItemType = ItemType.getItemType(memberID);
                    databaseActionCompiler.add(UserDatabaseActionBuilder.updateAddGroup(memberID, memberItemType,
                            null, true));
                }

                return DynamoDBHandler.getInstance().attemptTransaction(databaseActionCompiler);
            }
            else {
                throw new Exception("createGroupRequest is missing required fields!");
            }
        }
        else {
            throw new Exception("createGroupRequest not initialized for CREATE statement!");
        }
    }
}
