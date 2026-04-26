package com.fintech.userservice.service;

import com.fintech.userservice.dto.LoginRequest;
import com.fintech.userservice.dto.LoginResponse;
import com.fintech.userservice.dto.ValidateTokenRequest;
import com.fintech.userservice.dto.ValidateTokenResponse;
import com.fintech.userservice.event.UserEventProducer;
import com.fintech.userservice.model.User;
import com.fintech.userservice.model.UserStatus;
import com.fintech.userservice.repository.UserRepository;
import com.fintech.userservice.security.JwtUtil;
import io.jsonwebtoken.Claims;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final UserEventProducer eventProducer;

    public AuthService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder,
                       JwtUtil jwtUtil,
                       UserEventProducer eventProducer) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
        this.eventProducer = eventProducer;
    }

    public LoginResponse login(LoginRequest req) {
        User user = userRepository.findByUsername(req.getUsername())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid credentials"));

        if (user.getStatus() == UserStatus.INACTIVE) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Account is inactive");
        }

        if (!passwordEncoder.matches(req.getPassword(), user.getPasswordHash())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid credentials");
        }

        String token = jwtUtil.generate(user.getId(), user.getUsername(), user.getRole());
        eventProducer.publishUserAuthenticated(user.getId(), user.getRole());

        return new LoginResponse(token, user.getId(), user.getUsername(), user.getRole());
    }

    public ValidateTokenResponse validate(ValidateTokenRequest req) {
        if (!jwtUtil.isValid(req.getToken())) {
            return new ValidateTokenResponse(null, null, null, false);
        }
        Claims claims = jwtUtil.parse(req.getToken());
        return new ValidateTokenResponse(
                jwtUtil.getUserId(claims),
                jwtUtil.getUsername(claims),
                jwtUtil.getRole(claims),
                true
        );
    }
}
