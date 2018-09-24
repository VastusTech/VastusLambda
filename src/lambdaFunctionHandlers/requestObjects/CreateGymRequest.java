package lambdaFunctionHandlers.requestObjects;

public class CreateGymRequest {
    public String name;
    public String email;
    public String username;
    public String foundingDay;
    public String address;
    public String[] weeklyHours;
    public String sessionCapacity;
    public String gymType;
    public String paymentSplit;

    public CreateGymRequest(String name, String email, String username, String foundingDay, String address, String[]
            weeklyHours, String sessionCapacity, String gymType, String paymentSplit) {
        this.name = name;
        this.email = email;
        this.username = username;
        this.foundingDay = foundingDay;
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
