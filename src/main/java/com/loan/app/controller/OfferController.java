package com.loan.app.controller;

import com.loan.app.dto.OfferDto;
import com.loan.app.dto.PageResponseDto;
import com.loan.app.service.OfferService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/offers")
@RequiredArgsConstructor
public class OfferController {

    private final OfferService offerService;

    @PostMapping
    public ResponseEntity<OfferDto> createOffer(@Valid @RequestBody OfferDto offerDto) {
        OfferDto createdOffer = offerService.createOffer(offerDto);
        return new ResponseEntity<>(createdOffer, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<PageResponseDto<OfferDto>> getAllOpenOffers(
            @RequestParam(value = "pageNo", defaultValue = "0", required = false) int pageNo,
            @RequestParam(value = "pageSize", defaultValue = "20", required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = "id", required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = "desc", required = false) String sortDir) {
        return ResponseEntity.ok(offerService.getAllOpenOffers(pageNo, pageSize, sortBy, sortDir));
    }

    @GetMapping("/lender/{lenderId}")
    public ResponseEntity<PageResponseDto<OfferDto>> getOffersByLender(
            @PathVariable Long lenderId,
            @RequestParam(value = "pageNo", defaultValue = "0", required = false) int pageNo,
            @RequestParam(value = "pageSize", defaultValue = "50", required = false) int pageSize) {
        return ResponseEntity.ok(offerService.getOffersByLender(lenderId, pageNo, pageSize));
    }

    @GetMapping("/{id}")
    public ResponseEntity<OfferDto> getOfferById(@PathVariable Long id) {
        return ResponseEntity.ok(offerService.getOfferById(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteOffer(@PathVariable Long id) {
        offerService.deleteOffer(id);
        return ResponseEntity.ok("Offer deleted successfully");
    }
}
