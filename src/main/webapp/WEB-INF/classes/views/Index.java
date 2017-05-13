package views;

import models.Url;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class Index extends HttpServlet {
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        String url = req.getParameter("url");
        Boolean saved = false;
        try {
            Url newUrl = new Url();
            newUrl.setBase_url(url);
            if (newUrl.save() != 0) saved = true;
        } catch (ClassNotFoundException | SQLException | InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
        if (saved) {
            res.sendRedirect("/?saved=true");
        } else {
            res.sendRedirect("/?saved=false");
        }
    }

    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        String saved = req.getParameter("saved");
        if (saved != null && saved.compareTo("true") == 0) {
            req.setAttribute("message", "Url successfully saved !");
        } else if (saved != null && saved.compareTo("false") == 0) {
            req.setAttribute("message", "Error while saving url :/");
        } else {
            req.setAttribute("message", "");
        }
        try {
            Url url = new Url();
            ArrayList<Url> urls = url.getAll(Url.class);
            req.setAttribute("urls", urls);
        } catch (ClassNotFoundException | SQLException | InstantiationException | IllegalAccessException | NoSuchMethodException e) {
            e.printStackTrace();
        }
        this.getServletContext().getRequestDispatcher("/WEB-INF/index.jsp").forward(req, res);
    }
}
