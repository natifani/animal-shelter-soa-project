package com.soa.demo.adoption.dto;

import lombok.Data;
import lombok.NonNull;

import java.io.Serializable;

@Data
public class AdoptionRequestIncomingDTO implements Serializable {
    @NonNull
    private Long userId;
    @NonNull
    private Long animalId;
}
