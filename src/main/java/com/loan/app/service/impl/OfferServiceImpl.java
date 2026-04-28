package com.loan.app.service.impl;

import com.loan.app.dto.OfferDto;
import com.loan.app.dto.PageResponseDto;
import com.loan.app.entity.Offer;
import com.loan.app.entity.OfferStatus;
import com.loan.app.entity.User;
import com.loan.app.exception.ResourceNotFoundException;
import com.loan.app.repository.OfferRepository;
import com.loan.app.repository.UserRepository;
import com.loan.app.service.OfferService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@org.springframework.transaction.annotation.Transactional
public class OfferServiceImpl implements OfferService {

    private final OfferRepository offerRepository;
    private final UserRepository userRepository;

    // Helper: map Offer entity → OfferDto, populating read-only lender info
    private OfferDto mapToDto(Offer offer) {
        OfferDto dto = new OfferDto();
        dto.setId(offer.getId());
        dto.setAmount(offer.getAmount());
        dto.setInterestRate(offer.getInterestRate());
        dto.setTermMonths(offer.getTermMonths());
        dto.setDescription(offer.getDescription());
        dto.setOptionalTerms(offer.getOptionalTerms());
        dto.setStatus(offer.getStatus());
        if (offer.getLender() != null) {
            dto.setLenderId(offer.getLender().getId());
            dto.setLenderName(offer.getLender().getName());
            dto.setLenderEmail(offer.getLender().getEmail());
        }
        return dto;
    }

    @Override
    public OfferDto createOffer(OfferDto offerDto) {
        User lender = userRepository.findById(offerDto.getLenderId())
                .orElseThrow(() -> new ResourceNotFoundException("Lender not found with ID: " + offerDto.getLenderId()));

        Offer offer = new Offer();
        offer.setAmount(offerDto.getAmount());
        offer.setInterestRate(offerDto.getInterestRate());
        offer.setTermMonths(offerDto.getTermMonths());
        offer.setDescription(offerDto.getDescription());
        offer.setOptionalTerms(offerDto.getOptionalTerms());
        offer.setStatus(OfferStatus.OPEN);
        offer.setLender(lender);

        Offer savedOffer = offerRepository.save(offer);
        return mapToDto(savedOffer);
    }

    @Override
    public PageResponseDto<OfferDto> getAllOpenOffers(int pageNo, int pageSize, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name())
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);
        Page<Offer> offers = offerRepository.findByStatus(OfferStatus.OPEN, pageable);
        List<OfferDto> content = offers.getContent().stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
        return new PageResponseDto<>(content, offers.getNumber(), offers.getSize(),
                offers.getTotalElements(), offers.getTotalPages(), offers.isLast());
    }

    @Override
    public PageResponseDto<OfferDto> getOffersByLender(Long lenderId, int pageNo, int pageSize) {
        userRepository.findById(lenderId)
                .orElseThrow(() -> new ResourceNotFoundException("Lender not found with ID: " + lenderId));
        Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by("id").descending());
        Page<Offer> offers = offerRepository.findByLenderId(lenderId, pageable);
        List<OfferDto> content = offers.getContent().stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
        return new PageResponseDto<>(content, offers.getNumber(), offers.getSize(),
                offers.getTotalElements(), offers.getTotalPages(), offers.isLast());
    }

    @Override
    public OfferDto getOfferById(Long id) {
        Offer offer = offerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Offer not found with ID: " + id));
        return mapToDto(offer);
    }

    @Override
    public void deleteOffer(Long id) {
        Offer offer = offerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Offer not found with ID: " + id));
        offerRepository.delete(offer);
    }
}
