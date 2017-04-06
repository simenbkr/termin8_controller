package data.mapper;

import data.models.Plant;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class PlantMapper implements RowMapper<Plant> {

    @Override
    public Plant mapRow(ResultSet rs, int rowNum) throws SQLException {
    	return new Plant(rs.getInt("id"), rs.getString("name"), rs.getFloat("max_temp"),
    			rs.getFloat("min_temp"), rs.getFloat("min_moisture"), rs.getFloat("max_moisture"), rs.getTimestamp("last_watered"),
    			toBool(rs.getInt("automatic_water")),rs.getInt("room_id"), rs.getInt("plant_type_id"));
    }
    
    private boolean toBool(int i){
    	return i == 1;
    }
}
