package com.banking.accountservice.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@Builder
public class BalanceTransferResponse {

    private Long sourceAccountId;

    private Long destinationAccountId;

    private BigDecimal sourceBalance;

    private BigDecimal destinationBalance;
}