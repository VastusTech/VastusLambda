package lambdaFunctionHandlers.requestObjects;

public class CreateGymRequest {
    public String name;
    public String email;
    public String username;
    public String foundingDay;
    public String address;
    public String bio;

    public CreateGymRequest(String name, String email, String username, String foundingDay, String address, String bio) {
        this.name = name;
        this.email = email;
        this.username = username;
        this.foundingDay = foundingDay;
        this.address = address;
        this.bio = bio;
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }
}
