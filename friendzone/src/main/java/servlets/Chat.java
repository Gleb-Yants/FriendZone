package servlets;
/**
WebChat service - chat on websocket between 2 users, with history, notifications about new messages.
 */

        import static filters.SecurityFilter.USER;
        import java.io.IOException;
        import java.util.Map;
        import java.util.concurrent.ConcurrentHashMap;

        import javax.servlet.http.HttpSession;
        import javax.websocket.*;
        import javax.websocket.server.ServerEndpoint;

        import dao.interfaces.MessengerDao;
        import dao.interfaces.UserDao;
        import model.User;

        import static listeners.Provider.MESSENGER_DAO;
        import static listeners.Provider.USER_DAO;

        import org.apache.log4j.Logger;
        import util.GetHttpSessionConfigurator;
        import util.HTMLFilter;

@ServerEndpoint(value = "/websocket/chat", configurator = GetHttpSessionConfigurator.class)
public class Chat {
    private static final Logger LOG= Logger.getLogger(Chat.class);

    private static final ConcurrentHashMap<Integer, Chat> connections =
            new ConcurrentHashMap<>();

    private String nickname;
    private Session session;//for websocket
    private HttpSession httpSession;
    private int sender;//user id
    private int recipient;//user id
    private MessengerDao messDao;
    private UserDao userDao;
    private boolean isFirst = true;//add notification for recipient for first message

    public Chat() {
    }

    @OnOpen
    public void start(Session session, EndpointConfig config) {
        this.session = session;
        this.httpSession = (HttpSession) config.getUserProperties().get(HttpSession.class.getName());
        int id = ((User) httpSession.getAttribute(USER)).getId();
        connections.put(id, this);
        sender=id;
        nickname = ((User) httpSession.getAttribute(USER)).getFirstName();
        recipient = (Integer) httpSession.getAttribute("friendId");
        messDao = (MessengerDao) httpSession.getServletContext().getAttribute(MESSENGER_DAO);
        userDao = (UserDao) httpSession.getServletContext().getAttribute(USER_DAO);
        String [] message = messDao.getConversation(sender,  recipient).split("\n");//upload history
        userDao.removeNotification( recipient, sender);//mean that user read new message if it exist
        User user = userDao.getUserById(sender).get();
        httpSession.setAttribute(USER, user);//refresh user session attribute because of notification field
        //Map<Integer, Friend> allFriends = userDao.getAllFriends();
        //httpSession.getServletContext().setAttribute(FRIENDS, allFriends);
        for(String mess : message){
        broadcast(mess, sender,  recipient, true);}
    }

    @OnClose
    public void end() {
        connections.remove(sender);
    }

    @OnMessage
    public void incoming(String message) {
        if(isFirst==true){
            userDao.addNotification(sender,  recipient);//add notification for recipient for first message
            isFirst=false;
        }
        String filteredMessage = String.format("%s: %s",
                nickname, HTMLFilter.filter(message.toString()));//user: message...
        broadcast(filteredMessage, sender,  recipient, false);
        messDao.addMessage(message, nickname, sender,  recipient);//store in db; nickname==sender
    }

    @OnError
    public void onError(Throwable t) throws Throwable {
        LOG.info("(Expected, chat suddenly closed by user): " + t.toString());
    }

    private static void broadcast(String msg, int from, int to, boolean isHistory) {
        ConcurrentHashMap<Integer, Chat> conns = new ConcurrentHashMap<>();
        conns.put(from, connections.get(from));
        if(connections.get(to)!=null&&!isHistory){//duplicate history for enter
            conns.put(to, connections.get(to));
        }
        for (Map.Entry<Integer, Chat> pair : conns.entrySet())
        {
            Chat client = pair.getValue();
            {
                try {
                    synchronized (client) {
                        client.session.getBasicRemote().sendText(msg);
                    }
                } catch (IOException e) {
                    LOG.error("Chat Error: Failed to send message to client", e);
                    connections.remove(client);
                    try {
                        client.session.close();
                    } catch (IOException e1) {
                        LOG.error("Problem with closing session", e1);
                    }
                }
            }
        }
    }
}