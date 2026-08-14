package com.banking.accountservice.service.impl;

import com.banking.accountservice.dto.request.AccountSearchRequest;
import com.banking.accountservice.dto.request.CreateAccountRequest;
import com.banking.accountservice.dto.response.AccountResponse;
import com.banking.accountservice.entity.Account;
import com.banking.accountservice.enums.AccountStatus;
import com.banking.accountservice.enums.Currency;
import com.banking.accountservice.exception.AccountNotFoundException;
import com.banking.accountservice.repository.AccountRepository;
import com.banking.accountservice.validation.SortValidator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import static org.mockito.ArgumentMatchers.eq;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
 class AccountServiceImplTest {

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private SortValidator sortValidator;

    @InjectMocks
    private AccountServiceImpl accountService;

    @Test
    void shouldCreateAccount() {

        // Arrange
        CreateAccountRequest request = CreateAccountRequest.builder()
                .ownerName("Saba")
                .initialBalance(new BigDecimal("10.00"))
                .currency(Currency.EUR)
                .build();

        Account savedAccount = Account.builder()
                .id(1L)
                .ownerName("Saba")
                .balance(new BigDecimal("10.00"))
                .currency(Currency.EUR)
                .status(AccountStatus.ACTIVE)
                .iban("FI123456789012345676")
                .build();

        when(accountRepository.save(any(Account.class)))
                .thenReturn(savedAccount);

        // Act
        AccountResponse response =
                accountService.createAccount(request);

        // Assert (Response)
        assertEquals(1L, response.getId());
        assertEquals("Saba", response.getOwnerName());
        assertEquals(new BigDecimal("10.00"), response.getBalance());
        assertEquals(Currency.EUR, response.getCurrency());

        // Assert (Account sent to Repository)
        ArgumentCaptor<Account> captor =
                ArgumentCaptor.forClass(Account.class);

        verify(accountRepository).save(captor.capture());

        Account accountSent = captor.getValue();

        assertEquals("Saba", accountSent.getOwnerName());
        assertEquals(new BigDecimal("10.00"), accountSent.getBalance());
        assertEquals(Currency.EUR, accountSent.getCurrency());
        assertEquals(AccountStatus.ACTIVE, accountSent.getStatus());
        assertEquals("FI123456789012345676", accountSent.getIban());
    }

    @Test
    void shouldGetAllAccounts() {

        // Arrange
        AccountSearchRequest request = AccountSearchRequest.builder()
                .search("Saba")
                .currency(Currency.EUR)
                .status(AccountStatus.ACTIVE)
                .build();

        Account account = Account.builder()
                .id(1L)
                .ownerName("Saba")
                .balance(new BigDecimal("10.00"))
                .currency(Currency.EUR)
                .status(AccountStatus.ACTIVE)
                .iban("FI123456789012345676")
                .build();

        Pageable pageable = PageRequest.of(0, 10);

        Page<Account> accountPage =
                new PageImpl<>(List.of(account), pageable, 1);

        when(accountRepository.findAll(
                any(Specification.class),
                eq(pageable)
        )).thenReturn(accountPage);

        // Act
        Page<AccountResponse> response =
                accountService.getAllAccounts(request, pageable);

        // Assert
        assertEquals(1, response.getTotalElements());
        assertEquals(1, response.getNumberOfElements());

        AccountResponse first = response.getContent().get(0);

        assertEquals(1L, first.getId());
        assertEquals("Saba", first.getOwnerName());
        assertEquals(new BigDecimal("10.00"), first.getBalance());
        assertEquals(Currency.EUR, first.getCurrency());
        assertEquals(AccountStatus.ACTIVE, first.getStatus());
        assertEquals("FI123456789012345676", first.getIban());

        verify(accountRepository)
                .findAll(any(Specification.class), eq(pageable));

        verify(sortValidator)
                .validate(any(), any());
    }

    @Test
    void shouldReturnAccountWhenAccountExists() {

        Long accountId = 1L;

        Account account = Account.builder()
                .id(accountId)
                .ownerName("Saba")
                .build();

        when(accountRepository.findById(accountId))
                .thenReturn(Optional.of(account));

        AccountResponse response =
                accountService.getAccountById(accountId);

        assertEquals(accountId, response.getId());
        assertEquals("Saba", response.getOwnerName());

        verify(accountRepository).findById(accountId);
    }

    @Test
    void shouldThrowExceptionWhenAccountDoesNotExist() {

        Long accountId = 1L;

        when(accountRepository.findById(accountId))
                .thenReturn(Optional.empty());

        AccountNotFoundException exception = assertThrows(
                AccountNotFoundException.class,
                () -> accountService.getAccountById(accountId)
        );

        assertEquals(
                "Account with id " + accountId + " not found",
                exception.getMessage()
        );
    }


}
