package com.vcn.products.repository;

import com.vcn.products.model.Rating;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RatingRepository extends JpaRepository<Rating, Long>, PagingAndSortingRepository<Rating, Long> {

    Page<Rating> findByProductId(Long productId, Pageable pageable);
    Page<Rating> findByProductIdAndStars(Long productId, Integer stars, Pageable pageable);
    Optional<Rating> findByProductIdAndUsername(Long productId, String username);
}
