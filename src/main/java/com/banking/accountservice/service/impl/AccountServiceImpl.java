package com.banking.accountservice.service.impl;

import com.banking.accountservice.dto.request.AccountSearchRequest;
import com.banking.accountservice.dto.request.CreateAccountRequest;
import com.banking.accountservice.dto.response.AccountResponse;
import com.banking.accountservice.entity.Account;
import com.banking.accountservice.enums.AccountStatus;
import com.banking.accountservice.exception.AccountNotFoundException;
import com.banking.accountservice.mapper.AccountMapper;
import com.banking.accountservice.repository.AccountRepository;
import com.banking.accountservice.service.AccountService;
import com.banking.accountservice.specification.AccountSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
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
    public Page<AccountResponse> getAllAccounts(AccountSearchRequest request,
                                                Pageable pageable) {

        Specification<Account> specification =
                Specification.allOf(AccountSpecification.hasSearch(request.getSearch()))
                        .and(AccountSpecification.hasCurrency(request.getCurrency()))
                        .and(AccountSpecification.hasStatus(request.getStatus()));

        Page<Account> accounts =
                accountRepository.findAll(specification, pageable);

        return accounts.map(AccountMapper::toResponse);
    }

    @Override
    public AccountResponse getAccountById(Long id) {

        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new AccountNotFoundException(
                        "Account with id " + id + " not found"
                ));

        return AccountMapper.toResponse(account);
    }
}
