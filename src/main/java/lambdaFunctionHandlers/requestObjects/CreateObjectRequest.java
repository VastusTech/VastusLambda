package main.java.lambdaFunctionHandlers.requestObjects;

/**
 * Defines the interface for a Request object. Forces the request object to have no empty strings
 * inputted through Lambda.
 */
public abstract class CreateObjectRequest {
    public abstract boolean ifHasEmptyString();

    /**
     * Determines if any fields are empty Strings.
     *
     * @param fields The fields for the request.
     * @return Whether any fields are empty Strings.
     */
    public boolean hasEmptyString(String... fields) {
        for (String field : fields) {
            if (field != null && field.equals("")) {
                return true;
            }
        }
        return false;
    }

    /**
     * Determines whether if any array fields in the request have empty Strings.
     *
     * @param arrayFields The String array fields in the request.
     * @return Whether the array fields have any empty Strings.
     */
    public boolean arrayHasEmptyString(String[]... arrayFields) {
        for (String[] field : arrayFields) {
            if (field != null) {
                for (String arrayField : field) {
                    if (arrayField != null && arrayField.equals("")) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
}
