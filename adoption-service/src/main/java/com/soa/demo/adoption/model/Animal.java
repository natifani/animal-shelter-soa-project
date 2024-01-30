package com.soa.demo.adoption.model;

import com.soa.demo.adoption.util.Gender;
import com.soa.demo.adoption.util.Status;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Animal {
    private static final long serialVersionUID = 1L;
    @Id
    private Long id;
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
    @Column(unique = true)
    private Long kennelNumber;
    @NonNull
    private Status adoptionStatus;
    @Lob
    private byte[] image;
}

