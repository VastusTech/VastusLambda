package main.java.lambdaFunctionHandlers.highLevelHandlers.createDependencyHandlers;

import java.util.ArrayList;
import java.util.List;

import main.java.logic.Constants;
import main.java.databaseOperations.DatabaseActionCompiler;
import main.java.databaseOperations.databaseActionBuilders.EnterpriseDatabaseActionBuilder;
import main.java.lambdaFunctionHandlers.requestObjects.CreateEnterpriseRequest;

/**
 * TODO
 */
public class CreateEnterprise {
    public static List<DatabaseActionCompiler> getCompilers(String fromID, CreateEnterpriseRequest createEnterpriseRequest, boolean ifWithCreate) throws Exception {
        if (createEnterpriseRequest != null) {
            // Create enterprise
            if (true) { // TODO Check the Create Request
                List<DatabaseActionCompiler> compilers = new ArrayList<>();
                DatabaseActionCompiler databaseActionCompiler = new DatabaseActionCompiler();

                // TODO Permissions for Enterprises
                if (/* !fromID.equals(createCommentRequest.by) && */!fromID.equals(Constants.adminKey)) {
                    throw new Exception("PERMISSIONS ERROR: You can only create enterprises you own!");
                }

                // Create the object
                databaseActionCompiler.add(EnterpriseDatabaseActionBuilder.create(createEnterpriseRequest, ifWithCreate));

                // TODO Add the Enterprise to anything that it is required to be added to.

                compilers.add(databaseActionCompiler);

                return compilers;
            }
            else {
                throw new Exception("createCommentRequest is missing required fields!");
            }
        }
        else {
            throw new Exception("createEnterpriseRequest not initialized for CREATE statement!");
        }
    }
}
