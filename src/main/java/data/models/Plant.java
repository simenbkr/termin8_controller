package data.models;

import java.sql.Timestamp;

public class Plant {
    private int id;
    private String name;
    private float max_temp, min_temp, min_moisture, max_moisture;
    private Timestamp last_watered;
    private boolean automatic_water;
    private int room_id, plantType_id;

    public Plant(int id) {
        this.id = id;
    }

    public Plant(int id, String name, float max_temp, float min_temp,
                 float min_moisture, float max_moisture, Timestamp last_watered,
                 boolean automatic_water, int room_id, int plantType_id) {
        this.id = id;
        this.name = name;
        this.max_temp = max_temp;
        this.min_temp = min_temp;
        this.min_moisture = min_moisture;
        this.max_moisture = max_moisture;
        this.last_watered = last_watered;
        this.automatic_water = automatic_water;
        this.room_id = room_id;
        this.plantType_id = plantType_id;
    }


    public int getId(){
        return id;
    }

    public int getRoom_id() {
        return room_id;
    }

    public void setRoom_id(int room_id) {
        this.room_id = room_id;
    }

    public boolean isAutomatic_water() {
        return automatic_water;
    }

    public void setAutomatic_water(boolean automatic_water) {
        this.automatic_water = automatic_water;
    }

    public Timestamp getLast_watered() {
        return last_watered;
    }

    public void setLast_watered(Timestamp last_watered) {
        this.last_watered = last_watered;
    }

    public float getMax_temp() {
        return max_temp;
    }

    public void setMax_temp(float max_temp) {
        this.max_temp = max_temp;
    }

    public float getMin_temp() {
        return min_temp;
    }

    public void setMin_temp(float min_temp) {
        this.min_temp = min_temp;
    }

    public float getMin_moisture() {
        return min_moisture;
    }

    public void setMin_moisture(float min_moisture) {
        this.min_moisture = min_moisture;
    }

    public float getMax_moisture() {
        return max_moisture;
    }

    public void setMax_moisture(float max_moisture) {
        this.max_moisture = max_moisture;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPlantType_id() {
        return plantType_id;
    }

    public void setPlantType_id(int plantType_id) {
        this.plantType_id = plantType_id;
    }
}
