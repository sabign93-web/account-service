package com.banking.accountservice.service.impl;

import com.banking.accountservice.dto.request.AccountSearchRequest;
import com.banking.accountservice.dto.request.CreateAccountRequest;
import com.banking.accountservice.dto.response.AccountResponse;
import com.banking.accountservice.entity.Account;
import com.banking.accountservice.entity.Account_;
import com.banking.accountservice.enums.AccountStatus;
import com.banking.accountservice.exception.AccountNotFoundException;
import com.banking.accountservice.mapper.AccountMapper;
import com.banking.accountservice.repository.AccountRepository;
import com.banking.accountservice.service.AccountService;
import com.banking.accountservice.specification.AccountSpecification;
import com.banking.accountservice.util.IbanGenerator;
import com.banking.accountservice.validation.SortValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import static org.springframework.http.ResponseEntity.ok;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;
    private final SortValidator sortValidator;
    private static final Set<String> ALLOWED_SORT_FIELDS = Set.of(
            Account_.ID,
            Account_.OWNER_NAME,
            Account_.IBAN,
            Account_.BALANCE,
            Account_.CURRENCY,
            Account_.STATUS,
            Account_.CREATED_AT,
            Account_.UPDATED_AT
    );
    private final IbanGenerator ibanGenerator;

    @Override
    public AccountResponse createAccount(CreateAccountRequest request) {

        Account account = Account.builder()
                .ownerName(request.getOwnerName())
                .balance(request.getInitialBalance())
                .currency(request.getCurrency())
                .status(AccountStatus.ACTIVE)
                .iban(ibanGenerator.generateIban())
                .build();
        Account savedAccount = accountRepository.save(account);

        return AccountMapper.toResponse(savedAccount);
    }

    @Override
    public Page<AccountResponse> getAllAccounts(AccountSearchRequest request,
                                                Pageable pageable) {

       sortValidator.validate(
               pageable.getSort(),
               ALLOWED_SORT_FIELDS
       );

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
