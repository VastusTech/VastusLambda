package main.java.lambdaFunctionHandlers.requestObjects;

/**
 * The POJO for the request if the Lambda caller wants to create a Enterprise in the database.
 */
public class CreateEnterpriseRequest extends CreateObjectRequest {
    // TODO What needs to be in here?
    public CreateEnterpriseRequest() {

    }

    @Override
    public boolean ifHasEmptyString() {
        return false;
    }


}
