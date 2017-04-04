package data.models;

public class UserPlant {
    private int plant_id, user_id;

    public UserPlant(int plant_id, int user_id) {
        this.plant_id = plant_id;
        this.user_id = user_id;
    }


    public int getPlant_id() {
        return plant_id;
    }

    public void setPlant_id(int plant_id) {
        this.plant_id = plant_id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }
}
