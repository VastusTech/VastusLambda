package test.java.lambdaFunctionHandlers.highLevelHandlers.createDependencyHandlers;

import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import main.java.databaseObjects.Trainer;
import main.java.databaseOperations.DatabaseActionCompiler;
import main.java.databaseOperations.DynamoDBHandler;
import main.java.lambdaFunctionHandlers.highLevelHandlers.createDependencyHandlers.CreateTrainer;
import main.java.lambdaFunctionHandlers.requestObjects.CreateTrainerRequest;
import main.java.logic.Constants;
import main.java.testing.TestHelper;
import test.java.LocalDynamoDBCreationRule;

import static junit.framework.TestCase.assertNull;
import static org.junit.Assert.assertEquals;

public class CreateTrainerTest {
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
    public void createMinimumTrainer() throws Exception {
        String id = DynamoDBHandler.getInstance().attemptTransaction(CreateTrainer.getCompilers(
                Constants.adminKey, new CreateTrainerRequest(
                        "NAME", null,
                        null, "EMAIL",
                        "USERNAME", null,
                        null, null,
                        null, null,
                        null, null,
                        null
                ), 0
        ));
        Trainer trainer = Trainer.readTrainer(id);
        assertEquals("NAME", trainer.name);
        assertNull(trainer.gender);
        assertNull(trainer.birthday);
        assertEquals("EMAIL", trainer.email);
        assertEquals("USERNAME", trainer.username);
        assertNull(trainer.bio);
        assertNull(trainer.stripeID);
        assertNull(trainer.federatedID);
        assertNull(trainer.gym);
        assertNull(trainer.workoutSticker);
        assertNull(trainer.preferredIntensity);
        assertEquals(-1, trainer.workoutPrice);
        assertEquals(-1, trainer.workoutCapacity);
    }

    @Test
    public void createFederatedTrainer() throws Exception {
        String id = DynamoDBHandler.getInstance().attemptTransaction(CreateTrainer.getCompilers(
                Constants.adminKey, new CreateTrainerRequest(
                        "NAME", null,
                        null, "EMAIL",
                        "USERNAME", null,
                        "FEDERATEDID", null,
                        null, null,
                        null, null,
                        null
                ), 0
        ));
        Trainer trainer = Trainer.readTrainer(id);
        assertEquals("NAME", trainer.name);
        assertNull(trainer.gender);
        assertNull(trainer.birthday);
        assertEquals("EMAIL", trainer.email);
        assertEquals("USERNAME", trainer.username);
        assertNull(trainer.bio);
        assertNull(trainer.stripeID);
        assertEquals("FEDERATEDID", trainer.federatedID);
        assertNull(trainer.gym);
        assertNull(trainer.workoutSticker);
        assertNull(trainer.preferredIntensity);
        assertEquals(-1, trainer.workoutPrice);
        assertEquals(-1, trainer.workoutCapacity);
    }

    @Test
    public void createExtraInfoTrainer() throws Exception {
        String id = DynamoDBHandler.getInstance().attemptTransaction(CreateTrainer.getCompilers(
                Constants.adminKey, new CreateTrainerRequest(
                        "NAME", "GENDER",
                        "1998-09-05", "EMAIL",
                        "USERNAME", "STRIPEID",
                        null, "GYM",
                        "STICKER", "1",
                        "BIO", "10",
                        "100"
                ), 0
        ));
        Trainer trainer = Trainer.readTrainer(id);
        assertEquals("NAME", trainer.name);
        assertEquals("GENDER", trainer.gender);
        assertEquals("1998-09-05", trainer.birthday);
        assertEquals("EMAIL", trainer.email);
        assertEquals("USERNAME", trainer.username);
        assertEquals("BIO", trainer.bio);
        assertEquals("STRIPEID", trainer.stripeID);
        assertNull(trainer.federatedID);
        assertEquals("GYM", trainer.gym);
        assertEquals("STICKER", trainer.workoutSticker);
        assertEquals("1", trainer.preferredIntensity);
        assertEquals(100, trainer.workoutPrice);
        assertEquals(10, trainer.workoutCapacity);
    }

    @Test
    public void createExtraInfoFederatedTrainer() throws Exception {
        String id = DynamoDBHandler.getInstance().attemptTransaction(CreateTrainer.getCompilers(
                Constants.adminKey, new CreateTrainerRequest(
                        "NAME", "GENDER",
                        "1998-09-05", "EMAIL",
                        "USERNAME", "STRIPEID",
                        "FEDERATEDID", "GYM",
                        "STICKER", "1",
                        "BIO", "10",
                        "100"
                ), 0
        ));
        Trainer trainer = Trainer.readTrainer(id);
        assertEquals("NAME", trainer.name);
        assertEquals("GENDER", trainer.gender);
        assertEquals("1998-09-05", trainer.birthday);
        assertEquals("EMAIL", trainer.email);
        assertEquals("USERNAME", trainer.username);
        assertEquals("BIO", trainer.bio);
        assertEquals("STRIPEID", trainer.stripeID);
        assertEquals("FEDERATEDID", trainer.federatedID);
        assertEquals("GYM", trainer.gym);
        assertEquals("STICKER", trainer.workoutSticker);
        assertEquals("1", trainer.preferredIntensity);
        assertEquals(100, trainer.workoutPrice);
        assertEquals(10, trainer.workoutCapacity);
    }

    // ===========================================================================================
    // ==                              EXPECTED ERROR TESTS                                     ==
    // ===========================================================================================

    @Test(expected = Exception.class)
    public void failNoName() throws Exception {
        DynamoDBHandler.getInstance().attemptTransaction(CreateTrainer.getCompilers(
                Constants.adminKey, new CreateTrainerRequest(
                        null, null,
                        null, "EMAIL",
                        "USERNAME", null,
                        null, null,
                        null, null,
                        null, null,
                        null
                ), 0
        ));
    }

    @Test(expected = Exception.class)
    public void failNoEmail() throws Exception {
        DynamoDBHandler.getInstance().attemptTransaction(CreateTrainer.getCompilers(
                Constants.adminKey, new CreateTrainerRequest(
                        "NAME", null,
                        null, null,
                        "USERNAME", null,
                        null, null,
                        null, null,
                        null, null,
                        null
                ), 0
        ));
    }

    @Test(expected = Exception.class)
    public void failNoUsername() throws Exception {
        DynamoDBHandler.getInstance().attemptTransaction(CreateTrainer.getCompilers(
                Constants.adminKey, new CreateTrainerRequest(
                        "NAME", null,
                        null, "EMAIL",
                        null, null,
                        null, null,
                        null, null,
                        null, null,
                        null
                ), 0
        ));
    }

    @Test(expected = Exception.class)
    public void failBadBirthday() throws Exception {
        DynamoDBHandler.getInstance().attemptTransaction(CreateTrainer.getCompilers(
                Constants.adminKey, new CreateTrainerRequest(
                        "NAME", null,
                        "BAD-BIRTHDAY", "EMAIL",
                        "USERNAME", null,
                        null, null,
                        null, null,
                        null, null,
                        null
                ), 0
        ));
    }

    @Test(expected = Exception.class)
    public void failRepeatUsername() throws Exception {
        List<DatabaseActionCompiler> compilers = new ArrayList<>();
        compilers.addAll(CreateTrainer.getCompilers(
                Constants.adminKey, new CreateTrainerRequest(
                        "NAME", null,
                        null, "EMAIL",
                        "USERNAME", null,
                        null, null,
                        null, null,
                        null, null,
                        null
                ), 0));
        compilers.addAll(CreateTrainer.getCompilers(
                Constants.adminKey, new CreateTrainerRequest(
                        "NAME", null,
                        null, "EMAIL",
                        "USERNAME", null,
                        null, null,
                        null, null,
                        null, null,
                        null
                ), 0));
        DynamoDBHandler.getInstance().attemptTransaction(compilers);
    }
}
