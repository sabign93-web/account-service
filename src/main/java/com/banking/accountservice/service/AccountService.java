package com.banking.accountservice.service;

import com.banking.accountservice.dto.request.CreateAccountRequest;
import com.banking.accountservice.dto.response.AccountResponse;
import com.banking.accountservice.entity.Account;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface AccountService {

    AccountResponse createAccount(CreateAccountRequest request);

    List<AccountResponse> getAllAccounts();
}
