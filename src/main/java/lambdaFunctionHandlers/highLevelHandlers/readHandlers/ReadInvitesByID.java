package main.java.lambdaFunctionHandlers.highLevelHandlers.readHandlers;

import main.java.databaseObjects.Invite;
import main.java.lambdaFunctionHandlers.responseObjects.InviteResponse;
import main.java.lambdaFunctionHandlers.responseObjects.ObjectResponse;

import java.util.ArrayList;
import java.util.List;

public class ReadInvitesByID {
    public static List<ObjectResponse> handle(String[] reviewIDs) throws Exception {
        List<ObjectResponse> inviteResponses = new ArrayList<>();
        for (String id : reviewIDs) {
            inviteResponses.add(new InviteResponse(Invite.readInvite(id)));
        }
        return inviteResponses;
    }
}
