package com.example.studentmarksapp;

import com.google.gson.Gson;
import com.mongodb.util.JSON;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@WebServlet(name = "ResultEntryServlet", value = "/ResultEntryServlet")
public class ResultEntryServlet extends HttpServlet {
    DBType dbType = DBType.MONGO;
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        HttpSession session = request.getSession(true);
        int userID = Integer.parseInt((String) session.getAttribute("userID"));


        String studentGrades = request.getParameter("studentGrades");

        try {
            if (studentGrades != null) {
                Pattern pattern = Pattern.compile("\\{\"moduleID\":(\\d+),\"studentID\":\"(\\d+)\",\"grade\":\"(\\d+)\"\\}");
                Matcher matcher = pattern.matcher(studentGrades);
                while (matcher.find()) {
                    int moduleID = Integer.parseInt(matcher.group(1));
                    int studentID = Integer.parseInt(matcher.group(2));
                    int result = Integer.parseInt(matcher.group(3));
                    enterStudentResults(studentID, moduleID, result);
                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        ArrayList<String> courseModules = new ArrayList<>();
        ArrayList<Integer> students = new ArrayList<>();
        ArrayList<Integer> moduleIDs = new ArrayList<>();

        if (dbType == DBType.MONGO) {
            MongoScripts mongoScripts = new MongoScripts();
            courseModules = mongoScripts.getStaffModules(userID);
        }
        else
        {
            SQLScripts sqlScripts = new SQLScripts();
            courseModules = sqlScripts.getStaffModules(userID);
            students = sqlScripts.getStudentsOnModule(userID);
        }
        request.setAttribute("courseModules", courseModules);
        request.getRequestDispatcher("/resultEntry.jsp").forward(request, response);

    }
    private void enterStudentResults(int studentID, int moduleID, int result)
    {
        if (dbType == DBType.MONGO) {
            MongoScripts mongoScripts = new MongoScripts();
            mongoScripts.addStudentResult(studentID, moduleID, result);
        }
        else
        {
            SQLScripts sqlScripts = new SQLScripts();
            sqlScripts.addStudentResult(studentID, moduleID, result);
        }
    }
}