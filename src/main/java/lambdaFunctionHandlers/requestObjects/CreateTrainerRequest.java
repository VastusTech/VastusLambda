package main.java.lambdaFunctionHandlers.requestObjects;

public class CreateTrainerRequest extends CreateObjectRequest {
    // Required
    public String name;
    public String email;
    public String username;

    // Optional
    public String gender;
    public String birthday;
    public String bio;
    public String stripeID;
    public String federatedID;
    public String workoutCapacity;
    public String workoutPrice;
    public String workoutSticker;
    public String preferredIntensity;
    public String gym;

    public CreateTrainerRequest(String name, String gender, String birthday, String email, String username, String
            stripeID, String federatedID, String gym, String workoutSticker, String preferredIntensity, String bio,
                                String workoutCapacity, String workoutPrice) {
        this.name = name;
        this.gender = gender;
        this.birthday = birthday;
        this.email = email;
        this.username = username;
        this.stripeID = stripeID;
        this.federatedID = federatedID;
        this.gym = gym;
        this.workoutSticker = workoutSticker;
        this.preferredIntensity = preferredIntensity;
        this.bio = bio;
        this.workoutCapacity = workoutCapacity;
        this.workoutPrice = workoutPrice;
    }

    public CreateTrainerRequest() {}

    @Override
    public boolean ifHasEmptyString() {
        return hasEmptyString(name, gender, birthday, email, username, stripeID, federatedID, gym,
                workoutSticker, preferredIntensity, bio, workoutCapacity, workoutPrice);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getStripeID() {
        return stripeID;
    }

    public void setStripeID(String stripeID) {
        this.stripeID = stripeID;
    }

    public String getFederatedID() {
        return federatedID;
    }

    public void setFederatedID(String federatedID) {
        this.federatedID = federatedID;
    }

    public String getGym() {
        return gym;
    }

    public void setGym(String gym) {
        this.gym = gym;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getWorkoutSticker() {
        return workoutSticker;
    }

    public void setWorkoutSticker(String workoutSticker) {
        this.workoutSticker = workoutSticker;
    }

    public String getPreferredIntensity() {
        return preferredIntensity;
    }

    public void setPreferredIntensity(String preferredIntensity) {
        this.preferredIntensity = preferredIntensity;
    }

    public String getWorkoutCapacity() {
        return workoutCapacity;
    }

    public void setWorkoutCapacity(String workoutCapacity) {
        this.workoutCapacity = workoutCapacity;
    }

    public String getWorkoutPrice() {
        return workoutPrice;
    }

    public void setWorkoutPrice(String workoutPrice) {
        this.workoutPrice = workoutPrice;
    }
}
