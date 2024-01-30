package com.soa.demo.animalshelter.dto;

import com.soa.demo.animalshelter.util.Gender;
import com.soa.demo.animalshelter.util.Status;
import jakarta.persistence.Column;
import jakarta.persistence.Lob;
import lombok.Data;
import lombok.NonNull;

import java.io.Serializable;
import java.util.Date;

@Data
public class AnimalIncomingDTO implements Serializable {
    @NonNull
    private String name;
    @NonNull
    private String breed;
    private Integer age;
    @NonNull
    private Gender gender;
    @NonNull
    private Float weight;
    @NonNull
    private String color;
    @NonNull
    private Date arrivalDate;
    @Column(columnDefinition = "text")
    private String medicalInformation;
    @Column(columnDefinition = "text")
    private String specialNeeds;
    @NonNull
    private Long kennelNumber;
    @NonNull
    private Status adoptionStatus;
}
