package com.soa.demo.emailservice.model;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class RabbitMessage implements Serializable {
    private String emailAddress;
    private String message;
}

