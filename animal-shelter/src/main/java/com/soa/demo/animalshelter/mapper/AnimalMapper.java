package com.soa.demo.animalshelter.mapper;

import com.soa.demo.animalshelter.dto.AnimalIncomingDTO;
import com.soa.demo.animalshelter.dto.AnimalOutgoingDTO;
import com.soa.demo.animalshelter.model.Animal;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public abstract class AnimalMapper {
    public abstract AnimalOutgoingDTO animalToDto(Animal animal);
    public abstract Animal dtoToAnimal(AnimalIncomingDTO animalIncomingDTO);
    @IterableMapping(elementTargetType = AnimalOutgoingDTO.class)
    public abstract List<AnimalOutgoingDTO> animalsToDtos(List<Animal> animal);
}
