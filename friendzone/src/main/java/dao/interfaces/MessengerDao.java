package dao.interfaces;

/**
DAO for Messenger
 */
public interface MessengerDao {
    /**
    Add message in db
     */
    public void addMessage(String msg, String fromName, int from, int to);
    /**
     get conversation between 2 users
     */
    public String getConversation(int first, int second);
}
