package main.java.testing;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.ItemCollection;
import com.amazonaws.services.dynamodbv2.document.ScanOutcome;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.model.AttributeDefinition;
import com.amazonaws.services.dynamodbv2.model.KeySchemaElement;
import com.amazonaws.services.dynamodbv2.model.KeyType;
import com.amazonaws.services.dynamodbv2.model.ProvisionedThroughput;
import com.amazonaws.services.dynamodbv2.model.ScalarAttributeType;
import com.amazonaws.services.dynamodbv2.model.ScanRequest;
import com.amazonaws.services.dynamodbv2.model.ScanResult;
import com.amazonaws.services.dynamodbv2.xspec.ExpressionSpecBuilder;
import com.amazonaws.services.dynamodbv2.xspec.ScanExpressionSpec;
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
import java.net.URL;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import main.java.logic.Constants;

public class TestTableHelper {
    private static TestTableHelper instance = null;

    public synchronized static TestTableHelper getInstance() {
        if (instance == null) {
            instance = new TestTableHelper();
        }
        return instance;
    }

    private TestTableHelper() {}

    public Table createAndFillDatabaseTable(AmazonDynamoDB client, String jsonPath)
            throws IllegalArgumentException, IOException {
        return createAndFillTable(client, jsonPath, Constants.databaseTableName,
                "item_type", "id");
    }

    public Table createAndFillMessageTable(AmazonDynamoDB client, String jsonPath)
            throws IllegalArgumentException, IOException {
        return createAndFillTable(client, jsonPath, Constants.messageTableName, "board",
                "id");
    }

    public void saveTableToJSON(AmazonDynamoDB client, String tableName, String outFilePath) throws IOException {
        saveTableToJSON(new Table(client, tableName), outFilePath);
    }

    public void saveTableToJSON(Table table, String filePath) throws IOException {
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

    private Table createTable(AmazonDynamoDB client, String tableName, String hashKeyName, String rangeKeyName) {
        client.createTable(Arrays.asList(new AttributeDefinition(hashKeyName,
                        ScalarAttributeType.S), new AttributeDefinition(rangeKeyName,
                        ScalarAttributeType.S)), tableName, Arrays.asList(new
                        KeySchemaElement(hashKeyName, KeyType.HASH),
                        new KeySchemaElement(rangeKeyName, KeyType.RANGE)),
                new ProvisionedThroughput(10L, 10L));
        return new Table(client, tableName);
    }
}
