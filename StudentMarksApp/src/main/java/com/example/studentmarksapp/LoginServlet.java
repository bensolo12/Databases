package com.example.studentmarksapp;

import java.io.*;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import static com.mongodb.client.model.Filters.eq;

@WebServlet(name = "loginServlet", value = "/login-servlet")
public class LoginServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED, "GET method is not supported.");
    }
    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html");
        String username = request.getParameter("user-id");
        String password = request.getParameter("password");
        String dbType = request.getParameter("toggle-switch");
        try {
            if (checkLogin(username, password, dbType))
            {
                PrintWriter out = response.getWriter();
                out.println("logged in");
            }
            else
            {
                PrintWriter out = response.getWriter();
                out.println("not logged in");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean checkLogin(String username , String password, String dbType) throws SQLException {
        if (dbType == "checked") {
            MongoClient mongo = MongoClients.create();
            MongoDatabase db = mongo.getDatabase("StudentMarks");
            MongoCollection login = db.getCollection("Users");
            var check = login.find(eq("user_id", 1)).first();
            if (check != null) {
                return true;
            } else {
                return false;
            }
        } else {

            DBScripts db = new DBScripts();
            Connection connection = db.ConnectDB();
            String checkUserSQL = "SELECT * FROM Users WHERE user_id = " + username + " AND Password = " + password;
            connection.createStatement();
            var check = connection.prepareStatement(checkUserSQL).executeQuery();
            if (check != null) {
                return true;
            } else {
                return false;
            }
        }
    }
}