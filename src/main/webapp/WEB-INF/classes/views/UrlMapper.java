package views;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by roland on 13/05/2017.
 */
public class UrlMapper extends HttpServlet {
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {

    }

    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        req.setAttribute("pathinfo", req.getRequestURI());
        this.getServletContext().getRequestDispatcher("/WEB-INF/mapper.jsp").forward(req, res);
    }
}
