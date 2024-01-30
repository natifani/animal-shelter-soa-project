package com.soa.demo.animalshelter.kafka.message;

import com.soa.demo.animalshelter.util.Status;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AdoptionMessage implements Serializable {
    private Long animalId;
    private Status adoptionStatus;
}

