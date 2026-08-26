package com.banking.accountservice.controller;
import com.banking.accountservice.dto.request.AccountSearchRequest;
import com.banking.accountservice.dto.request.BalanceTransferRequest;
import com.banking.accountservice.dto.request.CreateAccountRequest;
import com.banking.accountservice.dto.response.AccountResponse;
import com.banking.accountservice.dto.response.BalanceTransferResponse;
import com.banking.accountservice.service.AccountService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import lombok.RequiredArgsConstructor;

@RestController

@RequestMapping("/api/v1/accounts")

@RequiredArgsConstructor

public class AccountController {

    private final AccountService accountService;

    @PostMapping
    public ResponseEntity<AccountResponse> createAccount(
            @Valid @RequestBody CreateAccountRequest request) {

        AccountResponse response = accountService.createAccount(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);

    }

    @GetMapping
    public ResponseEntity<Page<AccountResponse>> getAllAccounts(@ModelAttribute AccountSearchRequest request,
                                                                Pageable pageable) {
        Page<AccountResponse> response = accountService.getAllAccounts(request, pageable);
        return ResponseEntity.ok(response);
    }

   @GetMapping("/{id}")
    public ResponseEntity<AccountResponse> getAccountById(
            @PathVariable Long id) {
       System.out.println("Request thread: " + Thread.currentThread().getName());
        AccountResponse response = accountService.getAccountById(id);
        return ResponseEntity.ok(response);
   }

    @PostMapping("/balance-transfer")
    public ResponseEntity<BalanceTransferResponse> transferBalance(
            @Valid @RequestBody BalanceTransferRequest request) {

        BalanceTransferResponse response =
                accountService.transferBalance(request);

        return ResponseEntity.ok(response);
    }
}
