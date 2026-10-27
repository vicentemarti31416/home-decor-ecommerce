package com.vcn.products.service;

import com.vcn.products.model.*;
import org.springframework.http.HttpStatus;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.List;
import java.util.Map;

public interface ProductService {

    List<Product> findAll();
    Product findById(Long id);
    List<Product> findAllById(List<Long> ids);
    List<ProductResponse> findAllProductResponseById(List<Long> ids);
    Product save(Product product);
    void deleteImageByPublicId(String id);
    Product addRating(RatingRequest ratingRequest, Jwt jwt);
    Product updateRating(RatingRequest ratingRequest, Long ratingId, Jwt jwt);
    void setRatingAverage(Product productDB);
    void setTotalStars(Product productDB);
    void setStarsIndexed(Product productDB, RatingRequest ratingRequest);
    void updateStarsIndexed(Product productDB, Rating rating, RatingRequest ratingRequest);
    ProductRecord findByIdWebClient(Long id);
    HttpStatus updateStock(Map<Long, Long> products);
    Product saveWithPhoto(Product product);
    Product updateProduct(Product product);
    String getCustomerName(Jwt jwt);
    ProductRecord toProductRecord(Product product);
}
