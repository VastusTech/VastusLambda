package main.java.lambdaFunctionHandlers;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;

public class LambdaResponse {
    public String secretKey = "vastustechnologies";
    public String timestamp;
    public Object data;

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
