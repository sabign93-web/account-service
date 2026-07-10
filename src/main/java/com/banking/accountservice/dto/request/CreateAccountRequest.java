package com.banking.accountservice.dto.request;

import com.banking.accountservice.enums.Currency;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;

import java.math.BigDecimal;

@Getter

@Setter

@NoArgsConstructor

@AllArgsConstructor

@Builder

public class CreateAccountRequest {

    @NotBlank
    private String ownerName;

    @Positive
    private BigDecimal initialBalance;

    @NotNull
    private Currency currency;

}
