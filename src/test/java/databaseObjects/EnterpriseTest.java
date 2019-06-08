package test.java.databaseObjects;

import com.amazonaws.services.dynamodbv2.document.Item;

import main.java.logic.TimeHelper;

// TODO REVISIT ONCE IMPLEMENTED BETTER
public class EnterpriseTest {
    static Item getEnterpriseTestItem() throws Exception {
        return new Item()
                .withString("item_type", "Enterprise")
                .withString("id", "EN0001")
                .withNumber("marker", 1)
                .withString("time_created", TimeHelper.nowString()) ;
    }
}
