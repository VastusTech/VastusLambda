package test.java.databaseOperations;

import com.amazonaws.services.dynamodbv2.document.PrimaryKey;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.model.AttributeDefinition;
import com.amazonaws.services.dynamodbv2.model.CreateTableRequest;
import com.amazonaws.services.dynamodbv2.model.CreateTableResult;
import com.amazonaws.services.dynamodbv2.model.KeySchemaElement;
import com.amazonaws.services.dynamodbv2.model.KeyType;
import com.amazonaws.services.dynamodbv2.model.ListTablesResult;
import com.amazonaws.services.dynamodbv2.model.ProvisionedThroughput;
import com.amazonaws.services.dynamodbv2.model.ScalarAttributeType;

import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;

import main.java.databaseObjects.Client;
import main.java.databaseOperations.DatabaseActionCompiler;
import main.java.databaseOperations.DynamoDBHandler;
import main.java.lambdaFunctionHandlers.highLevelHandlers.updateSetDependencyHandlers.UserUpdateName;
import main.java.logic.Constants;
import main.java.testing.TestHelper;
import main.java.testing.TestTableHelper;
import test.java.LocalDynamoDBCreationRule;

import static org.junit.Assert.assertEquals;

public class DynamoDBHandlerTest {
    @ClassRule
    public static final LocalDynamoDBCreationRule dynamoDB = new LocalDynamoDBCreationRule();

    @Before
    public void init() throws Exception {
        TestHelper.reinitTablesFromJSON("table1.json", "table1.json");
    }

    @Test
    public void test1() throws Exception {
        DatabaseActionCompiler compiler = new DatabaseActionCompiler();
        compiler.addAll(UserUpdateName.getActions("admin", "CL0001", "Client", "Leonid"));
        DynamoDBHandler.getInstance().attemptTransaction(Collections.singletonList(compiler));
        Client client = Client.readClient("CL0001");
        assertEquals("Leonid", client.name);
        TestTableHelper.getInstance().saveTableToJSON(DynamoDBHandler.getInstance().client,
                Constants.databaseTableName, "outJSON.json");
    }
}
