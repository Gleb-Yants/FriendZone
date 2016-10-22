package servlets;
/**
WebChat service
 */

        import static filters.SecurityFilter.USER;
        import java.io.IOException;
        import java.util.Map;
        import java.util.concurrent.ConcurrentHashMap;
        import java.util.concurrent.atomic.AtomicInteger;

        import javax.servlet.http.HttpSession;
        import javax.websocket.*;
        import javax.websocket.server.ServerEndpoint;

        import dao.interfaces.MessengerDao;
        import dao.interfaces.UserDao;
        import model.Friend;
        import model.User;
        import org.apache.juli.logging.Log;
        import org.apache.juli.logging.LogFactory;

        import static listeners.Provider.FRIENDS;
        import static listeners.Provider.MESSENGER_DAO;
        import static listeners.Provider.USER_DAO;

        import org.apache.log4j.Logger;
        import util.HTMLFilter;

@ServerEndpoint(value = "/websocket/chat", configurator = GetHttpSessionConfigurator.class)
public class ChatAnnotation {

    private static final Logger LOG= Logger.getLogger(ChatAnnotation.class);

    private static final String GUEST_PREFIX = "Guest";
    private static final AtomicInteger connectionIds = new AtomicInteger(0);
    private static final ConcurrentHashMap<Integer, ChatAnnotation> connections =
            new ConcurrentHashMap<>();

    private String nickname;
    private Session session;
    private HttpSession httpSession;
    private int who;
    private int whom;
    private MessengerDao messDao;
    private UserDao userDao;
    private boolean isFirst = true;

    public ChatAnnotation() {
       // nickname = GUEST_PREFIX + connectionIds.getAndIncrement();

    }


    @OnOpen
    public void start(Session session, EndpointConfig config) {
        this.session = session;
        this.httpSession = (HttpSession) config.getUserProperties()
                .get(HttpSession.class.getName());
        int id = ((User) httpSession.getAttribute(USER)).getId();
        connections.put(id, this);
        who=id;
        nickname = ((User) httpSession.getAttribute(USER)).getFirstName();
        whom = (Integer) httpSession.getAttribute("friendId");
        messDao = (MessengerDao) httpSession.getServletContext().getAttribute(MESSENGER_DAO);
        userDao = (UserDao) httpSession.getServletContext().getAttribute(USER_DAO);
        String [] message = messDao.getConversation(who, whom).split("\n");
        userDao.removeNotification(whom, who);
        User user = userDao.getUserById(who).get();
        httpSession.setAttribute(USER, user);
        Map<Integer, Friend> allFriends = userDao.getAllFriends();
        httpSession.getServletContext().setAttribute(FRIENDS, allFriends);
        for(String mess : message){
        broadcast(mess, who, whom, true);}
    }


    @OnClose
    public void end() {
        connections.remove(who);
        String message = String.format("* %s %s",
                nickname, "has disconnected.");
        //broadcast(message);
    }


    @OnMessage
    public void incoming(String message) {
        // Never trust the client
        if(isFirst==true){
            userDao.addNotification(who, whom);
            isFirst=false;
        }
        String filteredMessage = String.format("%s: %s",
                nickname, HTMLFilter.filter(message.toString()));
        broadcast(filteredMessage, who, whom, false);
        messDao.addMessage(message, nickname, who, whom);
    }




    @OnError
    public void onError(Throwable t) throws Throwable {
        LOG.warn("Chat Error (maybe chat just closed by user): " + t.toString());
    }


    private static void broadcast(String msg, int from, int to, boolean isHistory) {
        ConcurrentHashMap<Integer, ChatAnnotation> conns = new ConcurrentHashMap<>();
        conns.put(from, connections.get(from));
        if(connections.get(to)!=null&&!isHistory){
            conns.put(to, connections.get(to));
        }
        for (Map.Entry<Integer, ChatAnnotation> pair : conns.entrySet())
        {
            ChatAnnotation client = pair.getValue();
            {
                try {
                    synchronized (client) {
                        client.session.getBasicRemote().sendText(msg);
                    }
                } catch (IOException e) {
                    LOG.warn("Chat Error: Failed to send message to client", e);
                    connections.remove(client);
                    try {
                        client.session.close();
                    } catch (IOException e1) {
                        LOG.warn("Problem with closing session", e1);
                    }
                }
            }
        }
    }
}