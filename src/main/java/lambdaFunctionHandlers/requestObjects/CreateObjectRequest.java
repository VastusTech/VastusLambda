package main.java.lambdaFunctionHandlers.requestObjects;

/**
 * Defines the interface for a Request object. Forces the request object to have no empty strings
 * inputted through Lambda.
 */
public abstract class CreateObjectRequest {
    public abstract boolean ifHasEmptyString();

    public boolean hasEmptyString(String... fields) {
        for (String field : fields) {
            if (field != null && field.equals("")) {
                return true;
            }
        }
        return false;
    }

    public boolean arrayHasEmptyString(String[]... arrayFields) {
        for (String[] field : arrayFields) {
            for (String arrayField : field) {
                if (arrayField != null && arrayField.equals("")) {
                    return true;
                }
            }
        }
        return false;
    }
}
