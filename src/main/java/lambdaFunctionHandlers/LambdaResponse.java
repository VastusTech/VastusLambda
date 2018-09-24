package main.java.lambdaFunctionHandlers;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;

public class LambdaResponse {
    private String secretKey = "vastustechnologies";
    private String timestamp;
    private Object data;

    public LambdaResponse(Object data) {
        timestamp = new DateTime().toString(DateTimeFormat.fullDateTime());
        this.data = data;
    }
}
