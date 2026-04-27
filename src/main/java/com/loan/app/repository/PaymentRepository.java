package com.loan.app.repository;

import com.loan.app.entity.Payment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {
    List<Payment> findByLoanId(Long loanId);
    Page<Payment> findByLoanId(Long loanId, Pageable pageable);
    
    // Derived count query
    long countByLoanId(Long loanId);
}
