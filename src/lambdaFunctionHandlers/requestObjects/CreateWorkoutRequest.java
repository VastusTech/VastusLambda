package lambdaFunctionHandlers.requestObjects;

public class CreateWorkoutRequest {
    public String timeInterval;
    public String trainerID;
    public String[] clientIDs;
    public String gymID;
    public String sticker;
    public String intensity;
    public String price;

    public CreateWorkoutRequest(String timeInterval, String trainerID, String[] clientIDs, String gymID, String sticker, String intensity, String price) {
        this.timeInterval = timeInterval;
        this.trainerID = trainerID;
        this.clientIDs = clientIDs;
        this.gymID = gymID;
        this.sticker = sticker;
        this.intensity = intensity;
        this.price = price;
    }

    public CreateWorkoutRequest() {}

    public String getTimeInterval() {

        return timeInterval;
    }

    public void setTimeInterval(String timeInterval) {
        this.timeInterval = timeInterval;
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
