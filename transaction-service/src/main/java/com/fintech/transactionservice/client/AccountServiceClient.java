package com.fintech.transactionservice.client;

import com.fintech.transactionservice.dto.AccountResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class AccountServiceClient {

    private final RestTemplate restTemplate;
    private final String accountServiceUrl;

    public AccountServiceClient(RestTemplate restTemplate,
                                @Value("${services.account-service.url}") String accountServiceUrl) {
        this.restTemplate = restTemplate;
        this.accountServiceUrl = accountServiceUrl;
    }

    public AccountResponse getAccount(String accountId) {
        ResponseEntity<AccountResponse> response = restTemplate.getForEntity(
                accountServiceUrl + "/accounts/" + accountId,
                AccountResponse.class
        );
        return response.getBody();
    }
}
