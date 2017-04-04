package data.models;

public class SensorHistory {
    private int id;
    private float temp, moisture;
    private int plant_id;

    public SensorHistory(int id, float temp, float moisture, int plant_id) {
        this.id = id;
        this.temp = temp;
        this.moisture = moisture;
        this.plant_id = plant_id;
    }

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

    public int getPlant_id() {
        return plant_id;
    }

    public void setPlant_id(int plant_id) {
        this.plant_id = plant_id;
    }
}
