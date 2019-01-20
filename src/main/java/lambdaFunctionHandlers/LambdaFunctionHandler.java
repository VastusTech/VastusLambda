package main.java.lambdaFunctionHandlers;

import com.amazonaws.services.lambda.runtime.Context;
import main.java.Logic.Constants;

public class LambdaFunctionHandler {
    public LambdaResponse handleRequest(LambdaRequest input, Context context) throws Exception {
        // LambdaResponse is initialized at the very end, before the return is sent
        // Put in a ping response to return as quickly as possible
        if (input.getAction() != null && input.getAction().equals("PING")) {
            return new LambdaResponse(null);
        }
        Constants.setLogger(context.getLogger());
        Constants.debugLog(input.toString());
        return new LambdaResponse(input.process());
    }
}
