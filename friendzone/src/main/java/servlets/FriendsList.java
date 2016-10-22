package servlets;

import dao.interfaces.UserDao;
import model.Friend;
import model.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static filters.SecurityFilter.USER;
import static listeners.Provider.FRIENDS;
import static listeners.Provider.USER_DAO;

/**
 * Created by Gleb_Yants on 30.09.2016.
 */
@WebServlet("/friendsList")
public class FriendsList extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User user = (User)req.getSession().getAttribute(USER);
        UserDao dao = (UserDao)req.getServletContext().getAttribute(USER_DAO);
        User userUpd = dao.getUserById(user.getId()).get();
        req.getSession().setAttribute(USER, userUpd);

        HashMap<Integer, Friend> friends = (HashMap<Integer, Friend>) getServletContext().getAttribute(FRIENDS);
        List<Integer> friendTemp = (userUpd.getFriends());
        List<Friend> myFriends = new ArrayList<>();
        for(Integer friend : friendTemp){
            myFriends.add(friends.get(friend));
        }
        req.setAttribute("FriendsList", myFriends);

        List<Integer> notif = (userUpd.getNotifications());
        List<Friend> notifications = new ArrayList<>();
        for(Integer not : notif){
            notifications.add(friends.get(not));
        }
        req.setAttribute("Notifications", notifications);

        req.getRequestDispatcher("/friends.jsp").forward(req,resp);
    }
}
