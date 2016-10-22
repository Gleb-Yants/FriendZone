package servlets;

/**
 * Created by Gleb_Yants on 20.09.2016.
 */
import dao.interfaces.UserDao;
import dao.mysql.MySQLUserDao;
import model.User;

import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import static listeners.Provider.USER_DAO;
import static filters.SecurityFilter.USER;

@WebServlet("/uploadServlet")
@MultipartConfig(maxFileSize = 16177215)    // upload file's size up to 16MB
public class FileUploadDBServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response) throws ServletException, IOException {

        // obtains the upload file part in this multipart request
        Part filePart = request.getPart("photo");
        String message;
        if(filePart.getSize()>0) {
            User user = ((User) request.getSession().getAttribute(USER));
            File photo = new File("C:\\images\\" + user.getLogin() + ".jpg");
            try (InputStream is = filePart.getInputStream(); FileOutputStream fos = new FileOutputStream(photo)) {
                byte[] buffer = new byte[1000];
                while (is.available() > 0) {
                    int count = is.read(buffer);
                    fos.write(buffer, 0, count);
                }
            }
            message = "File uploaded and saved into database";
            UserDao dao = (UserDao)request.getServletContext().getAttribute(USER_DAO);
            dao.storePhoto(user, "C:\\images\\" + user.getLogin() + ".jpg");// message will be sent back to client
            request.getSession().setAttribute(USER, dao.getUserByEmail(user.getLogin()).get());
        } else message = "File didnt upload";
            // sets the message in request scope
            request.setAttribute("Message", message);

            // forwards to the message page
            getServletContext().getRequestDispatcher("/message.jsp").forward(request, response);
        }

    }
