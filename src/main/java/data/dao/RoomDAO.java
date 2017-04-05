package data.dao;

import data.mapper.RoomMapper;
import data.models.Room;
import data.db.DB;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class RoomDAO implements IDAO<Room> {
    @Override
    public void update(Room room) {
        String sql = "UPDATE termin8_django_room SET name=? WHERE id=?";
        try {
            Connection connection = DB.getConnection();
            PreparedStatement st = connection.prepareStatement(sql);
            st.setString(1, room.getName());
            st.setInt(2, room.getId());
            st.executeUpdate();
            connection.close(); 
        } catch(SQLException e){
            e.printStackTrace();
        }
    }

    @Override
    public void delete(Room room) {
        String sql = "DELETE FROM termin8_django_room WHERE id=?";
        try {
            Connection connection = DB.getConnection();
            PreparedStatement st = connection.prepareStatement(sql);
            st.setInt(1, room.getId());
            st.executeUpdate();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int create(Room room) {
        String sql = "INSERT INTO termin8_django_room (navn) VALUES(?)";
        int lastId = -1;
        try {
            Connection connection = DB.getConnection();
            PreparedStatement st = connection.prepareStatement(sql);
            st.setString(1, room.getName());
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
    public List<Room> listAll() {
        List<Room> roomList = new ArrayList<>();
        String sql = "SELECT * FROM termin8_django_room";
        try {
            Connection connection = DB.getConnection();
            ResultSet rs = connection.createStatement().executeQuery(sql);
            rs.beforeFirst();
            RoomMapper mapper = new RoomMapper();
            while(rs.next()){
                roomList.add(mapper.mapRow(rs, rs.getRow()));
            }
            connection.close();
            return roomList;
        } catch(SQLException e){
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public Room getByID(int id) {
        String sql = "SELECT * FROM termin8_django_room WHERE id=" + String.valueOf(id);
        try {
            Connection connection = DB.getConnection();
            ResultSet rs = connection.createStatement().executeQuery(sql);
            rs.beforeFirst();
            rs.next();
            return new RoomMapper().mapRow(rs, 0);
        } catch(SQLException e){
            e.printStackTrace();
            return null;
        }
    }
}
