package com.banking.accountservice.mapper;

import com.banking.accountservice.dto.response.AccountResponse;
import com.banking.accountservice.entity.Account;

import java.util.List;

public class AccountMapper {

    public static AccountResponse toResponse(Account account) {
        return AccountResponse.builder()
                .id(account.getId())
                .iban(account.getIban())
                .ownerName(account.getOwnerName())
                .balance(account.getBalance())
                .currency(account.getCurrency())
                .status(account.getStatus())
                .build();
    }
}
