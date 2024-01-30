package com.soa.demo.authservice.service;

import com.soa.demo.authservice.kafka.message.AuthMessage;
import com.soa.demo.authservice.kafka.producer.AuthProducer;
import com.soa.demo.authservice.model.User;
import com.soa.demo.authservice.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final AuthProducer authProducer;

    @Autowired
    public UserService(UserRepository userRepository, AuthProducer authProducer) {
        this.userRepository = userRepository;
        this.authProducer = authProducer;
    }

    public void create(User user) {
        User newUser = userRepository.save(user);
        AuthMessage authMessage = new AuthMessage(newUser);
        authProducer.send(authMessage);
    }

    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public Boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    public Boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    public User findById(long userId) {
        return userRepository.findById(userId);
    }

}
