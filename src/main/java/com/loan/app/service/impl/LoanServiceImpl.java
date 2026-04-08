package com.loan.app.service.impl;

import com.loan.app.dto.LoanDto;
import com.loan.app.dto.PageResponseDto;
import com.loan.app.entity.Loan;
import com.loan.app.entity.LoanStatus;
import com.loan.app.entity.User;
import com.loan.app.exception.ResourceNotFoundException;
import com.loan.app.repository.LoanRepository;
import com.loan.app.repository.UserRepository;
import com.loan.app.service.LoanService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LoanServiceImpl implements LoanService {

    private final LoanRepository loanRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    // Helper: map loan to DTO and populate borrowerName / borrowerEmail from the User relation
    private LoanDto mapToDto(Loan loan) {
        LoanDto dto = modelMapper.map(loan, LoanDto.class);
        if (loan.getUser() != null) {
            dto.setUserId(loan.getUser().getId());
            dto.setBorrowerName(loan.getUser().getName());
            dto.setBorrowerEmail(loan.getUser().getEmail());
        }
        return dto;
    }

    @Override
    public LoanDto applyForLoan(LoanDto loanDto) {
        Long userId = loanDto.getUserId();
        if (userId == null) {
            throw new IllegalArgumentException("User ID must not be null");
        }
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + userId));

        Loan loan = modelMapper.map(loanDto, Loan.class);
        loan.setUser(user);
        loan.setStatus(LoanStatus.PENDING);
        loan.setApplyDate(LocalDate.now());

        Loan savedLoan = loanRepository.save(loan);
        return mapToDto(savedLoan);
    }

    @Override
    public PageResponseDto<LoanDto> getAllLoans(int pageNo, int pageSize, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);
        Page<Loan> loans = loanRepository.findAll(pageable);
        List<LoanDto> content = loans.getContent().stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
        return new PageResponseDto<>(content, loans.getNumber(), loans.getSize(), loans.getTotalElements(), loans.getTotalPages(), loans.isLast());
    }

    @Override
    public LoanDto getLoanById(Long id) {
        Loan loan = loanRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Loan not found with ID: " + id));
        return mapToDto(loan);
    }

    @Override
    public PageResponseDto<LoanDto> getLoansByUserId(Long userId, int pageNo, int pageSize, String sortBy, String sortDir) {
        userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + userId));

        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);
        Page<Loan> loans = loanRepository.findByUserId(userId, pageable);

        List<LoanDto> content = loans.getContent().stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
        return new PageResponseDto<>(content, loans.getNumber(), loans.getSize(), loans.getTotalElements(), loans.getTotalPages(), loans.isLast());
    }

    @Override
    public LoanDto updateLoanStatus(Long id, String status, String approvedBy) {
        Loan loan = loanRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Loan not found with ID: " + id));

        try {
            loan.setStatus(LoanStatus.valueOf(status.toUpperCase()));
            if (approvedBy != null) {
                loan.setApprovedBy(approvedBy);
            }
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid status value: " + status);
        }

        Loan updatedLoan = loanRepository.save(loan);
        return mapToDto(updatedLoan);
    }

    @Override
    public void deleteLoan(Long id) {
        Loan loan = loanRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Loan not found with ID: " + id));
        loanRepository.delete(loan);
    }
}
