package com.soa.demo.animalshelter.dto;

import com.soa.demo.animalshelter.util.Gender;
import com.soa.demo.animalshelter.util.Status;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NonNull;

import java.io.Serializable;
import java.util.Date;

@Data
public class AnimalOutgoingDTO implements Serializable {

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
    private byte[] image;
}
