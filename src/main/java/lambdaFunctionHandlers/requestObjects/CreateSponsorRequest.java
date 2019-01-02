package main.java.lambdaFunctionHandlers.requestObjects;

public class CreateSponsorRequest {
    // Required
    public String name;
    public String email;
    public String username;

    // Optional
    public String birthday;
    public String stripeID;
    public String federatedID;
    public String bio;

    public CreateSponsorRequest(String name, String gender, String birthday, String email, String username,
                                String stripeID, String federatedID, String bio) {
        this.name = name;
        this.birthday = birthday;
        this.email = email;
        this.username = username;
        this.stripeID = stripeID;
        this.federatedID = federatedID;
        this.bio = bio;
    }

    public CreateSponsorRequest() {}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }
}
