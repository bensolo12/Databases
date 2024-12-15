package com.example.studentmarksapp;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;
import java.util.ArrayList;

@WebServlet(name = "StudentResultServlet", value = "/StudentResultServlet")
public class StudentResultServlet extends HttpServlet {
    DBType dbType = DBType.SQL;
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(true);
        int userID = Integer.parseInt((String) session.getAttribute("userID"));

        ArrayList<String> studentList = new ArrayList<>();
        ArrayList<String> moduleGrades = new ArrayList<>();
        if (dbType == DBType.MONGO) {
            MongoScripts mongoScripts = new MongoScripts();
            studentList = mongoScripts.getAllStudents();
            moduleGrades = mongoScripts.getModuleGrades();
        }
        else
        {
            SQLScripts sqlScripts = new SQLScripts();
            studentList = sqlScripts.getAllStudents();
            moduleGrades = sqlScripts.getModuleGrades();
        }
        request.setAttribute("studentList", studentList);
        request.setAttribute("moduleGrades", moduleGrades);
        request.getRequestDispatcher("/studentResult.jsp").forward(request, response);
    }
}