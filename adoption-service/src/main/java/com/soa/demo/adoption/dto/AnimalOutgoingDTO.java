package com.soa.demo.adoption.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class AnimalOutgoingDTO implements Serializable {
    Long id;
    private String name;
    private String breed;
}
