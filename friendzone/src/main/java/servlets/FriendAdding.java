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
import java.util.HashMap;
import java.util.List;

import static filters.SecurityFilter.USER;
import static listeners.Provider.FRIENDS;
import static listeners.Provider.USER_DAO;

/**
 * Created by Gleb_Yants on 02.10.2016.
 */
@WebServlet("/friendAdding")
public class FriendAdding extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User user = ((User) req.getSession().getAttribute(USER));
        String myId = Integer.toString((user.getId()));
        String friendId = Integer.toString(Integer.parseInt(req.getParameter("friend_id")));
        String message;
        List<Integer> friendTemp = ((User)req.getSession().getAttribute(USER)).getFriends();
        if(friendTemp.contains(Integer.parseInt(friendId))){
            message = "You added this friend yet";
            req.setAttribute("Message", message);
            getServletContext().getRequestDispatcher("/message.jsp").forward(req, resp);
            return;
        }
        if(friendId==myId){
            message = "You are adding yourself";
            req.setAttribute("Message", message);
            getServletContext().getRequestDispatcher("/message.jsp").forward(req, resp);
            return;
        }else{
        UserDao dao = (UserDao)req.getServletContext().getAttribute(USER_DAO);
        dao.addFriend(myId, friendId);
        req.getSession().setAttribute(USER, dao.getUserByEmail(user.getLogin()).get());
            message = "Friend added";
            req.setAttribute("Message", message);
            getServletContext().getRequestDispatcher("/message.jsp").forward(req, resp);
        }
    }
}
