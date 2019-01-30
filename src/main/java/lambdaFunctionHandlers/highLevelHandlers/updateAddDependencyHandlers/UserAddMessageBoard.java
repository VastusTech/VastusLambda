package main.java.lambdaFunctionHandlers.highLevelHandlers.updateAddDependencyHandlers;

import java.util.ArrayList;
import java.util.List;

import main.java.Logic.Constants;
import main.java.databaseOperations.DatabaseAction;
import main.java.databaseOperations.databaseActionBuilders.UserDatabaseActionBuilder;

public class UserAddMessageBoard {
    public static List<DatabaseAction> getActions(String fromID, String userID, String itemType, String
              messageBoard) throws Exception {
        List<DatabaseAction> databaseActions = new ArrayList<>();

        if (!fromID.equals(userID) && !fromID.equals(Constants.adminKey)) {
            throw new Exception("PERMISSIONS ERROR: You can only update a user you are!");
        }

        databaseActions.add(UserDatabaseActionBuilder.updateAddMessageBoard(userID, itemType, messageBoard));

        return databaseActions;
    }
}
