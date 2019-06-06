package test.java.databaseObjects;

import com.amazonaws.services.dynamodbv2.document.Item;

import main.java.logic.TimeHelper;

public class InviteTest {
    static Item getInviteTestItem() throws Exception {
        return new Item()
                .withString("item_type", "Invite")
                .withString("id", "IN0001")
                .withNumber("marker", 1)
                .withString("time_created", TimeHelper.nowString())
                ;
    }
}
