package com.soa.demo.adoption.mapper;

import com.soa.demo.adoption.dto.UserOutgoingDTO;
import com.soa.demo.adoption.model.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public abstract class UserMapper {
    public abstract UserOutgoingDTO userToDto(User user);
}
