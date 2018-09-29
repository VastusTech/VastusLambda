package main.java.lambdaFunctionHandlers.highLevelHandlers.readHandlers;

import main.java.Logic.Constants;
import main.java.databaseObjects.Challenge;
import main.java.lambdaFunctionHandlers.responseObjects.ChallengeResponse;
import main.java.lambdaFunctionHandlers.responseObjects.ObjectResponse;

import java.util.ArrayList;
import java.util.List;

public class ReadChallengesByID {
    public static List<ObjectResponse> handle(String[] challengeIDs) throws Exception {
        List<ObjectResponse> challengeResponses = new ArrayList<>();
        for (String id : challengeIDs) {
            Challenge challenge = Challenge.readChallenge(id);
            Constants.debugLog("Read challenge id: " + challenge.id + "\n");
            ChallengeResponse challengeResponse = new ChallengeResponse(challenge);
            challengeResponses.add(challengeResponse);
        }
        return challengeResponses;
    }
}
