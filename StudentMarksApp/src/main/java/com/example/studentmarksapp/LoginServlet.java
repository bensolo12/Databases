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
import org.bson.Document;

import static com.mongodb.client.model.Filters.eq;

//The different types of databases to switch more easily
enum DBType {
    MONGO, SQL
}

//The different types of users
enum UserType {
    STUDENT, TEACHER, ADMIN
}

@WebServlet(name = "loginServlet", value = "/login-servlet")
public class LoginServlet extends HttpServlet {
    public DBType dbType = ConfigUtil.getDbType();
    public HttpSession session;
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED, "GET method is not supported.");
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html");
        session = request.getSession(true);
        String username = request.getParameter("user-id");
        String password = request.getParameter("password");
        int userID = 0;
        try {
            userID = (int) session.getAttribute("userID");
        }
        catch (NullPointerException e){}
        try {
            // If the user is already logged in, redirect to the index page
            if (checkLogin(username, password)) {
                UserType userType = getUserType(username);
                request.setAttribute("userName", username);
                request.setAttribute("userType", userType.toString());
                request.getRequestDispatcher("/index.jsp").forward(request, response);
            } else {
                PrintWriter out = response.getWriter();
                out.println("not logged in");
            }
        } catch (SQLException | ServletException e) {
            throw new RuntimeException(e);
        }
    }

    private UserType getUserType(String username) {
        UserType userType;
        String userID = "";
        // Check the user type - the mongo section of this should be in MongoScripts.java
        if (dbType == DBType.MONGO) {
            MongoClient mongo = MongoClients.create();
            MongoDatabase db = mongo.getDatabase("StudentMarks");
            MongoCollection<Document> login = db.getCollection("Users");
            Document user = login.find(eq("First_name", username)).first();
            if (user != null) {
                userID = ((Integer) user.get("user_id")).toString();
                session.setAttribute("userID", userID);
            }
        } else {
            SQLScripts db = new SQLScripts();
            userID = db.checkUserType(username);
            session.setAttribute("userID", userID);
        }
        int type = userID.charAt(0);
        if (type == 49) {
            userType = UserType.STUDENT;
        } else if (type == 50) {
            userType = UserType.TEACHER;
        } else {
            userType = UserType.ADMIN;
        }
        session.setAttribute("userType", userType);
        return userType;
    }

    public boolean checkLogin(String username, String password) throws SQLException {
        // Check the login - the mongo section of this should be in MongoScripts.java and the SQL section in SQLScripts.java
        if (dbType == DBType.MONGO) {
            MongoClient mongo = MongoClients.create();
            MongoDatabase db = mongo.getDatabase("StudentMarks");
            MongoCollection<Document> login = db.getCollection("Users");
            var check = login.find(eq("First_name", username)).first();
            if (check != null) {
                return ((String) check.get("Password")).equals(password);
            } else {
                return false;
            }
        } else {
            SQLScripts db = new SQLScripts();
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