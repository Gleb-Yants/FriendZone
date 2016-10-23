package servlets;

/**
Service for upload image from user; only jpgs; save imgs in fs by the user name;
 */
import dao.interfaces.UserDao;
import dao.mysql.MySQLUserDao;
import model.Friend;
import model.User;
import org.apache.log4j.Logger;

import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import static listeners.Provider.FRIENDS;
import static listeners.Provider.USER_DAO;
import static filters.SecurityFilter.USER;

@WebServlet("/uploadServlet")
@MultipartConfig(maxFileSize = 16177215)    // upload file's size up to 16MB
public class FileUploadDBServlet extends HttpServlet {
    private static final Logger LOG= Logger.getLogger(FileUploadDBServlet.class);

    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response) throws ServletException, IOException {
        Part filePart = request.getPart("photo");
        String message;
        if(filePart.getSize()>0) {
            User user = ((User) request.getSession().getAttribute(USER));
            File photo = new File("C:\\images\\" + user.getLogin() + ".jpg");//username for picture name
            try (InputStream is = filePart.getInputStream(); FileOutputStream fos = new FileOutputStream(photo)) {
                byte[] buffer = new byte[1000];
                while (is.available() > 0) {
                    int count = is.read(buffer);
                    fos.write(buffer, 0, count);
                }
            }
            LOG.info(user.getLogin()+" uploaded img on server");
            message = "File was uploaded and saved into database";
            UserDao dao = (UserDao)request.getServletContext().getAttribute(USER_DAO);
            dao.storePhoto(user, "C:\\images\\" + user.getLogin() + ".jpg");// store in db
            request.getSession().setAttribute(USER, dao.getUserByEmail(user.getLogin()).get());//refresh info about user photo for system
            Map<Integer, Friend> friends = dao.getAllFriends();
            request.getServletContext().setAttribute(FRIENDS, friends);//refresh info about user photo for system
        } else message = "File didnt upload";
            // sets the message in request scope
            request.setAttribute("Message", message);
            // forwards to the message page
            getServletContext().getRequestDispatcher("/message.jsp").forward(request, response);
        }

    }
