package com.fintech.accountservice.controller;

import com.fintech.accountservice.dto.CreateAccountRequest;
import com.fintech.accountservice.dto.UpdateBalanceRequest;
import com.fintech.accountservice.dto.UpdateStatusRequest;
import com.fintech.accountservice.model.Account;
import com.fintech.accountservice.service.AccountService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/accounts")
public class AccountController {

    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Account createAccount(@RequestBody CreateAccountRequest request) {
        return accountService.createAccount(request);
    }

    @GetMapping("/{accountId}")
    public Account getAccount(@PathVariable String accountId) {
        return accountService.getAccount(accountId);
    }

    @GetMapping("/customer/{customerId}")
    public List<Account> getAccountsByCustomer(@PathVariable String customerId) {
        return accountService.getAccountsByCustomer(customerId);
    }

    @PutMapping("/{accountId}/status")
    public Account updateStatus(@PathVariable String accountId,
                                @RequestBody UpdateStatusRequest request) {
        return accountService.updateStatus(accountId, request);
    }

    @PutMapping("/{accountId}/balance")
    public Account updateBalance(@PathVariable String accountId,
                                 @RequestBody UpdateBalanceRequest request) {
        return accountService.updateBalance(accountId, request);
    }
}
