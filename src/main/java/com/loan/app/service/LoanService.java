package com.loan.app.service;

import com.loan.app.dto.LoanDto;
import com.loan.app.dto.PageResponseDto;

public interface LoanService {
    LoanDto applyForLoan(LoanDto loanDto);
    PageResponseDto<LoanDto> getAllLoans(int pageNo, int pageSize, String sortBy, String sortDir);
    LoanDto getLoanById(Long id);
    PageResponseDto<LoanDto> getLoansByUserId(Long userId, int pageNo, int pageSize, String sortBy, String sortDir);
    LoanDto updateLoanStatus(Long id, String status, String approvedBy);
    void deleteLoan(Long id);
}
