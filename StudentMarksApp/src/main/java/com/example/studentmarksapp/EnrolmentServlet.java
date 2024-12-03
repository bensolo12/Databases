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
        HttpSession session = request.getSession(true);
        String userID = (String) session.getAttribute("userID");

        ArrayList<String> courses = MongoScripts.getCourses();
        ArrayList<String> modules = MongoScripts.getModules();
        Gson gson = new Gson();
        String coursesJson = gson.toJson(courses);
        String modulesJson = gson.toJson(modules);
        request.setAttribute("courses", coursesJson);
        request.setAttribute("modules", modules);
        request.setAttribute("userID", userID);
        request.getRequestDispatcher("/enrolment.jsp").forward(request, response);
        try{
            if (request.getParameter("course").isEmpty()){
                request.getRequestDispatcher("/enrolment.jsp").forward(request, response);
            }
            else{
                MongoScripts.enrolStudent(request.getParameter("course"), userID);
            }
        } catch (NullPointerException e){
            request.getRequestDispatcher("/enrolment.jsp").forward(request, response);
        }
    }
}