package main.java.lambdaFunctionHandlers.requestObjects;

// TODO Optional parameters at the get go?
public class CreateGymRequest {
    // Required
    public String name;
    public String email;
    public String username;
    public String address;
    public String sessionCapacity;

    // Optional
    public String foundingDay;
    public String stripeID;
    public String federatedID;
    public String gymType;
    public String paymentSplit;
    public String[] weeklyHours;
    public String bio;

    public CreateGymRequest(String name, String email, String username, String foundingDay, String stripeID,
                            String federatedID, String bio, String address, String[] weeklyHours, String sessionCapacity,
                            String gymType, String paymentSplit) {
        this.name = name;
        this.email = email;
        this.username = username;
        this.foundingDay = foundingDay;
        this.bio = bio;
        this.stripeID = stripeID;
        this.federatedID = federatedID;
        this.address = address;
        this.weeklyHours = weeklyHours;
        this.sessionCapacity = sessionCapacity;
        this.gymType = gymType;
        this.paymentSplit = paymentSplit;
    }

    public CreateGymRequest() {}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getFoundingDay() {
        return foundingDay;
    }

    public void setFoundingDay(String foundingDay) {
        this.foundingDay = foundingDay;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String[] getWeeklyHours() {
        return weeklyHours;
    }

    public void setWeeklyHours(String[] weeklyHours) {
        this.weeklyHours = weeklyHours;
    }

    public String getSessionCapacity() {
        return sessionCapacity;
    }

    public void setSessionCapacity(String sessionCapacity) {
        this.sessionCapacity = sessionCapacity;
    }

    public String getGymType() {
        return gymType;
    }

    public void setGymType(String gymType) {
        this.gymType = gymType;
    }

    public String getPaymentSplit() {
        return paymentSplit;
    }

    public void setPaymentSplit(String paymentSplit) {
        this.paymentSplit = paymentSplit;
    }
}
