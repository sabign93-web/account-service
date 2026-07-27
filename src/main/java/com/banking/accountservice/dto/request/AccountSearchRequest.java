package com.banking.accountservice.dto.request;

import com.banking.accountservice.enums.AccountStatus;
import com.banking.accountservice.enums.Currency;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class AccountSearchRequest {

    private String search;

    private Currency currency;

    private AccountStatus status;
}
