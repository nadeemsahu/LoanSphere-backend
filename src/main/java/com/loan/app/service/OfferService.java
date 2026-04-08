package com.loan.app.service;

import com.loan.app.dto.OfferDto;
import com.loan.app.dto.PageResponseDto;

public interface OfferService {
    OfferDto createOffer(OfferDto offerDto);
    PageResponseDto<OfferDto> getAllOpenOffers(int pageNo, int pageSize, String sortBy, String sortDir);
    PageResponseDto<OfferDto> getOffersByLender(Long lenderId, int pageNo, int pageSize);
    OfferDto getOfferById(Long id);
    void deleteOffer(Long id);
}
