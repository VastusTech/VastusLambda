package main.java.lambdaFunctionHandlers;

import com.amazonaws.services.lambda.runtime.Context;

public class LambdaFunctionHandler {
    public LambdaResponse handleRequest(LambdaRequest input, Context context) throws Exception {
        // LambdaResponse is initialized at the very end, before the return is sent
        return new LambdaResponse(input.process());
    }

}
