package data.models;

public class PlantType {
    private int id;
    private String name;
    private float max_temp, min_temp, min_moisture, max_moisture;

    public PlantType(int id, String name, float max_temp, float min_temp, float min_moisture, float max_moisture) {
        this.id = id;
        this.name = name;
        this.max_temp = max_temp;
        this.max_moisture = max_moisture;
        this.min_moisture = min_moisture;
        this.min_temp = min_temp;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
}
