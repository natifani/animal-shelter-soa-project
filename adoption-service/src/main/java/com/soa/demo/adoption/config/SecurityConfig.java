package com.soa.demo.adoption.config;

import com.soa.demo.adoption.jwt.JWTAccessDeniedHandler;
import com.soa.demo.adoption.jwt.JWTAuthenticationEntryPoint;
import com.soa.demo.adoption.jwt.JWTAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity(debug = true)
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    private final JWTAuthenticationEntryPoint authenticationEntryPoint;
    private final JWTAccessDeniedHandler accessDeniedHandler;
    private final JWTAuthenticationFilter authenticationFilter;

    @Autowired
    public SecurityConfig(JWTAuthenticationEntryPoint authenticationEntryPoint, JWTAccessDeniedHandler accessDeniedHandler, JWTAuthenticationFilter authenticationFilter) {
        this.authenticationEntryPoint = authenticationEntryPoint;
        this.accessDeniedHandler = accessDeniedHandler;
        this.authenticationFilter = authenticationFilter;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .csrf().disable()
                .authorizeRequests()
                .requestMatchers(HttpMethod.POST, "/api/adoptions/**").hasRole("USER")
                .requestMatchers(HttpMethod.PATCH, "/api/adoptions/*/accept").hasRole("ADMIN")
                .requestMatchers(HttpMethod.GET, "/api/adoptions").hasRole("USER")
                .requestMatchers(HttpMethod.GET, "/api/adoptions/all").hasRole("ADMIN")
                .and()
                .authorizeRequests().anyRequest().permitAll()
                .and()
                .formLogin().disable()
                .httpBasic().disable()
                .exceptionHandling()
                .authenticationEntryPoint(authenticationEntryPoint)
                .accessDeniedHandler(accessDeniedHandler)
                .and()
                .addFilterBefore(authenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web)
                -> web.ignoring().requestMatchers("/authenticate/register", "/authenticate/login", "/authenticate/refreshtoken", "/authenticate/logout");
    }

}
