package lambdaFunctionHandlers.requestObjects;

public class CreateWorkoutRequest {
    public String time;
    public String trainerID;
    public String[] clientIDs;
    public String gymID;
    public String sticker;
    public String intensity;
    public String price;

    public CreateWorkoutRequest(String time, String trainerID, String[] clientIDs, String gymID, String sticker, String intensity, String price) {
        this.time = time;
        this.trainerID = trainerID;
        this.clientIDs = clientIDs;
        this.gymID = gymID;
        this.sticker = sticker;
        this.intensity = intensity;
        this.price = price;
    }

    public CreateWorkoutRequest() {}

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTrainerID() {
        return trainerID;
    }

    public void setTrainerID(String trainerID) {
        this.trainerID = trainerID;
    }

    public String[] getClientIDs() {
        return clientIDs;
    }

    public void setClientIDs(String[] clientIDs) {
        this.clientIDs = clientIDs;
    }

    public String getGymID() {
        return gymID;
    }

    public void setGymID(String gymID) {
        this.gymID = gymID;
    }

    public String getSticker() {
        return sticker;
    }

    public void setSticker(String sticker) {
        this.sticker = sticker;
    }

    public String getIntensity() {
        return intensity;
    }

    public void setIntensity(String intensity) {
        this.intensity = intensity;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

}
