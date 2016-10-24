package dao.mysql;

import dao.interfaces.UserDao;
import exception.InvalidEmailException;
import model.Friend;
import model.User;
import org.apache.log4j.Logger;

import java.io.InputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.*;

import static util.CreateFriendsArray.varcharToArrayList;
import static listeners.Provider.dataSource;

/**
 DAO implementation for MySQL
 */
public class MySQLUserDao implements UserDao {
    private static final Logger LOG= Logger.getLogger(MySQLUserDao.class);
    public void registerUser(String email, String pass) throws InvalidEmailException{
        try(Connection conn = dataSource.getConnection()){
            Statement S = conn.createStatement();
            ResultSet RS = S.executeQuery("SELECT login FROM users");
            while (RS.next()){if (RS.getString(1).equals(email)) throw new InvalidEmailException();}
            PreparedStatement stmt = conn.prepareStatement(
                        "INSERT INTO users " +
                                "(login, pass)" +
                                "VALUES (?, ?)");
                stmt.setString(1, email);
                stmt.setString(2, pass);
                stmt.execute();
        conn.close();
        }
        catch(SQLException ex){LOG.error("Caught exception: ", ex);}
    }

    public Optional<String> getPasswordByEmail(String email){
        Optional<String> pass = Optional.empty();
        try(Connection conn = dataSource.getConnection()){
            PreparedStatement stmt = conn.prepareStatement("SELECT pass FROM users WHERE login = ?");
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                pass = Optional.of(rs.getString(1));
            }
        }
        catch(SQLException ex){LOG.error("Caught exception: ", ex);}
        return pass;
    }

    public Optional<User> getUserByEmail(String email){
        Optional<User> user = Optional.empty();
        try(Connection conn = dataSource.getConnection();){
            PreparedStatement stmt = conn.prepareStatement("SELECT user_id, pass, firstName," +
                    "lastName, telephone, aboutMe, photo, friends, notifications FROM users WHERE login = ?");
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                user = Optional.of(new User(rs.getInt(1), email, rs.getString(2), rs.getString(3),rs.getString(4),
                        rs.getInt(5),rs.getString(6),rs.getString(7),varcharToArrayList(rs.getString(8)),varcharToArrayList(rs.getString(9))));
            }//varcharToArrayList - util method for transforming string from db to ArrayList
        }
        catch(SQLException ex){LOG.error("Caught exception: ", ex);}
        return user;
    }

    public Optional<User> getUserById(int id){
        Optional<User> user = Optional.empty();
        try(Connection conn = dataSource.getConnection();){
            PreparedStatement stmt = conn.prepareStatement("SELECT login, pass, firstName," +
                    "lastName, telephone, aboutMe, photo, friends, notifications FROM users WHERE user_id = ?");
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                user = Optional.of(new User(id, rs.getString(1), rs.getString(2), rs.getString(3),rs.getString(4),
                        rs.getInt(5),rs.getString(6),rs.getString(7),varcharToArrayList(rs.getString(8)),varcharToArrayList(rs.getString(9))));
            }//varcharToArrayList - util method for transforming string from db to ArrayList
        }
        catch(SQLException ex){LOG.error("Caught exception: ", ex);}
        return user;
    }

    public void storePhoto(User user, String photoName){
        try(Connection conn = dataSource.getConnection();) {
            String sql = "UPDATE users SET photo=? where login=?";
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setString(1, photoName);
            statement.setString(2, user.getLogin());
            statement.execute();}catch(SQLException ex){LOG.error("Caught exception: ", ex);}
}
    public void changeUserSettings(User user, String param, String paramValue){//param=column name in users table
        try(Connection conn = dataSource.getConnection();) {
            String sql="";
            switch (param) {
                case "firstName":   sql = "UPDATE users SET firstName=? where login=?";
                    break;
                case "lastName":    sql = "UPDATE users SET lastName=? where login=?";
                    break;
                case "telephone":    sql = "UPDATE users SET telephone=? where login=?";
                    break;
                case "aboutMe":   sql = "UPDATE users SET aboutMe=? where login=?";
                    break;
            }
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setString(1, paramValue);
            statement.setString(2, user.getLogin());
            statement.execute();}catch(SQLException ex){LOG.error("Caught exception: ", ex);}
    }
// Get all users in system with friend model
    public Map<Integer, Friend> getAllFriends(){
        Friend friend;
        Map<Integer, Friend> friends = new HashMap<>();
        try(Connection conn = dataSource.getConnection();){
            PreparedStatement stmt = conn.prepareStatement("SELECT user_id, firstName," +
                    "lastName, telephone, aboutMe, photo FROM users");
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                friend = (new Friend(rs.getInt(1), rs.getString(2), rs.getString(3),rs.getInt(4),
                        rs.getString(5),rs.getString(6)));
                friends.put(friend.getId(), friend);
            }
        }
        catch(SQLException ex){LOG.error("Caught exception: ", ex);}
        return friends;
    }

    public void addFriend(String myId, String friendId){
        try(Connection conn = dataSource.getConnection();) {
            PreparedStatement stmt = conn.prepareStatement("SELECT friends FROM users WHERE user_id=?");
            stmt.setString(1, myId);
            ResultSet rs = stmt.executeQuery();
            StringBuilder friends = new StringBuilder();
            while (rs.next()) {
                friends.append(rs.getString(1));
                friends.append(friendId+",");//storing friend id in varchar column
            }
            String sql = "UPDATE users SET friends=? where user_id=?";
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setString(1, friends.toString());
            statement.setString(2, myId);
            statement.execute();}catch(SQLException ex){LOG.error("Caught exception: ", ex);}
    }

    public void addNotification(int from, int to){
        try(Connection conn = dataSource.getConnection()){
            PreparedStatement stmt = conn.prepareStatement("SELECT notifications FROM users WHERE user_id = ?");
            stmt.setInt(1, to);
            ResultSet rs = stmt.executeQuery();
            String notifTemp="";
            while (rs.next()) {
                notifTemp = rs.getString(1);//getting current notifications
            }
            ArrayList<String> notifTemp2 = new ArrayList<>(Arrays.asList((notifTemp.split(","))));
                if(!notifTemp2.contains(Integer.toString(from))){
                    String sql = "UPDATE users SET notifications=CONCAT(notifications, ?) where user_id=?";
                    PreparedStatement statement = conn.prepareStatement(sql);
                    statement.setString(1, from+",");//refreshing notifications with concatenation
                    statement.setInt(2, to);
                    statement.execute();

        }}
        catch(SQLException ex){LOG.error("Caught exception: ", ex);}
    }
    public void removeNotification(int from, int to){
        try(Connection conn = dataSource.getConnection()){
            PreparedStatement stmt = conn.prepareStatement("SELECT notifications FROM users WHERE user_id = ?");
            stmt.setInt(1, to);
            ResultSet rs = stmt.executeQuery();
            String notifTemp = "";
            while (rs.next()) {
                notifTemp = rs.getString(1);//getting current notifications
            }
            ArrayList<String> notifTemp2 = new ArrayList<>(Arrays.asList((notifTemp.split(","))));
            if(notifTemp2.contains(Integer.toString(from))){
                notifTemp2.remove(Integer.toString(from));//removing notification of user from
                StringBuilder resultNotif = new StringBuilder("");
                for(String str : notifTemp2){
                    resultNotif.append(str+",");
                }
                String sql = "UPDATE users SET notifications=? where user_id=?";
                PreparedStatement statement = conn.prepareStatement(sql);
                statement.setString(1, resultNotif.toString());
                statement.setInt(2, to);
                statement.execute();
            }
        }
        catch(SQLException ex){LOG.error("Caught exception: ", ex);}
    }
}
