package com.example.studentmarksapp;

import java.io.*;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static com.mongodb.client.model.Filters.eq;

enum DBType {
    MONGO, SQL
}

enum UserType {
    STUDENT, TEACHER, ADMIN
}

@WebServlet(name = "loginServlet", value = "/login-servlet")
public class LoginServlet extends HttpServlet {
    public DBType dbType = null;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED, "GET method is not supported.");
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html");
        String username = request.getParameter("user-id");
        String password = request.getParameter("password");
        //String dbType = request.getParameter("toggle-switch");
        try {
            if (checkLogin(username, password)) {
                PrintWriter out = response.getWriter();

                request.setAttribute("userType", getUserType(username));
                request.getRequestDispatcher("/index.jsp").forward(request, response);
            } else {
                PrintWriter out = response.getWriter();
                out.println("not logged in");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (ServletException e) {
            throw new RuntimeException(e);
        }
    }

    private UserType getUserType(String username) {
        UserType userType;
        DBScripts db = new DBScripts();
        String userID = db.checkUserType(username);
        int type = userID.charAt(0);
        if (type == 49) {
            userType = UserType.STUDENT;
        } else if (type == 50) {
            userType = UserType.TEACHER;
        } else {
            userType = UserType.ADMIN;
        }
        return userType;
    }

    public boolean checkLogin(String username, String password) throws SQLException {
        DBType dbType = DBType.SQL;
        if (dbType == DBType.MONGO) {
            MongoClient mongo = MongoClients.create();
            MongoDatabase db = mongo.getDatabase("StudentMarks");
            MongoCollection login = db.getCollection("Users");
            var check = login.find(eq("user_id", 1)).first();
            return check != null;
        } else {
            DBScripts db = new DBScripts();
            Connection connection = db.ConnectDB();
            String checkUserSQL = "SELECT * FROM USERS WHERE FIRST_NAME = ? AND PASSWORD = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(checkUserSQL);
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);
            ResultSet check = preparedStatement.executeQuery();
            return check.next();
        }
    }
}