package com.loan.app.service;

import com.loan.app.dto.PaymentDto;

import java.util.List;

public interface PaymentService {
    PaymentDto addPayment(PaymentDto paymentDto);
    List<PaymentDto> getAllPayments();
    List<PaymentDto> getPaymentsByLoanId(Long loanId);
    PaymentDto getPaymentById(Long id);
}
