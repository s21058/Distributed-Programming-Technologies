package com.example.TPO5_BS_S21058;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

@WebServlet(name = "ShowJsp",urlPatterns = "/ShowJsp")
public class ShowJsp extends HttpServlet {
    static ArrayList<Car> cars;

    public void doGet(HttpServletRequest request, HttpServletResponse response) {
        response.setContentType("text/html");
        cars = (ArrayList<Car>) request.getAttribute("Car");
        doPost(request,response);
        cars.clear();
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) {
        request.setAttribute("Car", cars);
        RequestDispatcher requestDispatcher = request.getRequestDispatcher("Find.jsp");
        try {
            requestDispatcher.forward(request, response);
        } catch (ServletException | IOException e) {
            e.printStackTrace();
        }
    }
}
