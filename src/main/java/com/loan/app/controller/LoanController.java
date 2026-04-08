package com.loan.app.controller;

import com.loan.app.dto.LoanDto;
import com.loan.app.dto.PageResponseDto;
import com.loan.app.service.LoanService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/loans")
@RequiredArgsConstructor
public class LoanController {

    private final LoanService loanService;

    @PostMapping("/apply")
    public ResponseEntity<LoanDto> applyForLoan(@Valid @RequestBody LoanDto loanDto) {
        LoanDto createdLoan = loanService.applyForLoan(loanDto);
        return new ResponseEntity<>(createdLoan, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<PageResponseDto<LoanDto>> getAllLoans(
            @RequestParam(value = "pageNo", defaultValue = "0", required = false) int pageNo,
            @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = "id", required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = "asc", required = false) String sortDir) {
        return ResponseEntity.ok(loanService.getAllLoans(pageNo, pageSize, sortBy, sortDir));
    }

    @GetMapping("/{id}")
    public ResponseEntity<LoanDto> getLoanById(@PathVariable Long id) {
        return ResponseEntity.ok(loanService.getLoanById(id));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<PageResponseDto<LoanDto>> getLoansByUserId(
            @PathVariable Long userId,
            @RequestParam(value = "pageNo", defaultValue = "0", required = false) int pageNo,
            @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = "id", required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = "asc", required = false) String sortDir) {
        return ResponseEntity.ok(loanService.getLoansByUserId(userId, pageNo, pageSize, sortBy, sortDir));
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<LoanDto> updateLoanStatus(@PathVariable Long id, 
                                                    @RequestParam String status,
                                                    @RequestParam(required = false) String approvedBy) {
        return ResponseEntity.ok(loanService.updateLoanStatus(id, status, approvedBy));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteLoan(@PathVariable Long id) {
        loanService.deleteLoan(id);
        return ResponseEntity.ok("Loan deleted successfully");
    }
}
