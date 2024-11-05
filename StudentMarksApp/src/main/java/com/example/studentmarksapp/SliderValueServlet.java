package com.example.studentmarksapp;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;

@WebServlet(name = "SliderValueServlet", value = "/update-slider-value")
public class SliderValueServlet extends HttpServlet {
    public static DBType dbType;
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String sliderValue = request.getParameter("sliderValue");
        dbType = sliderValue.equals("checked") ? DBType.MONGO : DBType.SQL;
    }
    public DBType getDbType() {
        return dbType;
    }
}