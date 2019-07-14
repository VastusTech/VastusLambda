package main.java.lambdaFunctionHandlers;

import org.joda.time.DateTimeZone;

import main.java.databaseOperations.exceptions.ExceedsDatabaseLimitException;
import main.java.logic.Constants;
import main.java.logic.ItemType;
import main.java.logic.TimeHelper;
import main.java.logic.debugging.SingletonTimer;
import main.java.databaseOperations.DatabaseActionCompiler;
import main.java.databaseOperations.DynamoDBHandler;
import main.java.lambdaFunctionHandlers.highLevelHandlers.createDependencyHandlers.*;
import main.java.lambdaFunctionHandlers.highLevelHandlers.deleteDependencyHandlers.*;
import main.java.lambdaFunctionHandlers.highLevelHandlers.updateAddDependencyHandlers.*;
import main.java.lambdaFunctionHandlers.highLevelHandlers.updateRemoveDependencyHandlers.*;
import main.java.lambdaFunctionHandlers.highLevelHandlers.updateSetDependencyHandlers.*;
import main.java.lambdaFunctionHandlers.requestObjects.*;

import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;

/**
 * This is the main POJO that is called from the lambda. Essentially this becomes the "payload"
 * object that is inputted from the client app for the AWS Lambda call.
 */
public class LambdaRequest {
    private String fromID;
    private String fromTimeZone;
    private String action;
    private String specifyAction;
    private String itemType;
    private String[] identifiers;
    private String secondaryIdentifier;
    private String attributeName;
    private String[] attributeValues;
    private String environmentType;

    private CreateClientRequest createClientRequest;
    private CreateTrainerRequest createTrainerRequest;
    private CreateGymRequest createGymRequest;
    private CreateWorkoutRequest createWorkoutRequest;
    private CreateReviewRequest createReviewRequest;
    private CreateEventRequest createEventRequest;
    private CreateChallengeRequest createChallengeRequest;
    private CreateInviteRequest createInviteRequest;
    private CreatePostRequest createPostRequest;
    private CreateSubmissionRequest createSubmissionRequest;
    private CreateGroupRequest createGroupRequest;
    private CreateCommentRequest createCommentRequest;
    private CreateSponsorRequest createSponsorRequest;
    private CreateMessageRequest createMessageRequest;
    private CreateStreakRequest createStreakRequest;
    private CreateEnterpriseRequest createEnterpriseRequest;
    private CreateDealRequest createDealRequest;
    private CreateProductRequest createProductRequest;
    private CreateAdminRequest createAdminRequest;

    /**
     * The values for an action to do for a single Lambda request.
     */
    private enum Action {
        CREATE,
        READ,
        UPDATESET,
        UPDATEADD,
        UPDATEREMOVE,
        DELETE,
//        PROCESS,
    }

    /**
     * The potential values for the name of an attribute to indicate.
     */
    private enum AttributeName {
        // User ===========================
        stripeID,
        name,
        gender,
        birthday,
        foundingDay,
        email,
        location,
        username,
        profileImagePath,
        profileImagePaths,
        scheduledWorkouts,
        completedWorkouts,
        scheduledTimes,
        reviewsBy,
        reviewsAbout,
        friendlinessRating,
        effectivenessRating,
        reliabilityRating,
        bio,
        friends,
        friendRequests,
        challenges,
        scheduledEvents,
        invitedEvents,
        ownedEvents,
        sentInvites,
        receivedInvites,
        groups,
        ownedGroups,
        messageBoards,
        productsOwned,
        // Client ==========================
        trainersFollowing,
        // Trainer =========================
        gym,
        availableTimes,
        workoutSticker,
        preferredIntensity,
        workoutCapacity,
        workoutPrice,
        followers,
        subscriptionPrice,
        certifications,
        // Gym =============================
        address,
        trainers,
        weeklyHours,
        vacationTimes,
        sessionCapacity,
        gymType,
        paymentSplit,
        // Workout =========================
        time,
        trainer,
        clients,
        capacity,
        sticker,
        intensity,
        missingReviews,
        price,
        // Review ==========================
        by,
        about,
        description,
        // Event ===========================
        title,
        members,
        access,
        restriction,
        tags,
        challenge,
        ifCompleted,
        // Challenge =======================
        goal,
        winner,
        difficulty,
        endTime,
        prize,
        prizeImagePath,
        // Invite ==========================
        // Post   ==========================
        picturePaths,
        videoPaths,
        likes,
        // Submission ======================
        approved,
        // Group ===========================
        owners,
        // Comment =========================
        comment,
        // Sponsor =========================
        // Message =========================
        lastSeenFor,
        // Streak =========================
        N,
        // Deal ============================
        productName,
        productCreditPrice,
        productImagePath,
        productImagePaths,
        validUntil,
        productStoreLink,
        products,
        score,
    }

    /**
     * Processes the inputs from the lambda request and executes it accordingly, updating the
     * database and sending notifications when applicable. Also thoroughly check the inputs and
     * current states of the database for integrity.
     *
     * @return Any information to return from the execution. For CREATE, it's the created ID.
     * @throws Exception If anything goes wrong in the execution.
     */
    public Object process() throws Exception {
        try {
            // First check all of the inputs for the stuff
            checkInputs();
            SingletonTimer.get().endAndPushCheckpoint("Switch for action");

            switch (Action.valueOf(action)) {
                case CREATE:
                    if (specifyAction.equals("ForSurvey") && itemType.equals("Review")) {
                        // Create Review and remove from workout missingReviews
                        if (identifiers.length == 1) {
                            return handleSurveyCreate(identifiers[0]);
                        }
                        else {
                            throw new Exception("There should be 1 identifier when creating a Review for the survey!");
                        }
                    }
                    else if (specifyAction.equals("")) {
                        // Create the item
                        if (identifiers.length == 0) {
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
                    throw new Exception("Reading with Lambda no longer supported! Just use GraphQL!");
                case UPDATESET:
                    if (specifyAction.equals("")) {
                        // Update the attribute: SET it.
                        // There are a number of things this could be. Some require checking, some require multi-faceted
                        if (identifiers.length == 1) {
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

    /**
     * Checks the inputs for initial viability of the JSON payload. Ensures that there are no empty
     * strings that will be rejected by DynamoDB.
     *
     * @throws Exception If there are any problems with the inputs.
     */
    private void checkInputs() throws Exception {
        // TimeZone, Action, ItemType, SpecifyAction, attributeName
        try {
            DateTimeZone.forID(fromTimeZone);
        }
        catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("From Time Zone not recognized!", e);
        }
        if (specifyAction == null) {
            specifyAction = "";
        }
        if (specifyAction.length() > Constants.hardStringLengthLimit) throw new ExceedsDatabaseLimitException("Specify Action String exceeds hard string limit");
        if (identifiers == null) {
            identifiers = new String[]{};
        }
        for (String identifier : identifiers) {
            if (identifier.length() > Constants.hardStringLengthLimit) throw new ExceedsDatabaseLimitException("Identifier String exceeds hard string limit");
        }
        if (secondaryIdentifier != null) {
            if (!itemType.equals("Message")) throw new Exception("You can only have a secondary identifier to identify a board!!!");
            else if (secondaryIdentifier.length() > Constants.hardStringLengthLimit) throw new ExceedsDatabaseLimitException("Secondary Identifier String exceeds hard string limit");
        }
        if (action != null  && fromID != null && !fromID.equals("") && itemType != null) {
            if (fromID.length() > Constants.hardStringLengthLimit) throw new ExceedsDatabaseLimitException("From ID exceeds hard string limit");
            if (attributeName != null) {
                if (attributeName.equals("")) throw new Exception("Attribute name is not allowed to be an empty string!");
                else if (attributeName.length() > Constants.hardStringLengthLimit) throw new ExceedsDatabaseLimitException("Attribute Name String exceeds hard string limit");
            }
            if (attributeValues != null) {
                for (String value : attributeValues) {
                    if (value != null) {
                        if (value.equals("")) throw new Exception("Attribute Value string is not allowed to be an empty string!");
                        else if (value.length() > Constants.hardStringLengthLimit) throw new ExceedsDatabaseLimitException("Attribute Value String exceeds hard limit");
                    }
                }
            }
            int numCreateRequest = 0;
            if (createClientRequest != null) {
                numCreateRequest++;
                Constants.debugLog("Has a create client request!\n");
                if (createClientRequest.ifHasEmptyString()) {
                    throw new Exception("No field inside Create Client Request can be empty string!");
                }
            }
            if (createTrainerRequest != null) {
                numCreateRequest++;
                Constants.debugLog("Has a create trainer request!\n");
                if (createTrainerRequest.ifHasEmptyString()) {
                    throw new Exception("No field inside Create Trainer Request can be empty string!");
                }
            }
            if (createGymRequest != null) {
                numCreateRequest++;
                Constants.debugLog("Has a create gym request!\n");
                if (createGymRequest.ifHasEmptyString()) {
                    throw new Exception("No field inside Create Gym Request can be empty string!");
                }
            }
            if (createWorkoutRequest != null) {
                numCreateRequest++;
                Constants.debugLog("Has a create workout request!\n");
                if (createWorkoutRequest.ifHasEmptyString()) {
                    throw new Exception("No field inside Create Workout Request can be empty string!");
                }
            }
            if (createReviewRequest != null) {
                numCreateRequest++;
                Constants.debugLog("Has a create review request!\n");
                if (createReviewRequest.ifHasEmptyString()) {
                    throw new Exception("No field inside Create Review Request can be empty string!");
                }
            }
            if (createEventRequest != null) {
                numCreateRequest++;
                Constants.debugLog("Has a create event request!\n");
                if (createEventRequest.ifHasEmptyString()) {
                    throw new Exception("No field inside Create Event Request can be empty string!");
                }
            }
            if (createChallengeRequest != null) {
                numCreateRequest++;
                Constants.debugLog("Has a create challenge request!\n");
                if (createChallengeRequest.ifHasEmptyString()) {
                    throw new Exception("No field inside Create Challenge Request can be empty string!");
                }
            }
            if (createInviteRequest != null) {
                numCreateRequest++;
                Constants.debugLog("Has a create invite request!\n");
                if (createInviteRequest.ifHasEmptyString()) {
                    throw new Exception("No field inside Create Invite Request can be empty string!");
                }
            }
            if (createPostRequest != null) {
                numCreateRequest++;
                Constants.debugLog("Has a create post request!\n");
                if (createPostRequest.ifHasEmptyString()) {
                    throw new Exception("No field inside Create Post Request can be empty string!");
                }
            }
            if (createSubmissionRequest != null) {
                numCreateRequest++;
                Constants.debugLog("Has a create submission request!\n");
                if (createSubmissionRequest.ifHasEmptyString()) {
                    throw new Exception("No field inside Create Submission Request can be empty string!");
                }
            }
            if (createGroupRequest != null) {
                numCreateRequest++;
                Constants.debugLog("Has a create group request!\n");
                if (createGroupRequest.ifHasEmptyString()) {
                    throw new Exception("No field inside Create Group Request can be empty string!");
                }
            }
            if (createCommentRequest != null) {
                numCreateRequest++;
                Constants.debugLog("Has a create comment request!\n");
                if (createCommentRequest.ifHasEmptyString()) {
                    throw new Exception("No field inside Create Comment Request can be empty string!");
                }
            }
            if (createSponsorRequest != null) {
                numCreateRequest++;
                Constants.debugLog("Has a create sponsor request!\n");
                if (createSponsorRequest.ifHasEmptyString()) {
                    throw new Exception("No field inside Create Sponsor Request can be empty string!");
                }
            }
            if (createMessageRequest != null) {
                numCreateRequest++;
                Constants.debugLog("Has a create message request!\n");
                if (createMessageRequest.ifHasEmptyString()) {
                    throw new Exception("No field inside Create Message Request can be empty string!");
                }
            }
            if (createStreakRequest != null) {
                numCreateRequest++;
                Constants.debugLog("Has a create streak request!\n");
                if (createStreakRequest.ifHasEmptyString()) {
                    throw new Exception("No field inside Create Streak Request can be empty string!");
                }
            }
            if (createEnterpriseRequest != null) {
                numCreateRequest++;
                Constants.debugLog("Has a create enterprise request!\n");
                if (createEnterpriseRequest.ifHasEmptyString()) {
                    throw new Exception("No field inside Create Enterprise Request can be empty string!");
                }
            }
            if (createDealRequest != null) {
                numCreateRequest++;
                Constants.debugLog("Has a create deal request!\n");
                if (createDealRequest.ifHasEmptyString()) {
                    throw new Exception("No field inside Create Deal Request can be empty string!");
                }
            }
            if (createProductRequest != null) {
                numCreateRequest++;
                Constants.debugLog("Has a create product request!\n");
                if (createProductRequest.ifHasEmptyString()) {
                    throw new Exception("No field inside Create Product Request can be empty string!");
                }
            }
            if (createAdminRequest != null) {
                numCreateRequest++;
                Constants.debugLog("Has a create admin request!\n");
                if (createAdminRequest.ifHasEmptyString()) {
                    throw new Exception("No field inside Create Admin Request can be empty string!");
                }
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

    /**
     * Handles a CREATE action for the given item type.
     *
     * @return The ID of the newly created item.
     * @throws Exception If anything goes wrong in the creation.
     */
    private String handleCreate() throws Exception {
        SingletonTimer.get().endAndPushCheckpoint("Get compilers for Create");
        List<DatabaseActionCompiler> compilers;
        try {
            switch (ItemType.valueOf(itemType)) {
                case Client:
                    compilers = CreateClient.getCompilers(fromID, createClientRequest, 0);
                    break;
                case Trainer:
                    compilers = CreateTrainer.getCompilers(fromID, createTrainerRequest, 0);
                    break;
                case Gym:
                    compilers = CreateGym.getCompilers(fromID, createGymRequest, 0);
                    break;
                case Workout:
                    compilers = CreateWorkout.getCompilers(fromID, createWorkoutRequest, 0);
                    break;
                case Review:
                    // Send a null for surveyWorkoutID because we're not creating it for a survey
                    compilers = CreateReview.getCompilers(fromID, createReviewRequest, null, 0);
                    break;
                case Event:
                    compilers = CreateEvent.getCompilers(fromID, createEventRequest, 0);
                    break;
                case Challenge:
                    compilers = CreateChallenge.getCompilers(fromID, createChallengeRequest, 0);
                    break;
                case Invite:
                    compilers = CreateInvite.getCompilers(fromID, createInviteRequest, 0);
                    break;
                case Post:
                    compilers = CreatePost.getCompilers(fromID, createPostRequest, 0, null);
                    break;
                case Submission:
                    compilers = CreateSubmission.getCompilers(fromID, createSubmissionRequest, 0);
                    break;
                case Group:
                    compilers = CreateGroup.getCompilers(fromID, createGroupRequest, 0);
                    break;
                case Comment:
                    compilers = CreateComment.getCompilers(fromID, createCommentRequest, 0);
                    break;
                case Sponsor:
                    compilers = CreateSponsor.getCompilers(fromID, createSponsorRequest, 0);
                    break;
                case Message:
                    compilers = CreateMessage.getCompilers(fromID, createMessageRequest, 0);
                    break;
                case Streak:
                    compilers = CreateStreak.getCompilers(fromID, createStreakRequest, 0, null);
                    break;
                case Enterprise:
                    compilers = CreateEnterprise.getCompilers(fromID, createEnterpriseRequest, 0);
                    break;
                case Deal:
                    compilers = CreateDeal.getCompilers(fromID, createDealRequest, 0);
                    break;
                case Product:
                    compilers = CreateProduct.getCompilers(fromID, createProductRequest, 0);
                    break;
                case Admin:
                    compilers = CreateAdmin.getCompilers(fromID, createAdminRequest, 0);
                    break;
                default:
                    throw new Exception("Item Type: " + itemType + " recognized but not handled?");
            }
        }
        catch (IllegalArgumentException e) {
            throw new Exception("Item Type: " + itemType + " not recognized! Error: " + e.getLocalizedMessage());
        }

        SingletonTimer.get().endAndPushCheckpoint("Attempt the transaction");
        return DynamoDBHandler.getInstance().attemptTransaction(compilers, ifDevelopment());
    }

    private String handleSurveyCreate(String workoutID) throws Exception {
        return DynamoDBHandler.getInstance().attemptTransaction(CreateReview.getCompilers(fromID,
                createReviewRequest, workoutID, 0), ifDevelopment());
    }

    /**
     * Handles a UPDATESET action for the given item.
     *
     * @param id The id of the item to update.
     * @throws Exception If anything goes wrong in the update.
     */
    public void handleUpdateSet(String id) throws Exception {
        // switch all attributes, then if necessary, item type
        SingletonTimer.get().endAndPushCheckpoint("Init compiler for Update Set");
        List<DatabaseActionCompiler> compilers = new ArrayList<>();
        DatabaseActionCompiler databaseActionCompiler = new DatabaseActionCompiler();

        String attributeValue = null;
        if (attributeValues.length != 1 && attributeValues.length != 0 && !attributeName.equals("weeklyHours")) {
            throw new Exception("For updating " + attributeName + " on " + itemType + "attributeValues must be only " +
                    "0 or 1 item long!");
        }
        else if (attributeValues.length == 0 && attributeName.equals("weeklyHours")) {
            // DynamoDB can't handle empty string sets
            attributeValues = null;
        }
        else {
            attributeValue = attributeValues[0];
        }

        ItemType type = ItemType.valueOf(itemType);

        try {
            switch (AttributeName.valueOf(attributeName)) {
                case name:
                    // TODO Change all of these into switch statements?
                    switch (type) {
                        case Client:
                        case Trainer:
                        case Gym:
                        case Sponsor:
                            databaseActionCompiler.addAll(UserUpdateName.getActions(fromID, id, itemType, attributeValue));
                            break;
                        default:
                            throw new Exception("Unable to perform " + action + " to " + attributeName + " for a " + itemType + "!");
                    }
                    break;
                case gender:
                    if (ItemType.isUser(itemType)) {
                        databaseActionCompiler.addAll(UserUpdateGender.getActions(fromID, id, itemType, attributeValue));
                    }
                    else {
                        throw new Exception("Unable to perform " + action + " to " + attributeName + " for a " + itemType + "!");
                    }
                    break;
                case birthday:
                    if (ItemType.isUser(itemType)) {
                        databaseActionCompiler.addAll(UserUpdateBirthday.getActions(fromID, id, itemType, attributeValue));
                    }
                    else {
                        throw new Exception("Unable to perform " + action + " to " + attributeName + " for a " + itemType + "!");
                    }
                    break;
                case location:
                    if (ItemType.isUser(itemType)) {
                        databaseActionCompiler.addAll(UserUpdateLocation.getActions(fromID, id, itemType, attributeValue));
                    }
                    else {
                        throw new Exception("Unable to perform " + action + " to " + attributeName + " for a " + itemType + "!");
                    }
                    break;
                case foundingDay:
                    if (itemType.equals("Gym")) {
                        databaseActionCompiler.addAll(GymUpdateFoundingDay.getActions(fromID, id, attributeValue));
                    }
                    else {
                        throw new Exception("Unable to perform " + action + " to " + attributeName + " for a " + itemType + "!");
                    }
                    break;
                case email:
                    if (ItemType.isUser(itemType)) {
                        databaseActionCompiler.addAll(UserUpdateEmail.getActions(fromID, id, itemType, attributeValue));
                    }
                    else {
                        throw new Exception("Unable to perform " + action + " to " + attributeName + " for a " + itemType + "!");
                    }
                    break;
                case stripeID:
                    if (ItemType.isUser(itemType)) {
                        databaseActionCompiler.addAll(UserUpdateStripeID.getActions(fromID, id, itemType, attributeValue));
                    }
                    else {
                        throw new Exception("Unable to perform " + action + " to " + attributeName + " for a " + itemType + "!");
                    }
                    break;
                case profileImagePath:
                    if (ItemType.isUser(itemType)) {
                        databaseActionCompiler.addAll(UserUpdateProfileImagePath.getActions(fromID, id, itemType,
                                attributeValue));
                    }
                    else {
                        throw new Exception("Unable to perform " + action + " to " + attributeName + " for a " + itemType + "!");
                    }
                    break;
                case bio:
                    if (ItemType.isUser(itemType)) {
                        databaseActionCompiler.addAll(UserUpdateBio.getActions(fromID, id, itemType, attributeValue));
                    }
                    else {
                        throw new Exception("Unable to perform " + action + " to " + attributeName + " for a " + itemType + "!");
                    }
                    break;
                case workoutSticker:
                    if (itemType.equals("Trainer")) {
                        databaseActionCompiler.addAll(TrainerUpdateWorkoutSticker.getActions(fromID, id, attributeValue));
                    }
                    else {
                        throw new Exception("Unable to perform " + action + " to " + attributeName + " for a " + itemType + "!");
                    }
                    break;
                case preferredIntensity:
                    if (itemType.equals("Trainer")) {
                        databaseActionCompiler.addAll(TrainerUpdatePreferredIntensity.getActions(fromID, id,
                                attributeValue));
                    }
                    else {
                        throw new Exception("Unable to perform " + action + " to " + attributeName + " for a " + itemType + "!");
                    }
                    break;
                case workoutCapacity:
                    if (itemType.equals("Trainer")) {
                        databaseActionCompiler.addAll(TrainerUpdateWorkoutCapacity.getActions(fromID, id, attributeValue));
                    }
                    else {
                        throw new Exception("Unable to perform " + action + " to " + attributeName + " for a " + itemType + "!");
                    }
                    break;
                case workoutPrice:
                    if (itemType.equals("Trainer")) {
                        databaseActionCompiler.addAll(TrainerUpdateWorkoutPrice.getActions(fromID, id, attributeValue));
                    }
                    else {
                        throw new Exception("Unable to perform " + action + " to " + attributeName + " for a " + itemType + "!");
                    }
                    break;
                case subscriptionPrice:
                    if (itemType.equals("Trainer")) {
                        databaseActionCompiler.addAll(TrainerUpdateSubscriptionPrice.getActions(fromID, id,
                                attributeValue));
                    }
                    else {
                        throw new Exception("Unable to perform " + action + " to " + attributeName + " for a " + itemType + "!");
                    }
                    break;
                case address:
                    if (itemType.equals("Gym")) {
                        databaseActionCompiler.addAll(GymUpdateAddress.getActions(fromID, id, attributeValue));
                    }
                    else if (itemType.equals("Event")) {
                        databaseActionCompiler.addAll(EventUpdateAddress.getActions(fromID, id, attributeValue));
                    }
                    else {
                        throw new Exception("Unable to perform " + action + " to " + attributeName + " for a " + itemType + "!");
                    }
                    break;
                case weeklyHours:
                    // TODO I want to handle this differently in the future
                    if (itemType.equals("Gym")) {
                        databaseActionCompiler.addAll(GymUpdateWeeklyHours.getActions(fromID, id, attributeValues));
                    }
                    else {
                        throw new Exception("Unable to perform " + action + " to " + attributeName + " for a " + itemType + "!");
                    }
                    break;
                case sessionCapacity:
                    if (itemType.equals("Gym")) {
                        databaseActionCompiler.addAll(GymUpdateSessionCapacity.getActions(fromID, id, attributeValue));
                    }
                    else {
                        throw new Exception("Unable to perform " + action + " to " + attributeName + " for a " + itemType + "!");
                    }
                    break;
                case gymType:
                    if (itemType.equals("Gym")) {
                        databaseActionCompiler.addAll(GymUpdateGymType.getActions(fromID, id, attributeValue));
                    }
                    else {
                        throw new Exception("Unable to perform " + action + " to " + attributeName + " for a " + itemType + "!");
                    }
                    break;
                case paymentSplit:
                    if (itemType.equals("Gym")) {
                        databaseActionCompiler.addAll(GymUpdatePaymentSplit.getActions(fromID, id, attributeValue));
                    }
                    else {
                        throw new Exception("Unable to perform " + action + " to " + attributeName + " for a " + itemType + "!");
                    }
                    break;
                case title:
                    if (itemType.equals("Event")) {
                        databaseActionCompiler.addAll(EventUpdateTitle.getActions(fromID, id, attributeValue));
                    }
                    else if (itemType.equals("Challenge")) {
                        databaseActionCompiler.addAll(ChallengeUpdateTitle.getActions(fromID, id, attributeValue));
                    }
                    else if (itemType.equals("Group")) {
                        databaseActionCompiler.addAll(GroupUpdateTitle.getActions(fromID, id, attributeValue));
                    }
                    else {
                        throw new Exception("Unable to perform " + action + " to " + attributeName + " for a " + itemType + "!");
                    }
                    break;
                case description:
                    if (itemType.equals("Event")) {
                        databaseActionCompiler.addAll(EventUpdateDescription.getActions(fromID, id, attributeValue));
                    }
                    else if (itemType.equals("Challenge")) {
                        databaseActionCompiler.addAll(ChallengeUpdateDescription.getActions(fromID, id, attributeValue));
                    }
                    else if (itemType.equals("Post")) {
                        databaseActionCompiler.addAll(PostUpdateDescription.getActions(fromID, id, attributeValue));
                    }
                    else if (itemType.equals("Submission")) {
                        databaseActionCompiler.addAll(SubmissionUpdateDescription.getActions(fromID, id, attributeValue));
                    }
                    else if (itemType.equals("Group")) {
                        databaseActionCompiler.addAll(GroupUpdateDescription.getActions(fromID, id, attributeValue));
                    }
                    else if (itemType.equals("Deal")) {
                        databaseActionCompiler.addAll(DealUpdateDescription.getActions(fromID, id, attributeValue));
                    }
                    else {
                        throw new Exception("Unable to perform " + action + " to " + attributeName + " for a " + itemType + "!");
                    }
                    break;
                case access:
                    if (itemType.equals("Event")) {
                        databaseActionCompiler.addAll(EventUpdateAccess.getActions(fromID, id, attributeValue));
                    }
                    else if (itemType.equals("Challenge")) {
                        databaseActionCompiler.addAll(ChallengeUpdateAccess.getActions(fromID, id, attributeValue));
                    }
                    else if (itemType.equals("Post")) {
                        databaseActionCompiler.addAll(PostUpdateAccess.getActions(fromID, id, attributeValue));
                    }
                    else if (itemType.equals("Group")) {
                        databaseActionCompiler.addAll(GroupUpdateAccess.getActions(fromID, id, attributeValue));
                    }
                    else {
                        throw new Exception("Unable to perform " + action + " to " + attributeName + " for a " + itemType + "!");
                    }
                    break;
                case restriction:
                    if (itemType.equals("Event")) {
                        databaseActionCompiler.addAll(EventUpdateRestriction.getActions(fromID, id, attributeValue));
                    }
                    else if (itemType.equals("Challenge")) {
                        databaseActionCompiler.addAll(ChallengeUpdateRestriction.getActions(fromID, id, attributeValue));
                    }
                    else if (itemType.equals("Group")) {
                        databaseActionCompiler.addAll(GroupUpdateRestriction.getActions(fromID, id, attributeValue));
                    }
                    else {
                        throw new Exception("Unable to perform " + action + " to " + attributeName + " for a " + itemType + "!");
                    }
                    break;
                case challenge:
                    if (itemType.equals("Event")) {
                        databaseActionCompiler.addAll(EventUpdateChallenge.getActions(fromID, id, attributeValue));
                    }
                    else {
                        throw new Exception("Unable to perform " + action + " to " + attributeName + " for a " + itemType + "!");
                    }
                    break;
                case ifCompleted:
                    if (itemType.equals("Event")) {
                        databaseActionCompiler.addAll(EventUpdateIfCompleted.getActions(fromID, id, attributeValue));
                    }
                    else {
                        throw new Exception("Unable to perform " + action + " to " + attributeName + " for a " + itemType + "!");
                    }
                    break;
                case goal:
                    if (itemType.equals("Challenge")) {
                        databaseActionCompiler.addAll(ChallengeUpdateGoal.getActions(fromID, id, attributeValue));
                    }
                    else {
                        throw new Exception("Unable to perform " + action + " to " + attributeName + " for a " + itemType + "!");
                    }
                    break;
                case capacity:
                    if (itemType.equals("Event")) {
                        databaseActionCompiler.addAll(EventUpdateCapacity.getActions(fromID, id, attributeValue));
                    }
                    else if (itemType.equals("Challenge")) {
                        databaseActionCompiler.addAll(ChallengeUpdateCapacity.getActions(fromID, id, attributeValue));
                    }
                    else {
                        throw new Exception("Unable to perform " + action + " to " + attributeName + " for a " + itemType + "!");
                    }
                    break;
                case difficulty:
                    if (itemType.equals("Challenge")) {
                        databaseActionCompiler.addAll(ChallengeUpdateDifficulty.getActions(fromID, id, attributeValue));
                    }
                    else {
                        throw new Exception("Unable to perform " + action + " to " + attributeName + " for a " + itemType + "!");
                    }
                    break;
                case endTime:
                    if (itemType.equals("Challenge")) {
                        databaseActionCompiler.addAll(ChallengeUpdateEndTime.getActions(fromID, id, attributeValue));
                    }
                    else {
                        throw new Exception("Unable to perform " + action + " to " + attributeName + " for a " + itemType + "!");
                    }
                    break;
                case prize:
                    if (itemType.equals("Challenge")) {
                        databaseActionCompiler.addAll(ChallengeUpdatePrize.getActions(fromID, id, attributeValue));
                    }
                    else {
                        throw new Exception("Unable to perform " + action + " to " + attributeName + " for a " + itemType + "!");
                    }
                    break;
                case prizeImagePath:
                    if (itemType.equals("Challenge")) {
                        databaseActionCompiler.addAll(ChallengeUpdatePrizeImagePath.getActions(fromID, id, attributeValue));
                    }
                    else {
                        throw new Exception("Unable to perform " + action + " to " + attributeName + " for a " + itemType + "!");
                    }
                    break;
                case winner:
                    if (itemType.equals("Challenge")) {
                        databaseActionCompiler.addAll(ChallengeUpdateWinner.getActions(fromID, id, attributeValue));
                    }
                    else {
                        throw new Exception("Unable to perform " + action + " to " + attributeName + " for a " + itemType + "!");
                    }
                    break;
                case approved:
                    if (itemType.equals("Submission")) {
                        databaseActionCompiler.addAll(SubmissionApprove.getActions(fromID, id, attributeValue));
                    }
                    else {
                        throw new Exception("Unable to perform " + action + " to " + attributeName + " for a " + itemType + "!");
                    }
                    break;
                case comment:
                    if (itemType.equals("Comment")) {
                        databaseActionCompiler.addAll(CommentUpdateComment.getActions(fromID, id, attributeValue));
                    }
                    else {
                        throw new Exception("Unable to perform " + action + " to " + attributeName + " for a " + itemType + "!");
                    }
                    break;
                case productName:
                    if (itemType.equals("Deal")) {
                        databaseActionCompiler.addAll(DealUpdateProductName.getActions(fromID, id, attributeValue));
                    }
                    else {
                        throw new Exception("Unable to perform " + action + " to " + attributeName + " for a " + itemType + "!");
                    }
                    break;
                case productImagePath:
                    if (itemType.equals("Deal")) {
                        databaseActionCompiler.addAll(DealUpdateProductImagePath.getActions(fromID, id, attributeValue));
                    }
                    else {
                        throw new Exception("Unable to perform " + action + " to " + attributeName + " for a " + itemType + "!");
                    }
                    break;
                case productCreditPrice:
                    if (itemType.equals("Deal")) {
                        databaseActionCompiler.addAll(DealUpdateProductCreditPrice.getActions(fromID, id, attributeValue));
                    }
                    else {
                        throw new Exception("Unable to perform " + action + " to " + attributeName + " for a " + itemType + "!");
                    }
                    break;
                case productStoreLink:
                    if (itemType.equals("Deal")) {
                        databaseActionCompiler.addAll(DealUpdateProductStoreLink.getActions(fromID, id, attributeValue));
                    }
                    else {
                        throw new Exception("Unable to perform " + action + " to " + attributeName + " for a " + itemType + "!");
                    }
                    break;
                case validUntil:
                    if (itemType.equals("Deal")) {
                        databaseActionCompiler.addAll(DealUpdateValidUntil.getActions(fromID, id, attributeValue));
                    }
                    else {
                        throw new Exception("Unable to perform " + action + " to " + attributeName + " for a " + itemType + "!");
                    }
                    break;
                case score:
                    if (itemType.equals("Deal")) {
                        databaseActionCompiler.addAll(DealUpdateScore.getActions(fromID, id, attributeValue));
                    }
                    else {
                        throw new Exception("Unable to perform " + action + " to " + attributeName + " for a " + itemType + "!");
                    }
                    break;
                default:
                    throw new Exception("Can't perform an UPDATESET operation on " + attributeName + "!");
            }
        }
        catch (IllegalArgumentException e) {
            throw new Exception("AttributeName: " + attributeName + " not recognized! Error: " + e.getLocalizedMessage());
        }

        compilers.add(databaseActionCompiler);
        DynamoDBHandler.getInstance().attemptTransaction(compilers, ifDevelopment());
    }

    /**
     * Handles a UPDATEADD action for the given item.
     *
     * @param id The id of the item to update.
     * @throws Exception If anything goes wrong in the update.
     */
    public void handleUpdateAdd(String id) throws Exception {
        // switch all attributes, then if necessary, item type
        SingletonTimer.get().endAndPushCheckpoint("Init compiler for Update Add");
        List<DatabaseActionCompiler> compilers = new ArrayList<>();
        DatabaseActionCompiler databaseActionCompiler = new DatabaseActionCompiler();

        String attributeValue = null;
        if (attributeValues.length != 1 && attributeValues.length != 0) {
            throw new Exception("For updating " + attributeName + " on " + itemType + "attributeValues must be 0 or " +
                    "1 item long!");
        }
        else if (attributeValues.length == 1) {
            // DynamoDB can't handle empty string sets
            attributeValue = attributeValues[0];
        }

        try {
            switch (AttributeName.valueOf(attributeName)) {
                case profileImagePaths:
                    if (ItemType.isUser(itemType)) {
                        databaseActionCompiler.addAll(UserAddProfileImagePath.getActions(fromID, id,
                                itemType, attributeValue));
                    }
                    else {
                        throw new Exception("Unable to perform " + action + " to " + attributeName + " for a " +
                                itemType + "!");
                    }
                    break;
                case scheduledWorkouts:
                    if (itemType.equals("Client")) {
                        databaseActionCompiler.addAll(ClientAddToWorkout.getActions(fromID, id, attributeValue));
                    }
                    else {
                        throw new Exception("Unable to perform " + action + " to " + attributeName + " for a " +
                                itemType + "!");
                    }
                    break;
                case friends:
                    if (ItemType.isUser(itemType)) {
                        databaseActionCompiler.addAll(UserAddFriend.getActions(fromID, id, itemType, attributeValue));
                    }
                    else {
                        throw new Exception("Unable to perform " + action + " to " + attributeName + " for a " +
                                itemType + "!");
                    }
                    break;
                case scheduledEvents:
                    if (ItemType.isUser(itemType)) {
                        databaseActionCompiler.addAll(UserAddToEvent.getActions(fromID, id, itemType, attributeValue));
                    } else {
                        throw new Exception("Unable to perform " + action + " to " + attributeName + " for a " +
                                itemType + "!");
                    }
                    break;
                case challenges:
                    if (ItemType.isUser(itemType)) {
                        compilers.addAll(UserAddToChallenge.getCompilers(fromID, id, itemType, attributeValue));
                    } else {
                        throw new Exception("Unable to perform " + action + " to " + attributeName + " for a " +
                                itemType + "!");
                    }
                    break;
                case groups:
                    if (ItemType.isUser(itemType)) {
                        databaseActionCompiler.addAll(UserAddToGroup.getActions(fromID, id, itemType, attributeValue));
                    } else {
                        throw new Exception("Unable to perform " + action + " to " + attributeName + " for a " +
                                itemType + "!");
                    }
                    break;
                case messageBoards:
                    if (ItemType.isUser(itemType)) {
                        databaseActionCompiler.addAll(UserAddMessageBoard.getActions(fromID, id, itemType, attributeValue));
                    } else {
                        throw new Exception("Unable to perform " + action + " to " + attributeName + " for a " +
                                itemType + "!");
                    }
                    break;
                case productsOwned:
                    if (ItemType.isUser(itemType)) {
                        databaseActionCompiler.addAll(UserAddProductsOwned.getActions(fromID, id, itemType, attributeValue));
                    } else {
                        throw new Exception("Unable to perform " + action + " to " + attributeName + " for a " +
                                itemType + "!");
                    }
                    break;
                    // TODO We should make a request system for this too
//                case ownedGroups:
//                    if (itemType.equals("Client") || itemType.equals("Trainer") || itemType.equals("Gym") || itemType
//                            .equals("Sponsor")) {
//                        databaseActionCompiler.addAll(UserAdd.getActions(fromID, id, itemType, attributeValue));
//                    } else {
//                        throw new Exception("Unable to perform " + action + " to " + attributeName + " for a " +
//                                itemType + "!");
//                    }
//                    break;
                case availableTimes:
                    if (itemType.equals("Trainer")) {
                        databaseActionCompiler.addAll(TrainerAddAvailableTime.getActions(fromID, id,
                                attributeValue));
                    } else {
                        throw new Exception("Unable to perform " + action + " to " + attributeName + " for a " +
                                itemType + "!");
                    }
                    break;
                case certifications:
                    if (itemType.equals("Trainer")) {
                        databaseActionCompiler.addAll(TrainerAddCertification.getActions(fromID, id,
                                attributeValue));
                    } else {
                        throw new Exception("Unable to perform " + action + " to " + attributeName + " for a " +
                                itemType + "!");
                    }
                    break;
                case vacationTimes:
                    if (itemType.equals("Gym")) {
                        databaseActionCompiler.addAll(GymAddVacationTime.getActions(fromID, id,
                                attributeValue));
                    } else {
                        throw new Exception("Unable to perform " + action + " to " + attributeName + " for a " +
                                itemType + "!");
                    }
                    break;
                case tags:
                    if (itemType.equals("Event")) {
                        databaseActionCompiler.addAll(EventAddTag.getActions(fromID, id, attributeValue));
                    }
                    else if (itemType.equals("Challenge")) {
                        databaseActionCompiler.addAll(ChallengeAddTag.getActions(fromID, id, attributeValue));
                    }
                    else if (itemType.equals("Group")) {
                        databaseActionCompiler.addAll(GroupAddTag.getActions(fromID, id, attributeValue));
                    }
                    else {
                        throw new Exception("Unable to perform " + action + " to " + attributeName + " for a " +
                                itemType + "!");
                    }
                    break;
                case picturePaths:
                    if (itemType.equals("Post")) {
                        databaseActionCompiler.addAll(PostAddPicturePath.getActions(fromID, id, attributeValue));
                    } else if (itemType.equals("Submission")) {
                        databaseActionCompiler.addAll(SubmissionAddPicturePath.getActions(fromID, id, attributeValue));
                    } else {
                        throw new Exception("Unable to perform " + action + " to " + attributeName + " for a " +
                                itemType + "!");
                    }
                    break;
                case videoPaths:
                    if (itemType.equals("Post")) {
                        databaseActionCompiler.addAll(PostAddVideoPath.getActions(fromID, id, attributeValue));
                    } else if (itemType.equals("Submission")) {
                        databaseActionCompiler.addAll(SubmissionAddVideoPath.getActions(fromID, id, attributeValue));
                    } else {
                        throw new Exception("Unable to perform " + action + " to " + attributeName + " for a " +
                                itemType + "!");
                    }
                    break;
                case likes:
                    if (itemType.equals("Post")) {
                        databaseActionCompiler.addAll(PostAddLike.getActions(fromID, id, attributeValue));
                    } else if (itemType.equals("Submission")) {
                        databaseActionCompiler.addAll(SubmissionAddLike.getActions(fromID, id, attributeValue));
                    } else if (itemType.equals("Comment")) {
                        databaseActionCompiler.addAll(CommentAddLike.getActions(fromID, id, attributeValue));
                    } else {
                        throw new Exception("Unable to perform " + action + " to " + attributeName + " for a " +
                                itemType + "!");
                    }
                    break;
                case lastSeenFor:
                    if (itemType.equals("Message")) {
                        databaseActionCompiler.addAll(MessageAddLastSeenFor.getActions(fromID, secondaryIdentifier, id, attributeValue));
                    } else {
                        throw new Exception("Unable to perform " + action + " to " + attributeName + " for a " +
                                itemType + "!");
                    }
                    break;
                case N:
                    if (itemType.equals("Streak")) {
                        databaseActionCompiler.addAll(StreakAddN.getActions(fromID, id, TimeHelper.now()));
                    } else {
                        throw new Exception("Unable to perform " + action + " to " + attributeName + " for a " +
                                itemType + "!");
                    }
                    break;
                case productImagePaths:
                    if (itemType.equals("Deal")) {
                        databaseActionCompiler.addAll(DealUpdateAddProductImagePath.getActions(fromID, id, attributeValue));
                    }
                    else {
                        throw new Exception("Unable to perform " + action + " to " + attributeName + " for a " + itemType + "!");
                    }
                    break;
                default:
                    throw new Exception("Can't perform an UPDATEADD operation on " + attributeName + "!");
            }
        }
        catch (IllegalArgumentException e) {
            throw new Exception("AttributeName: " + attributeName + " not recognized! Error: " + e.toString(), e);
        }

        compilers.add(databaseActionCompiler);
        DynamoDBHandler.getInstance().attemptTransaction(compilers, ifDevelopment());
    }

    /**
     * Handles a UPDATEREMOVE action for the given item.
     *
     * @param id The id of the item to update.
     * @throws Exception If anything goes wrong in the update.
     */
    private void handleUpdateRemove(String id) throws Exception {
        // switch all attributes, then if necessary, item type
        SingletonTimer.get().endAndPushCheckpoint("Init compiler for Update Remove");
        List<DatabaseActionCompiler> compilers = new ArrayList<>();
        DatabaseActionCompiler databaseActionCompiler = new DatabaseActionCompiler();

        String attributeValue = null;
        if (attributeValues.length != 1 && attributeValues.length != 0) {
            throw new Exception("For updating " + attributeName + " on " + itemType + "attributeValues must be 0 or " +
                    "1 item long!");
        }
        else if (attributeValues.length == 1) {
            // DynamoDB can't handle empty string sets
            attributeValue = attributeValues[0];
        }

        try {
            switch (AttributeName.valueOf(attributeName)) {
                case profileImagePaths:
                    if (ItemType.isUser(itemType)) {
                        databaseActionCompiler.addAll(UserRemoveProfileImagePath.getActions(fromID, id,
                                itemType, attributeValue));
                    }
                    else {
                        throw new Exception("Unable to perform " + action + " to " + attributeName + " for a " +
                                itemType + "!");
                    }
                    break;
                case scheduledWorkouts:
                    if (itemType.equals("Client")) {
                        databaseActionCompiler.addAll(ClientRemoveFromWorkout.getActions(fromID, id, attributeValue));
                    }
                    else {
                        throw new Exception("Unable to perform " + action + " to " + attributeName + " for a " +
                                itemType + "!");
                    }
                    break;
                case friends:
                    if (ItemType.isUser(itemType)) {
                        databaseActionCompiler.addAll(UserRemoveFriend.getActions(fromID, id, itemType, attributeValue));
                    }
                    else {
                        throw new Exception("Unable to perform " + action + " to " + attributeName + " for a " +
                                itemType + "!");
                    }
                    break;
                case scheduledEvents:
                    if (ItemType.isUser(itemType)) {
                        databaseActionCompiler.addAll(UserRemoveFromEvent.getActions(fromID, id, itemType, attributeValue));
                    } else {
                        throw new Exception("Unable to perform " + action + " to " + attributeName + " for a " +
                                itemType + "!");
                    }
                    break;
                case challenges:
                    if (ItemType.isUser(itemType)) {
                        databaseActionCompiler.addAll(UserRemoveFromChallenge.getActions(fromID, id, itemType,
                                attributeValue));
                    } else {
                        throw new Exception("Unable to perform " + action + " to " + attributeName + " for a " +
                                itemType + "!");
                    }
                    break;
                case groups:
                    if (ItemType.isUser(itemType)) {
                        databaseActionCompiler.addAll(UserRemoveFromGroup.getActions(fromID, id, itemType, attributeValue));
                    } else {
                        throw new Exception("Unable to perform " + action + " to " + attributeName + " for a " +
                                itemType + "!");
                    }
                    break;
                case messageBoards:
                    if (ItemType.isUser(itemType)) {
                        databaseActionCompiler.addAll(UserRemoveMessageBoard.getActions(fromID, id, itemType, attributeValue));
                    } else {
                        throw new Exception("Unable to perform " + action + " to " + attributeName + " for a " +
                                itemType + "!");
                    }
                    break;
                case availableTimes:
                    if (itemType.equals("Trainer")) {
                        databaseActionCompiler.addAll(TrainerRemoveAvailableTime.getActions(fromID, id, attributeValue));
                    }
                    else {
                        throw new Exception("Unable to perform " + action + " to " + attributeName + " for a " +
                                itemType + "!");
                    }
                    break;
                case vacationTimes:
                    if (itemType.equals("Gym")) {
                        databaseActionCompiler.addAll(GymRemoveVacationTime.getActions(fromID, id, attributeValue));
                    }
                    else {
                        throw new Exception("Unable to perform " + action + " to " + attributeName + " for a " +
                                itemType + "!");
                    }
                case tags:
                    if (itemType.equals("Event")) {
                        databaseActionCompiler.addAll(EventRemoveTag.getActions(fromID, id, attributeValue));
                    }
                    else if (itemType.equals("Challenge")) {
                        databaseActionCompiler.addAll(ChallengeRemoveTag.getActions(fromID, id, attributeValue));
                    }
                    else {
                        throw new Exception("Unable to perform " + action + " to " + attributeName + " for a " +
                                itemType + "!");
                    }
                    break;
                case picturePaths:
                    if (itemType.equals("Post")) {
                        databaseActionCompiler.addAll(PostRemovePicturePath.getActions(fromID, id, attributeValue));
                    } else if (itemType.equals("Submission")) {
                        databaseActionCompiler.addAll(SubmissionRemovePicturePath.getActions(fromID, id, attributeValue));
                    } else {
                        throw new Exception("Unable to perform " + action + " to " + attributeName + " for a " +
                                itemType + "!");
                    }
                    break;
                case videoPaths:
                    if (itemType.equals("Post")) {
                        databaseActionCompiler.addAll(PostRemoveVideoPath.getActions(fromID, id, attributeValue));
                    } else if (itemType.equals("Submission")) {
                        databaseActionCompiler.addAll(SubmissionRemoveVideoPath.getActions(fromID, id, attributeValue));
                    } else {
                        throw new Exception("Unable to perform " + action + " to " + attributeName + " for a " +
                                itemType + "!");
                    }
                    break;
                case likes:
                    if (itemType.equals("Post")) {
                        databaseActionCompiler.addAll(PostRemoveLike.getActions(fromID, id, attributeValue));
                    } else if (itemType.equals("Submission")) {
                        databaseActionCompiler.addAll(SubmissionRemoveLike.getActions(fromID, id, attributeValue));
                    } else if (itemType.equals("Comment")) {
                        databaseActionCompiler.addAll(CommentRemoveLike.getActions(fromID, id, attributeValue));
                    } else {
                        throw new Exception("Unable to perform " + action + " to " + attributeName + " for a " +
                                itemType + "!");
                    }
                    break;
                case productImagePaths:
                    if (itemType.equals("Deal")) {
                        databaseActionCompiler.addAll(DealUpdateRemoveProductImagePath.getActions(fromID, id, attributeValue));
                    }
                    else {
                        throw new Exception("Unable to perform " + action + " to " + attributeName + " for a " + itemType + "!");
                    }
                    break;
                default:
                    throw new Exception("Can't perform an UPDATEREMOVE operation on " + attributeName + "!");
            }
        }
        catch (IllegalArgumentException e) {
            throw new Exception("AttributeName: " + attributeName + " not recognized! Error: " + e.getLocalizedMessage());
        }

        compilers.add(databaseActionCompiler);
        DynamoDBHandler.getInstance().attemptTransaction(compilers, ifDevelopment());
    }

    /**
     * Handles a DELETE action for the given item.
     *
     * @param id The id of the item to delete.
     * @throws Exception If anything goes wrong in the deletion.
     */
    private void handleDelete(String id) throws Exception {
        SingletonTimer.get().endAndPushCheckpoint("Init compiler for Delete");
        List<DatabaseActionCompiler> compilers = new ArrayList<>();
        DatabaseActionCompiler databaseActionCompiler = new DatabaseActionCompiler();
        try {
            switch (ItemType.valueOf(itemType)) {
                case Client:
                    databaseActionCompiler.addAll(DeleteClient.getActions(fromID, id));
                    break;
                case Trainer:
                    databaseActionCompiler.addAll(DeleteTrainer.getActions(fromID, id));
                    break;
                case Gym:
                    databaseActionCompiler.addAll(DeleteGym.getActions(fromID, id));
                    break;
                case Workout:
                    databaseActionCompiler.addAll(DeleteWorkout.getActions(fromID, id));
                    break;
                case Review:
                    databaseActionCompiler.addAll(DeleteReview.getActions(fromID, id));
                    break;
                case Event:
                    databaseActionCompiler.addAll(DeleteEvent.getActions(fromID, id));
                    break;
                case Challenge:
                    databaseActionCompiler.addAll(DeleteChallenge.getActions(fromID, id));
                    break;
                case Invite:
                    databaseActionCompiler.addAll(DeleteInvite.getActions(fromID, id));
                    break;
                case Post:
                    databaseActionCompiler.addAll(DeletePost.getActions(fromID, id));
                    break;
                case Submission:
                    databaseActionCompiler.addAll(DeleteSubmission.getActions(fromID, id));
                    break;
                case Group:
                    databaseActionCompiler.addAll(DeleteGroup.getActions(fromID, id));
                    break;
                case Comment:
                    databaseActionCompiler.addAll(DeleteComment.getActions(fromID, id));
                    break;
                case Sponsor:
                    databaseActionCompiler.addAll(DeleteSponsor.getActions(fromID, id));
                    break;
                case Message:
                    databaseActionCompiler.addAll(DeleteMessage.getActions(fromID, secondaryIdentifier, id));
                    break;
                case Streak:
                    databaseActionCompiler.addAll(DeleteStreak.getActions(fromID, id));
                    break;
                case Deal:
                    databaseActionCompiler.addAll(DeleteDeal.getActions(fromID, id));
                    break;
                case Product:
                    databaseActionCompiler.addAll(DeleteProduct.getActions(fromID, id));
                    break;
                case Admin:
                    databaseActionCompiler.addAll(DeleteAdmin.getActions(fromID, id));
                    break;
                default:
                    throw new Exception("Item Type: " + itemType + " recognized but not handled?");
            }
        }
        catch (IllegalArgumentException e) {
            throw new Exception("Item Type: \"" + itemType + "\" not recognized! Error: " + e.getLocalizedMessage());
        }

        compilers.add(databaseActionCompiler);
        DynamoDBHandler.getInstance().attemptTransaction(compilers, ifDevelopment());
    }

    public boolean ifDevelopment() {
        return (environmentType != null) && (environmentType.equals("development"));
    }

    public LambdaRequest(String fromID, String fromTimeZone, String action, String specifyAction,
                         String itemType, String[] identifiers, String attributeName,
                         String secondaryIdentifier, String[] attributeValues,
                         String environmentType, CreateClientRequest createClientRequest,
                         CreateTrainerRequest createTrainerRequest, CreateGymRequest createGymRequest,
                         CreateWorkoutRequest createWorkoutRequest, CreateReviewRequest createReviewRequest,
                         CreateEventRequest createEventRequest, CreateChallengeRequest createChallengeRequest,
                         CreateInviteRequest createInviteRequest, CreatePostRequest createPostRequest,
                         CreateSubmissionRequest createSubmissionRequest, CreateGroupRequest createGroupRequest,
                         CreateCommentRequest createCommentRequest, CreateSponsorRequest createSponsorRequest,
                         CreateMessageRequest createMessageRequest, CreateStreakRequest createStreakRequest,
                         CreateEnterpriseRequest createEnterpriseRequest, CreateDealRequest createDealRequest,
                         CreateProductRequest createProductRequest) {
        this.fromID = fromID;
        this.fromTimeZone = fromTimeZone;
        this.action = action;
        this.specifyAction = specifyAction;
        this.itemType = itemType;
        this.identifiers = identifiers;
        this.secondaryIdentifier = secondaryIdentifier;
        this.attributeName = attributeName;
        this.attributeValues = attributeValues;
        this.environmentType = environmentType;
        this.createClientRequest = createClientRequest;
        this.createTrainerRequest = createTrainerRequest;
        this.createGymRequest = createGymRequest;
        this.createWorkoutRequest = createWorkoutRequest;
        this.createReviewRequest = createReviewRequest;
        this.createEventRequest = createEventRequest;
        this.createChallengeRequest = createChallengeRequest;
        this.createInviteRequest = createInviteRequest;
        this.createPostRequest = createPostRequest;
        this.createSubmissionRequest = createSubmissionRequest;
        this.createGroupRequest = createGroupRequest;
        this.createCommentRequest = createCommentRequest;
        this.createSponsorRequest = createSponsorRequest;
        this.createMessageRequest = createMessageRequest;
        this.createStreakRequest = createStreakRequest;
        this.createEnterpriseRequest = createEnterpriseRequest;
        this.createDealRequest = createDealRequest;
        this.createProductRequest = createProductRequest;
    }

    public LambdaRequest() {}

    public String getFromID() {
        return fromID;
    }

    public void setFromID(String fromID) {
        this.fromID = fromID;
    }

    public String getFromTimeZone() {
        return fromTimeZone;
    }

    public void setFromTimeZone(String fromTimeZone) {
        this.fromTimeZone = fromTimeZone;
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

    public String getSecondaryIdentifier() {
        return secondaryIdentifier;
    }

    public void setSecondaryIdentifier(String secondaryIdentifier) {
        this.secondaryIdentifier = secondaryIdentifier;
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

    public String getEnvironmentType() {
        return environmentType;
    }

    public void setEnvironmentType(String environmentType) {
        this.environmentType = environmentType;
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

    public CreateEventRequest getCreateEventRequest() {
        return createEventRequest;
    }

    public void setCreateEventRequest(CreateEventRequest createEventRequest) {
        this.createEventRequest = createEventRequest;
    }

    public CreateChallengeRequest getCreateChallengeRequest() {
        return createChallengeRequest;
    }

    public void setCreateChallengeRequest(CreateChallengeRequest createChallengeRequest) {
        this.createChallengeRequest = createChallengeRequest;
    }

    public CreateInviteRequest getCreateInviteRequest() {
        return createInviteRequest;
    }

    public void setCreateInviteRequest(CreateInviteRequest createInviteRequest) {
        this.createInviteRequest = createInviteRequest;
    }

    public CreatePostRequest getCreatePostRequest() {
        return createPostRequest;
    }

    public void setCreatePostRequest(CreatePostRequest createPostRequest) {
        this.createPostRequest = createPostRequest;
    }

    public CreateSubmissionRequest getCreateSubmissionRequest() {
        return createSubmissionRequest;
    }

    public void setCreateSubmissionRequest(CreateSubmissionRequest createSubmissionRequest) {
        this.createSubmissionRequest = createSubmissionRequest;
    }

    public CreateGroupRequest getCreateGroupRequest() {
        return createGroupRequest;
    }

    public void setCreateGroupRequest(CreateGroupRequest createGroupRequest) {
        this.createGroupRequest = createGroupRequest;
    }

    public CreateCommentRequest getCreateCommentRequest() {
        return createCommentRequest;
    }

    public void setCreateCommentRequest(CreateCommentRequest createCommentRequest) {
        this.createCommentRequest = createCommentRequest;
    }

    public CreateSponsorRequest getCreateSponsorRequest() {
        return createSponsorRequest;
    }

    public void setCreateSponsorRequest(CreateSponsorRequest createSponsorRequest) {
        this.createSponsorRequest = createSponsorRequest;
    }

    public CreateMessageRequest getCreateMessageRequest() {
        return createMessageRequest;
    }

    public void setCreateMessageRequest(CreateMessageRequest createMessageRequest) {
        this.createMessageRequest = createMessageRequest;
    }

    public CreateStreakRequest getCreateStreakRequest() {
        return createStreakRequest;
    }

    public void setCreateStreakRequest(CreateStreakRequest createStreakRequest) {
        this.createStreakRequest = createStreakRequest;
    }

    public CreateEnterpriseRequest getCreateEnterpriseRequest() {
        return createEnterpriseRequest;
    }

    public void setCreateEnterpriseRequest(CreateEnterpriseRequest createEnterpriseRequest) {
        this.createEnterpriseRequest = createEnterpriseRequest;
    }

    public CreateDealRequest getCreateDealRequest() {
        return createDealRequest;
    }

    public void setCreateDealRequest(CreateDealRequest createDealRequest) {
        this.createDealRequest = createDealRequest;
    }

    public CreateProductRequest getCreateProductRequest() {
        return createProductRequest;
    }

    public void setCreateProductRequest(CreateProductRequest createProductRequest) {
        this.createProductRequest = createProductRequest;
    }
}
