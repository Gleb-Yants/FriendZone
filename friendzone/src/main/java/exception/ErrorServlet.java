package exception;

import model.User;
import org.apache.log4j.Logger;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Optional;

import static filters.SecurityFilter.USER;

/**
 * Created by Gleb_Yants on 21.10.2016.
 */
@WebServlet("/errorServlet")
public class ErrorServlet extends HttpServlet {
    private static final Logger LOG= Logger.getLogger(ErrorServlet.class);
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Optional<User> user = Optional.ofNullable((User)req.getSession().getAttribute(USER));
        String userName;
        if(user.isPresent()){
            userName = user.get().getLogin();
        }else userName = "User not logged in";
        Throwable excep = (Throwable) req.getAttribute("javax.servlet.error.exception");
        String requestUri = (String) req.getAttribute("javax.servlet.error.request_uri");
        LOG.error("User: "+userName+" by this request: "+requestUri+" had exception", excep);
        req.getRequestDispatcher("/error.jsp").forward(req,resp);
    }
}
