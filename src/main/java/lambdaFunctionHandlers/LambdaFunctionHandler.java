package main.java.lambdaFunctionHandlers;

import com.amazonaws.services.lambda.runtime.Context;

import main.java.databaseOperations.DynamoDBHandler;
import main.java.logic.Constants;
import main.java.logic.debugging.SingletonTimer;

/**
 * This is the initial entry class for the AWS Lambda execution, where the context first begins.
 */
public class LambdaFunctionHandler {
    /**
     * This is the first method that is executed for AWS Lambda, where the payload is funnelled into
     * the {@link LambdaRequest} input and the {@link Context} indicates the context of the
     * execution.
     *
     * @param input The POJO to handle the input JSON.
     * @param context The context from the caller.
     * @return The response for the AWS Lambda call. What to return.
     * @throws Exception If anything goes wrong during the entire execution.
     */
    public LambdaResponse handleRequest(LambdaRequest input, Context context) throws Exception {
        // LambdaResponse is initialized at the very end, before the return is sent
        // Put in a ping response to return as quickly as possible
        if (input.getAction() != null && input.getAction().equals("PING")) {
            DynamoDBHandler.getInstance();
            return new LambdaResponse(null);
        }
        Constants.setIfDevelopment(input.ifDevelopment());
        Constants.setLogger(context.getLogger());
        Constants.debugLog(input.toString());
        // Start the time testing
        SingletonTimer.get().start("Get into input");
        LambdaResponse response = new LambdaResponse(input.process());
        SingletonTimer.get().finish();
        if (Constants.ifDebug) { Constants.debugLog(SingletonTimer.get().toString()); }
        return response;
    }
}
