package com.vcn.products.controller;

import com.vcn.products.service.RatingService;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/ratings")
public class RatingController {

    private final RatingService ratingService;

    public RatingController(RatingService ratingService) {
        this.ratingService = ratingService;
    }

    @GetMapping("/reviews-filtered/{productId}")
    public ResponseEntity<?> getReviewsFiltered(
            @PathVariable Long productId,
            Pageable pageable,
            @RequestParam(required = false) Integer stars) {
        return ResponseEntity.ok(ratingService.findReviewsFiltered(productId, stars, pageable));
    }

    @GetMapping("/{productId}")
    public ResponseEntity<?> getRating(@PathVariable Long productId, @AuthenticationPrincipal Jwt jwt) {
        return ResponseEntity.ok(ratingService.findReviewByProductIdAndUsername(productId, jwt.getSubject()));
    }
}