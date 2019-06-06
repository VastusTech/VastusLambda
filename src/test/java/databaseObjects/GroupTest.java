package test.java.databaseObjects;

import com.amazonaws.services.dynamodbv2.document.Item;

import main.java.logic.TimeHelper;

public class GroupTest {
    static Item getGroupTestItem() throws Exception {
        return new Item()
                .withString("item_type", "Group")
                .withString("id", "GR0001")
                .withNumber("marker", 1)
                .withString("time_created", TimeHelper.nowString())
                ;
    }
}
