package com.soa.demo.authservice.jwt;

import com.soa.demo.authservice.security.CustomUserDetailsService;
import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.SignatureException;
import java.util.Date;

@Component
@Slf4j
public class JWTUtils {
    
    @Value(value = "${jwt.secret}")
    private final String jwtSecret;

    private final int jwtExpirationMs = 7200000;

    private final JwtParser jwtParser;

    public JWTUtils() { this.jwtParser = Jwts.parser().setSigningKey(jwtSecret).build(); }

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    public String generateJwtToken(String username) {
        return generateTokenFromUsername(username);
    }

    public String generateTokenFromUsername(String username) {
        UserDetails userDetails= customUserDetailsService.loadUserByUsername(username);
        StringBuilder roles=new StringBuilder();
        userDetails.getAuthorities().forEach(role->{
            roles.append(role.getAuthority()+" ");
        });
        return Jwts.builder()
                .setSubject(username)
                .setIssuer(roles.toString())
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + jwtExpirationMs))
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();
    }

    public String getUserNameFromJwtToken(String token) {
        return jwtParser.parseClaimsJws(token).getBody().getSubject();
    }

    public boolean validateJwtToken(String authToken) {
        try {
            jwtParser.parseClaimsJws(authToken);
            return true;
        } catch (MalformedJwtException | ExpiredJwtException | UnsupportedJwtException | IllegalArgumentException e) {
            log.error(e.getMessage());
        }
        return false;
    }
}
