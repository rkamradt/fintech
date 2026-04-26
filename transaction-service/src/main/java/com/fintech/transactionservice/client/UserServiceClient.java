package com.fintech.transactionservice.client;

import com.fintech.transactionservice.dto.UserContext;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Component
public class UserServiceClient {

    private final RestTemplate restTemplate;
    private final String userServiceUrl;

    public UserServiceClient(RestTemplate restTemplate,
                             @Value("${services.user-service.url}") String userServiceUrl) {
        this.restTemplate = restTemplate;
        this.userServiceUrl = userServiceUrl;
    }

    public UserContext validateToken(String bearerToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", bearerToken);

        HttpEntity<Map<String, String>> request = new HttpEntity<>(
                Map.of("token", bearerToken.replace("Bearer ", "")),
                headers
        );

        ResponseEntity<UserContext> response = restTemplate.exchange(
                userServiceUrl + "/auth/validate",
                HttpMethod.POST,
                request,
                UserContext.class
        );
        return response.getBody();
    }
}
