package com.example.studentmarksapp;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import jakarta.websocket.Session;

import java.io.IOException;
import java.util.ArrayList;

@WebServlet(name = "ModuleSelectServlet", value = "/ModuleSelectServlet")
public class ModuleSelectServlet extends HttpServlet {
    DBType dbType = DBType.MONGO;
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
        ArrayList<String> courseModules = new ArrayList<>();
        if (dbType.equals(DBType.MONGO)) {
            MongoScripts mongoScripts = new MongoScripts();
            int course = mongoScripts.getUserCourseID(userID);
            courseModules = mongoScripts.getModules(course);
        } else {
            int course = sqlScripts.getUserCourseID(userID);
            courseModules = sqlScripts.getModules(course);

        }

        request.setAttribute("courseModules", courseModules);
        request.getRequestDispatcher("/moduleSelect.jsp").forward(request, response);
    }

    private void enterStudentModules(int studentID, String modules)
    {
        String[] moduleArray = modules.split(",");
        if (dbType == DBType.MONGO) {
            MongoScripts mongoScripts = new MongoScripts();
            for (String module : moduleArray) {
                mongoScripts.addStudentModule(studentID, Integer.parseInt(module));
            }
        }
        else
        {
            SQLScripts sqlScripts = new SQLScripts();
            for (String module : moduleArray) {
                sqlScripts.addStudentModule(studentID, Integer.parseInt(module));
            }
        }
    }
}