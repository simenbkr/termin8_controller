package data.dao;

import data.db.DB;
import data.mapper.PlantTypeMapper;
import data.models.PlantType;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PlantTypeDAO implements IDAO<PlantType> {
    @Override
    public void update(PlantType plantType) {
    	String sql = "UPDATE termin8_django_planttype SET name=?,max_temp=?,min_temp=?,max_moisture=?,min_moisture=? WHERE id=?";
    	
        try {
            Connection connection = DB.getConnection();
            PreparedStatement st = connection.prepareStatement(sql);
            st.setString(1, plantType.getName());
            st.setFloat(2, plantType.getMax_temp());
            st.setFloat(3, plantType.getMin_temp());
            st.setFloat(4, plantType.getMax_moisture());
            st.setFloat(5, plantType.getMin_moisture());
            st.setInt(6, plantType.getId());
            st.executeUpdate();
            connection.close(); 
        } catch(SQLException e){
            e.printStackTrace();
        }
    	

    }

    @Override
    public void delete(PlantType plantType) {
    	String sql = "DELETE FROM termin8_django_planttype WHERE id=?";
        try {
            Connection connection = DB.getConnection();
            PreparedStatement st = connection.prepareStatement(sql);
            st.setInt(1, plantType.getId());
            st.executeUpdate();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int create(PlantType plantType) {
    	String sql = "INSERT INTO termin8_django_planttype (navn,max_temp,min_temp,max_moisture,min_moisture) VALUES(?,?,?,?,?)";
        int lastId = -1;
        try {
            Connection connection = DB.getConnection();
            PreparedStatement st = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            st.setString(1, plantType.getName());
            st.setFloat(2, plantType.getMax_temp());
            st.setFloat(3, plantType.getMin_temp());
            st.setFloat(4, plantType.getMax_moisture());
            st.setFloat(5, plantType.getMin_moisture());
            
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
    public List<PlantType> listAll() {
    	List<PlantType> plantTypeList = new ArrayList<>();
        String sql = "SELECT * FROM termin8_django_planttype";
        try {
            Connection connection = DB.getConnection();
            ResultSet rs = connection.createStatement().executeQuery(sql);
            rs.beforeFirst();
            PlantTypeMapper mapper = new PlantTypeMapper();
            while(rs.next()){
                plantTypeList.add(mapper.mapRow(rs, rs.getRow()));
            }
            connection.close();
            return plantTypeList;
        } catch(SQLException e){
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public PlantType getByID(int id) {
    	String sql = "SELECT * FROM termin8_django_planttype WHERE id=" + String.valueOf(id);
        try {
            Connection connection = DB.getConnection();
            ResultSet rs = connection.createStatement().executeQuery(sql);
            rs.beforeFirst();
            rs.next();
            PlantType type = new PlantTypeMapper().mapRow(rs, 0);
            connection.close();
            return type;
        } catch(SQLException e){
            e.printStackTrace();
            return null;
        }
    }
}
