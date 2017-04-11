package data.dao;

import data.db.DB;
import data.mapper.SensorHistoryMapper;
import data.mapper.UserPlantMapper;
import data.models.SensorHistory;
import data.models.UserPlant;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserPlantDAO implements IDAO<UserPlant> {
    @Override
    public void update(UserPlant userPlant) {
        String sql = "UPDATE termin8_django_plant_owned_by SET plant_id=?, user_id=? WHERE id=?";

        try {
            Connection connection = DB.getConnection();
            PreparedStatement st = connection.prepareStatement(sql);
            st.setInt(1, userPlant.getPlant_id());
            st.setInt(2, userPlant.getUser_id());
            st.setInt(3, userPlant.getId());

            st.executeUpdate();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(UserPlant userPlant) {
        String sql = "DELETE FROM termin8_django_plant_owned_by WHERE id=?";
        try {
            Connection connection = DB.getConnection();
            PreparedStatement st = connection.prepareStatement(sql);
            st.setInt(1, userPlant.getId());
            st.executeUpdate();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    @Override
    public int create(UserPlant userPlant) {
        String sql = "INSERT INTO termin8_django_plant_owned_by (plant_id,user_id) VALUES(?,?)";
        int lastId = -1;
        try {
            Connection connection = DB.getConnection();
            PreparedStatement st = connection.prepareStatement(sql);
            st.setInt(1, userPlant.getPlant_id());
            st.setInt(2, userPlant.getUser_id());

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
    public List<UserPlant> listAll() {
        List<UserPlant> userPlantList = new ArrayList<>();
        String sql = "SELECT * FROM termin8_django_plant_owned_by";
        try {
            Connection connection = DB.getConnection();
            ResultSet rs = connection.createStatement().executeQuery(sql);
            rs.beforeFirst();
            UserPlantMapper mapper = new UserPlantMapper();
            while (rs.next()) {
                userPlantList.add(mapper.mapRow(rs, rs.getRow()));
            }
            connection.close();
            return userPlantList;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public UserPlant getByID(int id) {
        String sql = "SELECT * FROM termin8_django_plant_owned_by WHERE id=" + String.valueOf(id);
        try {
            Connection connection = DB.getConnection();
            ResultSet rs = connection.createStatement().executeQuery(sql);
            rs.beforeFirst();
            rs.next();
            return new UserPlantMapper().mapRow(rs, 0);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
}
