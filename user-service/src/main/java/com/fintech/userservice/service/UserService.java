package com.fintech.userservice.service;

import com.fintech.userservice.dto.CreateUserRequest;
import com.fintech.userservice.dto.UpdateRoleRequest;
import com.fintech.userservice.dto.UpdateStatusRequest;
import com.fintech.userservice.dto.UserResponse;
import com.fintech.userservice.event.UserEventProducer;
import com.fintech.userservice.model.User;
import com.fintech.userservice.model.UserRole;
import com.fintech.userservice.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserEventProducer eventProducer;

    public UserService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder,
                       UserEventProducer eventProducer) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.eventProducer = eventProducer;
    }

    public UserResponse createUser(CreateUserRequest req, String createdBy) {
        if (userRepository.findByUsername(req.getUsername()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Username already exists");
        }
        User user = new User();
        user.setUsername(req.getUsername());
        user.setPasswordHash(passwordEncoder.encode(req.getPassword()));
        user.setRole(req.getRole());
        user.setCreatedBy(createdBy);
        user.setFirstName(req.getFirstName());
        user.setLastName(req.getLastName());
        user.setEmail(req.getEmail());

        User saved = userRepository.save(user);
        eventProducer.publishUserCreated(
                saved.getId(), saved.getRole(), createdBy,
                saved.getFirstName(), saved.getLastName(), saved.getEmail());
        return new UserResponse(saved);
    }

    public UserResponse getUser(UUID userId) {
        return new UserResponse(findById(userId));
    }

    public UserResponse updateRole(UUID userId, UpdateRoleRequest req, String changedBy) {
        User user = findById(userId);
        var previousRole = user.getRole();
        user.setRole(req.getRole());
        User saved = userRepository.save(user);
        eventProducer.publishRoleChanged(saved.getId(), saved.getRole(), previousRole, changedBy);
        return new UserResponse(saved);
    }

    public UserResponse updateStatus(UUID userId, UpdateStatusRequest req, String changedBy) {
        User user = findById(userId);
        var previousStatus = user.getStatus();
        user.setStatus(req.getStatus());
        User saved = userRepository.save(user);
        eventProducer.publishStatusChanged(saved.getId(), saved.getStatus(), previousStatus, changedBy);
        return new UserResponse(saved);
    }

    public List<UserResponse> getTellers() {
        return userRepository.findByRole(UserRole.TELLER)
                .stream().map(UserResponse::new).collect(Collectors.toList());
    }

    public List<UserResponse> getCustomers() {
        return userRepository.findByRole(UserRole.CUSTOMER)
                .stream().map(UserResponse::new).collect(Collectors.toList());
    }

    private User findById(UUID userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
    }
}
