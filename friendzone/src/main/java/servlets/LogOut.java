package servlets;

import model.User;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

import static filters.SecurityFilter.USER;

/**
LogOut servlet
 */
@WebServlet("/logOut")
public class LogOut extends HttpServlet {
    private static final Logger LOG = Logger.getLogger(LogOut.class);
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        LOG.info("User "+((User)req.getSession().getAttribute(USER)).getLogin()+" log out");
        HttpSession session = req.getSession(false);
        if(session != null)
            session.invalidate();
        req.getRequestDispatcher("/login.jsp").forward(req,resp);
    }
}
