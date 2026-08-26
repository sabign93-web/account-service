package com.banking.accountservice.service.impl;

import com.banking.accountservice.dto.request.AccountSearchRequest;
import com.banking.accountservice.dto.request.BalanceTransferRequest;
import com.banking.accountservice.dto.request.CreateAccountRequest;
import com.banking.accountservice.dto.response.AccountResponse;
import com.banking.accountservice.dto.response.BalanceTransferResponse;
import com.banking.accountservice.entity.Account;
import com.banking.accountservice.entity.Account_;
import com.banking.accountservice.enums.AccountStatus;
import com.banking.accountservice.enums.Currency;
import com.banking.accountservice.exception.AccountNotActiveException;
import com.banking.accountservice.exception.AccountNotFoundException;
import com.banking.accountservice.exception.CurrencyMismatchException;
import com.banking.accountservice.exception.InsufficientBalanceException;
import com.banking.accountservice.mapper.AccountMapper;
import com.banking.accountservice.repository.AccountRepository;
import com.banking.accountservice.service.AccountService;
import com.banking.accountservice.specification.AccountSpecification;
import com.banking.accountservice.util.IbanGenerator;
import com.banking.accountservice.validation.SortValidator;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
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

    @Override
    @Transactional
    public BalanceTransferResponse transferBalance(BalanceTransferRequest request) {

        Account destinationAccount = accountRepository.findByIdForUpdate(request.getDestinationAccountId())
                .orElseThrow(() -> new AccountNotFoundException("Account with id " + request.getDestinationAccountId() + " not found"));
        Account sourceAccount = accountRepository.findByIdForUpdate(request.getSourceAccountId())
                .orElseThrow(() -> new AccountNotFoundException("Account with id " + request.getSourceAccountId() + " not found"));

        if (destinationAccount.getStatus() != AccountStatus.ACTIVE ) {
            throw new AccountNotActiveException(
                    "Account with id " + request.getDestinationAccountId() + " not Active"
            );
        }
        if (sourceAccount.getStatus() != AccountStatus.ACTIVE ) {
            throw new AccountNotActiveException(
                    "Account with id " + request.getSourceAccountId() + " not Active"
            );
        }

        if (destinationAccount.getCurrency() != request.getCurrency() ) {
            throw new CurrencyMismatchException(
                    "Account with id " + request.getDestinationAccountId() + " currency does not match transfer currency"
            );
        }
        if (sourceAccount.getCurrency() != request.getCurrency() ) {
            throw new CurrencyMismatchException(
                    "Account with id " + request.getSourceAccountId() + " currency does not match transfer currency"
            );
        }

        if (sourceAccount.getBalance().compareTo(request.getAmount()) < 0) {
            throw new InsufficientBalanceException(
                    "Insufficient balance for account with id " + request.getSourceAccountId()
            );
        }
        sourceAccount.setBalance(sourceAccount.getBalance().subtract(request.getAmount()));
        destinationAccount.setBalance(destinationAccount.getBalance().add(request.getAmount()));

        return BalanceTransferResponse.builder()
                .sourceAccountId(sourceAccount.getId())
                .destinationAccountId(destinationAccount.getId())
                .sourceBalance(sourceAccount.getBalance())
                .destinationBalance(destinationAccount.getBalance())
                .build();
    }
}
