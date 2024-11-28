package com.example.studentmarksapp;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import org.bson.Document;

import javax.print.Doc;
import java.io.IOException;
import java.util.ArrayList;
import com.google.gson.Gson;

@WebServlet(name = "EnrolmentServlet", value = "/EnrolmentServlet")
public class EnrolmentServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        ArrayList<String> courses = MongoScripts.getCourses();
        ArrayList<String> modules = MongoScripts.getModules();
        Gson gson = new Gson();
        String coursesJson = gson.toJson(courses);
        String modulesJson = gson.toJson(modules);
        request.setAttribute("courses", coursesJson);
        request.setAttribute("modules", modules);
        request.getRequestDispatcher("/enrolment.jsp").forward(request, response);
    }
    private String getSelectedCourse(HttpServletRequest request) {
        return request.getParameter("course");
    }
}