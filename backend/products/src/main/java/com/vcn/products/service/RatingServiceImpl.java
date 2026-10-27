package com.vcn.products.service;

import com.vcn.products.exception.NotFoundException;
import com.vcn.products.model.Rating;
import com.vcn.products.model.ReviewResponse;
import com.vcn.products.repository.RatingRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class RatingServiceImpl implements  RatingService{

    private final RatingRepository ratingRepository;

    public RatingServiceImpl(RatingRepository ratingRepository) {
        this.ratingRepository = ratingRepository;
    }

    @Override
    public Rating findById(Long id) {
        return ratingRepository.findById(id).orElseThrow(() -> new NotFoundException("Review not found"));
    }

    @Override
    public Page<ReviewResponse> findAllReviews(Long productId, Pageable pageable) {
        Page<Rating> ratings = ratingRepository.findByProductId(productId, pageable);
        return ratings.map(this::toReviewResponse);
    }

    @Override
    public Page<ReviewResponse> findAllReviewsFiltered(Long productId, Integer stars, Pageable pageable) {
        Page<Rating> ratings = ratingRepository.findByProductIdAndStars(productId, stars, pageable);
        return ratings.map(this::toReviewResponse);
    }

    @Override
    public ReviewResponse findReviewByProductIdAndUsername(Long productId, String username) {
        Rating ratingDB = ratingRepository
                .findByProductIdAndUsername(productId, username)
                .orElseThrow(() -> new NotFoundException("Review Not found"));
        return toReviewResponse(ratingDB);
    }

    @Override
    public ReviewResponse toReviewResponse(Rating rating) {
        return ReviewResponse.builder()
                .id(rating.getId())
                .stars(rating.getStars())
                .comment(rating.getComment())
                .customerName(rating.getCustomerName())
                .build();
    }
}
