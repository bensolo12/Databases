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
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED, "GET method is not supported.");
    }
    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html");
        String username = request.getParameter("user-id");
        String password = request.getParameter("password");
        if (checkLogin(username, password))
        {
            PrintWriter out = response.getWriter();
            out.println("logged in");
        }
        else
        {
            PrintWriter out = response.getWriter();
            out.println("not logged in");
        }
    }

    public boolean checkLogin(String username , String password) {
        MongoClient mongo = MongoClients.create();
        MongoDatabase db = mongo.getDatabase("StudentMarks");
        MongoCollection login = db.getCollection("Users");
        var check = login.find(eq("user_id",1)).first();
        if (check != null) {return true;}
        else {return false;}
    }
}