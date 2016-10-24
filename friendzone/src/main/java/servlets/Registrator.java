package servlets;

import dao.interfaces.UserDao;
import exception.InvalidEmailException;
import model.Friend;
import org.apache.log4j.Logger;
import org.mindrot.jbcrypt.BCrypt;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.regex.*;

import static listeners.Provider.FRIENDS;
import static listeners.Provider.USER_DAO;

/**
Registration servlet; include checking correctness of email
 */
@WebServlet("/registrator")
public class Registrator extends HttpServlet {
    private static final Logger LOG = Logger.getLogger(Registrator.class);
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String login = req.getParameter("email");
        String pass = req.getParameter("password");
        Pattern valid = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
        Matcher matcher = valid.matcher(login);
        if(matcher.find()){
            String hsPass = BCrypt.hashpw(pass, BCrypt.gensalt());
        UserDao ud = (UserDao) getServletContext().getAttribute(USER_DAO);
        ud.registerUser(login, hsPass);
            LOG.info("New user registered: "+login);
            Map<Integer, Friend> friends = ud.getAllFriends();
            req.getServletContext().setAttribute(FRIENDS, friends);//refresh all users variable
            resp.sendRedirect("login.jsp?info=Successful registration");
        }else throw new InvalidEmailException();
    }
}
