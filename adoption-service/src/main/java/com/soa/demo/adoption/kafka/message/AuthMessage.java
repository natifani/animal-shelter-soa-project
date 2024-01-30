package com.soa.demo.adoption.kafka.message;

import com.soa.demo.adoption.model.Role;
import com.soa.demo.adoption.model.User;
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

}
