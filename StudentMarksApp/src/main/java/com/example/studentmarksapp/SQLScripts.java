package com.example.studentmarksapp;

import java.sql.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.time.Year;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.google.gson.Gson;

import static java.time.Year.now;

public class SQLScripts {

    Gson gson = new Gson();

    // Connect to the Oracle database
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

    // Get all courses from the database
    public ArrayList<Integer> getCourses() {
        ArrayList<Integer> courses = new ArrayList<>();
        String getCoursesString = "SELECT COURSE_ID FROM COURSE";
        try (Connection connection = ConnectDB();
             PreparedStatement statement = connection.prepareStatement(getCoursesString)) {
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                courses.add(resultSet.getInt("COURSE_ID"));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return courses;
    }

    // Get all modules from the database
    public ArrayList<String> getModules() {
        Gson gson = new Gson();
        ArrayList<String> modules = new ArrayList<>();
        String getModulesString = "SELECT MODULES.*, COURSE.COURSE_NAME FROM MODULES JOIN COURSE ON MODULES.COURSE_ID = COURSE.COURSE_ID";
        try (Connection connection = ConnectDB();
             PreparedStatement statement = connection.prepareStatement(getModulesString)) {
            ResultSet resultSet = statement.executeQuery();
            ResultSetMetaData metaData = resultSet.getMetaData();
            int columnCount = metaData.getColumnCount();
            //Converts the data from the result set into json readable by the front end
            while (resultSet.next()) {
                Map<String, Object> row = new HashMap<>();
                for (int i = 1; i <= columnCount; i++) {
                    row.put(metaData.getColumnName(i).toLowerCase(), resultSet.getObject(i));
                }
                // Convert the map to JSON and add it to the list
                modules.add(gson.toJson(row));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return modules;
    }

    // Get all modules for a specific course
    public ArrayList<String> getModules(int courseID) {
        Gson gson = new Gson();
        ArrayList<String> modules = new ArrayList<>();
        String getModulesString = "SELECT MODULES.* FROM MODULES WHERE COURSE_ID = ?";
        try (Connection connection = ConnectDB();
             PreparedStatement statement = connection.prepareStatement(getModulesString)) {
            statement.setInt(1, courseID);
            ResultSet resultSet = statement.executeQuery();
            ResultSetMetaData metaData = resultSet.getMetaData();
            int columnCount = metaData.getColumnCount();
            //Converts the data from the result set into json readable by the front end
            while (resultSet.next()) {
                Map<String, Object> row = new HashMap<>();
                for (int i = 1; i <= columnCount; i++) {
                    row.put(metaData.getColumnName(i).toLowerCase(), resultSet.getObject(i));
                }
                // Convert the map to JSON and add it to the list
                modules.add(gson.toJson(row));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return modules;
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
                optional CHAR(1) CHECK (optional IN ('Y', 'N')),
                FOREIGN KEY (module_teacher) REFERENCES Users(user_id),
                FOREIGN KEY (course_id) REFERENCES Course(course_id)
            )
        """;
            statement.execute(createModulesTable);

            // Create "Students_Modules" table
            String createStudentsModulesTable = """
            CREATE TABLE Students_Modules (
                students INT NOT NULL,
                module_id INT NOT NULL,
                module_year INT,
                student_result INT,
                PRIMARY KEY (students, module_id),
                FOREIGN KEY (students) REFERENCES Users(user_id),
                FOREIGN KEY (module_id) REFERENCES Modules(module_id)
            )
        """;
            statement.execute(createStudentsModulesTable);
            String createStudentsCoursesTable = """
            CREATE TABLE Students_Courses (
                user_id INT NOT NULL,
                course_id INT NOT NULL,
                course_year INT,
                student_result INT,
                PRIMARY KEY (user_id, course_id),
                FOREIGN KEY (user_id) REFERENCES Users(user_id),
                FOREIGN KEY (course_id) REFERENCES Course(course_id)
            )
        """;
            statement.execute(createStudentsCoursesTable);

            System.out.println("Database schema created successfully!");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Check what type of user is logged in
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

    // Get course ID from course name
    public int getCourseIDFromName(String course) {
        String courseIDSQL = "SELECT COURSE_ID FROM COURSE WHERE COURSE_NAME = ?";
        try (Connection connection = ConnectDB();
             PreparedStatement preparedStatement = connection.prepareStatement(courseIDSQL)) {
            preparedStatement.setString(1, course);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return Integer.parseInt(resultSet.getString("COURSE_ID"));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return 0;
    }
    // Get course name from course ID
    public ArrayList<String> getCourseNamesFromID(ArrayList<Integer> courseIDs){
        ArrayList<String> courseNames = new ArrayList<>();
        String courseNameSQL = "SELECT COURSE_NAME FROM COURSE WHERE COURSE_ID = ?";
        try (Connection connection = ConnectDB();
             PreparedStatement preparedStatement = connection.prepareStatement(courseNameSQL)) {
            for (int courseID : courseIDs) {
                preparedStatement.setInt(1, courseID);
                ResultSet resultSet = preparedStatement.executeQuery();
                if (resultSet.next()) {
                    courseNames.add(gson.toJson(resultSet.getString("COURSE_NAME")));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return courseNames;
    }

    // Check if a student is enrolled in a course
    public boolean checkStudentIsEnrolled(String userID) {
        String checkEnrolledSQL = "SELECT ENROLLED FROM Users WHERE USER_ID = ?";
        try (Connection connection = ConnectDB();
             PreparedStatement preparedStatement = connection.prepareStatement(checkEnrolledSQL)) {
            preparedStatement.setInt(1, Integer.parseInt(userID));
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getString("ENROLLED").equals("Y");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return false;
    }

    // Enrol a student in a course
    public void enrolStudent(String course, String userID) {
        // Enrol the student in the course
        int courseID = getCourseIDFromName(course);
        String enrolStudentSQL = "INSERT INTO Students_Courses (USER_ID, COURSE_ID, COURSE_YEAR) VALUES (?, ?, ?)";
        // Update the user's "enrolled" status
        String updateEnrolledStatusSQL = "UPDATE Users SET ENROLLED = 'Y' WHERE USER_ID = ?";
        int year = Year.now().getValue();
        try (Connection connection = ConnectDB();
             PreparedStatement preparedStatement = connection.prepareStatement(enrolStudentSQL)) {
            PreparedStatement updateEnrolledStatusStatement = connection.prepareStatement(updateEnrolledStatusSQL);
            preparedStatement.setInt(1, Integer.parseInt(userID));
            preparedStatement.setInt(2, courseID);
            preparedStatement.setInt(3, year);
            preparedStatement.executeUpdate();
            updateEnrolledStatusStatement.setInt(1, Integer.parseInt(userID));
            updateEnrolledStatusStatement.executeQuery();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    // Get the course ID of a user
    public int getUserCourseID(int userID) {
        String getUserCourseSQL = "SELECT COURSE_ID FROM COURSE WHERE COURSE_ID = (SELECT COURSE_ID FROM STUDENTS_COURSES WHERE USER_ID = ?)";
        try (Connection connection = ConnectDB();
             PreparedStatement preparedStatement = connection.prepareStatement(getUserCourseSQL)) {
            preparedStatement.setInt(1, (int) userID);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt("COURSE_ID");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return 0;
    }

    // Add a student module
    public void addStudentModule(int studentID, int moduleID) {
        String addStudentModuleSQL = "INSERT INTO Students_Modules (STUDENTS, MODULE_ID, MODULE_YEAR) VALUES (?, ?, ?)";
        int year = Year.now().getValue();
        try (Connection connection = ConnectDB();
             PreparedStatement preparedStatement = connection.prepareStatement(addStudentModuleSQL)) {
            preparedStatement.setInt(1, studentID);
            preparedStatement.setInt(2, moduleID);
            preparedStatement.setInt(3, year);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    // Get all modules a staff member is teaching
    public ArrayList<String> getStaffModules(int staffID) {
        Gson gson = new Gson();
        ArrayList<String> modules = new ArrayList<>();
        String getModulesString = """
        SELECT MODULES.MODULE_ID, MODULE_NAME, MODULE_TEACHER, STUDENTS_MODULES.STUDENTS 
        FROM MODULES 
        LEFT JOIN STUDENTS_MODULES ON MODULES.MODULE_ID = STUDENTS_MODULES.MODULE_ID 
        WHERE MODULE_TEACHER = ?
    """;
        try (Connection connection = ConnectDB();
             PreparedStatement statement = connection.prepareStatement(getModulesString)) {
            statement.setInt(1, staffID);
            ResultSet resultSet = statement.executeQuery();
            Map<Integer, Map<String, Object>> moduleMap = new HashMap<>();
            while (resultSet.next()) {
                int moduleId = resultSet.getInt("MODULE_ID");
                if (!moduleMap.containsKey(moduleId)) {
                    Map<String, Object> module = new HashMap<>();
                    module.put("module_id", moduleId);
                    module.put("module_name", resultSet.getString("MODULE_NAME"));
                    module.put("module_teacher", resultSet.getInt("MODULE_TEACHER"));
                    module.put("students", new ArrayList<Integer>());
                    moduleMap.put(moduleId, module);
                }
                int studentId = resultSet.getInt("STUDENTS");
                if (studentId != 0) {
                    ((ArrayList<Integer>) moduleMap.get(moduleId).get("students")).add(studentId);
                }
            }
            for (Map<String, Object> module : moduleMap.values()) {
                modules.add(gson.toJson(module));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return modules;
    }

    // Add a student result
    public void addStudentResult(int studentID, int moduleID, int result){
        String addResultString = "UPDATE STUDENTS_MODULES SET STUDENT_RESULT = ? WHERE STUDENTS = ? AND MODULE_ID = ?";
        try (Connection connection = ConnectDB();
             PreparedStatement statement = connection.prepareStatement(addResultString)) {
            statement.setInt(1, result);
            statement.setInt(2, studentID);
            statement.setInt(3, moduleID);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    // Get all students
    public ArrayList<String> getAllStudents() {
        Gson gson = new Gson();
        ArrayList<String> students = new ArrayList<>();
        String getStudentsString = "SELECT * FROM USERS WHERE ENROLLED = 'Y'";
        try (Connection connection = ConnectDB();
             PreparedStatement statement = connection.prepareStatement(getStudentsString)) {
            ResultSet resultSet = statement.executeQuery();
            ResultSetMetaData metaData = resultSet.getMetaData();
            int columnCount = metaData.getColumnCount();
            while (resultSet.next()) {
                Map<String, Object> row = new HashMap<>();
                for (int i = 1; i <= columnCount; i++) {
                    row.put(metaData.getColumnName(i).toLowerCase(), resultSet.getObject(i));
                }
                students.add(gson.toJson(row));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return students;
    }

    //Get all module grades
    public ArrayList<String> getModuleGrades() {
        Gson gson = new Gson();
        ArrayList<String> moduleGrades = new ArrayList<>();
        String getModuleGradesString = "SELECT * FROM STUDENTS_MODULES";
        try (Connection connection = ConnectDB();
             PreparedStatement statement = connection.prepareStatement(getModuleGradesString)) {
            ResultSet resultSet = statement.executeQuery();
            ResultSetMetaData metaData = resultSet.getMetaData();
            int columnCount = metaData.getColumnCount();
            while (resultSet.next()) {
                Map<String, Object> row = new HashMap<>();
                for (int i = 1; i <= columnCount; i++) {
                    row.put(metaData.getColumnName(i).toLowerCase(), resultSet.getObject(i));
                }
                moduleGrades.add(gson.toJson(row));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return moduleGrades;
    }

    // Calculate the number of passes for a course
    public int calculatePasses(int courseID) {
        int passCount = 0;
        String getModulesString = "SELECT MODULE_ID FROM MODULES WHERE COURSE_ID = ?";
        try (Connection connection = ConnectDB();
             PreparedStatement statement = connection.prepareStatement(getModulesString)) {
            statement.setInt(1, courseID);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                int moduleID = resultSet.getInt("MODULE_ID");
                String getResultsString = "SELECT STUDENT_RESULT FROM STUDENTS_MODULES WHERE MODULE_ID = ?";
                try (PreparedStatement statement2 = connection.prepareStatement(getResultsString)) {
                    statement2.setInt(1, moduleID);
                    ResultSet resultSet2 = statement2.executeQuery();
                    while (resultSet2.next()) {
                        if (resultSet2.getInt("STUDENT_RESULT") >= 40) {
                            passCount++;
                        }
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return passCount;
    }
}