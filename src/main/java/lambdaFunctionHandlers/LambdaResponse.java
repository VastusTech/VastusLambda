package main.java.lambdaFunctionHandlers;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;

/**
 * The POJO for the response from the Lambda invocation.
 */
public class LambdaResponse {
    public String secretKey = "vastustechnologies";
    public String timestamp;
    public Object data;

    /**
     * Constructor for the response that takes in an Object to return as data. Also initializes the
     * return time string.
     *
     * @param data The data to return to the client.
     */
    public LambdaResponse(Object data) {
        timestamp = new DateTime().toString(DateTimeFormat.fullDateTime());
        this.data = data;
    }

    public String getSecretKey() {
        return secretKey;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public Object getData() {
        return data;
    }
}
