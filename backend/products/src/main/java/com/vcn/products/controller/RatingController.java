package com.vcn.products.controller;

import com.vcn.products.service.RatingService;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/ratings")
public class RatingController {

    private final RatingService ratingService;

    public RatingController(RatingService ratingService) {
        this.ratingService = ratingService;
    }

    @GetMapping("/reviews/{productId}")
    public ResponseEntity<?> getAllReviews(@PathVariable Long productId, Pageable pageable) {
        return ResponseEntity.ok(ratingService.findAllReviews(productId, pageable));
    }

    @GetMapping("/reviews/{productId}/{stars}")
    public ResponseEntity<?> getReviewsFiltered(@PathVariable Long productId, @PathVariable Integer stars, Pageable pageable) {
        return ResponseEntity.ok(ratingService.findAllReviewsFiltered(productId, stars, pageable));
    }

    @GetMapping("/{productId}")
    public ResponseEntity<?> getRating(@PathVariable Long productId, @AuthenticationPrincipal Jwt jwt) {
        return ResponseEntity.ok(ratingService.findReviewByProductIdAndUsername(productId, jwt.getSubject()));
    }
}