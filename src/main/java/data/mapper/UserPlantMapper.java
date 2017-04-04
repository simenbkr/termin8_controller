package data.mapper;

import data.models.UserPlant;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UserPlantMapper implements RowMapper<UserPlant> {
    @Override
    public UserPlant mapRow(ResultSet rs, int rowNum) throws SQLException {
        //TODO
    }
}
