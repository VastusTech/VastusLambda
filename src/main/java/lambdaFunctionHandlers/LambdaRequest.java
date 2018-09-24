package main.java.lambdaFunctionHandlers;

import main.java.Logic.Constants;
import main.java.Logic.ItemType;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import main.java.databaseObjects.*;
import main.java.databaseOperations.DatabaseAction;
import main.java.databaseOperations.DynamoDBHandler;
import main.java.databaseOperations.databaseActionBuilders.*;
import main.java.lambdaFunctionHandlers.requestObjects.*;
import main.java.lambdaFunctionHandlers.responseObjects.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


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

    private enum Action {
        CREATE,
        READ,
        UPDATESET,
        UPDATEADD,
        UPDATEREMOVE,
        DELETE
    }

    private enum AttributeName {
        name,
        gender,
        birthday,
        foundingDay,
        email,
        username,
        profileImagePath,
        scheduledWorkouts,
        scheduledWorkoutTimes,
        completedWorkouts,
        completedWorkoutTimes,
        reviewsBy,
        reviewsAbout,
        friendlinessRating,
        effectivenessRating,
        reliabilityRating,
        bio,
        friends,
        friendRequests,
        gymID,
        availableTimes,
        workoutSticker,
        preferredIntensity,
        workoutCapacity,
        workoutPrice,
        address,
        trainerIDs,
        weeklyHours,
        vacationTimes,
        sessionCapacity,
        gymType,
        paymentSplit,
        time,
        trainerID,
        clientIDs,
        capacity,
        sticker,
        intensity,
        missingReviews,
        price,
        byID,
        aboutID,
        description
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

            }

        }
        catch (IllegalArgumentException e) {
            throw new Exception("Action: \"" + action + "\" not recognized!");
        }

        // TODO HOW WOULD IT GET HERE????
        return null;
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
            if (createReviewRequest != null) {
                numCreateRequest++;
            }
            if (createTrainerRequest != null) {
                numCreateRequest++;
            }
            if (createGymRequest != null) {
                numCreateRequest++;
            }
            if (createWorkoutRequest != null) {
                numCreateRequest++;
            }
            if (createReviewRequest != null) {
                numCreateRequest++;
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

    // TODO WE MIGHT WANT TO PUT THESE CASES INTO FUNCTIONS (FUNCTION HANDLERS?)
    public String handleCreate() throws Exception {
        List<DatabaseAction> databaseActions = new ArrayList<>();

        try {
            switch (ItemType.valueOf(itemType)) {
                case Client:
                    if (createClientRequest != null) {
                        // Create client
                        databaseActions.add(ClientDatabaseActionBuilder.create(createClientRequest));
                    }
                    else {
                        throw new Exception("createClientRequest not initialized for CREATE statement!");
                    }
                    break;
                case Trainer:
                    if (createTrainerRequest != null) {
                        // Create trainer (with createTrainerRequest)
                        databaseActions.add(TrainerDatabaseActionBuilder.create(createTrainerRequest));
                        // Add to gym (with gymID and true for fromCreate
                        databaseActions.add(GymDatabaseActionBuilder.updateAddTrainerID(createTrainerRequest.gymID,
                                null, true));
                    }
                    else {
                        throw new Exception("createTrainerRequest not initialized for CREATE statement!");
                    }
                    break;
                case Gym:
                    if (createGymRequest != null) {
                        // Create Gym
                        databaseActions.add(GymDatabaseActionBuilder.create(createGymRequest));
                    }
                    else {
                        throw new Exception("createGymRequest not initialized for CREATE statement!");
                    }
                    break;
                case Workout:
                    if (createWorkoutRequest != null) {
                        // Create Workout
                        databaseActions.add(WorkoutDatabaseActionBuilder.create(createWorkoutRequest));
                        // Add to clients' scheduled workouts
                        // Add to clients' scheduled workout times
                        for (String clientID : createWorkoutRequest.clientIDs) {
                            databaseActions.add(ClientDatabaseActionBuilder.updateAddScheduledWorkout(clientID, null,
                                    true));
                            databaseActions.add(ClientDatabaseActionBuilder.updateAddScheduledWorkoutTime(clientID,
                                    createWorkoutRequest.time));
                        }
                        // Add to trainer's scheduled workouts
                        // Add to trainer's scheduled workout times
                        databaseActions.add(TrainerDatabaseActionBuilder.updateAddScheduledWorkout
                                (createWorkoutRequest.trainerID, null, true));
                        databaseActions.add(TrainerDatabaseActionBuilder.updateAddScheduledWorkoutTime
                                (createWorkoutRequest.trainerID, createWorkoutRequest.time));
                        // Add to gym's scheduled workouts
                        // Add to gym's scheduled workout times
                        databaseActions.add(GymDatabaseActionBuilder.updateAddScheduledWorkout
                                (createWorkoutRequest.gymID, null, true));
                        databaseActions.add(GymDatabaseActionBuilder.updateAddScheduledWorkoutTime
                                (createWorkoutRequest.gymID, createWorkoutRequest.time));
                    }
                    else {
                        throw new Exception("createWorkoutRequest not initialized for CREATE statement!");
                    }
                    break;
                case Review:
                    if (createReviewRequest != null) {
                        // Create Review
                        databaseActions.add(ReviewDatabaseActionBuilder.create(createReviewRequest));
                        // Add to by's reviews by
                        String byID = createReviewRequest.byID;
                        String byItemType = DatabaseObject.getItemType(byID);
                        if (byItemType == null) {
                            throw new Exception("Review ByID is invalid!");
                        }
                        databaseActions.add(UserDatabaseActionBuilder.updateAddReviewBy(createReviewRequest.byID,
                                byItemType, null ,true));
                        // Add to about's reviews about
                        String aboutID = createReviewRequest.aboutID;
                        String aboutItemType = DatabaseObject.getItemType(aboutID);
                        if (aboutItemType == null) {
                            throw new Exception("Review AboutID is invalid!");
                        }
                        databaseActions.add(UserDatabaseActionBuilder.updateAddReviewAbout(aboutID, aboutItemType,
                                null, true));
                        // Calculate about's ratings
                        Map<String, AttributeValue> aboutKey = new HashMap<>();
                        aboutKey.put("item_type", new AttributeValue(aboutItemType));
                        aboutKey.put("id", new AttributeValue(aboutID));
                        User user = DynamoDBHandler.getInstance().readItem(aboutKey);
                        float friendlinessRating = Float.parseFloat(createReviewRequest.friendlinessRating);
                        float effectivenessRating = Float.parseFloat(createReviewRequest.effectivenessRating);
                        float reliabilityRating = Float.parseFloat(createReviewRequest.reliabilityRating);
                        int numReviews = user.reviewsAbout.size();

                        // Basically it finds the "sum" of the ratings, using the current rating and the number of
                        // reviews. Then, it adds our rating value to it, then divides it by numReviews + 1.
                        float newFriendlinessRating = ((numReviews * user.friendlinessRating) +
                                friendlinessRating) / (numReviews + 1);
                        float newEffectivenessRating = ((numReviews * user.effectivenessRating) +
                                effectivenessRating) / (numReviews + 1);
                        float newReliabilityRating = ((numReviews * user.reliabilityRating) +
                                reliabilityRating) / (numReviews + 1);

                        // Updates the about item
                        databaseActions.add(UserDatabaseActionBuilder.updateFriendlinessRating(aboutID,
                                aboutItemType, Float.toString(newFriendlinessRating)));
                        databaseActions.add(UserDatabaseActionBuilder.updateEffectivenessRating(aboutID,
                                aboutItemType, Float.toString(newEffectivenessRating)));
                        databaseActions.add(UserDatabaseActionBuilder.updateReliabilityRating(aboutID,
                                aboutItemType, Float.toString(newReliabilityRating)));
                    }
                    else {
                        throw new Exception("createReviewRequest not initialized for CREATE statement!");
                    }
                    break;
            }
        }
        catch (IllegalArgumentException e) {
            throw new Exception("Item Type: \"" + itemType + "\" not recognized!");
        }

        return DynamoDBHandler.getInstance().attemptTransaction(databaseActions);
    }

    private String handleSurveyCreate(String workoutID) throws Exception {
        if (createReviewRequest != null) {
            List<DatabaseAction> databaseActions = new ArrayList<>();
            // Create Review
            databaseActions.add(ReviewDatabaseActionBuilder.create(createReviewRequest));
            // Add to by's reviews by
            String byID = createReviewRequest.byID;
            String byItemType = DatabaseObject.getItemType(byID);
            if (byItemType == null) {
                throw new Exception("Review ByID is invalid!");
            }
            databaseActions.add(UserDatabaseActionBuilder.updateAddReviewBy(createReviewRequest.byID,
                    byItemType, null ,true));
            // Add to about's reviews about
            String aboutID = createReviewRequest.aboutID;
            String aboutItemType = DatabaseObject.getItemType(aboutID);
            if (aboutItemType == null) {
                throw new Exception("Review AboutID is invalid!");
            }
            databaseActions.add(UserDatabaseActionBuilder.updateAddReviewAbout(aboutID, aboutItemType,
                    null, true));
            // Calculate about's ratings
            Map<String, AttributeValue> aboutKey = new HashMap<>();
            aboutKey.put("item_type", new AttributeValue(aboutItemType));
            aboutKey.put("id", new AttributeValue(aboutID));
            User user = DynamoDBHandler.getInstance().readItem(aboutKey);
            float friendlinessRating = Float.parseFloat(createReviewRequest.friendlinessRating);
            float effectivenessRating = Float.parseFloat(createReviewRequest.effectivenessRating);
            float reliabilityRating = Float.parseFloat(createReviewRequest.reliabilityRating);
            int numReviews = user.reviewsAbout.size();

            // Basically it finds the "sum" of the ratings, using the current rating and the number of
            // reviews. Then, it adds our rating value to it, then divides it by numReviews + 1.
            float newFriendlinessRating = ((numReviews * user.friendlinessRating) +
                    friendlinessRating) / (numReviews + 1);
            float newEffectivenessRating = ((numReviews * user.effectivenessRating) +
                    effectivenessRating) / (numReviews + 1);
            float newReliabilityRating = ((numReviews * user.reliabilityRating) +
                    reliabilityRating) / (numReviews + 1);

            // Updates the about item
            databaseActions.add(UserDatabaseActionBuilder.updateFriendlinessRating(aboutID,
                    aboutItemType, Float.toString(newFriendlinessRating)));
            databaseActions.add(UserDatabaseActionBuilder.updateEffectivenessRating(aboutID,
                    aboutItemType, Float.toString(newEffectivenessRating)));
            databaseActions.add(UserDatabaseActionBuilder.updateReliabilityRating(aboutID,
                    aboutItemType, Float.toString(newReliabilityRating)));

            // Remove id from Workout's missing reviews
            databaseActions.add(WorkoutDatabaseActionBuilder.updateRemoveMissingReview(workoutID, byID, true));
            return DynamoDBHandler.getInstance().attemptTransaction(databaseActions);
        }
        else {
            throw new Exception("createReviewRequest not initialized for CREATE statement!");
        }
    }

    private List<ObjectResponse> handleIDRead(String[] ids) throws Exception {
        try {
            List<ObjectResponse> objectResponses = new ArrayList<>();
            switch (ItemType.valueOf(itemType)) {
                case Client:
                    for (String id : ids) {
                        objectResponses.add(new ClientResponse(Client.readClient(id)));
                    }
                    break;
                case Trainer:
                    for (String id : ids) {
                        objectResponses.add(new TrainerResponse(Trainer.readTrainer(id)));
                    }
                    break;
                case Gym:
                    for (String id : ids) {
                        objectResponses.add(new GymResponse(Gym.readGym(id)));
                    }
                    break;
                case Workout:
                    for (String id : ids) {
                        objectResponses.add(new WorkoutResponse(Workout.readWorkout(id)));
                    }
                    break;
                case Review:
                    for (String id : ids) {
                        objectResponses.add(new ReviewResponse(Review.readReview(id)));
                    }
                    break;
            }
            return objectResponses;
        }
        catch (IllegalArgumentException e) {
            throw new Exception("Item Type: \"" + itemType + "\" not recognized!");
        }
    }

    private List<ObjectResponse> handleUsernameRead(String[] usernames) throws Exception {
        try {
            List<ObjectResponse> objectResponses = new ArrayList<>();
            switch (ItemType.valueOf(itemType)) {
                case Client:
                    for (String username : usernames) {
                        objectResponses.add(new ClientResponse(Client.queryClient(username)));
                    }
                    break;
                case Trainer:
                    for (String username : usernames) {
                        objectResponses.add(new TrainerResponse(Trainer.queryTrainer(username)));
                    }
                    break;
                case Gym:
                    for (String username : usernames) {
                        objectResponses.add(new GymResponse(Gym.queryGym(username)));
                    }
                    break;
                case Workout:
                    throw new Exception("Can't query a workout by a username!");
                case Review:
                    throw new Exception("Can't query a review by a username!");
            }
            return objectResponses;
        }
        catch (IllegalArgumentException e) {
            throw new Exception("Item Type: \"" + itemType + "\" not recognized!");
        }
    }

    public List<ObjectResponse> handleGetAll() throws Exception {
        // Already Checked, so go to town
        List<ObjectResponse> objectResponses = new ArrayList<>();
        for (DatabaseObject databaseObject : DynamoDBHandler.getInstance().getAll(itemType)) {
            objectResponses.add(databaseObject.getResponse());
        }
        return objectResponses;
    }

    public void handleUpdateSet(String id) throws Exception {
        // switch all attributes, then if necessary, item type
        List<DatabaseAction> databaseActions = new ArrayList<>();

        try {
            switch (AttributeName.valueOf(attributeName)) {
                case name:
                    if (itemType.equals("Client") || itemType.equals("Trainer") || itemType.equals("Gym")) {
                        if (attributeValues.length == 1) {
                            databaseActions.add(UserDatabaseActionBuilder.updateName(id, itemType, attributeValues[0]));
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
                case gender:
                    if (itemType.equals("Client") || itemType.equals("Trainer") || itemType.equals("Gym")) {
                        if (attributeValues.length == 1) {
                            databaseActions.add(UserDatabaseActionBuilder.updateGender(id, itemType,
                                    attributeValues[0]));
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
                case birthday:
                    if (itemType.equals("Client") || itemType.equals("Trainer")) {
                        if (attributeValues.length == 1) {
                            databaseActions.add(UserDatabaseActionBuilder.updateBirthday(id, itemType, attributeValues[0]));
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
                case foundingDay:
                    if (itemType.equals("Gym")) {
                        if (attributeValues.length == 1) {
                            databaseActions.add(GymDatabaseActionBuilder.updateFoundingDay(id, attributeValues[0]));
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
                case email:
                    if (itemType.equals("Client") || itemType.equals("Trainer") || itemType.equals("Gym")) {
                        if (attributeValues.length == 1) {
                            databaseActions.add(UserDatabaseActionBuilder.updateEmail(id, itemType, attributeValues[0]));
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
//                case username:
//                    if (itemType.equals("Client") || itemType.equals("Trainer") || itemType.equals("Gym")) {
//                        if (attributeValues.length == 1) {
//                            databaseActions.add(UserDatabaseActionBuilder.updateUser(id, itemType, attributeValues[0]));
//                        }
//                        else {
//                            throw new Exception("For updating " + attributeName + " on " + itemType +
//                                    "attributeValues must be only 1 long!");
//                        }
//
//                    }
//                    else {
//                        throw new Exception("Unable to perform " + action + " to " + attributeName + "for a " +
//                                itemType + "!");
//                    }
//                    break;
                case profileImagePath:
                    if (itemType.equals("Client") || itemType.equals("Trainer") || itemType.equals("Gym")) {
                        if (attributeValues.length == 1) {
                            databaseActions.add(UserDatabaseActionBuilder.updateProfileImagePath(id, itemType, attributeValues[0]));
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
                case bio:
                    if (itemType.equals("Client") || itemType.equals("Trainer") || itemType.equals("Gym")) {
                        if (attributeValues.length == 1) {
                            databaseActions.add(UserDatabaseActionBuilder.updateBio(id, itemType, attributeValues[0]));
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
//                case gymID:
//                    if (itemType.equals("Trainer")) {
//                        if (attributeValues.length == 1) {
//                            databaseActions.add(TrainerDatabaseActionBuilder.updateBirthday(id, itemType,
//                                    attributeValues[0]));
//                        }
//                        else {
//                            throw new Exception("For updating " + attributeName + " on " + itemType +
//                                    "attributeValues must be only 1 long!");
//                        }
//
//                    }
//                    else {
//                        throw new Exception("Unable to perform " + action + " to " + attributeName + "for a " +
//                                itemType + "!");
//                    }
//                    break;
                case workoutSticker:
                    if (itemType.equals("Trainer")) {
                        if (attributeValues.length == 1) {
                            databaseActions.add(TrainerDatabaseActionBuilder.updateWorkoutSticker(id, attributeValues[0]));
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
                case preferredIntensity:
                    if (itemType.equals("Trainer")) {
                        if (attributeValues.length == 1) {
                            databaseActions.add(TrainerDatabaseActionBuilder.updatePreferredIntensity(id, attributeValues[0]));
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
                case workoutCapacity:
                    if (itemType.equals("Trainer")) {
                        if (attributeValues.length == 1) {
                            databaseActions.add(TrainerDatabaseActionBuilder.updateWorkoutCapacity(id, attributeValues[0]));
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
                case workoutPrice:
                    if (itemType.equals("Trainer")) {
                        if (attributeValues.length == 1) {
                            databaseActions.add(TrainerDatabaseActionBuilder.updateWorkoutPrice(id, attributeValues[0]));
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
                case address:
                    if (itemType.equals("Gym")) {
                        if (attributeValues.length == 1) {
                            databaseActions.add(GymDatabaseActionBuilder.updateAddress(id, attributeValues[0]));
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
                case weeklyHours:
                    if (itemType.equals("Gym")) {
                        if (attributeValues.length > 0) {
                            // TODO We probably need to cancel any workouts that
                            // TODO THIS SHOULD BE ADDRESSED VERY SOON
                            databaseActions.add(GymDatabaseActionBuilder.updateWeeklyHours(id, attributeValues));
                        }
                        else {
                            throw new Exception("For updating " + attributeName + " on " + itemType +
                                    "attributeValues must be at least 1 long!");
                        }
                    }
                    else {
                        throw new Exception("Unable to perform " + action + " to " + attributeName + "for a " +
                                itemType + "!");
                    }
                    break;
                case sessionCapacity:
                    if (itemType.equals("Gym")) {
                        if (attributeValues.length == 1) {
                            // Should we worry about already scheduled workouts? Nah.
                            databaseActions.add(GymDatabaseActionBuilder.updateSessionCapacity(id, attributeValues[0]));
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
                case gymType:
                    if (itemType.equals("Gym")) {
                        if (attributeValues.length == 1) {
                            // TODO Set payment split to DEFAULT for whatever it's going into
                            databaseActions.add(GymDatabaseActionBuilder.updateGymType(id, attributeValues[0]));
                            databaseActions.add(GymDatabaseActionBuilder.updatePaymentSplit(id, Constants.nullAttributeValue));
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
                case paymentSplit:
                    if (itemType.equals("Gym")) {
                        if (attributeValues.length == 1) {
                            databaseActions.add(GymDatabaseActionBuilder.updatePaymentSplit(id, attributeValues[0]));
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
                default:
                    throw new Exception("Can't perform an UPDATESET operation on " + attributeName + "!");
            }
        }
        catch (IllegalArgumentException e) {
            throw new Exception("AttributeName: " + attributeName + " not recognized!");
        }

        DynamoDBHandler.getInstance().attemptTransaction(databaseActions);
    }

    public void handleUpdateAdd(String id) throws Exception {
        // switch all attributes, then if necessary, item type
        List<DatabaseAction> databaseActions = new ArrayList<>();

        try {
            switch (AttributeName.valueOf(attributeName)) {
                case scheduledWorkouts:
                    if (itemType.equals("Client")) {
                        if (attributeValues.length == 1) {
                            // Workout dependencies
                            databaseActions.add(ClientDatabaseActionBuilder.updateAddScheduledWorkout(id,
                                    attributeValues[0], false));
                            // Add to workout's clients
                            databaseActions.add(WorkoutDatabaseActionBuilder.updateAddClientID(attributeValues[0], id));
                            // Add to workout's missingReviews
                            databaseActions.add(WorkoutDatabaseActionBuilder.updateAddMissingReview
                                    (attributeValues[0], id));
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
//                case scheduledWorkoutTimes:
//                    break;
//                case completedWorkouts:
//                    break;
//                case completedWorkoutTimes:
//                    break;
//                case reviewsBy:
//                    break;
//                case reviewsAbout:
//                    break;
                case friends:
                    if (itemType.equals("Client")) {
                        if (attributeValues.length == 1) {
                            // Mututal friends
                            databaseActions.add(ClientDatabaseActionBuilder.updateAddFriend(id, attributeValues[0],
                                    true));
                            databaseActions.add(ClientDatabaseActionBuilder.updateAddFriend(attributeValues[0], id,
                                    false));
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
                case friendRequests:
                    if (itemType.equals("Client")) {
                        if (attributeValues.length == 1) {
                            if (!fromID.equals(attributeValues[0])) {
                                databaseActions.add(ClientDatabaseActionBuilder.updateAddFriendRequest(id,
                                        attributeValues[0]));
                            }
                            else {
                                throw new Exception("He tried to make himself his own friend! The absolute madlad.");
                            }
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
                case availableTimes:
                    if (itemType.equals("Trainer")) {
                        if (attributeValues.length == 1) {
                            databaseActions.add(TrainerDatabaseActionBuilder.updateAddAvailableTime(id,
                                    attributeValues[0]));
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
//                case trainerIDs:
//                    if (itemType.equals("Gym")) {
//
//                    }
//                    else {
//                        throw new Exception("Unable to perform " + action + " to " + attributeName + "for a " +
//                                itemType + "!");
//                    }
//                    break;
                case vacationTimes:
                    if (itemType.equals("Gym")) {
                        if (attributeValues.length == 1) {
                            // TODO Cancel workouts if it's applicable
                            // TODO This will need to be addressed SOOON!
                            databaseActions.add(GymDatabaseActionBuilder.updateAddVacationTime(id, attributeValues[0]));
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
//                case clientIDs:
//                    if (itemType.equals("Workout")) {
//
//                    }
//                    else {
//                        throw new Exception("Unable to perform " + action + " to " + attributeName + "for a " +
//                                itemType + "!");
//                    }
//                    break;
//                case missingReviews:
//                    if (itemType.equals("Workout")) {
//
//                    }
//                    else {
//                        throw new Exception("Unable to perform " + action + " to " + attributeName + "for a " +
//                                itemType + "!");
//                    }
//                    break;
                default:
                    throw new Exception("Can't perform an UPDATEADD operation on " + attributeName + "!");
            }
        }
        catch (IllegalArgumentException e) {
            throw new Exception("AttributeName: " + attributeName + " not recognized!");
        }

        DynamoDBHandler.getInstance().attemptTransaction(databaseActions);
    }

    public void handleUpdateRemove(String id) throws Exception {
        // switch all attributes, then if necessary, item type
        List<DatabaseAction> databaseActions = new ArrayList<>();

        try {
            switch (AttributeName.valueOf(attributeName)) {
                case scheduledWorkouts:
                    if (itemType.equals("Client")) {
                        if (attributeValues.length == 1) {
                            // TODO This should also have the potential to delete the workout!!!!!
                            // Workout Dependencies
                            databaseActions.add(ClientDatabaseActionBuilder.updateRemoveScheduledWorkout(id,
                                    attributeValues[0]));
                            // Update workout clients
                            databaseActions.add(WorkoutDatabaseActionBuilder.updateRemoveClientID(attributeValues[0],
                                    id));
                            // Update workout missing reviews
                            databaseActions.add(WorkoutDatabaseActionBuilder.updateRemoveMissingReview
                                    (attributeValues[0], id, false));

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
//                case scheduledWorkoutTimes:
//                    break;
//                case completedWorkouts:
//                    break;
//                case completedWorkoutTimes:
//                    break;
//                case reviewsBy:
//                    break;
//                case reviewsAbout:
//                    break;
                case friends:
                    if (itemType.equals("Client")) {
                        if (attributeValues.length == 1) {
                            // Mutual Friend loss
                            databaseActions.add(ClientDatabaseActionBuilder.updateRemoveFriend(id,
                                    attributeValues[0]));
                            databaseActions.add(ClientDatabaseActionBuilder.updateRemoveFriend(attributeValues[0], id));
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
                case friendRequests:
                    if (itemType.equals("Client")) {
                        if (attributeValues.length == 1) {
                            databaseActions.add(ClientDatabaseActionBuilder.updateRemoveFriendRequest(id,
                                    attributeValues[0]));
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
                case availableTimes:
                    if (itemType.equals("Trainer")) {
                        if (attributeValues.length == 1) {
                            databaseActions.add(TrainerDatabaseActionBuilder.updateRemoveAvailableTime(id,
                                    attributeValues[0]));
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
//                case trainerIDs:
//                    break;
                case vacationTimes:
                    if (itemType.equals("Gym")) {
                        if (attributeValues.length == 1) {
                            databaseActions.add(GymDatabaseActionBuilder.updateRemoveVacationTime(id,
                                    attributeValues[0]));
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
//                case clientIDs:
//                    break;
//                case missingReviews:
//                    break;
                default:
                    throw new Exception("Can't perform an UPDATEREMOVE operation on " + attributeName + "!");
            }
        }
        catch (IllegalArgumentException e) {
            throw new Exception("AttributeName: " + attributeName + " not recognized!");
        }

        DynamoDBHandler.getInstance().attemptTransaction(databaseActions);
    }

    public void handleDelete(String id) throws Exception {
        try {
            switch (ItemType.valueOf(itemType)) {
                // TODO MAKE SURE THESE PEOPLE ARE ALLOWED TO DO THIS (Workout and Review)
                // TODO THIS WILL BE WAY EASIER ONCE I MAKE FUNCTIONS FOR EACH ACTION
                case Client:
                    Client client = Client.readClient(id);
                    // Remove reviews from reviews by and reviews about
                    // Remove from all scheduled workouts and completed workouts
                        // Also remove from missing reviews in the workouts
                    // TODO This should also be able to delete the workout potentially?
                    // Delete the Client
                    break;
                case Trainer:
                    // Remove all reviews in reviews by and reviews about
                    // Remove all workouts in scheduled workouts and completed workouts (Cancel them)
                    // Remove all workout times in scheduled workout times and completed workout times
                    // Remove from gym's trainers field
                    // Delete the Trainer
                    break;
                case Gym:
                    // TODO THIS IS PROBLEMATIC
                    // TODO SEE IF WE CAN GO INTO AWS COGNITO AND DELETE USERS FROM A USER POOL FOR THIS
                    // Remove all reviews in reviews by and reviews about
                    // Remove all workouts
                    // Remove all trainers in trainers
                    break;
                case Workout:
                    // TODO REFUNDS IF IT'S IN SCHEDULED WORKOUTS
                    // Remove from clients' scheduled workouts and completed workouts
                    // Remove clients' scheduled workout times and  completed workout times
                    // remove from trainer's scheduled and completed workouts
                    // remove from trainer's scheduled and completed workout times
                    // Remove from gym's scheduled and completed workout times
                    break;
                case Review:
                    // Remove from reviews about field
                    // Remove from reviews about field
                    break;
            }
        }
        catch (IllegalArgumentException e) {
            throw new Exception("Item Type: \"" + itemType + "\" not recognized!");
        }
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
