package web.service;


import org.springframework.validation.BindingResult;
import web.model.User;

import java.util.List;

public interface UserService {
    List<User> getAllUsers();

    void saveUser(User user);


    void removeUser(long id);

    void editUser(User user);

    User getUserById(long id);

    User getByUserName(String userName);


}
