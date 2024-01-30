package com.soa.demo.adoption.mapper;


import com.soa.demo.adoption.dto.AdoptionRequestIncomingDTO;
import com.soa.demo.adoption.dto.AdoptionRequestOutgoingDTO;
import com.soa.demo.adoption.model.AdoptionRequest;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring", uses = {AnimalMapper.class, UserMapper.class})
public abstract class AdoptionMapper {
    public abstract AdoptionRequestOutgoingDTO adoptionToDto(AdoptionRequest adoptionRequest);
    public abstract AdoptionRequest dtoToAdoption(AdoptionRequestIncomingDTO adoptionRequestIncomingDTO);
    @IterableMapping(elementTargetType = AdoptionRequestOutgoingDTO.class)
    public abstract List<AdoptionRequestOutgoingDTO> adoptionsToDtos(List<AdoptionRequest> adoptionRequests);
}
