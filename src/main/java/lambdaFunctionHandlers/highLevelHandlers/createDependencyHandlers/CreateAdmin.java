package main.java.lambdaFunctionHandlers.highLevelHandlers.createDependencyHandlers;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.List;

import main.java.databaseOperations.DatabaseActionCompiler;
import main.java.databaseOperations.databaseActionBuilders.AdminDatabaseActionBuilder;
import main.java.lambdaFunctionHandlers.requestObjects.CreateAdminRequest;
import main.java.logic.Constants;

public class CreateAdmin {
    public static List<DatabaseActionCompiler> getCompilers(String fromID, CreateAdminRequest createAdminRequest, int depth) throws Exception {
        if (createAdminRequest != null) {
            // Create admin
            if (createAdminRequest.name != null && createAdminRequest.email != null
                    && createAdminRequest.username != null) {
                List<DatabaseActionCompiler> compilers = new ArrayList<>();
                DatabaseActionCompiler databaseActionCompiler = new DatabaseActionCompiler();

                if (!fromID.equals(Constants.adminKey)) {
                    throw new Exception("PERMISSIONS ERROR: You can only create an Admin using the admin key!");
                }

                // Check to see if the request features are well formed (i.e not invalid date or time)
                if (createAdminRequest.birthday != null) { new DateTime(createAdminRequest.birthday); }

                // TODO If we ever try to create Sponsors automatically, figure out which
                // TODO attributes need which passover Identifiers.
                databaseActionCompiler.add(AdminDatabaseActionBuilder.create(createAdminRequest, null));

                compilers.add(databaseActionCompiler);

                return compilers;
            }
            else {
                throw new Exception("createAdminRequest is missing required fields!");
            }
        }
        else {
            throw new Exception("createAdminRequest not initialized for CREATE statement!");
        }
    }
}
