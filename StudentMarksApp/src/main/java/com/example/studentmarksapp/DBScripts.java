package com.example.studentmarksapp;

import java.sql.*;

public class DBScripts {
    public Connection ConnectDB() {
        String url = "jdbc:oracle:thin:@//oracle.glos.ac.uk:1521/orclpdb.chelt.local";
        String username = "s4101382";
        String password = "s4101382!";
        try {
            return DriverManager.getConnection(url, username, password);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean checkIfTableExists() {
        String checkTableSQL = "SELECT * FROM Users";
        try (Connection connection = ConnectDB();
             Statement statement = connection.createStatement()) {
            statement.execute(checkTableSQL);
            return true;
        } catch (SQLException e) {
            return false;
        }
    }

    public void createTable() throws SQLException {
        // SQL statement to create a table
        String createTableSQL = "CREATE TABLE Users ("
                + "user_id NUMBER(5) PRIMARY KEY NOT NULL, "
                + "First_name VARCHAR(20), "
                + "Second_name VARCHAR(20), "
                + "DOB VARCHAR(15) NOT NULL, "
                + "Password VARCHAR(20) NOT NULL, "
                + "Enrolled NUMBER(1)"
                + ")";

        String createSequenceSQL = "CREATE SEQUENCE user_seq START WITH 1 INCREMENT BY 1";
        String createTriggerSQL = "CREATE OR REPLACE TRIGGER user_trigger "
                + "BEFORE INSERT ON Users "
                + "FOR EACH ROW "
                + "BEGIN "
                + "SELECT user_seq.NEXTVAL INTO :new.user_id FROM dual; "
                + "END;";

        try (Connection connection = ConnectDB();
             Statement statement = connection.createStatement()) {
            statement.execute(createTableSQL);
            statement.execute(createSequenceSQL);
            statement.execute(createTriggerSQL);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public String checkUserType(String username) {
        String checkUserTypeSQL = "SELECT USER_ID FROM Users WHERE FIRST_NAME = ?";
        try (Connection connection = ConnectDB();
             PreparedStatement preparedStatement = connection.prepareStatement(checkUserTypeSQL)) {
            preparedStatement.setString(1, username);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getString("USER_ID");
            } else {
                return null;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}