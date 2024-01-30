package com.soa.demo.authservice.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserLoginIncomingDTO {

    @NotBlank
    private String username;

    @NotBlank
    private String password;
}
