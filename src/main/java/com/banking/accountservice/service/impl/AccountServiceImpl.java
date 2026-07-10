package com.banking.accountservice.service.impl;

import com.banking.accountservice.dto.request.CreateAccountRequest;
import com.banking.accountservice.dto.response.AccountResponse;
import com.banking.accountservice.entity.Account;
import com.banking.accountservice.enums.AccountStatus;
import com.banking.accountservice.mapper.AccountMapper;
import com.banking.accountservice.repository.AccountRepository;
import com.banking.accountservice.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

import static org.springframework.http.ResponseEntity.ok;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;

    @Override
    public AccountResponse createAccount(CreateAccountRequest request) {

        Account account = Account.builder()
                .ownerName(request.getOwnerName())
                .balance(request.getInitialBalance())
                .currency(request.getCurrency())
                .status(AccountStatus.ACTIVE)
                .iban("FI123456789012345676")
                .build();
        Account savedAccount = accountRepository.save(account);

        return AccountMapper.toResponse(savedAccount);
    }

    @Override
    public List<AccountResponse> getAllAccounts() {
        List<Account> accounts = accountRepository.findAll();
        return accounts.stream()
                .map(AccountMapper :: toResponse)
                .toList();
    }
}
