package data.mapper;

import data.models.Plant;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class PlantMapper implements RowMapper<Plant> {

    @Override
    public Plant mapRow(ResultSet rs, int rowNum) throws SQLException {
        //TODO
    }
}
