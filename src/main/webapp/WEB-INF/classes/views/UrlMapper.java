package views;

import models.Url;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

/**
 * Created by roland on 13/05/2017.
 */
public class UrlMapper extends HttpServlet {
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {

    }

    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        String urlInPath = req.getRequestURI().replace("/urls/", "");
        Url url = null;
        try {
            url = new Url();
            url.findBy("shorter_url", urlInPath);
        } catch (ClassNotFoundException | SQLException | InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
        if (url != null) {
            res.sendRedirect((String)url.getAttributes().get("base_url").getValue());
        }
    }
}
