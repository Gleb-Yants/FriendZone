package service;

import dao.interfaces.UserDao;
import model.User;
import org.mindrot.jbcrypt.BCrypt;

import java.util.Optional;

/**
Checking login and pass
 */
public class SecurityService {

    private UserDao userDao;

    public SecurityService(UserDao userDao) {
        this.userDao = userDao;
    }

    public Optional<User> checkAndGetUser(String userName, String password) {

        final Optional<String> passwordByEmail = userDao.getPasswordByEmail(userName);

        if (passwordByEmail.isPresent()) {
            String pwdHash = passwordByEmail.get();
            return BCrypt.checkpw(password, pwdHash)//jbcrypt
                    ? userDao.getUserByEmail(userName)
                    : Optional.empty();
        } else
            return Optional.empty();

    }
}
