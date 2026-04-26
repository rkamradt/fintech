package com.fintech.userservice;

import com.fintech.userservice.model.User;
import com.fintech.userservice.model.UserRole;
import com.fintech.userservice.model.UserStatus;
import com.fintech.userservice.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class UserServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(UserServiceApplication.class, args);
    }

    // Bootstrap a default manager so the system is usable from a clean state
    @Bean
    CommandLineRunner seedDefaultManager(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            if (userRepository.count() == 0) {
                User manager = new User();
                manager.setUsername("admin");
                manager.setPasswordHash(passwordEncoder.encode("admin"));
                manager.setRole(UserRole.MANAGER);
                manager.setStatus(UserStatus.ACTIVE);
                manager.setCreatedBy("system");
                userRepository.save(manager);
            }
        };
    }
}
