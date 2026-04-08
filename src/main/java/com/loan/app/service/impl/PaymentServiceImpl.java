package com.loan.app.service.impl;

import com.loan.app.dto.PaymentDto;
import com.loan.app.entity.Loan;
import com.loan.app.entity.LoanStatus;
import com.loan.app.entity.Payment;
import com.loan.app.exception.ResourceNotFoundException;
import com.loan.app.repository.LoanRepository;
import com.loan.app.repository.PaymentRepository;
import com.loan.app.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final LoanRepository loanRepository;
    private final ModelMapper modelMapper;

    @Override
    public PaymentDto addPayment(PaymentDto paymentDto) {
        Loan loan = loanRepository.findById(paymentDto.getLoanId())
                .orElseThrow(() -> new ResourceNotFoundException("Loan not found with ID: " + paymentDto.getLoanId()));
                
        if (loan.getStatus() != LoanStatus.ACTIVE) {
            throw new IllegalArgumentException("Cannot make payments for a loan that is not ACTIVE.");
        }

        Payment payment = modelMapper.map(paymentDto, Payment.class);
        payment.setLoan(loan);
        if (payment.getPaymentDate() == null) {
            payment.setPaymentDate(LocalDate.now());
        }

        Payment savedPayment = paymentRepository.save(payment);
        PaymentDto resultDto = modelMapper.map(savedPayment, PaymentDto.class);
        resultDto.setLoanId(savedPayment.getLoan().getId());
        return resultDto;
    }

    @Override
    public List<PaymentDto> getAllPayments() {
        return paymentRepository.findAll().stream()
                .map(payment -> {
                    PaymentDto dto = modelMapper.map(payment, PaymentDto.class);
                    dto.setLoanId(payment.getLoan().getId());
                    return dto;
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<PaymentDto> getPaymentsByLoanId(Long loanId) {
        loanRepository.findById(loanId)
                .orElseThrow(() -> new ResourceNotFoundException("Loan not found with ID: " + loanId));

        return paymentRepository.findByLoanId(loanId).stream()
                .map(payment -> {
                    PaymentDto dto = modelMapper.map(payment, PaymentDto.class);
                    dto.setLoanId(payment.getLoan().getId());
                    return dto;
                })
                .collect(Collectors.toList());
    }

    @Override
    public PaymentDto getPaymentById(Long id) {
        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Payment not found with ID: " + id));
        PaymentDto resultDto = modelMapper.map(payment, PaymentDto.class);
        resultDto.setLoanId(payment.getLoan().getId());
        return resultDto;
    }
}
