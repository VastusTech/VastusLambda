package main.java.notifications;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.lambda.AWSLambda;
import com.amazonaws.services.lambda.AWSLambdaClientBuilder;
import com.amazonaws.services.lambda.model.InvokeRequest;
import com.amazonaws.services.lambda.model.InvokeResult;

import main.java.logic.Constants;

/**
 * Handles all of the Lambda invocation logic
 */
public class LambdaInvokeHandler {
    // Singleton Pattern!
    private static LambdaInvokeHandler instance;

    private AWSLambda client;

    /**
     * Get instance method that gets the single instance of the Singleton class.
     *
     * TODO This gets problematic if we ever have multi-threading.
     *
     * @return The LambdaInvokeHandler instance of the execution.
     */
    static public LambdaInvokeHandler getInstance() {
        if (LambdaInvokeHandler.instance == null) {
            LambdaInvokeHandler.instance = new LambdaInvokeHandler();
        }
        return LambdaInvokeHandler.instance;
    }

    /**
     * Private constructor for the Singleton handler that initializes the client for the Lambda
     * invocation.
     */
    private LambdaInvokeHandler() {
        // Initialize the handler
        client = AWSLambdaClientBuilder.standard().withRegion(Regions.fromName("us-east-1")).build();
    }

    /**
     * Invokes a Lambda function with a payload that needs to be a string of a properly formed JSON.
     *
     * @param functionName The name of the function to call.
     * @param payload The string of the JSON object to call the function with.
     */
    void invokeLambda(String functionName, String payload) {
        Constants.debugLog("SENDING LAMBDA TO FUNCTION = " + functionName + ", PAYLOAD: \n" + payload);
        InvokeRequest request = new InvokeRequest().withFunctionName(functionName).withPayload(payload);
        // TODO How to skip the response?
        InvokeResult result = client.invoke(request);
        Constants.debugLog("LOG RESULT FROM " + functionName + " LAMBDA: \n" + result.getLogResult());
    }
}
