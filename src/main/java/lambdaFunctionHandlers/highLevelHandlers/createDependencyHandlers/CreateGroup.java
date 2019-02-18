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
import java.util.List;

public class CreateGroup {
    public static List<DatabaseActionCompiler> getCompilers(String fromID, CreateGroupRequest createGroupRequest, boolean ifWithCreate) throws Exception {
        if (createGroupRequest != null) {
            // Create client
            if (createGroupRequest.title != null && createGroupRequest.description != null
                    && createGroupRequest.access != null) {
                List<DatabaseActionCompiler> compilers = new ArrayList<>();
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
                databaseActionCompiler.add(GroupDatabaseActionBuilder.create(createGroupRequest, ifWithCreate));

                // Add to the owners' ownedGroups
                for (String ownerID : createGroupRequest.owners) {
                    String ownerItemType = ItemType.getItemType(ownerID);

                    if (ownerItemType.equals("Client") && createGroupRequest.access.equals("public")) {
                        throw new Exception("No owners of a public group can be Clients!");
                    }

                    databaseActionCompiler.add(UserDatabaseActionBuilder.updateAddOwnedGroup(ownerID, ownerItemType,
                            null, true));
                }
                // Add the group to everyone else too
                for (String memberID : createGroupRequest.members) {
                    String memberItemType = ItemType.getItemType(memberID);
                    databaseActionCompiler.add(UserDatabaseActionBuilder.updateAddGroup(memberID, memberItemType,
                            null, true));
                }

                compilers.add(databaseActionCompiler);

                // TODO Automatically create post

                return compilers;
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
