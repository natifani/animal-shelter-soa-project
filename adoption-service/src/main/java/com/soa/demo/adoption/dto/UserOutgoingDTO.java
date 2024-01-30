package com.soa.demo.adoption.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class UserOutgoingDTO implements Serializable {
    private String username;
    private String email;
}
