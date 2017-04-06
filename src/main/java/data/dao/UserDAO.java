package data.dao;


import data.db.DB;
import data.mapper.UserMapper;
import data.models.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserDAO implements IDAO<User> {
    @Override
    public void update(User user) {
String sql = "UPDATE auth_user SET password=?,last_login=?,is_superuser=?,username=?,first_name=?,last_name=?,email=?,is_staff=?,is_active=?,date_joined=? WHERE id=?";
    	
        try {
            Connection connection = DB.getConnection();
            PreparedStatement st = connection.prepareStatement(sql);
            st.setString(1, user.getPassword());
            st.setTimestamp(2, user.getLast_login());
            st.setInt(3, user.getSuperuser() ? 1 : 0);
            st.setString(4, user.getUsername());
            st.setString(5, user.getFirstname());
            st.setString(6, user.getLastname());
            st.setString(7, user.getEmail());
            st.setInt(8, user.getStaff() ? 1 : 0);
            st.setInt(9, user.getActive() ? 1 : 0);
            st.setTimestamp(10, user.getDate_joined());
            st.setInt(11, user.getId());
        
            st.executeUpdate();
            connection.close(); 
        } catch(SQLException e){
            e.printStackTrace();
        }
    }
    

    @Override
    public void delete(User user) {
    	String sql = "DELETE FROM auth_user WHERE id=?";
        try {
            Connection connection = DB.getConnection();
            PreparedStatement st = connection.prepareStatement(sql);
            st.setInt(1, user.getId());
            st.executeUpdate();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    @Override
    public int create(User user) {
    	String sql = "INSERT INTO auth_user (password,last_login,is_superuser,username,first_name,last_name,email,is_staff,is_active,date_joined) VALUES(?,?,?,?,?,?,?,?,?,?)";
        int lastId = -1;
        try {
            Connection connection = DB.getConnection();
            PreparedStatement st = connection.prepareStatement(sql);
            st.setString(1, user.getPassword());
            st.setTimestamp(2, user.getLast_login());
            st.setInt(3, user.getSuperuser() ? 1 : 0);
            st.setString(4, user.getUsername());
            st.setString(5, user.getFirstname());
            st.setString(6, user.getLastname());
            st.setString(7, user.getEmail());
            st.setInt(8, user.getStaff() ? 1 : 0);
            st.setInt(9, user.getActive() ? 1 : 0);
            st.setTimestamp(10, user.getDate_joined());
            
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
    public List<User> listAll() {
    	List<User> userList = new ArrayList<>();
        String sql = "SELECT * FROM auth_user";
        try {
            Connection connection = DB.getConnection();
            ResultSet rs = connection.createStatement().executeQuery(sql);
            rs.beforeFirst();
            UserMapper mapper = new UserMapper();
            while(rs.next()){
            	userList.add(mapper.mapRow(rs, rs.getRow()));
            }
            connection.close();
            return userList;
        } catch(SQLException e){
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public User getByID(int id) {
    	String sql = "SELECT * FROM auth_user WHERE id=" + String.valueOf(id);
        try {
            Connection connection = DB.getConnection();
            ResultSet rs = connection.createStatement().executeQuery(sql);
            rs.beforeFirst();
            rs.next();
            return new UserMapper().mapRow(rs, 0);
        } catch(SQLException e){
            e.printStackTrace();
            return null;
        }
    }
   }

