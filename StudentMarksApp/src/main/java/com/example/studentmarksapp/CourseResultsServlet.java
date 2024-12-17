package com.example.studentmarksapp;

import com.google.gson.Gson;
import jakarta.json.Json;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;
import java.util.ArrayList;

@WebServlet(name = "CourseResultsServlet", value = "/CourseResultsServlet")
public class CourseResultsServlet extends HttpServlet {
    DBType dbType = DBType.MONGO;
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(true);
        int userID = Integer.parseInt((String) session.getAttribute("userID"));
        ArrayList<Integer> courses = new ArrayList<>();
        ArrayList<Integer> passes = new ArrayList<>();

        if (dbType.equals(DBType.MONGO)) {
            MongoScripts mongoScripts = new MongoScripts();
            courses = MongoScripts.getCourses();

        } else {
            SQLScripts sqlScripts = new SQLScripts();
            //courses = sqlScripts.getCourses();
        }
        assert courses != null;
        for(int course : courses){
            passes.add(calculateGrades(course));
        }
        //create a json object with the course ID and the number of passes

        var coursePasses = Json.createObjectBuilder()
                .add("courseID", courses.toString())
                .add("passes", passes.toString())
                .build();

        Gson gson = new Gson();

        request.setAttribute("coursePasses", coursePasses.toString());
        request.setAttribute("courseList", gson.toJson(courses));
        request.getRequestDispatcher("/courseResults.jsp").forward(request, response);

    }

    private int calculateGrades(int courseID){
        //For each course, get all modules and calculate the average grade for each student
        //Get all students in the course, if the grade is over 40, add to the pass count
        int passCount = 0;
        if (dbType.equals(DBType.MONGO)) {
            MongoScripts mongoScripts = new MongoScripts();
            passCount = mongoScripts.calculatePasses(courseID);
        } else {
            SQLScripts sqlScripts = new SQLScripts();
            passCount = sqlScripts.calculatePasses(courseID);
        }
        return passCount;
    }
}