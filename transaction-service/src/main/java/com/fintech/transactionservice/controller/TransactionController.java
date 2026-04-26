package com.fintech.transactionservice.controller;

import com.fintech.transactionservice.dto.*;
import com.fintech.transactionservice.service.TransactionService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/transactions")
public class TransactionController {

    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @PostMapping("/deposit")
    @PreAuthorize("hasRole('TELLER') or hasRole('MANAGER')")
    public ResponseEntity<TransactionResponse> deposit(
            @Valid @RequestBody DepositRequest request,
            @AuthenticationPrincipal UserContext actor) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(transactionService.deposit(request, actor));
    }

    @PostMapping("/withdraw")
    @PreAuthorize("hasRole('TELLER') or hasRole('MANAGER')")
    public ResponseEntity<TransactionResponse> withdraw(
            @Valid @RequestBody WithdrawRequest request,
            @AuthenticationPrincipal UserContext actor) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(transactionService.withdraw(request, actor));
    }

    @PostMapping("/transfer")
    @PreAuthorize("hasRole('TELLER') or hasRole('MANAGER')")
    public ResponseEntity<TransactionResponse> transfer(
            @Valid @RequestBody TransferRequest request,
            @AuthenticationPrincipal UserContext actor) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(transactionService.transfer(request, actor));
    }

    @GetMapping("/account/{accountId}")
    @PreAuthorize("hasRole('CUSTOMER') or hasRole('TELLER') or hasRole('MANAGER')")
    public ResponseEntity<List<TransactionResponse>> getByAccount(
            @PathVariable String accountId,
            @AuthenticationPrincipal UserContext actor) {
        return ResponseEntity.ok(transactionService.getByAccount(accountId, actor));
    }

    @GetMapping("/{transactionId}")
    @PreAuthorize("hasRole('CUSTOMER') or hasRole('TELLER') or hasRole('MANAGER')")
    public ResponseEntity<TransactionResponse> getById(@PathVariable String transactionId) {
        return ResponseEntity.ok(transactionService.getById(transactionId));
    }

    @GetMapping("/user/{userId}")
    @PreAuthorize("hasRole('MANAGER')")
    public ResponseEntity<List<TransactionResponse>> getByUser(@PathVariable String userId) {
        return ResponseEntity.ok(transactionService.getByUser(userId));
    }
}
