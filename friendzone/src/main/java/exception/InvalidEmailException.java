package exception;

import javax.servlet.ServletException;
/**
Exception for incorrect email
 */
public class InvalidEmailException extends ServletException {
    @Override
    public String getMessage() {
        return "Incorrect email name (некорректный емейл)";
    }
}
