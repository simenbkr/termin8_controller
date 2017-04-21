package data.dao;

import data.db.DB;
import data.mapper.PlantMapper;
import data.models.Plant;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PlantDAO implements IDAO<Plant> {


    @Override
    public void update(Plant plant) {
        String sql = "UPDATE termin8_django_plant SET name=?,last_watered=?,automatic_water=?,plant_type_id=?,room_id=? WHERE id=?";

        try {
            Connection connection = DB.getConnection();
            PreparedStatement st = connection.prepareStatement(sql);
            st.setString(1, plant.getName());
            st.setTimestamp(2, plant.getLast_watered());
            st.setInt(3, plant.isAutomatic_water() ? 1 : 0);
            st.setInt(4, plant.getPlantType_id());
            st.setInt(5, plant.getRoom_id());
            st.setInt(6, plant.getId());

            st.executeUpdate();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void delete(Plant plant) {
        String sql = "DELETE FROM termin8_django_plant WHERE id=?";
        try {
            Connection connection = DB.getConnection();
            PreparedStatement st = connection.prepareStatement(sql);
            st.setInt(1, plant.getId());
            st.executeUpdate();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    @Override
    public int create(Plant plant) {
        String sql = "INSERT INTO termin8_django_plant (navn,last_watered,automatic_water,plant_type_id,room_id) VALUES(?,?,?,?,?)";
        int lastId = -1;
        try {
            Connection connection = DB.getConnection();
            PreparedStatement st = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            st.setString(1, plant.getName());
            st.setTimestamp(2, plant.getLast_watered());
            st.setInt(3, plant.isAutomatic_water() ? 1 : 0);
            st.setInt(4, plant.getPlantType_id());
            st.setInt(5, plant.getRoom_id());

            st.execute();
            ResultSet rs = st.getGeneratedKeys();
            if (rs.next()) {
                lastId = rs.getInt(1);
            }
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lastId;
    }

    @Override
    public List<Plant> listAll() {
        List<Plant> plantList = new ArrayList<>();
        String sql = "SELECT * FROM termin8_django_plant";
        try {
            Connection connection = DB.getConnection();
            ResultSet rs = connection.createStatement().executeQuery(sql);
            rs.beforeFirst();
            PlantMapper mapper = new PlantMapper();
            while (rs.next()) {
                plantList.add(mapper.mapRow(rs, rs.getRow()));
            }
            connection.close();
            return plantList;
        } catch (SQLException e) {
            //e.printStackTrace();
            return null;
        }
    }

    @Override
    public Plant getByID(int id) {
        String sql = "SELECT * FROM termin8_django_plant WHERE id=" + String.valueOf(id);
        try {
            Connection connection = DB.getConnection();
            ResultSet rs = connection.createStatement().executeQuery(sql);
            rs.beforeFirst();
            rs.next();
            return new PlantMapper().mapRow(rs, 0);
        } catch (SQLException e) {
            //e.printStackTrace();
            return null;
        }
    }
}

