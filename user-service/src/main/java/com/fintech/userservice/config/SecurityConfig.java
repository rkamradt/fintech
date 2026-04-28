package com.fintech.userservice.config;

import com.fintech.userservice.security.XUserIdFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final XUserIdFilter xUserIdFilter;

    public SecurityConfig(XUserIdFilter xUserIdFilter) {
        this.xUserIdFilter = xUserIdFilter;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/actuator/health").permitAll()
                        .requestMatchers("/h2-console/**").permitAll()
                        // Manager-only
                        .requestMatchers(HttpMethod.POST, "/users").hasRole("MANAGER")
                        .requestMatchers(HttpMethod.PUT, "/users/*/role").hasRole("MANAGER")
                        .requestMatchers(HttpMethod.PUT, "/users/*/status").hasRole("MANAGER")
                        .requestMatchers(HttpMethod.GET, "/users/tellers").hasRole("MANAGER")
                        // Teller or Manager
                        .requestMatchers(HttpMethod.GET, "/users/customers").hasAnyRole("TELLER", "MANAGER")
                        // Any authenticated user
                        .anyRequest().authenticated()
                )
                .headers(headers -> headers.frameOptions(fo -> fo.disable()))
                .addFilterBefore(xUserIdFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }
}
