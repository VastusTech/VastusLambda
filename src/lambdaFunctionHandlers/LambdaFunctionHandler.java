package lambdaFunctionHandlers;

import com.amazonaws.services.lambda.runtime.Context;

// TODO MAKE SURE THAT ALL THE CONSTRUCTORS AND SETTERS/GETTERS ARE IN HERE
class LambdaRequest {

}

class LambdaResponse {

}

public class LambdaFunctionHandler {
    public LambdaResponse handleRequest(LambdaRequest input, Context context) throws Exception {
        context.getLogger().log("Input: " + input);

        // TODO: implement your handler
        // return "Hello from Lambda";
        return new LambdaResponse();
    }

}
