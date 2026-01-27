package org.example;

import org.example.config.DatabaseConfig;
import org.example.dao.GuestDAO;
import org.example.model.Guest;

import java.sql.Connection;

public class Main {
    public static void main(String[] args) {
        try{
            Connection connection = DatabaseConfig.getConnection();
            if(!connection.isClosed()){
                System.out.println("Database connected successfully");
                GuestDAO guestDAO = new GuestDAO();
                guestDAO.deleteById(1);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}