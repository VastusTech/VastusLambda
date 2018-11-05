package main.java.lambdaFunctionHandlers;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import main.java.Logic.Constants;

public class LambdaFunctionHandler {
    public LambdaResponse handleRequest(LambdaRequest input, Context context) throws Exception {
        // LambdaResponse is initialized at the very end, before the return is sent
        Constants.setLogger(context.getLogger());
        Constants.debugLog(input.toString());
        return new LambdaResponse(input.process());
    }
}
