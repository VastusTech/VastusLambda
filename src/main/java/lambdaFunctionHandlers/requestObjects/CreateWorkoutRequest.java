package main.java.lambdaFunctionHandlers.requestObjects;

public class CreateWorkoutRequest {
    // Required
    public String time;
    public String trainer;
    public String[] clients;
    public String capacity;
    public String gym;
    public String sticker;
    public String intensity;
    public String price;

    public CreateWorkoutRequest(String time, String trainer, String[] clients, String capacity, String gym, String
            sticker, String intensity, String price) {
        this.time = time;
        this.trainer = trainer;
        this.clients = clients;
        this.gym = gym;
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

    public String getTrainer() {
        return trainer;
    }

    public void setTrainer(String trainer) {
        this.trainer = trainer;
    }

    public String[] getClients() {
        return clients;
    }

    public void setClients(String[] clients) {
        this.clients = clients;
    }

    public String getCapacity() {
        return capacity;
    }

    public void setCapacity(String capacity) {
        this.capacity = capacity;
    }

    public String getGym() {
        return gym;
    }

    public void setGym(String gym) {
        this.gym = gym;
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
