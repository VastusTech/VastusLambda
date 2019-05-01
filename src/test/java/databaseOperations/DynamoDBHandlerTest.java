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

import main.java.databaseObjects.Client;
import main.java.databaseOperations.DynamoDBHandler;
import main.java.logic.Constants;
import main.java.testing.TestHelper;
import main.java.testing.TestTableHelper;
import test.java.LocalDynamoDBCreationRule;

import static org.junit.Assert.assertEquals;

public class DynamoDBHandlerTest {
    @ClassRule
    public static final LocalDynamoDBCreationRule dynamoDB = new LocalDynamoDBCreationRule();

    @Test
    public void testInit() throws Exception {
//        DynamoDBHandler.getInstance().readItem(Constants.databaseTableName, new PrimaryKey());
        Client client = Client.readClient("CL0001");
        TestTableHelper.getInstance().saveTableToJSON(DynamoDBHandler.getInstance().client,
                Constants.databaseTableName, "outJSON.json");
    }
}
