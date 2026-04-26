package com.fintech.userservice.event;

import com.fintech.userservice.model.UserRole;
import com.fintech.userservice.model.UserStatus;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Map;
import java.util.UUID;

@Component
public class UserEventProducer {

    private static final String TOPIC_USER_CREATED = "user.created";
    private static final String TOPIC_ROLE_CHANGED = "user.role.changed";
    private static final String TOPIC_STATUS_CHANGED = "user.status.changed";
    private static final String TOPIC_AUTHENTICATED = "user.authenticated";

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public UserEventProducer(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void publishUserCreated(UUID userId, UserRole role, String createdBy,
                                   String firstName, String lastName, String email) {
        Map<String, Object> event = Map.of(
                "userId", userId.toString(),
                "role", role.name(),
                "createdBy", createdBy,
                "customerProfile", Map.of(
                        "firstName", firstName != null ? firstName : "",
                        "lastName", lastName != null ? lastName : "",
                        "email", email != null ? email : ""
                )
        );
        kafkaTemplate.send(TOPIC_USER_CREATED, userId.toString(), event);
    }

    public void publishRoleChanged(UUID userId, UserRole newRole, UserRole previousRole, String changedBy) {
        Map<String, Object> event = Map.of(
                "userId", userId.toString(),
                "newRole", newRole.name(),
                "previousRole", previousRole.name(),
                "changedBy", changedBy
        );
        kafkaTemplate.send(TOPIC_ROLE_CHANGED, userId.toString(), event);
    }

    public void publishStatusChanged(UUID userId, UserStatus newStatus, UserStatus previousStatus, String changedBy) {
        Map<String, Object> event = Map.of(
                "userId", userId.toString(),
                "newStatus", newStatus.name(),
                "previousStatus", previousStatus.name(),
                "changedBy", changedBy
        );
        kafkaTemplate.send(TOPIC_STATUS_CHANGED, userId.toString(), event);
    }

    public void publishUserAuthenticated(UUID userId, UserRole role) {
        Map<String, Object> event = Map.of(
                "userId", userId.toString(),
                "role", role.name(),
                "loginTimestamp", Instant.now().toString()
        );
        kafkaTemplate.send(TOPIC_AUTHENTICATED, userId.toString(), event);
    }
}
