package servlets;

import dao.interfaces.UserDao;
import dao.mysql.MySQLUserDao;
import model.Friend;
import model.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

import static filters.SecurityFilter.USER;
import static listeners.Provider.FRIENDS;
import static listeners.Provider.USER_DAO;

/**
 * Created by Gleb_Yants on 21.09.2016.
 */
@WebServlet("/changer")
public class SettingsChanger extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String command = req.getParameter("command");
        User user = ((User) req.getSession().getAttribute(USER));
        UserDao dao = (UserDao) req.getServletContext().getAttribute(USER_DAO);
        switch (command) {
            case "fName":  dao.changeUserSettings(user, "firstName", req.getParameter("fName"));
                break;
            case "lName":  dao.changeUserSettings(user, "lastName", req.getParameter("lName"));
                break;
            case "phone":  dao.changeUserSettings(user, "telephone", req.getParameter("phone"));
                break;
            case "me":  dao.changeUserSettings(user, "aboutMe", req.getParameter("me"));
                break;
        }
        req.getSession().setAttribute(USER, dao.getUserByEmail(user.getLogin()).get());
        Map<Integer, Friend> friends = dao.getAllFriends();
        req.getServletContext().setAttribute(FRIENDS, friends);
        String message = "User's information uploaded";
        req.setAttribute("Message", message);
        // forwards to the message page
        getServletContext().getRequestDispatcher("/message.jsp").forward(req, resp);
    }
}
