package com.bookstore.jbehave.service;

import com.bookstore.jbehave.model.User;
import com.bookstore.jbehave.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public String registerUser(User user) {
        userRepository.save(user);
        return "User registered successfully!";
    }
}
