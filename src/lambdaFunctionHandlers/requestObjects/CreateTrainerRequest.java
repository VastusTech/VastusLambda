package lambdaFunctionHandlers.requestObjects;

public class CreateTrainerRequest {
    public String name;
    public String gender;
    public String birthday;
    public String email;
    public String username;
    public String gymID;
    // public String bio;

    public CreateTrainerRequest(String name, String gender, String birthday, String email, String username, String gymID) {
        this.name = name;
        this.gender = gender;
        this.birthday = birthday;
        this.email = email;
        this.username = username;
        this.gymID = gymID;
        // this.bio = bio;
    }

    public CreateTrainerRequest() {}

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

    public String getGymID() {
        return gymID;
    }

    public void setGymID(String gymID) {
        this.gymID = gymID;
    }

//    public String getBio() {
//        return bio;
//    }
//
//    public void setBio(String bio) {
//        this.bio = bio;
//    }
}
