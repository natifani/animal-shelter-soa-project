package com.soa.demo.animalshelter.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Encoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;

@Component
@Slf4j
public class JWTUtils extends SecurityProperties.Filter {

    @Value(value = "${jwt.secret}")
    private final String jwtSecret;

    private final JwtParser jwtParser;

    public JWTUtils(){
        this.jwtParser = Jwts.parser().setSigningKey(jwtSecret).build();
    }

    public String getUserNameFromJwtToken(String token) {
        if(token.startsWith("Bearer")){
            token = token.substring(7);
        }
        String username= jwtParser.parseClaimsJws(token).getBody().getSubject();
        return username;
    }
    public String[] getRoleNamesFromJwtToken(String token) {
        if(token.startsWith("Bearer")){
            token = token.substring(7);
        }
        String username=jwtParser.parseClaimsJws(token).getBody().getIssuer();
        return  username.split(" ");
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
