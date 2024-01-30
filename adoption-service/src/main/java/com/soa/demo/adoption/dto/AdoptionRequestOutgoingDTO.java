package com.soa.demo.adoption.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class AdoptionRequestOutgoingDTO implements Serializable {
    private Long id;
    private UserOutgoingDTO user;
    private AnimalOutgoingDTO animal;
    private Date requestDate;
}
