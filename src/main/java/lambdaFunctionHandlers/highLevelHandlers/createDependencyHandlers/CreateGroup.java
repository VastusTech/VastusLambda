package main.java.lambdaFunctionHandlers.highLevelHandlers.createDependencyHandlers;

import main.java.databaseObjects.User;
import main.java.databaseOperations.exceptions.PermissionsException;
import main.java.logic.Constants;
import main.java.logic.ItemType;
import main.java.databaseOperations.DatabaseActionCompiler;
import main.java.databaseOperations.databaseActionBuilders.GroupDatabaseActionBuilder;
import main.java.databaseOperations.databaseActionBuilders.UserDatabaseActionBuilder;
import main.java.lambdaFunctionHandlers.requestObjects.CreateGroupRequest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Creates a Group in the database, checks the inputs, and adds it to the members and the owners.
 */
public class CreateGroup {
    public static List<DatabaseActionCompiler> getCompilers(String fromID, CreateGroupRequest createGroupRequest, int depth) throws Exception {
        if (createGroupRequest != null) {
            // Create client
            if (createGroupRequest.title != null && createGroupRequest.access != null) {
                List<DatabaseActionCompiler> compilers = new ArrayList<>();
                DatabaseActionCompiler databaseActionCompiler = new DatabaseActionCompiler();

                if (createGroupRequest.owners == null) {
                    if (Constants.isAdmin(fromID)) {
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

                if (!Constants.isAdmin(fromID)) {
                    User from = User.readUser(fromID);
                    for (String owner : createGroupRequest.owners) {
                        if (!owner.equals(fromID) && !from.friends.contains(owner)) {
                            throw new PermissionsException("Cannot make a Group with an owner who is not your friend!");
                        }
                    }
                    for (String member : createGroupRequest.members) {
                        if (!member.equals(fromID) && !from.friends.contains(member)) {
                            throw new PermissionsException("Cannot make a Group with a member who is not your friend!");
                        }
                    }
                }

                if (!(createGroupRequest.access.equals("public")
                        || createGroupRequest.access.equals("private"))) {
                    throw new Exception("Unrecognized access = " + createGroupRequest.access);
                }

                if (!(createGroupRequest.restriction == null
                        || createGroupRequest.restriction.equals("invite"))) {
                    throw new Exception("Unrecognized restriction = " + createGroupRequest.restriction);
                }

                // Create the group
                // TODO If we ever try to create Groups automatically, figure out which
                // TODO attributes need which passover Identifiers.
                databaseActionCompiler.add(GroupDatabaseActionBuilder.create(createGroupRequest, null));

                // Add to the owners' ownedGroups
                for (String ownerID : createGroupRequest.owners) {
                    String ownerItemType = ItemType.getItemType(ownerID);

                    // TODO Edit
                    if (ownerItemType.equals("Client") && createGroupRequest.access.equals("public")) {
                        throw new PermissionsException("No owners of a public group can be Clients!");
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
