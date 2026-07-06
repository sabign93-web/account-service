package com.banking.accountservice.dto.request;

import com.banking.accountservice.enums.Currency;
import lombok.*;

import java.math.BigDecimal;

@Getter

@Setter

@NoArgsConstructor

@AllArgsConstructor

@Builder

public class CreateAccountRequest {

    private String ownerName;

    private BigDecimal initialBalance;

    private Currency currency;

}
