package main.java.lambdaFunctionHandlers.highLevelHandlers.createDependencyHandlers;

import java.util.ArrayList;
import java.util.List;

import main.java.logic.Constants;
import main.java.databaseOperations.DatabaseActionCompiler;
import main.java.databaseOperations.databaseActionBuilders.EnterpriseDatabaseActionBuilder;
import main.java.lambdaFunctionHandlers.requestObjects.CreateEnterpriseRequest;

/**
 * Creates an Enterprise in the database. TODO More details needed to finish this.
 */
public class CreateEnterprise {
    public static List<DatabaseActionCompiler> getCompilers(String fromID, CreateEnterpriseRequest createEnterpriseRequest, int depth) throws Exception {
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
                // TODO If we ever try to create Enterprises automatically, figure out which
                // TODO attributes need which passover Identifiers.
                databaseActionCompiler.add(EnterpriseDatabaseActionBuilder.create(createEnterpriseRequest, null));

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
