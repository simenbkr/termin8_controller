package data.mapper;

import data.models.SensorHistory;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;


public class SensorHistoryMapper implements RowMapper<SensorHistory> {
    @Override
    public SensorHistory mapRow(ResultSet rs, int rowNum) throws SQLException {
    	return new SensorHistory(rs.getInt("id"), rs.getFloat("temp"), rs.getFloat("moisture"),
    			rs.getInt("plant_id"), rs.getTimestamp("timestamp"));
    }
}
