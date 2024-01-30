package com.soa.demo.adoption.mapper;

import com.soa.demo.adoption.dto.AnimalOutgoingDTO;
import com.soa.demo.adoption.model.Animal;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public abstract class AnimalMapper {
    public abstract AnimalOutgoingDTO animalToDto(Animal animal);
}

