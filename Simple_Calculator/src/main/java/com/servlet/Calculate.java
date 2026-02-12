package com.servlet;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/Calculate")
public class Calculate extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        double num1 = Double.parseDouble(request.getParameter("num1"));
        double num2 = Double.parseDouble(request.getParameter("num2"));
        String operation = request.getParameter("operation");

        double result = 0;

        switch (operation) {
            case "add":
                result = num1 + num2;
                break;

            case "sub":
                result = num1 - num2;
                break;

            case "mul":
                result = num1 * num2;
                break;

            case "div":
                if (num2 != 0) {
                    result = num1 / num2;
                } else {
                    out.println("<h3>Cannot divide by zero!</h3>");
                    return;
                }
                break;
            default:
                out.println("<h3>Invalid Operation</h3>");
                return;
        }

        out.println("<html><body>");
        out.println("<h2>Result: " + result + "</h2>");
        out.println("<a href='index.html'>Go Back</a>");
        out.println("</body></html>");
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        out.println("<h3> this will work with post </h3>");
    }
}

