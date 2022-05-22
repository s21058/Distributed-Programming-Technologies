package com.example.TPO5_BS_S21058;

import java.io.*;
import java.util.ArrayList;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
@WebServlet(name ="SendingServlet",urlPatterns = "/sending-to-another-servlet")
public class SendingServlet extends HttpServlet {
    String car;
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
            response.setContentType("text/html");
        String str = request.getParameter("type");
        String str1 = request.getParameter("mark");
        String str2 = request.getParameter("year");
        String str3 = request.getParameter("fuel consumption");
        for (int i = 0; i <4 ; i++) {
            if(str.isEmpty()){
                str=" ";
            }else if(str1.isEmpty()){
                str1=" ";
            }else if(str2.isEmpty()){
                str2=" ";
            }else if(str3.isEmpty()){
                str3=" ";
            }
        }
        car=str+","+str1+","+str2+","+str3;
        System.out.println(car);
        doPost(request,response);
        str="";
        str1="";
        str2="";
        str3="";
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        request.setAttribute("Car",car);
        RequestDispatcher requestDispatcher = request.getRequestDispatcher("/FindingServlet");
        try {
            requestDispatcher.forward(request, response);
        } catch (ServletException e) {
            e.printStackTrace();
        }
    }



}