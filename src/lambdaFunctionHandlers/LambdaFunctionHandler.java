package lambdaFunctionHandlers;

import com.amazonaws.services.lambda.runtime.Context;
import lambdaFunctionHandlers.requestObjects.*;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

enum Action {
    CREATE,
    READ,
    UPDATESET,
    UPDATEADD,
    UPDATEREMOVE,
    DELETE;

    public static Boolean contains(String string) {
        for (Action action: values()) {
            if (action.name().equals(string)) {
                return true;
            }
        }
        return false;
    }

}

enum SpecifyAction {
    BYID,
    BYUSERNAME,
    GETALL,
    FORSURVEY;

    public static Boolean contains(String string) {
        for (SpecifyAction specifyAction: values()) {
            if (specifyAction.name().equals(string)) {
                return true;
            }
        }
        return false;
    }
}

// TODO MAKE SURE THAT ALL THE CONSTRUCTORS AND SETTERS/GETTERS ARE IN HERE
class LambdaRequest {
    String fromID;
    String action;
    String specifyAction;
    String itemType;
    String attributeName;
    String[] attributeValues;

    CreateClientRequest createClientRequest;
    CreateTrainerRequest createTrainerRequest;
    CreateGymRequest createGymRequest;
    CreateWorkoutRequest createWorkoutRequest;
    CreateReviewRequest createReviewRequest;
}

class LambdaResponse {
    String secretKey = "vastustechnologies";
    String timestamp;
    Object data;

    public LambdaResponse(Object data) {
        timestamp = new DateTime().toString(DateTimeFormat.fullDateTime());
        this.data = data;
    }
}

public class LambdaFunctionHandler {
    public LambdaResponse handleRequest(LambdaRequest input, Context context) throws Exception {
        // context.getLogger().log("Input: " + input);

        // TODO: implement your handler
        // return "Hello from Lambda";
        return new LambdaResponse("Hello World!");
    }

}
