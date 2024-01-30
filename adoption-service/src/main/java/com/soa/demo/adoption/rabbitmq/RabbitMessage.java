package com.soa.demo.adoption.rabbitmq;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

@Data
@AllArgsConstructor
public class RabbitMessage implements Serializable {
    private String emailAddress;
    private String message;
}
