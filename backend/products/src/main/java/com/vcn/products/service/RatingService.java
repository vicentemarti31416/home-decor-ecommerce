package com.vcn.products.service;

import com.vcn.products.model.Rating;
import com.vcn.products.model.ReviewResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface RatingService {

    Rating findById(Long id);
    Page<ReviewResponse> findAllReviews(Long productId, Pageable pageable);
    Page<ReviewResponse> findAllReviewsFiltered(Long productId, Integer stars, Pageable pageable);
    ReviewResponse findReviewByProductIdAndUsername(Long productId, String username);
    ReviewResponse toReviewResponse(Rating rating);
}
