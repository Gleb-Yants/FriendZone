package servlets;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Properties;


/**
Servlet for changing language
 */
@WebServlet("/langChanger")
public class LangChanger extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Properties prop = (Properties) req.getServletContext().getAttribute(req.getParameter("language"));
        req.getSession().setAttribute("lang", prop);
        req.getRequestDispatcher("/login.jsp").forward(req,resp);
    }
}
