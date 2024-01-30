package com.soa.demo.authservice.controller;

import com.soa.demo.authservice.exception.RefreshTokenException;
import com.soa.demo.authservice.exception.RoleException;
import com.soa.demo.authservice.dto.*;
import com.soa.demo.authservice.jwt.JWTUtils;
import com.soa.demo.authservice.mapper.UserMapper;
import com.soa.demo.authservice.model.RefreshToken;
import com.soa.demo.authservice.model.Role;
import com.soa.demo.authservice.model.User;
import com.soa.demo.authservice.security.CustomUserDetails;
import com.soa.demo.authservice.service.RefreshTokenService;
import com.soa.demo.authservice.service.RoleService;
import com.soa.demo.authservice.service.UserService;
import com.soa.demo.authservice.util.RoleE;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/api/authenticate")
public class AuthController {
    private final UserService userService;
    private final RoleService roleService;
    private final RefreshTokenService refreshTokenService;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final JWTUtils jwtUtils;

    private final UserMapper userMapper;

    @Autowired
    public AuthController(UserService userService, RoleService roleService, RefreshTokenService refreshTokenService, AuthenticationManager authenticationManager, PasswordEncoder passwordEncoder, JWTUtils jwtUtils, UserMapper userMapper) {
        this.userService = userService;
        this.roleService = roleService;
        this.refreshTokenService = refreshTokenService;
        this.authenticationManager = authenticationManager;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtils = jwtUtils;
        this.userMapper = userMapper;
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> register(@RequestBody @Valid UserRegisterIncomingDTO userRegisterIncomingDTO) {
        if (userService.existsByUsername(userRegisterIncomingDTO.getUsername())) {
            return ResponseEntity.badRequest().body("Username already exists");
        }

        if(userService.existsByEmail(userRegisterIncomingDTO.getEmail())){
            return ResponseEntity.badRequest().body("Email already exists");
        }

        User user = new User();
        user.setEmail(userRegisterIncomingDTO.getEmail());
        user.setUsername(userRegisterIncomingDTO.getUsername());
        user.setPassword(passwordEncoder.encode(userRegisterIncomingDTO.getPassword()));
        Set<Role> roles = new HashSet<>();

        if (userRegisterIncomingDTO.getRoles() != null) {
            userRegisterIncomingDTO.getRoles().forEach(role -> {
                if (Objects.equals(role, "ROLE_ADMIN")) {
                    Role adminRole = null;
                    if(roleService.findByName(RoleE.ROLE_ADMIN).isEmpty()){
                        adminRole = new Role(RoleE.ROLE_ADMIN);
                    }else{
                        adminRole = roleService.findByName(RoleE.ROLE_ADMIN)
                                .orElseThrow(() -> new RoleException("Error: Admin Role is not found."));
                    }
                    roles.add(adminRole);
                } else {
                    Role userRole = null;

                    if(roleService.findByName(RoleE.ROLE_USER).isEmpty()){
                        userRole = new Role(RoleE.ROLE_USER);
                    }else{
                        userRole = roleService.findByName(RoleE.ROLE_USER)
                                .orElseThrow(() -> new RoleException("Error: User Role is not found."));
                    }

                    roles.add(userRole);
                }
            });
        } else {
            roleService.findByName(RoleE.ROLE_USER).ifPresentOrElse((role) -> roles.add(role), () -> roles.add(new Role(RoleE.ROLE_USER)));
        }

        user.setRoles(roles);
        userService.create(user);

        return ResponseEntity.ok("User registered successfully");
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody  @Valid UserLoginIncomingDTO userLoginIncomingDTO) {

        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(userLoginIncomingDTO.getUsername(),userLoginIncomingDTO.getPassword());

        Authentication authentication = authenticationManager.authenticate(usernamePasswordAuthenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        String jwt = jwtUtils.generateJwtToken(userDetails.getUsername());

        List<String> roles = userDetails.getAuthorities().stream().map(item -> item.getAuthority())
                .collect(Collectors.toList());

        RefreshToken refreshToken = refreshTokenService.createRefreshToken(userDetails.getId());

        JwtOutgoingDTO jwtOutgoingDTO = new JwtOutgoingDTO();
        jwtOutgoingDTO.setEmail(userDetails.getEmail());
        jwtOutgoingDTO.setUsername(userDetails.getUsername());
        jwtOutgoingDTO.setId(userDetails.getId());
        jwtOutgoingDTO.setToken(jwt);
        jwtOutgoingDTO.setRefreshToken(refreshToken.getToken());
        jwtOutgoingDTO.setRoles(roles);

        return ResponseEntity.ok(jwtOutgoingDTO);
    }

    @PostMapping("/refreshtoken")
    public ResponseEntity<?> refreshtoken(@RequestBody @Valid TokenRefreshIncomingDTO tokenRefreshIncomingDTO) {

        String requestRefreshToken = tokenRefreshIncomingDTO.getRefreshToken();

        RefreshToken token = refreshTokenService.findByToken(requestRefreshToken)
                .orElseThrow(() -> new RefreshTokenException(requestRefreshToken + "Refresh token is not in database!"));

        RefreshToken deletedToken = refreshTokenService.verifyExpiration(token);

        User userRefreshToken = deletedToken.getUser();

        String newToken = jwtUtils.generateTokenFromUsername(userRefreshToken.getUsername());

        return ResponseEntity.ok(new TokenRefreshOutgoingDTO(newToken, requestRefreshToken));
    }

    @DeleteMapping("/logout")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void logout(@RequestBody @Valid TokenRefreshIncomingDTO tokenRefreshIncomingDTO) {
        String requestRefreshToken = tokenRefreshIncomingDTO.getRefreshToken();

        RefreshToken token = refreshTokenService.findByToken(requestRefreshToken)
                .orElseThrow(() -> new RefreshTokenException(requestRefreshToken + "Refresh token is not in database!"));

        refreshTokenService.delete(token);
    }

}
