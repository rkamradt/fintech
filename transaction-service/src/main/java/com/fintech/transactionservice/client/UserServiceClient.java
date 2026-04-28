package com.fintech.transactionservice.client;

import com.fintech.transactionservice.dto.UserContext;
import com.fintech.transactionservice.client.UserServiceResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class UserServiceClient {

    private final RestTemplate restTemplate;
    private final String userServiceUrl;

    public UserServiceClient(RestTemplate restTemplate,
                             @Value("${services.user-service.url}") String userServiceUrl) {
        this.restTemplate = restTemplate;
        this.userServiceUrl = userServiceUrl;
    }

    public UserContext getUser(String userId) {
        ResponseEntity<UserServiceResponse> response = restTemplate.exchange(
                userServiceUrl + "/users/" + userId,
                HttpMethod.GET,
                null,
                UserServiceResponse.class
        );
        UserServiceResponse body = response.getBody();
        if (body == null) return null;
        return new UserContext(body.id(), body.role());
    }
}
