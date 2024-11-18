package com.example.studentmarksapp;

import com.mongodb.client.*;

import org.bson.Document;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;

import static com.mongodb.client.model.Projections.fields;
import static com.mongodb.client.model.Projections.include;
import static com.mongodb.client.model.Sorts.descending;

@WebServlet(name = "Registration", value = "/registration")
public class Registration extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        DBType dbType = DBType.SQL;
        //String userID = request.getParameter("userID");
        String first_name = request.getParameter("First_Name");
        String second_name = request.getParameter("Second_Name");
        String password = request.getParameter("Password");
        String DOB = request.getParameter("DOB");

        PrintWriter out = response.getWriter();
        out.println("Processing user registration ........");
        Document user = new Document()
                .append("user_id", getUserID())
                .append("First_name", first_name)
                .append("Password", password);
        if (dbType == DBType.MONGO) {
            createUserMongo(user);
        } else {
            try {
                createUserSQL(first_name, second_name, password, DOB);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void createUserMongo(Document customer) {
        MongoClient mongo = MongoClients.create();
        MongoDatabase database = mongo.getDatabase("StudentMarks");
        MongoCollection collection = database.getCollection("Users");
        collection.insertOne(customer);
    }

    public void createUserSQL(String firstName, String secondName, String password, String DOB) throws SQLException {
        DBScripts db = new DBScripts();
        db.createOracleSchema();
        //if (!db.checkIfTableExists("User")) db.createTable();

        //Enter Registration info into user table
        String enterUserSQL = "INSERT INTO Users (USER_ID, FIRST_NAME ,Second_name, DOB, Password) VALUES (?, ?, ?, ?, ?)";
        try (Connection connection = db.ConnectDB();
             PreparedStatement preparedStatement = connection.prepareStatement(enterUserSQL)) {
            preparedStatement.setInt(1, 1);
            preparedStatement.setString(2, firstName);
            preparedStatement.setString(3, secondName);
            preparedStatement.setDate(4, Date.valueOf(DOB)); // Assuming `DOB` is a `String` in `yyyy-mm-dd` format
            preparedStatement.setString(5, password);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public int getUserID() {
        MongoClient mongo = MongoClients.create();
        MongoDatabase db = mongo.getDatabase("StudentMarks");
        MongoCollection<Document> login = db.getCollection("Users");
        MongoCursor<Document> cursor = login.find()
                .projection(fields(include("user_id"))) // Replace with the field you want
                .sort(descending("timestampField")) // Replace with your sorting field
                .limit(1)
                .iterator();
        if (cursor.hasNext()) {
            return cursor.next().getInteger("user_id") + 1;

        } else {
            return 0;
        }
    }
}