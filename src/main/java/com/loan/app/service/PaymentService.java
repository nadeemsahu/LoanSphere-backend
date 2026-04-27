package com.loan.app.service;

import com.loan.app.dto.PageResponseDto;
import com.loan.app.dto.PaymentDto;

public interface PaymentService {
    PaymentDto addPayment(PaymentDto paymentDto);
    PageResponseDto<PaymentDto> getAllPayments(int pageNo, int pageSize, String sortBy, String sortDir);
    PageResponseDto<PaymentDto> getPaymentsByLoanId(Long loanId, int pageNo, int pageSize, String sortBy, String sortDir);
    PaymentDto getPaymentById(Long id);
}
