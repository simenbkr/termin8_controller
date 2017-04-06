package data.mapper;

import data.models.User;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UserMapper implements RowMapper<User> {
    @Override
    public User mapRow(ResultSet rs, int rowNum) throws SQLException {
    	return new User(rs.getInt("id"), rs.getString("email"), rs.getString("password"), rs.getTimestamp("last_login"), 
    			toBool(rs.getInt("is_superuser")), rs.getString("username"), rs.getString("first_name"),
    			rs.getString("last_name"),toBool(rs.getInt("is_staff")),
    			toBool(rs.getInt("is_active")), rs.getTimestamp("date_joined"));
    }
    
    private boolean toBool(int i){
    	return i == 1;
    }
}
