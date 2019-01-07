package main.java.notifications;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.lambda.AWSLambda;
import com.amazonaws.services.lambda.AWSLambdaClientBuilder;
import com.amazonaws.services.lambda.model.InvokeRequest;
import com.amazonaws.services.lambda.model.InvokeResult;

import javax.json.Json;

import main.java.Logic.Constants;

public class LambdaInvokeHandler {
    // Singleton Pattern!
    private static LambdaInvokeHandler instance;

    private AWSLambda client;

    // Used for the singleton pattern!
    static public LambdaInvokeHandler getInstance() {
        if (LambdaInvokeHandler.instance == null) {
            LambdaInvokeHandler.instance = new LambdaInvokeHandler();
        }
        return LambdaInvokeHandler.instance;
    }

    private LambdaInvokeHandler() {
        // Initialize the handler
        client = AWSLambdaClientBuilder.standard().withRegion(Regions.fromName("us-east-1")).build();
    }

    public void invokeLambda(String functionName, String payload) {
        InvokeRequest request = new InvokeRequest().withFunctionName(functionName).withPayload(payload);
        // TODO How to skip the response?
        InvokeResult result = client.invoke(request);
        Constants.debugLog("LOG RESULT FROM FIREBASE LAMBDA: \n" + result.getLogResult());
    }
}
