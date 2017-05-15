package views;

import models.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet(name = "Subscribe")
public class Subscribe extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String name = request.getParameter("name");
        String pwd = request.getParameter("pwd");
        try {
            User userTest = new User();
            if (userTest.findBy("name", name) != 0) {
                response.sendRedirect("/?userExist=true");
            }
            else if (name.isEmpty() || pwd.isEmpty()) {
                response.sendRedirect("/?emptyInput=true");
            }
            else {
                User newUser = new User();
                newUser.getAttributes().get("name").setValue(name);
                newUser.getAttributes().get("pwd").setValue(pwd);
                if (newUser.save() != 0) {
                    response.sendRedirect("/?userSaved=true");
                } else {
                    response.sendRedirect("/?userSaved=false");
                }
            }
        } catch (ClassNotFoundException | SQLException | InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
