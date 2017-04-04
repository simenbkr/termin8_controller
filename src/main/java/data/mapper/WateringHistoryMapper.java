package data.mapper;

import data.models.WateringHistory;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class WateringHistoryMapper implements RowMapper<WateringHistory> {
    @Override
    public WateringHistory mapRow(ResultSet rs, int rowNum) throws SQLException {
        //TODO
    }
}
