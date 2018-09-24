package main.java.lambdaFunctionHandlers;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import main.java.Logic.Constants;

public class LambdaFunctionHandler {
    public LambdaResponse handleRequest(LambdaRequest input, Context context) throws Exception {
        // LambdaResponse is initialized at the very end, before the return is sent
        Constants.logger = context.getLogger();
        Constants.logger.log("This is what the logger does.");
        return new LambdaResponse(input.process());
    }

}
