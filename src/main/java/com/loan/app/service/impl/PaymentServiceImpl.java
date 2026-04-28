package com.loan.app.service.impl;

import com.loan.app.dto.PageResponseDto;
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
@org.springframework.transaction.annotation.Transactional
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final LoanRepository loanRepository;
    private final ModelMapper modelMapper;

    @Override
    @org.springframework.transaction.annotation.Transactional
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
        
        // Check if loan should be closed
        java.math.BigDecimal totalPaid = paymentRepository.findByLoanId(loan.getId()).stream()
                .map(Payment::getAmount)
                .reduce(java.math.BigDecimal.ZERO, java.math.BigDecimal::add);
                
        if (totalPaid.compareTo(loan.getAmount()) >= 0) {
            loan.setStatus(LoanStatus.CLOSED);
            loanRepository.save(loan);
        }

        PaymentDto resultDto = modelMapper.map(savedPayment, PaymentDto.class);
        resultDto.setLoanId(savedPayment.getLoan().getId());
        return resultDto;
    }

    @Override
    public PageResponseDto<PaymentDto> getAllPayments(int pageNo, int pageSize, String sortBy, String sortDir) {
        org.springframework.data.domain.Sort sort = sortDir.equalsIgnoreCase(org.springframework.data.domain.Sort.Direction.ASC.name()) ?
                org.springframework.data.domain.Sort.by(sortBy).ascending() : org.springframework.data.domain.Sort.by(sortBy).descending();

        org.springframework.data.domain.Pageable pageable = org.springframework.data.domain.PageRequest.of(pageNo, pageSize, sort);
        org.springframework.data.domain.Page<Payment> payments = paymentRepository.findAll(pageable);

        List<PaymentDto> content = payments.getContent().stream()
                .map(payment -> {
                    PaymentDto dto = modelMapper.map(payment, PaymentDto.class);
                    dto.setLoanId(payment.getLoan().getId());
                    return dto;
                })
                .collect(Collectors.toList());

        return new com.loan.app.dto.PageResponseDto<>(
                content,
                payments.getNumber(),
                payments.getSize(),
                payments.getTotalElements(),
                payments.getTotalPages(),
                payments.isLast()
        );
    }

    @Override
    public PageResponseDto<PaymentDto> getPaymentsByLoanId(Long loanId, int pageNo, int pageSize, String sortBy, String sortDir) {
        loanRepository.findById(loanId)
                .orElseThrow(() -> new ResourceNotFoundException("Loan not found with ID: " + loanId));

        org.springframework.data.domain.Sort sort = sortDir.equalsIgnoreCase(org.springframework.data.domain.Sort.Direction.ASC.name()) ?
                org.springframework.data.domain.Sort.by(sortBy).ascending() : org.springframework.data.domain.Sort.by(sortBy).descending();

        org.springframework.data.domain.Pageable pageable = org.springframework.data.domain.PageRequest.of(pageNo, pageSize, sort);
        org.springframework.data.domain.Page<Payment> payments = paymentRepository.findByLoanId(loanId, pageable);

        List<PaymentDto> content = payments.getContent().stream()
                .map(payment -> {
                    PaymentDto dto = modelMapper.map(payment, PaymentDto.class);
                    dto.setLoanId(payment.getLoan().getId());
                    return dto;
                })
                .collect(Collectors.toList());

        return new com.loan.app.dto.PageResponseDto<>(
                content,
                payments.getNumber(),
                payments.getSize(),
                payments.getTotalElements(),
                payments.getTotalPages(),
                payments.isLast()
        );
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
