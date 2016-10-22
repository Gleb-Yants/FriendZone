package filters;

import model.User;
import org.apache.log4j.Logger;
import service.SecurityService;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;

import static listeners.Provider.SECURITY_SERVICE;

/**
This filter checks, whether user yet logged in, if not - start security service, if yes - chain do filter
 */

@WebFilter("/*")
public class SecurityFilter implements Filter {
    private static final Logger LOG= Logger.getLogger(SecurityFilter.class);

    public static final String USER = "user";
    private final HashSet<String> allowedURI = new HashSet<>(Arrays.asList("","/login.jsp", "/registration.jsp", "/registrator","/langChanger"));

    private SecurityService securityService;
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        securityService = (SecurityService) filterConfig.getServletContext().getAttribute(SECURITY_SERVICE);
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpReq = (HttpServletRequest) request;
        final HttpSession session = httpReq.getSession(true);
        String path = httpReq.getRequestURI().substring(httpReq.getContextPath().length()).replaceAll("[/]+$", "");
        if (Optional.ofNullable(session.getAttribute(USER)).isPresent()) {
            chain.doFilter(request, response);
        }else if(allowedURI.contains(path)){
            chain.doFilter(request,response);
        } else {
            final Optional<User> userOptional = securityService.checkAndGetUser(request.getParameter("login"), request.getParameter("pass"));
            if (userOptional.isPresent()) {
                LOG.info("Attempt log in successed for "+ userOptional.get().getLogin());
                session.setAttribute(USER, userOptional.get());
                chain.doFilter(request, response);
            } else {
                LOG.info("Attempt failed for inputed login:"+request.getParameter("login"));
                if(allowedURI.contains(path)){
                    request.getRequestDispatcher(path).forward(request, response);
                }else{ request.getRequestDispatcher("login.jsp").forward(request, response);}
            }
        }
    }

    @Override
    public void destroy() {

    }
}
