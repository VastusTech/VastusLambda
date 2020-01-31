package main.java.lambdaFunctionHandlers.requestObjects;

import main.java.databaseOperations.exceptions.ExceedsDatabaseLimitException;
import main.java.logic.Constants;

/**
 * Defines the interface for a Request object. Forces the request object to have no empty strings
 * inputted through Lambda.
 */
public abstract class CreateObjectRequest {
    /**
     * The method that will be called to check if the create object request has any empty strings.
     *
     * @return Whether the request contains empty strings or not.
     */
    public abstract boolean ifHasEmptyString() throws ExceedsDatabaseLimitException;

    /**
     * Determines if any fields are empty Strings.
     *
     * @param fields The fields for the request.
     * @return Whether any fields are empty Strings.
     * @throws ExceedsDatabaseLimitException If any strings exceed the hard database limit.
     */
    public static boolean hasEmptyString(String... fields) throws ExceedsDatabaseLimitException {
        for (String field : fields) {
            if (field != null) {
                if (field.equals("")) {
                    return true;
                }
                else if (field.length() > Constants.hardStringLengthLimit){
                    throw new ExceedsDatabaseLimitException("String in Create request field exceeds hard length limit!");
                }
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
    public static boolean arrayHasEmptyString(String[]... arrayFields) throws ExceedsDatabaseLimitException {
        for (String[] field : arrayFields) {
            if (field != null) {
                for (String arrayField : field) {
                    if (arrayField != null) {
                        if (arrayField.equals("")) {
                            return true;
                        }
                        else if (arrayField.length() > Constants.hardStringLengthLimit) {
                            throw new ExceedsDatabaseLimitException("String in Create request array field exceeds hard length limit!");
                        }
                    }
                }
            }
        }
        return false;
    }
}
