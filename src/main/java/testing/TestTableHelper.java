package main.java.testing;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.model.AttributeDefinition;
import com.amazonaws.services.dynamodbv2.model.CreateTableRequest;
import com.amazonaws.services.dynamodbv2.model.DeleteTableRequest;
import com.amazonaws.services.dynamodbv2.model.GlobalSecondaryIndex;
import com.amazonaws.services.dynamodbv2.model.KeySchemaElement;
import com.amazonaws.services.dynamodbv2.model.KeyType;
import com.amazonaws.services.dynamodbv2.model.Projection;
import com.amazonaws.services.dynamodbv2.model.ProjectionType;
import com.amazonaws.services.dynamodbv2.model.ProvisionedThroughput;
import com.amazonaws.services.dynamodbv2.model.ScalarAttributeType;
import com.amazonaws.services.dynamodbv2.util.TableUtils;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeType;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import main.java.logic.Constants;

/**
 * Class to help with the creation and initialization of a DynamoDB Local instance and its tables.
 */
public class TestTableHelper {
    private static TestTableHelper instance = null;

    /**
     * Gets the instance, using the singleton design pattern to ensure that only one instance exists
     * at a given time.
     *
     * @return The only instance for the TestTableHelper class.
     */
    public synchronized static TestTableHelper getInstance() {
        if (instance == null) {
            instance = new TestTableHelper();
        }
        return instance;
    }

    /**
     * The empty constructor for the test table helper.
     */
    private TestTableHelper() {}

    /**
     * Initializes or re-initializes a test database table from a JSON file.
     *
     * @param client The {@link AmazonDynamoDB} client to initialize the table for.
     * @param jsonPath The relative path from the test "resources/databaseTestTables" directory for
     *                 the JSON file to initialize the table with.
     * @return The newly created or reinitialized {@link Table}.
     * @throws IllegalArgumentException If the JSON path is bad or the JSON file is malformed.
     * @throws IOException If anything goes wrong in the table creation or initialization.
     */
    Table reinitTestDatabaseTable(AmazonDynamoDB client, String jsonPath) throws
            IllegalArgumentException, IOException {
        TableUtils.deleteTableIfExists(client, new DeleteTableRequest(Constants.databaseTableName));
        return createAndFillDatabaseTable(client, jsonPath);
    }

    /**
     * Initializes or re-initializes a test messages table from a JSON file.
     *
     * @param client The {@link AmazonDynamoDB} client to initialize the table for.
     * @param jsonPath The relative path from the test "resources/messagesTestTables" directory for
     *                 the JSON file to initialize the table with.
     * @return The newly created or reinitialized {@link Table}.
     * @throws IllegalArgumentException If the JSON path is bad or the JSON file is malformed.
     * @throws IOException If anything goes wrong in the table creation or initialization.
     */
    Table reinitTestMessagesTable(AmazonDynamoDB client, String jsonPath) throws
            IllegalArgumentException, IOException {
        TableUtils.deleteTableIfExists(client, new DeleteTableRequest(Constants.messageTableName));
        return createAndFillMessageTable(client, jsonPath);
    }

    /**
     * Creates and fills the database table for the given client using the given JSON file.
     *
     * @param client The {@link AmazonDynamoDB} client to initialize the table for.
     * @param jsonPath The relative path from the test "resources/databaseTestTables" directory for
     *                 the JSON file to initialize the table with.
     * @return The newly created {@link Table} object.
     * @throws IllegalArgumentException If the JSON path is bad or the JSON file is malformed.
     * @throws IOException If anything goes wrong in the table creation or initialization.
     */
    private Table createAndFillDatabaseTable(AmazonDynamoDB client, String jsonPath)
            throws IllegalArgumentException, IOException {
        return createAndFillTable(client, jsonPath, Constants.databaseTableName,
                "item_type", "id");
    }

    /**
     * Creates and fills the message table for the given client using the given JSON file.
     *
     * @param client The {@link AmazonDynamoDB} client to initialize the table for.
     * @param jsonPath The relative path from the test "resources/messagesTestTables" directory for
     *                 the JSON file to initialize the table with.
     * @return The newly created {@link Table} object.
     * @throws IllegalArgumentException If the JSON path is bad or the JSON file is malformed.
     * @throws IOException If anything goes wrong in the table creation or initialization.
     */
    private Table createAndFillMessageTable(AmazonDynamoDB client, String jsonPath)
            throws IllegalArgumentException, IOException {
        return createAndFillTable(client, jsonPath, Constants.messageTableName, "board",
                "id");
    }

    /**
     * Saves the table to a JSON file to a path given, which is related to the entire project
     * directory.
     *
     * @param client The {@link AmazonDynamoDB} client to get the table from.
     * @param tableName The name of the table to save.
     * @param outFilePath The path of the file to print the table to.
     * @throws IOException
     */
    public void saveTableToJSON(AmazonDynamoDB client, String tableName, String outFilePath) throws IOException {
        saveTableToJSON(new Table(client, tableName), outFilePath);
    }

    /**
     * Saves the table to a JSON file to a path given, which is related to the entire project
     * directory.
     *
     * @param table The {@link Table} object to print.
     * @param filePath The path of the file to print the table to.
     * @throws IOException
     */
    private void saveTableToJSON(Table table, String filePath) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        ArrayNode rootNode = mapper.createArrayNode();
        for (Item item : table.scan()) {
            rootNode.add(mapper.readTree(item.toJSON()));
        }
        BufferedWriter writer = new BufferedWriter(new FileWriter(filePath));
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
        writer.write(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(rootNode));
        writer.close();
    }

    /**
     * Creates and fills a table with the given schema and the given JSON file. The file must be
     * valid syntax so that the {@link Table} can properly store its attributes.
     *
     * @param client The {@link AmazonDynamoDB} client to initialize the table for.
     * @param jsonPath The absolute file path from the project directory to get the JSON file from.
     * @param tableName The name of the table to create.
     * @param hashKeyName The name of the hash key to create the table with.
     * @param rangeKeyName The name of the range key to create the table with.
     * @return The newly created {@link Table} object.
     * @throws IllegalArgumentException If the JSON path is bad or the JSON file is malformed.
     * @throws IOException If anything goes wrong in the table creation or initialization.
     */
    private Table createAndFillTable(AmazonDynamoDB client, String jsonPath, String tableName,
                                    String hashKeyName, String rangeKeyName) throws
            IllegalArgumentException, IOException {
        JsonParser parser = new JsonFactory().createParser(new File(jsonPath));

        JsonNode rootNode = new ObjectMapper().readTree(parser);

        if (rootNode.getNodeType() != JsonNodeType.ARRAY) {
            throw new IllegalArgumentException("JSON File structure invalid! Top attribute not ARRAY");
        }

        Iterator<JsonNode> iter = rootNode.iterator();

        ObjectNode currentNode;

        Table table = createTable(client, tableName, hashKeyName, rangeKeyName);

        while (iter.hasNext()) {
            currentNode = (ObjectNode) iter.next();

            Item item = new Item();

            if (!(currentNode.has(hashKeyName) && currentNode.has(rangeKeyName))) {
                throw new IllegalArgumentException("JSON File structure invalid! Item does " +
                        "not have the " + hashKeyName + " and " + rangeKeyName + " attributes!");
            }

            // Set the primary key of the item
            item = item.withPrimaryKey(hashKeyName, currentNode.get(hashKeyName).textValue(),
                    rangeKeyName, currentNode.get(rangeKeyName).textValue());

            Iterator<Map.Entry<String, JsonNode>> attributeNameIterator = currentNode.fields();
            while (attributeNameIterator.hasNext()) {
                Map.Entry<String, JsonNode> entry = attributeNameIterator.next();
                JsonNode attribute = entry.getValue();
                switch (attribute.getNodeType()) {
                    case STRING:
                        item = item.withString(entry.getKey(), attribute.textValue());
                        break;
                    case ARRAY:
                        Set<String> attributeSet = new HashSet<>();
                        for (JsonNode setAttribute : attribute) {
                            if (!setAttribute.isTextual()) {
                                throw new IllegalArgumentException("JSON File structure " +
                                        "invalid! Set attribute is not a String");
                            }
                            attributeSet.add(setAttribute.textValue());
                        }
                        item = item.withStringSet(entry.getKey(), attributeSet);
                        break;
                    case NUMBER:
                        item = item.withNumber(entry.getKey(), attribute.numberValue());
                        break;
                    case NULL:
                        break;
                    default:
                        throw new IllegalArgumentException("JSON File structure invalid! " +
                                "Item contains illegal attribute type = " +
                                attribute.getNodeType().toString());
                }
            }
            table.putItem(item);
        }

        parser.close();

        return table;
    }

    /**
     * Creates a Table for the given client using the given schema and potentially adds Global
     * indexes if it's the database table.
     *
     * @param client The {@link AmazonDynamoDB} client to initialize the table for.
     * @param tableName The name of the table to create.
     * @param hashKeyName The name of the hash key to create the table with.
     * @param rangeKeyName The name of the range key to create the table with.
     * @return The created {@link Table} object.
     */
    private Table createTable(AmazonDynamoDB client, String tableName, String hashKeyName, String rangeKeyName) {
        CreateTableRequest request = new CreateTableRequest(Arrays.asList(new
                AttributeDefinition(hashKeyName, ScalarAttributeType.S),
                new AttributeDefinition(rangeKeyName, ScalarAttributeType.S)),
                tableName, Arrays.asList(new KeySchemaElement(hashKeyName, KeyType.HASH),
                new KeySchemaElement(rangeKeyName, KeyType.RANGE)),
                new ProvisionedThroughput(10L, 10L));
        if (tableName.equals(Constants.databaseTableName)) {
            request.setAttributeDefinitions(Arrays.asList(new
                            AttributeDefinition("item_type", ScalarAttributeType.S),
                    new AttributeDefinition("username", ScalarAttributeType.S),
                    new AttributeDefinition("id", ScalarAttributeType.S)));
            request = request.withGlobalSecondaryIndexes(
                    new GlobalSecondaryIndex()
                    .withIndexName("item_type-username-index")
                    .withProvisionedThroughput(new ProvisionedThroughput()
                            .withReadCapacityUnits((long) 10)
                            .withWriteCapacityUnits((long) 10))
                    .withProjection(new Projection().withProjectionType(ProjectionType.ALL))
                    .withKeySchema(Arrays.asList(new KeySchemaElement("item_type", KeyType.HASH),
                    new KeySchemaElement("username", KeyType.RANGE)))
            );
        }

        client.createTable(request);

        return new Table(client, tableName);
    }
}
