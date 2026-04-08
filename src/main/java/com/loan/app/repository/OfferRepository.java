package com.loan.app.repository;

import com.loan.app.entity.Offer;
import com.loan.app.entity.OfferStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OfferRepository extends JpaRepository<Offer, Long> {
    Page<Offer> findByStatus(OfferStatus status, Pageable pageable);
    Page<Offer> findByLenderId(Long lenderId, Pageable pageable);
}
