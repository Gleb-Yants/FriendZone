package dao.mysql;

import dao.interfaces.MessengerDao;
import exception.InvalidEmailException;
import model.Friend;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static listeners.Provider.FRIENDS;
import static listeners.Provider.dataSource;
import static util.GetLessValue.getLessValue;
/**
DAO implementation for MySQL
 */
public class MySQLMessengerDao implements MessengerDao {
    Map<Integer, Friend> friends;

    public MySQLMessengerDao(Map<Integer, Friend> friends) {
        this.friends = friends;
    }

    @Override
    public void addMessage(String msg, String fromName, int from, int to) {
        boolean convFirst = false;
        String betweenWhom = getLessValue(from,to);
        try(Connection conn = dataSource.getConnection()){
            Statement S = conn.createStatement();
            ResultSet RS = S.executeQuery("SELECT betweenThese FROM messages");
            while (RS.next()){if (RS.getString(1).equals(betweenWhom)){
                PreparedStatement stmt = conn.prepareStatement(
                        "UPDATE messages SET conversation=CONCAT(conversation, ?) WHERE betweenThese"+
                                "=?");
                stmt.setString(1, fromName+": "+msg+"\n");
                stmt.setString(2, betweenWhom);
                stmt.execute();
                convFirst=true;
            }}
            if(!convFirst){
                PreparedStatement stmt = conn.prepareStatement(
                        "INSERT INTO messages (betweenThese, conversation) VALUES (?, ?)");
                stmt.setString(1, betweenWhom);
                stmt.setString(2, fromName+": "+msg+"\n");
                stmt.execute();}
            conn.close();
        }
        catch(SQLException ex){ex.printStackTrace();}
    }

    @Override
    public String getConversation(int first, int second) {
        String betweenWhom = getLessValue(first,second);
        String convers = "empty";
        try(Connection conn = dataSource.getConnection()){
            PreparedStatement stmt = conn.prepareStatement("SELECT conversation FROM messages WHERE betweenThese=? ");
            stmt.setString(1, betweenWhom);
            ResultSet RS = stmt.executeQuery();
            while (RS.next()) {
               convers = RS.getString(1);
            }
            conn.close();
        }
        catch(SQLException ex){ex.printStackTrace();}
        return convers;
    }
}
