package web.service;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.context.annotation.Bean;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import web.dao.UserRepository;
import web.model.User;

import javax.validation.ValidationException;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;


    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;

    }

    @Bean
    private BCryptPasswordEncoder encoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }


    @Override
    public void removeUser(long id) {
        userRepository.deleteById(id);
    }

    @Override
    public void saveUser(User user) throws ValidationException {

        user.setPassword(encoder().encode(user.getPassword()));
        if (getByUserName(user.getUsername()) != null) {
            // Пользователь уже существует
            throw new ValidationException("Пользователь уже существует");
        }
        userRepository.save(user);


    }


    @Override
    public User getUserById(long id) {
        return userRepository.getById(id);
    }

    @Override
    public User getByUserName(String userName) {
        return userRepository.findByUserName(userName);
    }


}
