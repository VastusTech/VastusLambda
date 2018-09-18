package databaseOperations;

import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.AttributeValueUpdate;

import java.util.Map;

abstract public class DatabaseAction {
    // This defines what it's doing to the database (CRUD)
    DBAction action;
    Map<String, AttributeValue> item;

    // These should only be used for UPDATE and UPDATESAFE databaseOperations.DBAction's
    Map<String, AttributeValueUpdate> updateItem;
    String conditionalExpression;
}
