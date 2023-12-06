package com.soa.demo.animalshelter.model;

import com.soa.demo.animalshelter.util.Gender;
import com.soa.demo.animalshelter.util.Status;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.util.Date;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Animal {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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
