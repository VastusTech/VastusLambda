package main.java.lambdaFunctionHandlers.requestObjects;

// TODO Optional parameters at the get go?
public class CreateClientRequest {
    public String name;
    public String gender;
    public String birthday;
    public String email;
    public String username;

    public CreateClientRequest(String name, String gender, String birthday, String email, String username) {
        this.name = name;
        this.gender = gender;
        this.birthday = birthday;
        this.email = email;
        this.username = username;
    }

    public CreateClientRequest() {}

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
}
