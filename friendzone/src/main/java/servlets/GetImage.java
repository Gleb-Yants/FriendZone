package servlets;

import model.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static filters.SecurityFilter.USER;

/**
Servlet for getting img from fs
 */
@WebServlet("/getImage")
public class GetImage extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String ava = req.getParameter("avatar");
        if(!ava.equals("NoImage")){
        Path path = Paths.get(ava);
        byte[] data = Files.readAllBytes(path);
        resp.setContentType("image/jpeg");
        resp.getOutputStream().write(data);
        resp.getOutputStream().flush();
        resp.getOutputStream().close();}
    }
}
