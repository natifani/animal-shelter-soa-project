package com.soa.demo.adoption.kafka.listener;

import com.soa.demo.adoption.kafka.message.AnimalMessage;
import com.soa.demo.adoption.kafka.message.AuthMessage;
import com.soa.demo.adoption.model.User;
import com.soa.demo.adoption.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class AuthListener {
    private UserService userService;

    @Autowired
    public AuthListener(UserService userService) {
        this.userService = userService;
    }

    @KafkaListener(topics = "auth-topic", containerFactory = "kafkaListenerAuthContainerFactory")
    public void newAuthListener(AuthMessage authMessage) {
        User user = new User();
        user.setId(authMessage.getId());
        user.setEmail(authMessage.getEmail());
        user.setUsername(authMessage.getUsername());
        user.setPassword(authMessage.getPassword());
        user.setRoles(authMessage.getRoles());
        userService.create(user);
    }
}
