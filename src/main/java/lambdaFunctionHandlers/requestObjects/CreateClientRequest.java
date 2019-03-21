package main.java.lambdaFunctionHandlers.requestObjects;

// TODO Optional parameters at the get go?
public class CreateClientRequest extends CreateObjectRequest {
    // Required
    public String name;
    public String email;
    public String username;

    // Optional
    public String gender;
    public String birthday;
    public String stripeID;
    public String federatedID;
    public String bio;
    public String enterpriseID;

    public CreateClientRequest(String name, String gender, String birthday, String email, String username, String
            bio, String stripeID, String federatedID, String enterpriseID) {
        this.name = name;
        this.gender = gender;
        this.birthday = birthday;
        this.email = email;
        this.username = username;
        this.bio = bio;
        this.stripeID = stripeID;
        this.federatedID = federatedID;
        this.enterpriseID = enterpriseID;
    }

    public CreateClientRequest() {}

    @Override
    public boolean ifHasEmptyString() {
        return hasEmptyString(name, gender, birthday, email, username, bio, stripeID, federatedID);
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
}
