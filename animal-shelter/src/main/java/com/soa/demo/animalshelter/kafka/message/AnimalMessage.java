package com.soa.demo.animalshelter.kafka.message;

import com.soa.demo.animalshelter.model.Animal;
import com.soa.demo.animalshelter.util.Gender;
import com.soa.demo.animalshelter.util.Status;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AnimalMessage implements Serializable {
    private Long id;
    private String name;
    private String breed;
    private Integer age;
    private Gender gender;
    private Float weight;
    private String color;
    private Date arrivalDate;
    private String medicalInformation;
    private String specialNeeds;
    private Long kennelNumber;
    private Status adoptionStatus;
    private String action;

    public AnimalMessage(Animal animal, String action) {
        this.id = animal.getId();
        this.name = animal.getName();
        this.breed = animal.getBreed();
        this.age = animal.getAge();
        this.gender = animal.getGender();
        this.weight = animal.getWeight();
        this.color = animal.getColor();
        this.arrivalDate = animal.getArrivalDate();
        this.medicalInformation = animal.getMedicalInformation();
        this.specialNeeds = animal.getSpecialNeeds();
        this.kennelNumber = animal.getKennelNumber();
        this.adoptionStatus = animal.getAdoptionStatus();
        this.action = action;
    }
}
