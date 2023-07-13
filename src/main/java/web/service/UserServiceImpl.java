package web.service;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.context.annotation.Bean;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import org.springframework.transaction.support.TransactionTemplate;
import web.dao.UserRepository;
import web.model.User;


import javax.transaction.Transactional;
import javax.validation.ValidationException;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {


    private final UserRepository userRepository;
    private final TransactionTemplate transactionTemplate;


    @Autowired
    public UserServiceImpl(UserRepository userRepository, TransactionTemplate transactionTemplate) {
        this.userRepository = userRepository;

        this.transactionTemplate = transactionTemplate;
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
    @Transactional
    public void editUser(User user) {

        user.setPassword(encoder().encode(user.getPassword()));

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
