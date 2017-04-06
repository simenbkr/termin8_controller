package data.mapper;

import data.models.PlantType;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class PlantTypeMapper implements RowMapper<PlantType> {
    @Override
    public PlantType mapRow(ResultSet rs, int rowNum) throws SQLException {
    	return new PlantType(rs.getInt("id"), rs.getString("name"));
    }
}
