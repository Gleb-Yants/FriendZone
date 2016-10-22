package dao.interfaces;

import exception.InvalidEmailException;
import model.Friend;
import model.User;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
DAO for User
 */
public interface UserDao {
    void registerUser(String email, String pass) throws InvalidEmailException;
    Optional<String> getPasswordByEmail(String email);
    Optional<User> getUserByEmail(String email);
    Optional<User> getUserById(int id);
    /**
store photo name in db and file in fs
     */
    public void storePhoto(User user, String photoName);
    /**
change first and last name, avatar, info about user...
     */
    public void changeUserSettings(User user, String param, String paramValue);
    /**
get all "friends", that there are in social network
     */
    public Map<Integer, Friend> getAllFriends();
    /**
add friend (in fact add in following) for certain user
     */
    public void addFriend(String myId, String friendId);
    public void addNotification(int from, int to);
    public void removeNotification(int from, int to);
}
