package views;

import models.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet(name = "Login")
public class Login extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String name = request.getParameter("name");
        String pwd = request.getParameter("pwd");
        try {
            User user = new User();
            if (user.findBy("name", name) != 0) {
                String userPwd = (String)user.getAttributes().get("pwd").getValue();
                if (userPwd.compareTo(pwd) == 0) {
                    request.getSession().setAttribute(
                            "user_name",
                            user.getAttributes().get("name").getValue()
                    );
                    request.getSession().setAttribute(
                            "user_id",
                            user.getAttributes().get("id").getValue()
                    );
                }
            }
        } catch (ClassNotFoundException | SQLException | InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
        response.sendRedirect("/");
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
