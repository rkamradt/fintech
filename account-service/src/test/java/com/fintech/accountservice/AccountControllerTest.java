package com.fintech.accountservice;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fintech.accountservice.dto.CreateAccountRequest;
import com.fintech.accountservice.dto.UpdateBalanceRequest;
import com.fintech.accountservice.dto.UpdateStatusRequest;
import com.fintech.accountservice.kafka.AccountEventProducer;
import com.fintech.accountservice.model.AccountStatus;
import com.fintech.accountservice.model.AccountType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.math.BigDecimal;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class AccountControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    AccountEventProducer eventProducer;

    @Test
    void createAndRetrieveAccount() throws Exception {
        CreateAccountRequest req = new CreateAccountRequest("cust-001", AccountType.CHECKING, new BigDecimal("500.00"));

        MvcResult result = mockMvc.perform(post("/accounts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.accountId").isNotEmpty())
                .andExpect(jsonPath("$.status").value("ACTIVE"))
                .andExpect(jsonPath("$.balance").value(500.00))
                .andReturn();

        String accountId = objectMapper.readTree(result.getResponse().getContentAsString())
                .get("accountId").asText();

        mockMvc.perform(get("/accounts/{id}", accountId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.customerId").value("cust-001"));
    }

    @Test
    void listAccountsByCustomer() throws Exception {
        CreateAccountRequest req = new CreateAccountRequest("cust-002", AccountType.SAVINGS, new BigDecimal("1000.00"));

        mockMvc.perform(post("/accounts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isCreated());

        mockMvc.perform(get("/accounts/customer/cust-002"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].customerId").value("cust-002"));
    }

    @Test
    void updateAccountStatus() throws Exception {
        CreateAccountRequest req = new CreateAccountRequest("cust-003", AccountType.CHECKING, BigDecimal.ZERO);

        MvcResult result = mockMvc.perform(post("/accounts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isCreated())
                .andReturn();

        String accountId = objectMapper.readTree(result.getResponse().getContentAsString())
                .get("accountId").asText();

        UpdateStatusRequest statusReq = new UpdateStatusRequest(AccountStatus.SUSPENDED, "manager-001");

        mockMvc.perform(put("/accounts/{id}/status", accountId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(statusReq)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("SUSPENDED"));
    }

    @Test
    void updateAccountBalance() throws Exception {
        CreateAccountRequest req = new CreateAccountRequest("cust-004", AccountType.SAVINGS, new BigDecimal("200.00"));

        MvcResult result = mockMvc.perform(post("/accounts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isCreated())
                .andReturn();

        String accountId = objectMapper.readTree(result.getResponse().getContentAsString())
                .get("accountId").asText();

        UpdateBalanceRequest balanceReq = new UpdateBalanceRequest(new BigDecimal("350.00"), "txn-001");

        mockMvc.perform(put("/accounts/{id}/balance", accountId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(balanceReq)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.balance").value(350.00));
    }

    @Test
    void getUnknownAccountReturns404() throws Exception {
        mockMvc.perform(get("/accounts/does-not-exist"))
                .andExpect(status().isNotFound());
    }
}
