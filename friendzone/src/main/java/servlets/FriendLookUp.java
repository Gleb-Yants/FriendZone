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
import java.util.Map;

import static listeners.Provider.FRIENDS;
import static listeners.Provider.USER_DAO;

/**
Servlet for searching users by case insensitive query (first and last name)
 */
@WebServlet("/lookUp")
public class FriendLookUp extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Map<Integer, Friend> allFriends = ((UserDao) getServletContext().getAttribute(USER_DAO)).getAllFriends();
        getServletContext().setAttribute(FRIENDS, allFriends);//get actual info about users in system
        Map<Integer, Friend> friends = (HashMap<Integer, Friend>) getServletContext().getAttribute(FRIENDS);
        String searchQuery = (req.getParameter("searchQuery")).trim();
        ArrayList<Friend> searchResult = new ArrayList<>();
        for (Map.Entry<Integer, Friend> pair : friends.entrySet())
        {
            Friend friend = pair.getValue();
            if(friend.getFirstName()!=null&&friend.getLastName()!=null){
            String first = friend.getFirstName();
            String second = friend.getLastName();
            if(first.equalsIgnoreCase(searchQuery)||second.equalsIgnoreCase(searchQuery)){
                searchResult.add(friend);
               }
            }
        }
        if(searchResult.isEmpty()){
           String message = "Founded nothing";
            // sets the message in request scope
            req.setAttribute("Message", message);
            getServletContext().getRequestDispatcher("/message.jsp").forward(req, resp);
            return;
        }else{
            req.setAttribute("FriendsList", searchResult);
            req.getRequestDispatcher("/friends.jsp").forward(req,resp);
        }
    }
}
