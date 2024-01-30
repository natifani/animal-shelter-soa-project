package com.soa.demo.adoption.service;

import com.soa.demo.adoption.model.Animal;
import com.soa.demo.adoption.model.User;
import com.soa.demo.adoption.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void create(User user) {
        userRepository.save(user);
    }

    public boolean checkIfExists(Long id) {
        return userRepository.existsById(id);
    }

    public User findByUsername(String username) { return userRepository.findByUsername(username).orElse(null);
    }
    public User findById(Long id) { return userRepository.findById(id).orElse(null); }
}
