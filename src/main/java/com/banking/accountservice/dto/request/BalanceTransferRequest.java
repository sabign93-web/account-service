package com.banking.accountservice.dto.request;

import com.banking.accountservice.enums.Currency;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class BalanceTransferRequest {

    @NotNull
    private Long sourceAccountId;

    @NotNull
    private Long destinationAccountId;

    @NotNull
    @Positive
    private BigDecimal amount;

    @NotNull
    private Currency currency;
}