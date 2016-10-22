package listeners;

import dao.interfaces.MessengerDao;
import dao.interfaces.UserDao;
import dao.mysql.MySQLMessengerDao;
import dao.mysql.MySQLUserDao;
import model.Friend;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import service.SecurityService;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import javax.sql.DataSource;
import java.io.*;
import java.util.*;

/**
Provider for DAO services, DB properties, map of all users, security service
 */
@WebListener
public class Provider implements ServletContextListener {
    public static final String USER_DAO = "userDao";
    public static final String FRIENDS = "friends";
    public static final String DBPROP = "java:comp/env/jdbc/friendzone";
    public static final String SECURITY_SERVICE = "securityService";
    public static final String MESSENGER_DAO = "messengerDao";
    public static final String LOCALE_EN = "en";
    public static final String LOCALE_RU = "ru";
    public static final String PATH_TO_EN_PROPERTIES = "C:\\My projects\\FriendZone\\friendzone\\src\\main\\webapp\\resources\\prop_en.properties";
    public static final String PATH_TO_RU_PROPERTIES = "C:\\My projects\\FriendZone\\friendzone\\src\\main\\webapp\\resources\\prop_ru.properties";

    private static final Logger LOG= Logger.getLogger(Provider.class);

    public InitialContext initContext;
    public static DataSource dataSource;
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        final ServletContext servletContext = sce.getServletContext();

        String log4jConfigFile = servletContext.getInitParameter("log4j-config-location");
        String fullPath = servletContext.getRealPath("") + File.separator + log4jConfigFile;
        PropertyConfigurator.configure(fullPath);
        if(!log4jConfigFile.isEmpty()){
            LOG.info("log4jConfigFile found");
        }else{
            LOG.error("log4jConfigFile not found");
        }

        try{
        initContext= new InitialContext();
            dataSource = (DataSource) initContext.lookup(DBPROP);
            LOG.info("DB connected");}
        catch(NamingException ex){
            LOG.error("DB not connected", ex);
        }

        final UserDao userDao = new MySQLUserDao();
        servletContext.setAttribute(USER_DAO, userDao);
        if(userDao!=null){
            LOG.info("userDao created");
        }else{
            LOG.error("userDao not created");
        }

        Map<Integer, Friend> friends = userDao.getAllFriends();
        servletContext.setAttribute(FRIENDS, friends);

        final MessengerDao messDao = new MySQLMessengerDao(friends);
        servletContext.setAttribute(MESSENGER_DAO, messDao);

        if(messDao!=null){
            LOG.info("messDao created");
        }else{
            LOG.error("messDao not created");
        }

        SecurityService ss = new SecurityService(userDao);
        servletContext.setAttribute(SECURITY_SERVICE, ss);
        if(ss!=null){
            LOG.info("Security service created");
        }else{
            LOG.error("Security service not created");
        }

        Properties prop = new Properties();
        try(BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(PATH_TO_EN_PROPERTIES), "UTF-8"))){
            prop.load(reader);
            LOG.info("en properties for i18n loaded");
        }catch(IOException ex){LOG.error("en properties for i18n not loaded", ex);}
        servletContext.setAttribute(LOCALE_EN, prop);

        Properties prop2 = new Properties();
        try(BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(PATH_TO_RU_PROPERTIES), "UTF-8"))){
            prop2.load(reader);
            LOG.info("ru properties for i18n loaded");
        }catch(IOException ex){LOG.error("ru properties for i18n not loaded", ex);}
        servletContext.setAttribute(LOCALE_RU, prop2);
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {

    }
}
