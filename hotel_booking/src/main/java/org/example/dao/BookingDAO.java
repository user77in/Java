package org.example.dao;

import org.example.config.DatabaseConfig;
import org.example.model.Booking;

import javax.xml.crypto.Data;
import java.awt.print.Book;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BookingDAO {

    // create booking (transactional)
    public int createBooking(Booking booking) throws Exception {
        Connection connection = null;
        try {
            connection = DatabaseConfig.getConnection();
            connection.setAutoCommit(false);
            int bookingId = insertBooking(connection, booking);
            RoomDAO roomDAO = new RoomDAO();
            roomDAO.updateStatus(connection, booking.getRoomId(), "BOOKED");
            connection.commit();
            return bookingId;
        } catch (Exception e) {
            if (connection != null) {
                connection.rollback();
            }
            throw new SQLException(e);
        } finally {
            if (connection != null) {
                connection.close();
            }
        }

    }

    // helper method of create booking
    private int insertBooking(Connection connection, Booking booking) throws SQLException {
        String query = "insert into bookings (guest_id,room_id,check_in,check_out,status) values(?,?,?,?,?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setInt(1, booking.getGuestId());
            preparedStatement.setInt(2, booking.getRoomId());
            preparedStatement.setDate(3, Date.valueOf(booking.getCheckIn()));
            preparedStatement.setDate(4, Date.valueOf(booking.getCheckout()));
            preparedStatement.setString(5, booking.getStatus());

            preparedStatement.executeUpdate();
            ResultSet resultSet = preparedStatement.getGeneratedKeys();
            if (resultSet.next()) {
                return resultSet.getInt(1);
            }
        }
        throw new SQLException("Booking insertion failed....");
    }

    public Booking findBookingById(int id) throws Exception {
        String query = "select * form bookings where id = ?";
        try (Connection connection = DatabaseConfig.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return mapRow(resultSet);
            }
            return null;
        }
    }


    // update booking status
    public void updateStatus(Connection connection, int bookingId, String status) throws Exception {
        String query = "update bookings set status = ? where id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, bookingId);
            preparedStatement.setString(2, status);
            if (preparedStatement.executeUpdate() == 0) {
                throw new SQLException("Booking not found...");
            }
        }
    }

    public List<Booking> findByGuestId(int id) throws Exception {
        String query = "select * from bookings where guest_id = ?";
        List<Booking> bookings = new ArrayList<>();
        try (Connection connection = DatabaseConfig.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                bookings.add(mapRow(resultSet));
            }
        }
        return bookings;
    }

    // cancel booking (transactional)
    public void cancelBooking(int bookingId) throws Exception {
        Connection connection = null;
        try {
            connection = DatabaseConfig.getConnection();
            connection.setAutoCommit(false);
            Booking booking = findBookingById(bookingId);
            if (booking == null) {
                throw new SQLException("Booking not found");
            }
            updateStatus(connection, bookingId, "CANCELLED");
            RoomDAO roomDAO = new RoomDAO();
            roomDAO.updateStatus(connection, booking.getRoomId(), "AVAILABLE");
            connection.commit();
        } catch (Exception e) {
            if (connection != null) connection.rollback();
            throw new SQLException("Cancel booking failed" + e.getMessage());
        } finally {
            if (connection != null) connection.close();
        }
    }
    public Booking findBookingById(Connection connection, int id) throws Exception {
        String query = "select * form bookings where id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return mapRow(resultSet);
            }
            return null;
        }
    }


    private Booking mapRow(ResultSet resultSet) throws SQLException {
        Booking booking = new Booking();
        booking.setId(resultSet.getInt("id"));
        booking.setGuestId(resultSet.getInt("guest_id"));
        booking.setRoomId(resultSet.getInt("room_id"));
        booking.setCheckIn(resultSet.getDate("check_in").toLocalDate());
        booking.setCheckout(resultSet.getDate("check_out").toLocalDate());
        booking.setStatus(resultSet.getString("status"));
        return booking;
    }

}
