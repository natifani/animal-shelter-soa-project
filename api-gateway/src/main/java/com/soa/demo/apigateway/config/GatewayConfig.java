package com.soa.demo.apigateway.config;

import com.soa.demo.apigateway.filter.JWTAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayConfig {

    private final JWTAuthenticationFilter authFilter;

    public GatewayConfig(JWTAuthenticationFilter authFilter) {
        this.authFilter = authFilter;
    }

    @Autowired


    @Bean
    public RouteLocator routes(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("AUTH-SERVICE", r -> r.path("/authenticate/**").filters(f -> f.filter(authFilter)).uri("lb://AUTH-SERVICE"))
                .route("ANIMAL-SHELTER-SERVICE", r -> r.path("/api/animals/**").filters(f -> f.filter(authFilter)).uri("lb://ANIMAL-SHELTER-SERVICE"))
                .build();
    }
}
