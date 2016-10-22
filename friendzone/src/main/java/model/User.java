package model;

/**
Model for user
 */
import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    int id;
    String login;
    String password;
    String firstName;
    String lastName;
    int telephone;
    String aboutMe;
    String photo;
    List<Integer> friends;
    List<Integer> notifications;
}
