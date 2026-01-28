package HotelManagementSystem;
import java.sql.*;
import java.util.*;

/**
 * DAOIMPL (Data Access Object Implementation)
 * This class handles all direct database interactions using JDBC.
 */
public class DAOIMPL implements DAO {
    // Database credentials
    private final String URL = "jdbc:mysql://localhost:3306/hotel_db";
    private final String USER = "root"; 
    private final String PASS = "Suhas@2004"; 

    // REMOVED: validateUser (Login no longer required)

    @Override
    public void addBooking(DTO dto) {
        String sql = "INSERT INTO reservations (guest_name, room_no, days, bill) VALUES (?,?,?,?)";
        try (Connection c = DriverManager.getConnection(URL, USER, PASS);
             PreparedStatement p = c.prepareStatement(sql)) {
            p.setString(1, dto.getGuestName());
            p.setInt(2, dto.getRoomNo());
            p.setInt(3, dto.getDays());
            p.setDouble(4, dto.getBill());
            p.executeUpdate();
        } catch (SQLException e) { e.printStackTrace(); }
    }

    @Override
    public List<DTO> getAllBookings() {
        List<DTO> list = new ArrayList<>();
        String sql = "SELECT * FROM reservations";
        try (Connection c = DriverManager.getConnection(URL, USER, PASS);
             Statement s = c.createStatement(); 
             ResultSet rs = s.executeQuery(sql)) {
            while (rs.next()) {
                list.add(new DTO(rs.getInt(1), rs.getString(2), rs.getInt(3), rs.getInt(4), rs.getDouble(5)));
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }

    @Override
    public List<DTO> searchByName(String name) {
        List<DTO> results = new ArrayList<>();
        String sql = "SELECT * FROM reservations WHERE guest_name LIKE ?";
        try (Connection c = DriverManager.getConnection(URL, USER, PASS);
             PreparedStatement p = c.prepareStatement(sql)) {
            p.setString(1, "%" + name + "%");
            ResultSet rs = p.executeQuery();
            while (rs.next()) {
                results.add(new DTO(rs.getInt(1), rs.getString(2), rs.getInt(3), rs.getInt(4), rs.getDouble(5)));
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return results;
    }

    @Override
    public double getRoomPrice(int roomNo) {
        String sql = "SELECT price FROM rooms WHERE room_no=?";
        try (Connection c = DriverManager.getConnection(URL, USER, PASS);
             PreparedStatement p = c.prepareStatement(sql)) {
            p.setInt(1, roomNo);
            ResultSet rs = p.executeQuery();
            if (rs.next()) return rs.getDouble("price");
        } catch (SQLException e) { e.printStackTrace(); }
        return -1;
    }

    @Override
    public void updateStatus(int roomNo, String status) {
        String sql = "UPDATE rooms SET status=? WHERE room_no=?";
        try (Connection c = DriverManager.getConnection(URL, USER, PASS);
             PreparedStatement p = c.prepareStatement(sql)) {
            p.setString(1, status);
            p.setInt(2, roomNo);
            p.executeUpdate();
        } catch (SQLException e) { e.printStackTrace(); }
    }

    @Override
    public DTO getBookingByRoom(int roomNo) {
        String sql = "SELECT * FROM reservations WHERE room_no=?";
        try (Connection c = DriverManager.getConnection(URL, USER, PASS);
             PreparedStatement p = c.prepareStatement(sql)) {
            p.setInt(1, roomNo);
            ResultSet rs = p.executeQuery();
            if (rs.next()) {
                return new DTO(rs.getInt(1), rs.getString(2), rs.getInt(3), rs.getInt(4), rs.getDouble(5));
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return null;
    }

    @Override
    public void deleteBooking(int roomNo) {
        String sql = "DELETE FROM reservations WHERE room_no=?";
        try (Connection c = DriverManager.getConnection(URL, USER, PASS);
             PreparedStatement p = c.prepareStatement(sql)) {
            p.setInt(1, roomNo);
            p.executeUpdate();
        } catch (SQLException e) { e.printStackTrace(); }
    }

    @Override
    public List<String> getRoomAvailability() {
        List<String> list = new ArrayList<>();
        // ORDER BY room_no is critical for the "Range" display logic in Service.java
        String sql = "SELECT room_no, room_type, status FROM rooms ORDER BY room_no ASC";
        try (Connection c = DriverManager.getConnection(URL, USER, PASS);
             Statement s = c.createStatement(); 
             ResultSet rs = s.executeQuery(sql)) {
            while (rs.next()) {
                list.add(String.format("Room %d [%-8s]: %s", 
                        rs.getInt(1), rs.getString(2).trim(), rs.getString(3)));
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }

    @Override
    public int getOccupiedCountByType(String type) {
        String sql = "SELECT COUNT(*) FROM rooms WHERE TRIM(room_type) = ? AND status = 'Occupied'";
        try (Connection c = DriverManager.getConnection(URL, USER, PASS);
             PreparedStatement p = c.prepareStatement(sql)) {
            p.setString(1, type.trim());
            ResultSet rs = p.executeQuery();
            if (rs.next()) return rs.getInt(1);
        } catch (SQLException e) { e.printStackTrace(); }
        return 0;
    }

    @Override
    public double calculateTotalRevenue() {
        String sql = "SELECT SUM(bill) FROM reservations";
        try (Connection c = DriverManager.getConnection(URL, USER, PASS);
             Statement s = c.createStatement(); 
             ResultSet rs = s.executeQuery(sql)) {
            if (rs.next()) return rs.getDouble(1);
        } catch (SQLException e) { e.printStackTrace(); }
        return 0.0;
    }

    @Override
    public int getFirstAvailableRoom(String type) {
        String sql = "SELECT room_no FROM rooms WHERE TRIM(room_type) = ? AND status = 'Available' ORDER BY room_no ASC LIMIT 1";
        try (Connection c = DriverManager.getConnection(URL, USER, PASS);
             PreparedStatement p = c.prepareStatement(sql)) {
            p.setString(1, type.trim());
            ResultSet rs = p.executeQuery();
            if (rs.next()) return rs.getInt("room_no");
        } catch (SQLException e) { e.printStackTrace(); }
        return -1;
    }

    @Override
    public DTO getGuestByRoom(int roomNo) {
        String sql = "SELECT * FROM reservations WHERE room_no = ?";
        try (Connection c = DriverManager.getConnection(URL, USER, PASS);
             PreparedStatement p = c.prepareStatement(sql)) {
            p.setInt(1, roomNo);
            ResultSet rs = p.executeQuery();
            if (rs.next()) {
                return new DTO(rs.getInt(1), rs.getString(2), rs.getInt(3), rs.getInt(4), rs.getDouble(5));
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return null;
    }
}