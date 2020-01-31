package test.java.lambdaFunctionHandlers.highLevelHandlers.createDependencyHandlers;

import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;

import main.java.databaseObjects.Client;
import main.java.databaseObjects.Group;
import main.java.databaseObjects.Trainer;
import main.java.databaseOperations.DynamoDBHandler;
import main.java.lambdaFunctionHandlers.highLevelHandlers.createDependencyHandlers.CreateGroup;
import main.java.lambdaFunctionHandlers.requestObjects.CreateGroupRequest;
import main.java.logic.Constants;
import main.java.testing.TestHelper;
import test.java.LocalDynamoDBCreationRule;

import static junit.framework.TestCase.assertNull;
import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;

public class CreateGroupTest {
    @ClassRule
    public static LocalDynamoDBCreationRule rule = new LocalDynamoDBCreationRule();

    @Before
    public void init() throws Exception {
        TestHelper.reinitTablesFromJSON("table1.json", "table1.json");;
    }

    // ===========================================================================================
    // ==                            EXPECTED SUCCESS TESTS                                     ==
    // ===========================================================================================

    @Test
    public void createBareGroup() throws Exception {
        String id = DynamoDBHandler.getInstance().attemptTransaction(CreateGroup.getCompilers(
                Constants.adminKey, new CreateGroupRequest(
                        "TITLE", "DESCRIPTION", "private", null,
                        null, new String[]{"CL0001"}, null, null, null
                ), 0
        ));
        Group group = Group.readGroup(id);
        assertEquals("TITLE", group.title);
        assertEquals("DESCRIPTION", group.description);
        assertEquals("private", group.access);
        assertNull(group.motto);
        assertNull(group.groupImagePath);
        assertEquals(1, group.owners.size());
        assertTrue(group.owners.contains("CL0001"));
        assertEquals(1, group.members.size());
        assertTrue(group.members.contains("CL0001"));
        assertTrue(group.tags.isEmpty());
        assertNull(group.restriction);
        assertTrue(Client.readClient("CL0001").ownedGroups.contains(id));
        assertTrue(Client.readClient("CL0001").groups.contains(id));
    }

    @Test
    public void createGroupWithExtraInfo() throws Exception {
        String id = DynamoDBHandler.getInstance().attemptTransaction(CreateGroup.getCompilers(
                Constants.adminKey, new CreateGroupRequest(
                        "TITLE", "DESCRIPTION", "private", "MOTTO",
                        "GROUPIMAGEPATH", new String[]{"CL0001"}, null,
                        new String[]{"Endurance", "HIIT"}, null
                ), 0
        ));
        Group group = Group.readGroup(id);
        assertEquals("TITLE", group.title);
        assertEquals("DESCRIPTION", group.description);
        assertEquals("private", group.access);
        assertEquals("MOTTO", group.motto);
        assertEquals(id + "/GROUPIMAGEPATH", group.groupImagePath);
        assertEquals(1, group.owners.size());
        assertTrue(group.owners.contains("CL0001"));
        assertEquals(1, group.members.size());
        assertTrue(group.members.contains("CL0001"));
        assertEquals(2, group.tags.size());
        assertTrue(group.tags.contains("Endurance"));
        assertTrue(group.tags.contains("HIIT"));
        assertNull(group.restriction);
        assertTrue(Client.readClient("CL0001").ownedGroups.contains(id));
        assertTrue(Client.readClient("CL0001").groups.contains(id));
    }

    @Test
    public void createBareGroupAsClient() throws Exception {
        String id = DynamoDBHandler.getInstance().attemptTransaction(CreateGroup.getCompilers(
                "CL0001", new CreateGroupRequest(
                        "TITLE", "DESCRIPTION", "private", null,
                        null, new String[]{"CL0001"}, null, null, null
                ), 0
        ));
        Group group = Group.readGroup(id);
        assertEquals("TITLE", group.title);
        assertEquals("DESCRIPTION", group.description);
        assertEquals("private", group.access);
        assertNull(group.motto);
        assertNull(group.groupImagePath);
        assertEquals(1, group.owners.size());
        assertTrue(group.owners.contains("CL0001"));
        assertEquals(1, group.members.size());
        assertTrue(group.members.contains("CL0001"));
        assertTrue(group.tags.isEmpty());
        assertNull(group.restriction);
        assertTrue(Client.readClient("CL0001").ownedGroups.contains(id));
        assertTrue(Client.readClient("CL0001").groups.contains(id));
    }

    @Test
    public void createBareGroupAsTrainer() throws Exception {
        String id = DynamoDBHandler.getInstance().attemptTransaction(CreateGroup.getCompilers(
                "TR0001", new CreateGroupRequest(
                        "TITLE", "DESCRIPTION", "private", null,
                        null, new String[]{"TR0001"}, null, null, null
                ), 0
        ));
        Group group = Group.readGroup(id);
        assertEquals("TITLE", group.title);
        assertEquals("DESCRIPTION", group.description);
        assertEquals("private", group.access);
        assertNull(group.motto);
        assertNull(group.groupImagePath);
        assertEquals(1, group.owners.size());
        assertTrue(group.owners.contains("TR0001"));
        assertEquals(1, group.members.size());
        assertTrue(group.members.contains("TR0001"));
        assertTrue(group.tags.isEmpty());
        assertNull(group.restriction);
        assertTrue(Trainer.readTrainer("TR0001").ownedGroups.contains(id));
        assertTrue(Trainer.readTrainer("TR0001").groups.contains(id));
    }

    @Test
    public void createBareGroupAsClientWithoutExplicitOwners() throws Exception {
        String id = DynamoDBHandler.getInstance().attemptTransaction(CreateGroup.getCompilers(
                "CL0001", new CreateGroupRequest(
                        "TITLE", "DESCRIPTION", "private", null,
                        null, null, null, null, null
                ), 0
        ));
        Group group = Group.readGroup(id);
        assertEquals("TITLE", group.title);
        assertEquals("DESCRIPTION", group.description);
        assertEquals("private", group.access);
        assertNull(group.motto);
        assertNull(group.groupImagePath);
        assertEquals(1, group.owners.size());
        assertTrue(group.owners.contains("CL0001"));
        assertEquals(1, group.members.size());
        assertTrue(group.members.contains("CL0001"));
        assertTrue(group.tags.isEmpty());
        assertNull(group.restriction);
        assertTrue(Client.readClient("CL0001").ownedGroups.contains(id));
        assertTrue(Client.readClient("CL0001").groups.contains(id));
    }

    @Test
    public void createRestrictedGroup() throws Exception {
        String id = DynamoDBHandler.getInstance().attemptTransaction(CreateGroup.getCompilers(
                Constants.adminKey, new CreateGroupRequest(
                        "TITLE", "DESCRIPTION", "private", null,
                        null, new String[]{"CL0001"}, null, null,
                        "invite"
                ), 0
        ));
        Group group = Group.readGroup(id);
        assertEquals("TITLE", group.title);
        assertEquals("DESCRIPTION", group.description);
        assertEquals("private", group.access);
        assertNull(group.motto);
        assertNull(group.groupImagePath);
        assertEquals(1, group.owners.size());
        assertTrue(group.owners.contains("CL0001"));
        assertEquals(1, group.members.size());
        assertTrue(group.members.contains("CL0001"));
        assertTrue(group.tags.isEmpty());
        assertEquals("invite", group.restriction);
        assertTrue(Client.readClient("CL0001").ownedGroups.contains(id));
        assertTrue(Client.readClient("CL0001").groups.contains(id));
    }

    @Test
    public void createRestrictedGroupWithExtraInfo() throws Exception {
        String id = DynamoDBHandler.getInstance().attemptTransaction(CreateGroup.getCompilers(
                Constants.adminKey, new CreateGroupRequest(
                        "TITLE", "DESCRIPTION", "private", "MOTTO",
                        "GROUPIMAGEPATH", new String[]{"CL0001"}, null,
                        new String[]{"Endurance", "HIIT"}, "invite"
                ), 0
        ));
        Group group = Group.readGroup(id);
        assertEquals("TITLE", group.title);
        assertEquals("DESCRIPTION", group.description);
        assertEquals("private", group.access);
        assertEquals("MOTTO", group.motto);
        assertEquals(id + "/GROUPIMAGEPATH", group.groupImagePath);
        assertEquals(1, group.owners.size());
        assertTrue(group.owners.contains("CL0001"));
        assertEquals(1, group.members.size());
        assertTrue(group.members.contains("CL0001"));
        assertEquals(2, group.tags.size());
        assertTrue(group.tags.contains("Endurance"));
        assertTrue(group.tags.contains("HIIT"));
        assertEquals("invite", group.restriction);
        assertTrue(Client.readClient("CL0001").ownedGroups.contains(id));
        assertTrue(Client.readClient("CL0001").groups.contains(id));
    }

    // with existing members
    @Test
    public void createGroupWithExistingMembers() throws Exception {
        String id = DynamoDBHandler.getInstance().attemptTransaction(CreateGroup.getCompilers(
                Constants.adminKey, new CreateGroupRequest(
                        "TITLE", "DESCRIPTION", "private", null,
                        null, new String[]{"CL0001"}, new String[]{"CL0002", "CL0003"},
                        null, null
                ), 0
        ));
        Group group = Group.readGroup(id);
        assertEquals("TITLE", group.title);
        assertEquals("DESCRIPTION", group.description);
        assertEquals("private", group.access);
        assertNull(group.motto);
        assertNull(group.groupImagePath);
        assertEquals(1, group.owners.size());
        assertTrue(group.owners.contains("CL0001"));
        assertEquals(3, group.members.size());
        assertTrue(group.members.contains("CL0001"));
        assertTrue(group.members.contains("CL0002"));
        assertTrue(group.members.contains("CL0003"));
        assertTrue(group.tags.isEmpty());
        assertNull(group.restriction);
        assertTrue(Client.readClient("CL0001").ownedGroups.contains(id));
        assertTrue(Client.readClient("CL0001").groups.contains(id));
        assertTrue(Client.readClient("CL0002").groups.contains(id));
        assertTrue(Client.readClient("CL0003").groups.contains(id));
    }

    @Test
    public void createGroupWithExistingMembersAsClient() throws Exception {
        String id = DynamoDBHandler.getInstance().attemptTransaction(CreateGroup.getCompilers(
                "CL0001", new CreateGroupRequest(
                        "TITLE", "DESCRIPTION", "private", null,
                        null, new String[]{"CL0001"}, new String[]{"CL0002"},
                        null, null
                ), 0
        ));
        Group group = Group.readGroup(id);
        assertEquals("TITLE", group.title);
        assertEquals("DESCRIPTION", group.description);
        assertEquals("private", group.access);
        assertNull(group.motto);
        assertNull(group.groupImagePath);
        assertEquals(1, group.owners.size());
        assertTrue(group.owners.contains("CL0001"));
        assertEquals(2, group.members.size());
        assertTrue(group.members.contains("CL0001"));
        assertTrue(group.members.contains("CL0002"));
        assertTrue(group.tags.isEmpty());
        assertNull(group.restriction);
        assertTrue(Client.readClient("CL0001").ownedGroups.contains(id));
        assertTrue(Client.readClient("CL0001").groups.contains(id));
        assertTrue(Client.readClient("CL0002").groups.contains(id));
    }

    // with multiple owners
    @Test
    public void createGroupWithMultipleOwners() throws Exception {
        String id = DynamoDBHandler.getInstance().attemptTransaction(CreateGroup.getCompilers(
                Constants.adminKey, new CreateGroupRequest(
                        "TITLE", "DESCRIPTION", "private", null,
                        null, new String[]{"CL0001", "CL0002", "CL0003"},
                        null, null, null
                ), 0
        ));
        Group group = Group.readGroup(id);
        assertEquals("TITLE", group.title);
        assertEquals("DESCRIPTION", group.description);
        assertEquals("private", group.access);
        assertNull(group.motto);
        assertNull(group.groupImagePath);
        assertEquals(3, group.owners.size());
        assertTrue(group.owners.contains("CL0001"));
        assertTrue(group.owners.contains("CL0002"));
        assertTrue(group.owners.contains("CL0003"));
        assertEquals(3, group.members.size());
        assertTrue(group.members.contains("CL0001"));
        assertTrue(group.members.contains("CL0002"));
        assertTrue(group.members.contains("CL0003"));
        assertTrue(group.tags.isEmpty());
        assertNull(group.restriction);
        assertTrue(Client.readClient("CL0001").ownedGroups.contains(id));
        assertTrue(Client.readClient("CL0002").ownedGroups.contains(id));
        assertTrue(Client.readClient("CL0003").ownedGroups.contains(id));
        assertTrue(Client.readClient("CL0001").groups.contains(id));
        assertTrue(Client.readClient("CL0002").groups.contains(id));
        assertTrue(Client.readClient("CL0003").groups.contains(id));
    }

    @Test
    public void createGroupWithMultipleOwnersAsClient() throws Exception {
        String id = DynamoDBHandler.getInstance().attemptTransaction(CreateGroup.getCompilers(
                "CL0001", new CreateGroupRequest(
                        "TITLE", "DESCRIPTION", "private", null,
                        null, new String[]{"CL0001", "CL0002"},
                        null, null, null
                ), 0
        ));
        Group group = Group.readGroup(id);
        assertEquals("TITLE", group.title);
        assertEquals("DESCRIPTION", group.description);
        assertEquals("private", group.access);
        assertNull(group.motto);
        assertNull(group.groupImagePath);
        assertEquals(2, group.owners.size());
        assertTrue(group.owners.contains("CL0001"));
        assertTrue(group.owners.contains("CL0002"));
        assertEquals(2, group.members.size());
        assertTrue(group.members.contains("CL0001"));
        assertTrue(group.members.contains("CL0002"));
        assertTrue(group.tags.isEmpty());
        assertNull(group.restriction);
        assertTrue(Client.readClient("CL0001").ownedGroups.contains(id));
        assertTrue(Client.readClient("CL0002").ownedGroups.contains(id));
        assertTrue(Client.readClient("CL0001").groups.contains(id));
        assertTrue(Client.readClient("CL0002").groups.contains(id));
    }

    // ===========================================================================================
    // ==                              EXPECTED ERROR TESTS                                     ==
    // ===========================================================================================

    // wrong from id
    @Test(expected = Exception.class)
    public void failNullFromID() throws Exception {
        DynamoDBHandler.getInstance().attemptTransaction(CreateGroup.getCompilers(
                null, new CreateGroupRequest(
                        "TITLE", "DESCRIPTION", "private", null,
                        null, new String[]{"CL0001"}, null, null, null
                ), 0
        ));
    }

    // zero owners with admin from id
    @Test(expected = Exception.class)
    public void failNotOwnersAndAdminFromID() throws Exception {
        DynamoDBHandler.getInstance().attemptTransaction(CreateGroup.getCompilers(
                Constants.adminKey, new CreateGroupRequest(
                        "TITLE", "DESCRIPTION", "private", null,
                        null, null, null, null, null
                ), 0
        ));
    }

    // without necessary info
    @Test(expected = Exception.class)
    public void failNoTitle() throws Exception {
        DynamoDBHandler.getInstance().attemptTransaction(CreateGroup.getCompilers(
                "CL0001", new CreateGroupRequest(
                        null, "DESCRIPTION", "private", null,
                        null, new String[]{"CL0001"}, null, null, null
                ), 0
        ));
    }
    @Test(expected = Exception.class)
    public void failNoAccess() throws Exception {
        DynamoDBHandler.getInstance().attemptTransaction(CreateGroup.getCompilers(
                "CL0001", new CreateGroupRequest(
                        "TITLE", "DESCRIPTION", null, null,
                        null, new String[]{"CL0001"}, null, null, null
                ), 0
        ));
    }

    // client create public group
    @Test(expected = Exception.class)
    public void failClientPublicGroup() throws Exception {
        DynamoDBHandler.getInstance().attemptTransaction(CreateGroup.getCompilers(
                "CL0001", new CreateGroupRequest(
                        "TITLE", "DESCRIPTION", "public", null,
                        null, new String[]{"CL0001"}, null, null, null
                ), 0
        ));
    }

    @Test(expected = Exception.class)
    public void failIncorrectRestriction() throws Exception {
        DynamoDBHandler.getInstance().attemptTransaction(CreateGroup.getCompilers(
                "CL0001", new CreateGroupRequest(
                        "TITLE", "DESCRIPTION", "private", null,
                        null, new String[]{"CL0001"}, null, null,
                        "RESTRICTION"
                ), 0
        ));
    }

    @Test(expected = Exception.class)
    public void failIncorrectAccess() throws Exception {
        DynamoDBHandler.getInstance().attemptTransaction(CreateGroup.getCompilers(
                "CL0001", new CreateGroupRequest(
                        "TITLE", "DESCRIPTION", "ACCESS", null,
                        null, new String[]{"CL0001"}, null, null,
                        null
                ), 0
        ));
    }
    @Test(expected = Exception.class)
    public void failMembersNotFriendsOfFromID() throws Exception {
        DynamoDBHandler.getInstance().attemptTransaction(CreateGroup.getCompilers(
                "CL0001", new CreateGroupRequest(
                        "TITLE", "DESCRIPTION", "ACCESS", null,
                        null, new String[]{"CL0001"}, new String[] {"CL0003"}, null,
                        null
                ), 0
        ));
    }
    @Test(expected = Exception.class)
    public void failOwnersNotFriendsOfFromID() throws Exception {
        DynamoDBHandler.getInstance().attemptTransaction(CreateGroup.getCompilers(
                "CL0001", new CreateGroupRequest(
                        "TITLE", "DESCRIPTION", "ACCESS", null,
                        null, new String[]{"CL0001", "CL0003"}, null, null,
                        null
                ), 0
        ));
    }
}
