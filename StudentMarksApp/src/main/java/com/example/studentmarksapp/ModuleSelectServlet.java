package com.example.studentmarksapp;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import jakarta.websocket.Session;

import java.io.IOException;
import java.util.ArrayList;

@WebServlet(name = "ModuleSelectServlet", value = "/ModuleSelectServlet")
public class ModuleSelectServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(true);
        SQLScripts sqlScripts = new SQLScripts();
        int userID = Integer.parseInt((String) session.getAttribute("userID"));

        if (request.getParameter("selectedModules") != null) {
            enterStudentModules(userID, request.getParameter("selectedModules"));
        }

        int course = sqlScripts.getUserCourseID(userID);
        ArrayList<String> courseModules = sqlScripts.getModules(course);
        request.setAttribute("courseModules", courseModules);
        request.getRequestDispatcher("/moduleSelect.jsp").forward(request, response);
    }

    private void enterStudentModules(int studentID, String modules)
    {
        SQLScripts sqlScripts = new SQLScripts();
        String[] moduleArray = modules.split(",");
        for (String module : moduleArray) {
            sqlScripts.addStudentModule(studentID, Integer.parseInt(module));
        }
    }
}