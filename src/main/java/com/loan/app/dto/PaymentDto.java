package com.loan.app.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
public class PaymentDto {
    private Long id;

    @NotNull(message = "Amount is mandatory")
    @DecimalMin(value = "1.0", message = "Amount must be greater than 0")
    private BigDecimal amount;

    private LocalDate paymentDate;

    @NotNull(message = "Loan ID must be provided")
    private Long loanId;
}
