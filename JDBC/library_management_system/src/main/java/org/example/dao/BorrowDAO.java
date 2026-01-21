package org.example.dao;

import org.example.config.DatabaseConfig;
import org.example.model.BorrowRecord;

import java.sql.*;
import java.time.LocalDate;

public class BorrowDAO {

    // borrow a book
    public void borrowBook(BorrowRecord record) {
        String query = "INSERT INTO borrowed_books (book_id, member_id, borrow_date, due_date, status) " +
                "VALUES (?,?,?,?,?)";
        try (Connection connection = DatabaseConfig.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setInt(1, record.getBookId());
            preparedStatement.setInt(2, record.getMemberId());
            preparedStatement.setDate(3, Date.valueOf(record.getBorrowDate()));
            preparedStatement.setDate(4, Date.valueOf(record.getDueDate()));
            preparedStatement.setString(5, record.getStatus());

            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
                if (generatedKeys.next()) {
                    record.setId(generatedKeys.getInt(1));
                }
                System.out.println("Book borrow successfully");
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error borrowing book: " + e.getMessage());
        }
    }

    // return a book
    public void returnBook(int borrowId, LocalDate returnDate, double lateFee) {
        String query = "UPDATE borrowed_books SET return_date = ?, late_fee = ?, status = 'returned' WHERE id = ?";
        try (Connection connection = DatabaseConfig.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setDate(1, Date.valueOf(returnDate));
            preparedStatement.setDouble(2, lateFee);
            preparedStatement.setInt(3, borrowId);

            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Book returned successfully");
                if (lateFee > 0) {
                    System.out.println("Late fee : " + lateFee);
                }
            } else {
                throw new RuntimeException("Borrow record not found...");
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error returning book: " + e.getMessage());
        }
    }

    // get borrow record by id
    public BorrowRecord getBorrowRecordById(int id){
        String query = "SELECT bb.*, b.title as book.title, "+
                "CONCAT(m.first_name"
    }

}
