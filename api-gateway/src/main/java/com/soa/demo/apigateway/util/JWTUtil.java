package com.soa.demo.apigateway.util;

import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class JWTUtil {

    @Value(value = "${jwt.secret}")
    private final String jwtSecret;

    private final JwtParser jwtParser;

    public JWTUtil(){
        this.jwtParser = Jwts.parser().setSigningKey(jwtSecret).build();
    }

    public Claims getClaims(final String token) {
        try {
            Claims body = jwtParser
                    .parseClaimsJws(token).getBody();
            return body;
        } catch (Exception e) {
            System.out.println(e.getMessage() + " => " + e);
        }
        return null;
    }

    public boolean validateJwtToken(String authToken) {
        try {
            jwtParser.parseClaimsJws(authToken);
            return true;
        } catch (MalformedJwtException | ExpiredJwtException | UnsupportedJwtException | IllegalArgumentException e) {
            log.error("JwtUtils | validateJwtToken | Invalid JWT token: {}", e.getMessage());
            throw new MalformedJwtException(e.getMessage());
        }
    }

}
