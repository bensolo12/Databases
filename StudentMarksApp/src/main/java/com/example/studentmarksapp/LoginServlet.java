package com.example.studentmarksapp;

import java.io.*;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import static com.mongodb.client.model.Filters.eq;
import static java.lang.System.out;

@WebServlet(name = "loginServlet", value = "/login-servlet")
public class LoginServlet extends HttpServlet {

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html");
        String username = request.getParameter("user-id");
        String password = request.getParameter("password");
        if (checkLogin(username, password))
        {
            out.println("Logged in");
        }
        else out.println("Not logged in");
    }

    public boolean checkLogin(String username , String password) {
        MongoClient mongo = MongoClients.create();
        MongoDatabase db = mongo.getDatabase("dbSales");
        MongoCollection login = db.getCollection("Collection_Registration");
        var check = login.find(eq("First_name",username)).first();
        if (check != null) {return true;}
        else {return false;}
    }
}