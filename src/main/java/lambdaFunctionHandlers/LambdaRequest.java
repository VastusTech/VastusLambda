package main.java.lambdaFunctionHandlers;

import main.java.Logic.Constants;
import main.java.Logic.ItemType;
import main.java.databaseObjects.DatabaseObject;
import main.java.databaseOperations.DatabaseAction;
import main.java.databaseOperations.DatabaseActionCompiler;
import main.java.databaseOperations.DynamoDBHandler;
import main.java.databaseOperations.databaseActionBuilders.*;
import main.java.lambdaFunctionHandlers.highLevelHandlers.createDependencyHandlers.*;
import main.java.lambdaFunctionHandlers.highLevelHandlers.deleteDependencyHandlers.*;
import main.java.lambdaFunctionHandlers.highLevelHandlers.readHandlers.*;
import main.java.lambdaFunctionHandlers.highLevelHandlers.updateAddDependencyHandlers.*;
import main.java.lambdaFunctionHandlers.highLevelHandlers.updateRemoveDependencyHandlers.*;
import main.java.lambdaFunctionHandlers.highLevelHandlers.updateSetDependencyHandlers.*;
import main.java.lambdaFunctionHandlers.requestObjects.*;
import main.java.lambdaFunctionHandlers.responseObjects.*;
import java.util.List;


// TODO MAKE SURE THAT ALL THE CONSTRUCTORS AND SETTERS/GETTERS ARE IN HERE
public class LambdaRequest {
    private String fromID;
    private String action;
    private String specifyAction;
    private String itemType;
    private String[] identifiers;
    private String attributeName;
    private String[] attributeValues;

    private CreateClientRequest createClientRequest;
    private CreateTrainerRequest createTrainerRequest;
    private CreateGymRequest createGymRequest;
    private CreateWorkoutRequest createWorkoutRequest;
    private CreateReviewRequest createReviewRequest;
    private CreatePartyRequest createPartyRequest;
    private CreateChallengeRequest createChallengeRequest;

    private enum Action {
        CREATE,
        READ,
        UPDATESET,
        UPDATEADD,
        UPDATEREMOVE,
        DELETE
    }

    private enum AttributeName {
        // User ===========================
        name,
        gender,
        birthday,
        foundingDay,
        email,
        username,
        profileImagePath,
        scheduledWorkouts,
        completedWorkouts,
        scheduledTimes,
        reviewsBy,
        reviewsAbout,
        friendlinessRating,
        effectivenessRating,
        reliabilityRating,
        bio,
        // Client ==========================
        friends,
        friendRequests,
        scheduledParties,
        scheduledChallenges,
        // Trainer =========================
        gymID,
        availableTimes,
        workoutSticker,
        preferredIntensity,
        workoutCapacity,
        workoutPrice,
        // Gym =============================
        address,
        trainerIDs,
        weeklyHours,
        vacationTimes,
        sessionCapacity,
        gymType,
        paymentSplit,
        // Workout =========================
        time,
        trainerID,
        clientIDs,
        capacity,
        sticker,
        intensity,
        missingReviews,
        price,
        // Review ==========================
        byID,
        aboutID,
        description,
        // Party ===========================
        title,
        memberIDs,
        // Challenge =======================
        goal,
    }

    // This is where the inputs are handled!
    public Object process() throws Exception {
        try {
            // First check all of the inputs for the stuff
            checkInputs();

            switch (Action.valueOf(action)) {
                // TODO Check permissions
                case CREATE:
                    if (specifyAction.equals("ForSurvey") && itemType.equals("Review")) {
                        // Create Review and remove from workout missingReviews
                        if (identifiers.length == 1) {
                            if (createReviewRequest != null && !fromID.equals(createReviewRequest.byID)) {
                                throw new Exception("The FromID doesn't have permission to write that review!");
                            }
                            return handleSurveyCreate(identifiers[0]);
                        }
                        else {
                            throw new Exception("There should be 1 identifier when creating a Review for the survey!");
                        }
                    }
                    else if (specifyAction.equals("")) {
                        // Create the item
                        if (identifiers.length == 0) {
                            // TODO Permissions for creating a workout?
                            if (createReviewRequest != null && !fromID.equals(createReviewRequest.byID)) {
                                throw new Exception("The FromID doesn't have permission to create that item!");
                            }
                            return handleCreate();
                        }
                        else {
                            throw new Exception("There should be no identifies for a normal CREATE statement!");
                        }
                    }
                    else {
                        // Unacceptable action series
                        throw new Exception("Specify Action: \"" + specifyAction + "\" not recognized for action \"" +
                                action + "\" on itemType \"" + itemType + "\" !");
                    }
                case READ:
                    if (specifyAction.equals("ByID") || specifyAction.equals("")) {
                        // Read using the ID number provided
                        return handleIDRead(identifiers);
                    }
                    else if (specifyAction.equals("ByUsername")) {
                        // Read using the username provided
                        return handleUsernameRead(identifiers);
                    }
                    else if (specifyAction.equals("GetAll") && itemType.equals("Gym")) {
                        // Read all of the gyms
                        return handleGetAll();
                    }
                    else {
                        // Unacceptable action series
                        throw new Exception("Specify Action: \"" + specifyAction + "\" not recognized for action \"" +
                                action + "\" on itemType \"" + itemType + "\" !");
                    }
                case UPDATESET:
                    if (specifyAction.equals("")) {
                        // Update the attribute: SET it.
                        // There are a number of things this could be. Some require checking, some require multi-faceted
                        if (identifiers.length == 1) {
                            if ((itemType.equals("Client") || itemType.equals("Trainer") || itemType.equals("Gym")) &&
                                    !identifiers[0].equals(fromID)) {
                                throw new Exception("The FromID does not have the permissions to update that object!");
                            }
                            if (attributeName == null || attributeName.equals("") || attributeValues == null) {
                                throw new Exception("Need to set attributeName and attributeValues for update " +
                                        "statement");
                            }
                            handleUpdateSet(identifiers[0]);
                            return null;
                        }
                        else {
                            throw new Exception("There should only be one identifier in an UPDATESET statement!");
                        }
                    }
                    else {
                        // Unacceptable action series
                        throw new Exception("Specify Action: \"" + specifyAction + "\" not recognized for action \"" +
                                action + "\" on itemType \"" + itemType + "\" !");
                    }
                case UPDATEADD:
                    if (specifyAction.equals("")) {
                        // Update the attribute: ADD it.
                        // There are a number of things this could be. Some require checking, some require multi-faceted
                        if (identifiers.length == 1) {
                            if ((itemType.equals("Client") || itemType.equals("Trainer") || itemType.equals("Gym")) &&
                                    !identifiers[0].equals(fromID) && attributeName != null && attributeName.equals
                                    ("friendRequests")) {
                                throw new Exception("The FromID does not have the permissions to update that object!");
                            }
                            if (attributeName == null || attributeName.equals("") || attributeValues == null) {
                                throw new Exception("Need to set attributeName and attributeValues for update " +
                                        "statement");
                            }
                            handleUpdateAdd(identifiers[0]);
                            return null;
                        }
                        else {
                            throw new Exception("There should only be one identifier in an UPDATEADD statement!");
                        }
                    }
                    else {
                        // Unacceptable action series
                        throw new Exception("Specify Action: \"" + specifyAction + "\" not recognized for action \"" +
                                action + "\" on itemType \"" + itemType + "\" !");
                    }
                case UPDATEREMOVE:
                    if (specifyAction.equals("")) {
                        // Update the attribute: REMOVE it.
                        // There are a number of things this could be. Some require checking, some require multi-faceted
                        if (identifiers.length == 1) {
                            if ((itemType.equals("Client") || itemType.equals("Trainer") || itemType.equals("Gym")) &&
                                    !identifiers[0].equals(fromID)) {
                                throw new Exception("The FromID does not have the permissions to update that object!");
                            }
                            if (attributeName == null || attributeName.equals("") || attributeValues == null) {
                                throw new Exception("Need to set attributeName and attributeValues for update " +
                                        "statement");
                            }
                            handleUpdateRemove(identifiers[0]);
                            return null;
                        }
                        else {
                            throw new Exception("There should only be one identifier in an UPDATEREMOVE statement!");
                        }
                    }
                    else {
                        // Unacceptable action series
                        throw new Exception("Specify Action: \"" + specifyAction + "\" not recognized for action \"" +
                                action + "\" on itemType \"" + itemType + "\" !");
                    }
                case DELETE:
                    if (specifyAction.equals("")) {
                        // Delete the item from the database.
                        if (identifiers.length == 1) {
                            if ((itemType.equals("Client") || itemType.equals("Trainer") || itemType.equals("Gym")) &&
                                    !identifiers[0].equals(fromID)) {
                                throw new Exception("The FromID does not have the permissions to update that object!");
                            }
                            handleDelete(identifiers[0]);
                            return null;
                        }
                        else {
                            throw new Exception("There should only be one identifier in a DELETE statement!");
                        }
                    }
                    else {
                        // Unacceptable action series
                        throw new Exception("Specify Action: \"" + specifyAction + "\" not recognized for action \"" +
                                action + "\" on itemType \"" + itemType + "\" !");
                    }
                default:
                    throw new Exception("Item type: " + itemType + " recognized but not handled?");
            }

        }
        catch (IllegalArgumentException e) {
            throw new Exception("Action: \"" + action + "\" not recognized! Error: " + e.getLocalizedMessage());
        }
    }

    private void checkInputs() throws Exception {
        // Action, ItemType, SpecifyAction, attributeName
        if (specifyAction == null) {
            specifyAction = "";
        }
        if (identifiers == null) {
            identifiers = new String[]{};
        }

        if (action != null  && fromID != null && !fromID.equals("") && itemType != null) {
            if (attributeName != null) {
                if (attributeName.equals("")) {
                    throw new Exception("No fields are allowed to be empty strings!");
                }
            }

            if (attributeValues != null) {
                for (String value : attributeValues) {
                    if (value.equals("")) {
                        throw new Exception("No fields are allowed to be empty strings!");
                    }
                }
            }
            int numCreateRequest = 0;
            if (createClientRequest != null) {
                numCreateRequest++;
                Constants.debugLog("Has a create client request!\n");
            }
            if (createTrainerRequest != null) {
                numCreateRequest++;
                Constants.debugLog("Has a create trainer request!\n");
            }
            if (createGymRequest != null) {
                numCreateRequest++;
                Constants.debugLog("Has a create gym request!\n");
            }
            if (createWorkoutRequest != null) {
                numCreateRequest++;
                Constants.debugLog("Has a create workout request!\n");
            }
            if (createReviewRequest != null) {
                numCreateRequest++;
                Constants.debugLog("Has a create review request!\n");
            }
            if (createPartyRequest != null) {
                numCreateRequest++;
                Constants.debugLog("Has a create party request!\n");
            }
            if (createChallengeRequest != null) {
                numCreateRequest++;
                Constants.debugLog("Has a create challenge request!\n");
            }
            if (numCreateRequest > 1) {
                throw new Exception("Only one create request allowed at a time!");
            }
        }
        else {
            //throw new Exception("action = " + action + ", fromID = " + fromID + ", itemType = " + itemType +  "!");
            throw new Exception("action, fromID, and itemType fields must be inputted");
        }
    }

    private String handleCreate() throws Exception {
        try {
            // TODO Check the permissions for these guys
            switch (ItemType.valueOf(itemType)) {
                case Client:
                    return CreateClient.handle(createClientRequest);
                case Trainer:
                    return CreateTrainer.handle(createTrainerRequest);
                case Gym:
                    return CreateGym.handle(createGymRequest);
                case Workout:
                    return CreateWorkout.handle(createWorkoutRequest);
                case Review:
                    // Send a null for surveyWorkoutID because we're not creating it for a survey
                    return CreateReview.handle(createReviewRequest, null);
                case Party:
                    return CreateParty.handle(createPartyRequest);
                case Challenge:
                    return CreateChallenge.handle(createChallengeRequest);
                default:
                    throw new Exception("Item Type: " + itemType + " recognized but not handled?");
            }
        }
        catch (IllegalArgumentException e) {
            throw new Exception("Item Type: " + itemType + " not recognized! Error: " + e.getLocalizedMessage());
        }
    }

    private String handleSurveyCreate(String workoutID) throws Exception {
        return CreateReview.handle(createReviewRequest, workoutID);
    }

    private List<ObjectResponse> handleIDRead(String[] ids) throws Exception {
        try {
            switch (ItemType.valueOf(itemType)) {
                case Client:
                    return ReadClientsByID.handle(ids);
                case Trainer:
                    return ReadTrainersByID.handle(ids);
                case Gym:
                    return ReadGymsByID.handle(ids);
                case Workout:
                    return ReadWorkoutsByID.handle(ids);
                case Review:
                    return ReadReviewsByID.handle(ids);
                case Party:
                    return ReadPartiesByID.handle(ids);
                case Challenge:
                    return ReadChallengesByID.handle(ids);
                default:
                    throw new Exception("Item Type: " + itemType + " recognized but not handled?");
            }
        }
        catch (IllegalArgumentException e) {
            throw new Exception("Item Type: " + itemType + " not recognized! Error: " + e.getLocalizedMessage());
        }
    }

    private List<ObjectResponse> handleUsernameRead(String[] usernames) throws Exception {
        try {
            switch (ItemType.valueOf(itemType)) {
                case Client:
                    return ReadClientsByUsername.handle(usernames);
                case Trainer:
                    return ReadTrainersByUsername.handle(usernames);
                case Gym:
                    return ReadGymsByUsername.handle(usernames);
                case Workout:
                    throw new Exception("Can't query a workout by a username!");
                case Review:
                    throw new Exception("Can't query a review by a username!");
                case Party:
                    throw new Exception("Can't query a party by a username!");
                case Challenge:
                    throw new Exception("Can't query a challenge by a username!");
                default:
                    throw new Exception("Item Type: " + itemType + " recognized but not handled?");
            }
        }
        catch (IllegalArgumentException e) {
            throw new Exception("Item Type: " + itemType + " not recognized! Error: " + e.getLocalizedMessage());
        }
    }

    private List<ObjectResponse> handleGetAll() throws Exception {
        // Already Checked, so go to town
        return ReadAllGyms.handle();
    }

    public void handleUpdateSet(String id) throws Exception {
        // switch all attributes, then if necessary, item type
        DatabaseActionCompiler databaseActionCompiler = new DatabaseActionCompiler();

        if (attributeValues.length != 1 && !attributeName.equals("weeklyHours")) {
            throw new Exception("For updating " + attributeName + " on " + itemType + "attributeValues must be only 1" +
                    " item long!");
        }
        else if (attributeValues.length == 0 && attributeName.equals("weeklyHours")) {
            // DynamoDB can't handle empty string sets
            attributeValues = null;
        }

        try {
            switch (AttributeName.valueOf(attributeName)) {
                case name:
                    if (itemType.equals("Client") || itemType.equals("Trainer") || itemType.equals("Gym")) {
                        databaseActionCompiler.addAll(UserUpdateName.getActions(id, itemType, attributeValues[0]));
                    }
                    else {
                        throw new Exception("Unable to perform " + action + " to " + attributeName + "for a " + itemType + "!");
                    }
                    break;
                case gender:
                    if (itemType.equals("Client") || itemType.equals("Trainer") || itemType.equals("Gym")) {
                        databaseActionCompiler.addAll(UserUpdateGender.getActions(id, itemType, attributeValues[0]));
                    }
                    else {
                        throw new Exception("Unable to perform " + action + " to " + attributeName + "for a " + itemType + "!");
                    }
                    break;
                case birthday:
                    if (itemType.equals("Client") || itemType.equals("Trainer")) {
                        databaseActionCompiler.addAll(UserUpdateBirthday.getActions(id, itemType, attributeValues[0]));
                    }
                    else {
                        throw new Exception("Unable to perform " + action + " to " + attributeName + "for a " + itemType + "!");
                    }
                    break;
                case foundingDay:
                    if (itemType.equals("Gym")) {
                        databaseActionCompiler.addAll(GymUpdateFoundingDay.getActions(id, attributeValues[0]));
                    }
                    else {
                        throw new Exception("Unable to perform " + action + " to " + attributeName + "for a " + itemType + "!");
                    }
                    break;
                case email:
                    if (itemType.equals("Client") || itemType.equals("Trainer") || itemType.equals("Gym")) {
                        databaseActionCompiler.addAll(UserUpdateEmail.getActions(id, itemType, attributeValues[0]));
                    }
                    else {
                        throw new Exception("Unable to perform " + action + " to " + attributeName + "for a " + itemType + "!");
                    }
                    break;
                case profileImagePath:
                    if (itemType.equals("Client") || itemType.equals("Trainer") || itemType.equals("Gym")) {
                        databaseActionCompiler.addAll(UserUpdateProfileImagePath.getActions(id, itemType,
                                attributeValues[0]));
                    }
                    else {
                        throw new Exception("Unable to perform " + action + " to " + attributeName + "for a " + itemType + "!");
                    }
                    break;
                case bio:
                    if (itemType.equals("Client") || itemType.equals("Trainer") || itemType.equals("Gym")) {
                        databaseActionCompiler.addAll(UserUpdateBio.getActions(id, itemType, attributeValues[0]));
                    }
                    else {
                        throw new Exception("Unable to perform " + action + " to " + attributeName + "for a " + itemType + "!");
                    }
                    break;
                case workoutSticker:
                    if (itemType.equals("Trainer")) {
                        databaseActionCompiler.addAll(TrainerUpdateWorkoutSticker.getActions(id, attributeValues[0]));
                    }
                    else {
                        throw new Exception("Unable to perform " + action + " to " + attributeName + "for a " + itemType + "!");
                    }
                    break;
                case preferredIntensity:
                    if (itemType.equals("Trainer")) {
                        databaseActionCompiler.addAll(TrainerUpdatePreferredIntensity.getActions(id,
                                attributeValues[0]));
                    }
                    else {
                        throw new Exception("Unable to perform " + action + " to " + attributeName + "for a " + itemType + "!");
                    }
                    break;
                case workoutCapacity:
                    if (itemType.equals("Trainer")) {
                        databaseActionCompiler.addAll(TrainerUpdateWorkoutCapacity.getActions(id, attributeValues[0]));
                    }
                    else {
                        throw new Exception("Unable to perform " + action + " to " + attributeName + "for a " + itemType + "!");
                    }
                    break;
                case workoutPrice:
                    if (itemType.equals("Trainer")) {
                        databaseActionCompiler.addAll(TrainerUpdateWorkoutPrice.getActions(id, attributeValues[0]));
                    }
                    else {
                        throw new Exception("Unable to perform " + action + " to " + attributeName + "for a " + itemType + "!");
                    }
                    break;
                case address:
                    if (itemType.equals("Gym")) {
                        databaseActionCompiler.addAll(GymUpdateAddress.getActions(id, attributeValues[0]));
                    }
                    else if (itemType.equals("Party")) {
                        databaseActionCompiler.addAll(PartyUpdateAddress.getActions(id, attributeValues[0]));
                    }
                    else if (itemType.equals("Challenge")) {
                        databaseActionCompiler.addAll(ChallengeUpdateAddress.getActions(id, attributeValues[0]));
                    }
                    else {
                        throw new Exception("Unable to perform " + action + " to " + attributeName + "for a " + itemType + "!");
                    }
                    break;
                case weeklyHours:
                    if (itemType.equals("Gym")) {
                        databaseActionCompiler.addAll(GymUpdateWeeklyHours.getActions(id, attributeValues));
                    }
                    else {
                        throw new Exception("Unable to perform " + action + " to " + attributeName + "for a " + itemType + "!");
                    }
                    break;
                case sessionCapacity:
                    if (itemType.equals("Gym")) {
                        databaseActionCompiler.addAll(GymUpdateSessionCapacity.getActions(id, attributeValues[0]));
                    }
                    else {
                        throw new Exception("Unable to perform " + action + " to " + attributeName + "for a " + itemType + "!");
                    }
                    break;
                case gymType:
                    if (itemType.equals("Gym")) {
                        databaseActionCompiler.addAll(GymUpdateGymType.getActions(id, attributeValues[0]));
                    }
                    else {
                        throw new Exception("Unable to perform " + action + " to " + attributeName + "for a " + itemType + "!");
                    }
                    break;
                case paymentSplit:
                    if (itemType.equals("Gym")) {
                        databaseActionCompiler.addAll(GymUpdatePaymentSplit.getActions(id, attributeValues[0]));
                    }
                    else {
                        throw new Exception("Unable to perform " + action + " to " + attributeName + "for a " + itemType + "!");
                    }
                    break;
                case title:
                    if (itemType.equals("Party")) {
                        databaseActionCompiler.addAll(PartyUpdateTitle.getActions(id, attributeValues[0]));
                    }
                    else if (itemType.equals("Challenge")) {
                        databaseActionCompiler.addAll(ChallengeUpdateTitle.getActions(id, attributeValues[0]));
                    }
                    else {
                        throw new Exception("Unable to perform " + action + " to " + attributeName + "for a " + itemType + "!");
                    }
                    break;
                case description:
                    if (itemType.equals("Party")) {
                        databaseActionCompiler.addAll(PartyUpdateDescription.getActions(id, attributeValues[0]));
                    }
                    else if (itemType.equals("Challenge")) {
                        databaseActionCompiler.addAll(ChallengeUpdateDescription.getActions(id, attributeValues[0]));
                    }
                    else {
                        throw new Exception("Unable to perform " + action + " to " + attributeName + "for a " + itemType + "!");
                    }
                    break;
                case goal:
                    if (itemType.equals("Challenge")) {
                        databaseActionCompiler.addAll(ChallengeUpdateGoal.getActions(id, attributeValues[0]));
                    }
                    else {
                        throw new Exception("Unable to perform " + action + " to " + attributeName + "for a " + itemType + "!");
                    }
                    break;
                default:
                    throw new Exception("Can't perform an UPDATESET operation on " + attributeName + "!");
            }
        }
        catch (IllegalArgumentException e) {
            throw new Exception("AttributeName: " + attributeName + " not recognized! Error: " + e.getLocalizedMessage());
        }

        DynamoDBHandler.getInstance().attemptTransaction(databaseActionCompiler.getDatabaseActions());
    }

    public void handleUpdateAdd(String id) throws Exception {
        // switch all attributes, then if necessary, item type
        DatabaseActionCompiler databaseActionCompiler = new DatabaseActionCompiler();

        try {
            switch (AttributeName.valueOf(attributeName)) {
                case scheduledWorkouts:
                    if (itemType.equals("Client")) {
                        if (attributeValues.length == 1) {
                            databaseActionCompiler.addAll(ClientAddToWorkout.getActions(id, attributeValues[0]));
                        }
                        else {
                            throw new Exception("For updating " + attributeName + " on " + itemType +
                                    "attributeValues must be only 1 long!");
                        }
                    }
                    else {
                        throw new Exception("Unable to perform " + action + " to " + attributeName + "for a " +
                                itemType + "!");
                    }
                    break;
                case friends:
                    if (itemType.equals("Client")) {
                        databaseActionCompiler.addAll(ClientAddFriends.getActions(id, attributeValues));
                    }
                    else {
                        throw new Exception("Unable to perform " + action + " to " + attributeName + "for a " +
                                itemType + "!");
                    }
                    break;
                case friendRequests:
                    if (itemType.equals("Client")) {
                        databaseActionCompiler.addAll(ClientAddFriendRequests.getActions(id, attributeValues));
                    }
                    else {
                        throw new Exception("Unable to perform " + action + " to " + attributeName + "for a " +
                                itemType + "!");
                    }
                    break;
                case scheduledParties:
                    if (attributeValues.length == 1) {
                        if (itemType.equals("Client") && attributeValues[0].equals("Party")) {
                            databaseActionCompiler.addAll(ClientAddToParty.getActions(id, attributeValues[0]));
                        } else {
                            throw new Exception("Unable to perform " + action + " to " + attributeName + "for a " +
                                    itemType + "!");
                        }
                    }
                    else {
                        throw new Exception("For updating " + attributeName + " on " + itemType +
                                "attributeValues must be only 1 long!");
                    }
                    break;
                case scheduledChallenges:
                    if (attributeValues.length == 1) {
                        if (itemType.equals("Client") && attributeValues[0].equals("Challenge")) {
                            databaseActionCompiler.addAll(ClientAddToChallenge.getActions(id, attributeValues[0]));
                        } else {
                            throw new Exception("Unable to perform " + action + " to " + attributeName + "for a " +
                                    itemType + "!");
                        }
                    }
                    else {
                        throw new Exception("For updating " + attributeName + " on " + itemType +
                                "attributeValues must be only 1 long!");
                    }
                    break;
                case availableTimes:
                    if (itemType.equals("Trainer")) {
                        databaseActionCompiler.addAll(TrainerAddAvailableTimes.getActions(id, attributeValues));
                    }
                    else {
                        throw new Exception("Unable to perform " + action + " to " + attributeName + "for a " +
                                itemType + "!");
                    }
                    break;
                case vacationTimes:
                    if (itemType.equals("Gym")) {
                        databaseActionCompiler.addAll(GymAddVacationTimes.getActions(id, attributeValues));
                    }
                    else {
                        throw new Exception("Unable to perform " + action + " to " + attributeName + "for a " +
                                itemType + "!");
                    }
                    break;
                default:
                    throw new Exception("Can't perform an UPDATEADD operation on " + attributeName + "!");
            }
        }
        catch (IllegalArgumentException e) {
            throw new Exception("AttributeName: " + attributeName + " not recognized! Error: " + e.getLocalizedMessage());
        }

        DynamoDBHandler.getInstance().attemptTransaction(databaseActionCompiler.getDatabaseActions());
    }

    public void handleUpdateRemove(String id) throws Exception {
        // switch all attributes, then if necessary, item type
        DatabaseActionCompiler databaseActionCompiler = new DatabaseActionCompiler();

        try {
            switch (AttributeName.valueOf(attributeName)) {
                case scheduledWorkouts:
                    if (itemType.equals("Client")) {
                        if (attributeValues.length == 1) {
                            databaseActionCompiler.addAll(ClientRemoveFromWorkout.getActions(id, attributeValues[0]));
                        }
                        else {
                            throw new Exception("For updating " + attributeName + " on " + itemType +
                                    "attributeValues must be only 1 long!");
                        }
                    }
                    else {
                        throw new Exception("Unable to perform " + action + " to " + attributeName + "for a " +
                                itemType + "!");
                    }
                    break;
                case friends:
                    if (itemType.equals("Client")) {
                        databaseActionCompiler.addAll(ClientRemoveFriends.getActions(id, attributeValues));
                    }
                    else {
                        throw new Exception("Unable to perform " + action + " to " + attributeName + "for a " +
                                itemType + "!");
                    }
                    break;
                case friendRequests:
                    if (itemType.equals("Client")) {
                        databaseActionCompiler.addAll(ClientRemoveFriendRequests.getActions(id, attributeValues));
                    }
                    else {
                        throw new Exception("Unable to perform " + action + " to " + attributeName + "for a " +
                                itemType + "!");
                    }
                    break;
                case scheduledParties:
                    if (attributeValues.length == 1) {
                        if (itemType.equals("Client") && attributeValues[0].equals("Party")) {
                            databaseActionCompiler.addAll(ClientRemoveFromParty.getActions(id, attributeValues[0]));
                        } else {
                            throw new Exception("Unable to perform " + action + " to " + attributeName + "for a " +
                                    itemType + "!");
                        }
                    }
                    else {
                        throw new Exception("For updating " + attributeName + " on " + itemType +
                                "attributeValues must be only 1 long!");
                    }
                    break;
                case scheduledChallenges:
                    if (attributeValues.length == 1) {
                        if (itemType.equals("Client") && attributeValues[0].equals("Challenge")) {
                            databaseActionCompiler.addAll(ClientRemoveFromChallenge.getActions(id, attributeValues[0]));
                        } else {
                            throw new Exception("Unable to perform " + action + " to " + attributeName + "for a " +
                                    itemType + "!");
                        }
                    }
                    else {
                        throw new Exception("For updating " + attributeName + " on " + itemType +
                                "attributeValues must be only 1 long!");
                    }
                    break;
                case availableTimes:
                    if (itemType.equals("Trainer")) {
                        databaseActionCompiler.addAll(TrainerRemoveAvailableTimes.getActions(id, attributeValues));
                    }
                    else {
                        throw new Exception("Unable to perform " + action + " to " + attributeName + "for a " +
                                itemType + "!");
                    }
                    break;
                case vacationTimes:
                    if (itemType.equals("Gym")) {
                        databaseActionCompiler.addAll(GymRemoveVacationTimes.getActions(id, attributeValues));
                    }
                    else {
                        throw new Exception("Unable to perform " + action + " to " + attributeName + "for a " +
                                itemType + "!");
                    }
                    break;
                default:
                    throw new Exception("Can't perform an UPDATEREMOVE operation on " + attributeName + "!");
            }
        }
        catch (IllegalArgumentException e) {
            throw new Exception("AttributeName: " + attributeName + " not recognized! Error: " + e.getLocalizedMessage());
        }

        DynamoDBHandler.getInstance().attemptTransaction(databaseActionCompiler.getDatabaseActions());
    }

    public void handleDelete(String id) throws Exception {
        DatabaseActionCompiler databaseActionCompiler = new DatabaseActionCompiler();

        try {
            switch (ItemType.valueOf(itemType)) {
                case Client:
                    databaseActionCompiler.addAll(DeleteClient.getActions(id));
                    break;
                case Trainer:
                    databaseActionCompiler.addAll(DeleteTrainer.getActions(id));
                    break;
                case Gym:
                    databaseActionCompiler.addAll(DeleteGym.getActions(id));
                    break;
                // TODO MAKE SURE THESE PEOPLE ARE ALLOWED TO DO THIS (Workout and Review and Party and Challenge.)
                case Workout:
                    databaseActionCompiler.addAll(DeleteWorkout.getActions(id));
                    break;
                case Review:
                    databaseActionCompiler.addAll(DeleteReview.getActions(id));
                    break;
                case Party:
                    databaseActionCompiler.addAll(DeleteParty.getActions(id));
                case Challenge:
                    databaseActionCompiler.addAll(DeleteChallenge.getActions(id));
                default:
                    throw new Exception("Item Type: " + itemType + " recognized but not handled?");
            }
        }
        catch (IllegalArgumentException e) {
            throw new Exception("Item Type: \"" + itemType + "\" not recognized! Error: " + e.getLocalizedMessage());
        }
        DynamoDBHandler.getInstance().attemptTransaction(databaseActionCompiler.getDatabaseActions());
    }

    public LambdaRequest(String fromID, String action, String specifyAction, String itemType, String[] identifiers, String
            attributeName, String[] attributeValues, CreateClientRequest createClientRequest, CreateTrainerRequest createTrainerRequest, CreateGymRequest createGymRequest, CreateWorkoutRequest createWorkoutRequest, CreateReviewRequest createReviewRequest) {
        this.fromID = fromID;
        this.action = action;
        this.specifyAction = specifyAction;
        this.itemType = itemType;
        this.identifiers = identifiers;
        this.attributeName = attributeName;
        this.attributeValues = attributeValues;
        this.createClientRequest = createClientRequest;
        this.createTrainerRequest = createTrainerRequest;
        this.createGymRequest = createGymRequest;
        this.createWorkoutRequest = createWorkoutRequest;
        this.createReviewRequest = createReviewRequest;
    }

    public LambdaRequest() {}

    public String getFromID() {
        return fromID;
    }

    public void setFromID(String fromID) {
        this.fromID = fromID;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getSpecifyAction() {
        return specifyAction;
    }

    public void setSpecifyAction(String specifyAction) {
        this.specifyAction = specifyAction;
    }

    public String getItemType() {
        return itemType;
    }

    public void setItemType(String itemType) {
        this.itemType = itemType;
    }

    public String[] getIdentifiers() {
        return identifiers;
    }

    public void setIdentifiers(String[] identifiers) {
        this.identifiers = identifiers;
    }

    public String getAttributeName() {
        return attributeName;
    }

    public void setAttributeName(String attributeName) {
        this.attributeName = attributeName;
    }

    public String[] getAttributeValues() {
        return attributeValues;
    }

    public void setAttributeValues(String[] attributeValues) {
        this.attributeValues = attributeValues;
    }

    public CreateClientRequest getCreateClientRequest() {
        return createClientRequest;
    }

    public void setCreateClientRequest(CreateClientRequest createClientRequest) {
        this.createClientRequest = createClientRequest;
    }

    public CreateTrainerRequest getCreateTrainerRequest() {
        return createTrainerRequest;
    }

    public void setCreateTrainerRequest(CreateTrainerRequest createTrainerRequest) {
        this.createTrainerRequest = createTrainerRequest;
    }

    public CreateGymRequest getCreateGymRequest() {
        return createGymRequest;
    }

    public void setCreateGymRequest(CreateGymRequest createGymRequest) {
        this.createGymRequest = createGymRequest;
    }

    public CreateWorkoutRequest getCreateWorkoutRequest() {
        return createWorkoutRequest;
    }

    public void setCreateWorkoutRequest(CreateWorkoutRequest createWorkoutRequest) {
        this.createWorkoutRequest = createWorkoutRequest;
    }

    public CreateReviewRequest getCreateReviewRequest() {
        return createReviewRequest;
    }

    public void setCreateReviewRequest(CreateReviewRequest createReviewRequest) {
        this.createReviewRequest = createReviewRequest;
    }

}
