package com.loan.app.dto;

import com.loan.app.entity.LoanStatus;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
public class LoanDto {
    private Long id;

    @NotNull(message = "Amount cannot be empty")
    @DecimalMin(value = "1000.0", message = "Amount must be at least 1000")
    private BigDecimal amount;

    @NotNull(message = "Term in months cannot be empty")
    @Min(value = 1, message = "Term must be at least 1 month")
    private Integer termMonths;

    private BigDecimal interestRate = new BigDecimal("5.0"); // default interest

    private LoanStatus status = LoanStatus.PENDING;

    private LocalDate applyDate;

    private String purpose;

    private String approvedBy;

    @NotNull(message = "User ID must be provided")
    private Long userId;

    // Read-only: populated by service layer from the associated User entity
    private String borrowerName;
    private String borrowerEmail;
}
