package data.dao;


import data.db.DB;
import data.mapper.WateringHistoryMapper;
import data.models.WateringHistory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class WateringHistoryDAO implements IDAO<WateringHistory> {
    @Override
    public void update(WateringHistory wateringHistory) {
        	String sql = "UPDATE termin8_django_wateringhistory SET temp=?,moisture=?,time_watered=?,plant_id=? WHERE id=?";
        	
            try {
                Connection connection = DB.getConnection();
                PreparedStatement st = connection.prepareStatement(sql);
                st.setDouble(1, wateringHistory.getTemp());
                st.setDouble(2,wateringHistory.getMoisture());
                st.setTimestamp(3, wateringHistory.getTime_watered());
                st.setInt(4, wateringHistory.getPlant_id());
                st.setInt(5, wateringHistory.getId());
               
            
                st.executeUpdate();
                connection.close(); 
            } catch(SQLException e){
                e.printStackTrace();
            }
        }

    @Override
    public void delete(WateringHistory wateringHistory) {
    	String sql = "DELETE FROM termin8_django_wateringhistory WHERE id=?";
        try {
            Connection connection = DB.getConnection();
            PreparedStatement st = connection.prepareStatement(sql);
            st.setInt(1, wateringHistory.getId());
            st.executeUpdate();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    @Override
    public int create(WateringHistory wateringHistory) {
    	String sql = "INSERT INTO termin8_django_wateringhistory (temp,moisture,time_watered,plant_id) VALUES(?,?,?,?)";
        int lastId = -1;
        try {
            Connection connection = DB.getConnection();
            PreparedStatement st = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            st.setDouble(1, wateringHistory.getTemp());
            st.setDouble(2,wateringHistory.getMoisture());
            st.setTimestamp(3, wateringHistory.getTime_watered());
            st.setInt(4, wateringHistory.getPlant_id());
            
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
    public List<WateringHistory> listAll() {
    	List<WateringHistory> wateringHistoryList = new ArrayList<>();
        String sql = "SELECT * FROM termin8_django_wateringhistory";
        try {
            Connection connection = DB.getConnection();
            ResultSet rs = connection.createStatement().executeQuery(sql);
            rs.beforeFirst();
            WateringHistoryMapper mapper = new WateringHistoryMapper();
            while(rs.next()){
            	wateringHistoryList.add(mapper.mapRow(rs, rs.getRow()));
            }
            connection.close();
            return wateringHistoryList;
        } catch(SQLException e){
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public WateringHistory getByID(int id) {
        String sql = "SELECT * FROM termin8_django_wateringhistory WHERE id=" + String.valueOf(id);
        try {
            Connection connection = DB.getConnection();
            ResultSet rs = connection.createStatement().executeQuery(sql);
            rs.beforeFirst();
            rs.next();
            return new WateringHistoryMapper().mapRow(rs, 0);
        } catch(SQLException e){
            e.printStackTrace();
            return null;
        }
    }
   }

