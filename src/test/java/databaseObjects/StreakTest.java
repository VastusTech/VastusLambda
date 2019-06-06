package test.java.databaseObjects;

import com.amazonaws.services.dynamodbv2.document.Item;

import main.java.databaseObjects.Streak;
import main.java.logic.TimeHelper;

public class StreakTest {
    static Item getStreakTestItem() throws Exception {
        return new Item()
                .withString("item_type", "Streak")
                .withString("id", "ST0001")
                .withNumber("marker", 1)
                .withString("time_created", TimeHelper.nowString())
                .withString("owner", "OWNER")
                .withString("about", "ABOUT")
                .withNumber("N", 1)
                .withNumber("bestN", 1)
                .withNumber("currentN", 1)
                .withString("lastUpdated", TimeHelper.nowString())
                .withString("lastAttemptStarted", TimeHelper.nowString())
                .withString("streakType", "STREAKTYPE")
                .withString("updateSpanType", "daily")
                .withString("updateInterval", "2")
                .withString("streakN", "1") ;
    }

    public void test() {
        Streak streak;
    }
}
