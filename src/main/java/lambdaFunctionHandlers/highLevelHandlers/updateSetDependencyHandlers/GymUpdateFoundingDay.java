package main.java.lambdaFunctionHandlers.highLevelHandlers.updateSetDependencyHandlers;

import main.java.databaseOperations.DatabaseAction;
import main.java.databaseOperations.databaseActionBuilders.GymDatabaseActionBuilder;
import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.List;

public class GymUpdateFoundingDay {
    public static List<DatabaseAction> getActions(String gymID, String foundingDay) throws Exception {
        return UserUpdateBirthday.getActions(gymID, "Gym", foundingDay);
    }
}
