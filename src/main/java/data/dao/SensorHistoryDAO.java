package data.dao;
import data.db.DB;
import data.mapper.PlantMapper;
import data.mapper.SensorHistoryMapper;
import data.models.Plant;
import data.models.SensorHistory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SensorHistoryDAO implements IDAO<SensorHistory> {
    @Override
    public void update(SensorHistory sensorHistory) {
    	String sql = "UPDATE termin8_django_sensorHistory SET temp=?,moisture=?,timestamp=?,plant_id=? WHERE id=?";
    	
        try {
            Connection connection = DB.getConnection();
            PreparedStatement st = connection.prepareStatement(sql);
            st.setDouble(1, sensorHistory.getTemp());
            st.setDouble(2,sensorHistory.getMoisture());
            st.setTimestamp(3, sensorHistory.getTime());
            st.setInt(4, sensorHistory.getPlant_id());
            st.setInt(5, sensorHistory.getId());
        
            st.executeUpdate();
            connection.close(); 
        } catch(SQLException e){
            e.printStackTrace();
        }
    }

    

    @Override
    public void delete(SensorHistory sensorHistory) {
    	String sql = "DELETE FROM termin8_django_sensorHistory WHERE id=?";
        try {
            Connection connection = DB.getConnection();
            PreparedStatement st = connection.prepareStatement(sql);
            st.setInt(1, sensorHistory.getId());
            st.executeUpdate();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    @Override
    public int create(SensorHistory sensorHistory) {
    	String sql = "INSERT INTO termin8_django_sensorHistory (temp,moisture,timestamp,plant_id) VALUES(?,?,?,?)";
        int lastId = -1;
        try {
            Connection connection = DB.getConnection();
            PreparedStatement st = connection.prepareStatement(sql);
            st.setDouble(1, sensorHistory.getTemp());
            st.setDouble(2, sensorHistory.getMoisture());
            st.setTimestamp(3, sensorHistory.getTime());
            st.setInt(4, sensorHistory.getPlant_id());
            
            st.execute();
            ResultSet rs = st.getGeneratedKeys();
            if(rs.next()){
                lastId = rs.getInt(1);
            }
            connection.close();
        } catch(SQLException e){
            e.printStackTrace();
        }
        return lastId;
    }

    @Override
    public List<SensorHistory> listAll() {
    	List<SensorHistory> sensorHistoryList = new ArrayList<>();
        String sql = "SELECT * FROM termin8_django_sensorHistory";
        try {
            Connection connection = DB.getConnection();
            ResultSet rs = connection.createStatement().executeQuery(sql);
            rs.beforeFirst();
            SensorHistoryMapper mapper = new SensorHistoryMapper();
            while(rs.next()){
            	sensorHistoryList.add(mapper.mapRow(rs, rs.getRow()));
            }
            connection.close();
            return sensorHistoryList;
        } catch(SQLException e){
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public SensorHistory getByID(int id) {
    	String sql = "SELECT * FROM termin8_django_sensorHistory WHERE id=" + String.valueOf(id);
        try {
            Connection connection = DB.getConnection();
            ResultSet rs = connection.createStatement().executeQuery(sql);
            rs.beforeFirst();
            rs.next();
            return new SensorHistoryMapper().mapRow(rs, 0);
        } catch(SQLException e){
            e.printStackTrace();
            return null;
        }
    }
   }

