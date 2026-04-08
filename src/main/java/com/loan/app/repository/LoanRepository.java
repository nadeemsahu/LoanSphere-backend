package com.loan.app.repository;

import com.loan.app.entity.Loan;
import com.loan.app.entity.LoanStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LoanRepository extends JpaRepository<Loan, Long> {
    Page<Loan> findByUserId(Long userId, Pageable pageable);
    List<Loan> findByUserId(Long userId);
    List<Loan> findByStatus(LoanStatus status);
    
    @Query("SELECT l FROM Loan l WHERE l.user.id = :userId AND l.status = :status")
    List<Loan> findLoansByUserIdAndStatus(@Param("userId") Long userId, @Param("status") LoanStatus status);
}
