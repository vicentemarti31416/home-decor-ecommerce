package com.vcn.products.controller;

import com.vcn.products.model.*;
import com.vcn.products.service.ImageUploadService;
import com.vcn.products.service.ProductService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

@RestController
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;
    private final ImageUploadService imageUploadService;

    public ProductController(ProductService productService, ImageUploadService imageUploadService) {
        this.productService = productService;
        this.imageUploadService = imageUploadService;
    }

    @GetMapping
    public ResponseEntity<List<Product>> findAll() {
        List<Product> products = productService.findAll();
        return ResponseEntity.ok().body(products);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Product> findProductById(@PathVariable Long id) {
        return ResponseEntity.ok(productService.findById(id));
    }

    @PostMapping("/ids")
        public ResponseEntity<?> findAllById(@Validated @RequestBody List<Long> ids, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) return validate(bindingResult);
        return ResponseEntity.ok(productService.findAllById(ids));
    }

    @PostMapping("/product-response")
    public ResponseEntity<?> findAllProductResponseById(@Validated @RequestBody List<Long> ids, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) return validate(bindingResult);
        return ResponseEntity.ok(productService.findAllProductResponseById(ids));
    }

    @PostMapping("/rating")
    public ResponseEntity<?> addRating(@Validated @RequestBody RatingRequest ratingRequest, @AuthenticationPrincipal Jwt jwt, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) return validate(bindingResult);
        return ResponseEntity.ok(productService.addRating(ratingRequest, jwt));
    }

    @PutMapping("/rating/{ratingId}")
    public ResponseEntity<?> updateRating(@Validated @RequestBody RatingRequest ratingRequest, @PathVariable Long ratingId, @AuthenticationPrincipal Jwt jwt, BindingResult bindingResult){
        if (bindingResult.hasErrors()) return validate(bindingResult);
        return ResponseEntity.ok(productService.updateRating(ratingRequest, ratingId, jwt));
    }

    @GetMapping("/restclient/{id}")
    public ProductRecord findByIdWebClient(@PathVariable Long id) {
        return productService.findByIdWebClient(id);
    }

    @PostMapping("/restclient/updateStock")
    public HttpStatus updateStock(@Validated @RequestBody Map<Long, Long> products, BindingResult bindingResult) {
        return productService.updateStock(products);
    }

    @PostMapping
    public ResponseEntity<?> saveWithPhoto(@RequestBody Product product, BindingResult bindingResult) throws IOException {
        if (bindingResult.hasErrors()) return validate(bindingResult);
        return ResponseEntity.status(HttpStatus.CREATED).body(productService.saveWithPhoto(product));
    }

    @PostMapping("/upload")
    public ResponseEntity<Map<String, String>> save(@RequestParam("file") MultipartFile file) {
        return ResponseEntity.ok(imageUploadService.uploadImage(file));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateProduct(@Validated @RequestBody Product product, BindingResult bindingResult, @PathVariable Long id) throws IOException {
        if (bindingResult.hasErrors()) return validate(bindingResult);
        return ResponseEntity.status(HttpStatus.CREATED).body(productService.updateProduct(product));
    }

    @DeleteMapping("/{publicId}")
    public ResponseEntity<Void> deleteById(@PathVariable String publicId) {
        productService.deleteImageByPublicId(publicId);
        return ResponseEntity.noContent().build();
    }

    protected ResponseEntity<?> validate(BindingResult bindingResult) {
        Map<String, Object> errors = new HashMap<>();
        bindingResult.getFieldErrors().forEach(error ->
                errors.put(error.getField(),
                        "Field: ".concat(error.getField()).concat(" : ").concat(Objects.requireNonNull(error.getDefaultMessage()))));
        return ResponseEntity.badRequest().body(errors);
    }
}
