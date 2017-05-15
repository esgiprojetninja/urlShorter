package views;

import models.Url;
import models.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

@WebServlet(name = "MyUrls")
public class MyUrls extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (request.getSession().getAttribute("user_id") == null) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.sendRedirect("/");
        } else {
            try {
                User user = new User();
                if (user.find((int)request.getSession().getAttribute("user_id")) != 0) {
                    Url url = new Url();
                    ArrayList<Url> urls = url.getAll(
                            Url.class,
                            "user_id",
                            String.valueOf(user.getAttributes().get("id").getValue())
                    );
                    request.setAttribute("urls", urls);
                    this.getServletContext().getRequestDispatcher("/WEB-INF/classes/templates/MyUrls.jsp").forward(request, response);
                } else {
                    response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                    response.sendRedirect("/");
                }
            } catch (ClassNotFoundException | SQLException | InstantiationException | IllegalAccessException | NoSuchMethodException e) {
                e.printStackTrace();
            }
        }
    }
}
