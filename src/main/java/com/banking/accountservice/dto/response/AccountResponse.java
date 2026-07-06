package com.banking.accountservice.dto.response;

import com.banking.accountservice.enums.AccountStatus;
import com.banking.accountservice.enums.Currency;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AccountResponse {

    private Long id;
    private String iban;
    private String ownerName;
    private BigDecimal balance;
    private Currency currency;
    private AccountStatus status;

}
