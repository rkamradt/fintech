package com.fintech.transactionservice.config;

import com.fintech.transactionservice.security.XUserIdFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    private final XUserIdFilter xUserIdFilter;

    public SecurityConfig(XUserIdFilter xUserIdFilter) {
        this.xUserIdFilter = xUserIdFilter;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/h2-console/**", "/actuator/**").permitAll()
                .anyRequest().authenticated()
            )
            .headers(headers -> headers.frameOptions(fo -> fo.disable()))
            .addFilterBefore(xUserIdFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
