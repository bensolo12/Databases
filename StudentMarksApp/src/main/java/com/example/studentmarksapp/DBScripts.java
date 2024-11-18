package com.example.studentmarksapp;

import java.sql.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class DBScripts {
    public Connection ConnectDB() {
        createOracleSchema();
        String url = "jdbc:oracle:thin:@//oracle.glos.ac.uk:1521/orclpdb.chelt.local";
        String username = "s4101382";
        String password = "s4101382!";
        try {
            return DriverManager.getConnection(url, username, password);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean checkIfTableExists(String tableName) {
        String checkTableSQL = "SELECT * FROM " + tableName + " WHERE ROWNUM = 1";
        try (Connection connection = ConnectDB();
             PreparedStatement statement = connection.prepareStatement(checkTableSQL)) {
            ResultSet resultSet = statement.executeQuery();
            return resultSet.next();
        } catch (SQLException e) {
            return false;
        }
    }

    public void createOracleSchema() {
        String url = "jdbc:oracle:thin:@//oracle.glos.ac.uk:1521/orclpdb.chelt.local";
        String username = "s4101382";
        String password = "s4101382!";
        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            Statement statement = connection.createStatement();

            // Create "Users" table
            String createUsersTable = """
            CREATE TABLE Users (
                user_id INT NOT NULL PRIMARY KEY,
                first_name VARCHAR2(50),
                second_name VARCHAR2(50),
                DOB DATE NOT NULL,
                password VARCHAR2(50) NOT NULL,
                enrolled CHAR(1) CHECK (enrolled IN ('Y', 'N'))
            )
        """;
            statement.execute(createUsersTable);

            // Create "Course" table
            String createCourseTable = """
            CREATE TABLE Course (
                course_id INT NOT NULL PRIMARY KEY,
                course_name VARCHAR2(100)
            )
        """;
            statement.execute(createCourseTable);

            // Create "Modules" table
            String createModulesTable = """
            CREATE TABLE Modules (
                module_id INT NOT NULL PRIMARY KEY,
                module_name VARCHAR2(100),
                module_dateTime DATE,
                module_teacher INT,
                course_id INT,
                module_CATS INT,
                FOREIGN KEY (module_teacher) REFERENCES Users(user_id),
                FOREIGN KEY (course_id) REFERENCES Course(course_id)
            )
        """;
            statement.execute(createModulesTable);

            // Create "Students_Modules" table
            String createStudentsModulesTable = """
            CREATE TABLE Students_Modules (
                user_id INT NOT NULL,
                module_id INT NOT NULL,
                module_year DATE,
                student_result INT,
                PRIMARY KEY (user_id, module_id),
                FOREIGN KEY (user_id) REFERENCES Users(user_id),
                FOREIGN KEY (module_id) REFERENCES Modules(module_id)
            )
        """;
            statement.execute(createStudentsModulesTable);

            System.out.println("Database schema created successfully!");

        } catch (Exception e) {
            e.printStackTrace();
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