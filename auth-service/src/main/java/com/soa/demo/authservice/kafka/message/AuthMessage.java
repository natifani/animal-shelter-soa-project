package com.soa.demo.authservice.kafka.message;

import com.soa.demo.authservice.model.Role;
import com.soa.demo.authservice.model.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AuthMessage implements Serializable {
    private Long id;
    private String username;
    private String password;
    private String email;
    private Set<Role> roles;

    public AuthMessage(User user) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.password = user.getPassword();
        this.email = user.getEmail();
        this.roles = user.getRoles();
    }
}
