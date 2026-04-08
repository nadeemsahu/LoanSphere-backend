package com.loan.app.dto;

import com.loan.app.entity.OfferStatus;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class OfferDto {

    private Long id;

    @NotNull(message = "Amount cannot be empty")
    @DecimalMin(value = "1000.0", message = "Offer amount must be at least 1000")
    private BigDecimal amount;

    @NotNull(message = "Interest rate cannot be empty")
    @DecimalMin(value = "0.1", message = "Interest rate must be at least 0.1%")
    private BigDecimal interestRate;

    @NotNull(message = "Term in months cannot be empty")
    @Min(value = 1, message = "Term must be at least 1 month")
    private Integer termMonths;

    private String description;

    private String optionalTerms;

    private OfferStatus status;

    // Write: lender's user ID (required on creation)
    @NotNull(message = "Lender ID must be provided")
    private Long lenderId;

    // Read-only: populated by service layer from User entity
    private String lenderName;
    private String lenderEmail;
}
