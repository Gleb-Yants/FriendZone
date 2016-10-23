package servlets;

import model.Friend;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import static listeners.Provider.FRIENDS;

/**
Servlet for moving to concrete friend
 */
@WebServlet("/friendGetter")
public class FriendGetter extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HashMap<Integer, Friend> friends = (HashMap<Integer, Friend>) getServletContext().getAttribute(FRIENDS);
        Friend friend = friends.get(Integer.parseInt(req.getParameter("id")));
        req.setAttribute("Friend", friend);
        req.getRequestDispatcher("/friend.jsp").forward(req,resp);
    }
}
