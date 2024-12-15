package com.lab4.loginuit;

import java.security.NoSuchAlgorithmException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MySQLConnector {

    // Database connection details
    private static final String URL = "jdbc:mysql://10.0.2.2:3306/sinhvien";
    private static final String USER = "root";
    private static final String PASSWORD = "";

    // User table name and columns
    private static final String TABLE_USER = "user";
    private static final String COLUMN_USER_ID = "user_id";
    private static final String COLUMN_USER_NAME = "user_name";
    private static final String COLUMN_USER_EMAIL = "user_email";
    private static final String COLUMN_USER_PASSWORD = "user_password";

    public MySQLConnector() throws ClassNotFoundException {}

    public void addUser(User user) throws NoSuchAlgorithmException, ClassNotFoundException {
        String insertSQL = "INSERT INTO " + TABLE_USER + " (" + COLUMN_USER_NAME + ", "
                + COLUMN_USER_EMAIL + ", " + COLUMN_USER_PASSWORD + ") VALUES (?, ?, ?);";
        Class.forName("com.mysql.jdbc.Driver");
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement pstmt = connection.prepareStatement(insertSQL)) {
            pstmt.setString(1, user.getName());
            pstmt.setString(2, user.getEmail());
            pstmt.setString(3, user.getPassword());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<User> getAllUser() throws ClassNotFoundException {
        List<User> userList = new ArrayList<>();
        String selectSQL = "SELECT * FROM " + TABLE_USER + " ORDER BY " + COLUMN_USER_NAME + " ASC;";
        Class.forName("com.mysql.jdbc.Driver");
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(selectSQL)) {

            while (rs.next()) {
                User user = new User();
                user.setId(rs.getInt(COLUMN_USER_ID));
                user.setName(rs.getString(COLUMN_USER_NAME));
                user.setEmail(rs.getString(COLUMN_USER_EMAIL));
                user.setPassword(rs.getString(COLUMN_USER_PASSWORD));
                userList.add(user);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return userList;
    }

    public void updateUser(User user) throws ClassNotFoundException {
        String updateSQL = "UPDATE " + TABLE_USER + " SET " + COLUMN_USER_NAME + " = ?, "
                + COLUMN_USER_EMAIL + " = ?, " + COLUMN_USER_PASSWORD + " = ? WHERE " + COLUMN_USER_ID + " = ?;";
        Class.forName("com.mysql.jdbc.Driver");
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement pstmt = connection.prepareStatement(updateSQL)) {
            pstmt.setString(1, user.getName());
            pstmt.setString(2, user.getEmail());
            pstmt.setString(3, user.getPassword());
            pstmt.setInt(4, user.getId());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteUser(User user) throws ClassNotFoundException {
        String deleteSQL = "DELETE FROM " + TABLE_USER + " WHERE " + COLUMN_USER_ID + " = ?;";
        Class.forName("com.mysql.jdbc.Driver");
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement pstmt = connection.prepareStatement(deleteSQL)) {
            pstmt.setInt(1, user.getId());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean checkUser(String email) throws ClassNotFoundException {
        String selectSQL = "SELECT 1 FROM " + TABLE_USER + " WHERE " + COLUMN_USER_EMAIL + " = ?;";
        Class.forName("com.mysql.jdbc.Driver");
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement pstmt = connection.prepareStatement(selectSQL)) {
            pstmt.setString(1, email);
            try (ResultSet rs = pstmt.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean checkUser(String username, String password) throws ClassNotFoundException {
        String selectSQL = "SELECT 1 FROM " + TABLE_USER + " WHERE " + COLUMN_USER_NAME + " = ? AND " + COLUMN_USER_PASSWORD + " = ?;";
        Class.forName("com.mysql.jdbc.Driver");
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement pstmt = connection.prepareStatement(selectSQL)) {
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            try (ResultSet rs = pstmt.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public String getEmail(String username) throws ClassNotFoundException {
        String selectSQL = "SELECT " + COLUMN_USER_EMAIL + " FROM " + TABLE_USER + " WHERE " + COLUMN_USER_NAME + " = ?;";
        Class.forName("com.mysql.jdbc.Driver");
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement pstmt = connection.prepareStatement(selectSQL)) {
            pstmt.setString(1, username);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getString(COLUMN_USER_EMAIL);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
