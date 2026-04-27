package com.loan.app.controller;

import com.loan.app.dto.PaymentDto;
import com.loan.app.service.PaymentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;



@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping
    public ResponseEntity<PaymentDto> addPayment(@Valid @RequestBody PaymentDto paymentDto) {
        PaymentDto createdPayment = paymentService.addPayment(paymentDto);
        return new ResponseEntity<>(createdPayment, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<com.loan.app.dto.PageResponseDto<PaymentDto>> getAllPayments(
            @RequestParam(value = "page", defaultValue = "0", required = false) int pageNo,
            @RequestParam(value = "size", defaultValue = "100", required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = "paymentDate", required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = "desc", required = false) String sortDir
    ) {
        return ResponseEntity.ok(paymentService.getAllPayments(pageNo, pageSize, sortBy, sortDir));
    }

    @GetMapping("/loan/{loanId}")
    public ResponseEntity<com.loan.app.dto.PageResponseDto<PaymentDto>> getPaymentsByLoanId(
            @PathVariable Long loanId,
            @RequestParam(value = "page", defaultValue = "0", required = false) int pageNo,
            @RequestParam(value = "size", defaultValue = "100", required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = "paymentDate", required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = "desc", required = false) String sortDir
    ) {
        return ResponseEntity.ok(paymentService.getPaymentsByLoanId(loanId, pageNo, pageSize, sortBy, sortDir));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PaymentDto> getPaymentById(@PathVariable Long id) {
        return ResponseEntity.ok(paymentService.getPaymentById(id));
    }
}
