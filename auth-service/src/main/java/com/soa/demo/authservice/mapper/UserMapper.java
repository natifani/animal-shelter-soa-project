package com.soa.demo.authservice.mapper;

import com.soa.demo.authservice.dto.UserRegisterIncomingDTO;
import com.soa.demo.authservice.model.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public abstract class UserMapper {
    // public abstract User registerDtoToUser(UserRegisterIncomingDTO userRegisterIncomingDTO);
}
