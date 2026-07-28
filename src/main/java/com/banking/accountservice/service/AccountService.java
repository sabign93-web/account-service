package com.banking.accountservice.service;

import com.banking.accountservice.dto.request.AccountSearchRequest;
import com.banking.accountservice.dto.request.CreateAccountRequest;
import com.banking.accountservice.dto.response.AccountResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.banking.accountservice.entity.Account;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface AccountService {

    AccountResponse createAccount(CreateAccountRequest request);

    Page<AccountResponse> getAllAccounts(
            AccountSearchRequest request,
            Pageable pageable
    );

    AccountResponse getAccountById(Long id);
}
