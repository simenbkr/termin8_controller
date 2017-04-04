package data.models;

import java.sql.Timestamp;

public class WateringHistory {
    private int id;
    private float temp, moisture;
    private Timestamp time_watered;
    private int plant_id;


    public int getId() {
        return id;
    }

    public float getTemp() {
        return temp;
    }

    public void setTemp(float temp) {
        this.temp = temp;
    }

    public float getMoisture() {
        return moisture;
    }

    public void setMoisture(float moisture) {
        this.moisture = moisture;
    }

    public Timestamp getTime_watered() {
        return time_watered;
    }

    public void setTime_watered(Timestamp time_watered) {
        this.time_watered = time_watered;
    }

    public int getPlant_id() {
        return plant_id;
    }

    public void setPlant_id(int plant_id) {
        this.plant_id = plant_id;
    }
}
