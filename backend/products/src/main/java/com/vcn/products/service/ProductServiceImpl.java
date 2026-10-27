package com.vcn.products.service;

import com.vcn.products.exception.ForbiddenException;
import com.vcn.products.exception.NotFoundException;
import com.vcn.products.exception.UpdatingStockException;
import com.vcn.products.model.*;
import com.vcn.products.repository.ProductRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClient;

import java.util.*;
import java.util.stream.IntStream;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final ImageUploadService imageUploadService;
    private final RestClient restClient;
    private final RatingService ratingService;

    public ProductServiceImpl(ProductRepository productRepository,
                              ImageUploadService imageUploadService,
                              RestClient restClient, RatingService ratingService) {
        this.productRepository = productRepository;
        this.imageUploadService = imageUploadService;
        this.restClient = restClient;
        this.ratingService = ratingService;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Product> findAll() {
        return productRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Product findById(Long id) {
        return productRepository.findById(id).orElseThrow(() -> new NotFoundException("Not found"));
    }

    @Override
    public List<Product> findAllById(List<Long> ids) {
        return productRepository.findByIdIn(ids);
    }

    @Override
    public List<ProductResponse> findAllProductResponseById(List<Long> ids) {
        List<Product> products = findAllById(ids);
        return products
                .stream()
                .map(product -> {
                    ProductResponse productResponse = ProductResponse
                            .builder()
                            .id(product.getId())
                            .name(product.getName())
                            .price(product.getPrice().doubleValue())
                            .reference(product.getReference())
                            .photo(product.getPhotos().getFirst().getUrl())
                            .rating(product.getRating())
                            .build();
                    return productResponse;
                })
                .toList();
    }

    @Override
    @Transactional
    public Product save(Product product) {
        return productRepository.save(product);
    }

    @Override
    @Transactional
    public void deleteImageByPublicId(String publicId) {
        imageUploadService.deleteImageByPublicId(publicId);
    }

    @Override
    @Transactional
    public Product addRating(RatingRequest ratingRequest, Jwt jwt) {
        Product productDB = findById(ratingRequest.productId());
        Rating rating = new Rating(
                null,
                ratingRequest.stars(),
                ratingRequest.comment(),
                getCustomerName(jwt),
                jwt.getSubject(),
                productDB
        );

        productDB.getRatings().add(rating);

        setRatingAverage(productDB);

        setTotalStars(productDB);

        setStarsIndexed(productDB, ratingRequest);

        return save(productDB);
    }


    @Override
    @Transactional
    public Product updateRating(RatingRequest ratingRequest, Long ratingId, Jwt jwt) {
        Rating ratingDB = ratingService.findById(ratingId);
        Product productDB = ratingDB.getProduct();

        if (!jwt.getSubject().equals(ratingDB.getUsername())) {
            throw new ForbiddenException("No tiene permiso para realizar esta acción");

        }

        ratingDB.setComment(ratingRequest.comment());

        // Solo vuelve a realizar los cálculos si las estrellas de la valoración son distintas
        if (!ratingDB.getStars().equals(ratingRequest.stars())) {
            updateStarsIndexed(productDB, ratingDB, ratingRequest);
            ratingDB.setStars(ratingRequest.stars());
            setRatingAverage(productDB);
        }

        return save(productDB);
    }

    // Obtiene todas las valoraciones del producto, saca la media y setea el product con ella
    @Override
    public void setRatingAverage(Product productDB) {
        IntStream intStream = productDB.getRatings().stream().map(Rating::getStars).mapToInt(value -> value);
        OptionalDouble averageOptional = intStream.average(); // Con IntStream saca automáticamente la media
        Double average = null;
        if (averageOptional.isPresent()) average = averageOptional.getAsDouble();
        productDB.setRating(average); // Guarda la media como Double
    }

    // Obtiene el conteo total de valoraciones de estrellas del product para saber cuántas valoraciones ha recibido
    @Override
    public void setTotalStars(Product productDB) {
        int total_stars = 0;
        if (productDB.getStatistics() == null || productDB.getStatistics().isEmpty()) {
            List<Integer> statistics = new ArrayList<>(Collections.nCopies(6, 0));
            productDB.setStatistics(statistics);
        } else {
            total_stars = productDB.getStatistics().get(0);
        }
        productDB.getStatistics().set(0, ++total_stars);
    }

    // Obtiene el contador por el número de estrellas del ratingRequest(5, 4, 3...)
    // El primer valor del list, el cero (0), almacena el número total de valoraciones
    // El valor del índice 1 del list almacena el número total de valoraciones de una estrella
    // El valor del índice 2 las valoraciones de 2 estrellas, el índice 3 las de tres estrellas...
    @Override
    public void setStarsIndexed(Product productDB, RatingRequest ratingRequest) {
        // Obtiene el contador de stars según el índice
        int stars = productDB.getStatistics().get(ratingRequest.stars());
        // Sube uno el contador de stars para el índice dado
        productDB.getStatistics().set(ratingRequest.stars(), ++stars);
    }

    /**
     * Actualiza la tabla statistics, quita un voto de stars antiguo y añade uno nuevo
     * Rating es la valoración antigua, la que hay en la base de datos
     * RatingRequest es la valoración nueva
     * Primero obtiene los votos de una cantidad de estrellas por su índice (5, 4, 3...)
     * Después decrementa un voto en el antiguo índice y sube uno en el nuevo
     */
    @Override
    public void updateStarsIndexed(Product productDB, Rating rating, RatingRequest ratingRequest) {
        int ratingCounter = productDB.getStatistics().get(rating.getStars());
        productDB.getStatistics().set(rating.getStars(), --ratingCounter);
        int ratingRequestCounter = productDB.getStatistics().get(ratingRequest.stars());
        productDB.getStatistics().set(ratingRequest.stars(), ++ratingRequestCounter);
    }

    @Override
    public ProductRecord findByIdWebClient(Long id) {
        Product productDB = findById(id);
        return toProductRecord(productDB);
    }

    @Override
    public HttpStatus updateStock(Map<Long, Long> products) {
        try {
            products.forEach((productId, quantity) -> {
                Product productDB = findById(productId);
                productDB.setStock(productDB.getStock() - quantity);
                save(productDB);
            });
        } catch (RuntimeException e) {
            throw new UpdatingStockException("Updating Stock Exception");
        }
        return HttpStatus.OK;
    }

    @Override
    public Product saveWithPhoto(Product product) {
        List<Photo> photos = new ArrayList<>(product.getPhotos());
        photos.forEach((photo -> photo.setProduct(product)));
        save(product);
        return product;
    }

    @Override
    public Product updateProduct(Product product) {
        Product productDB = findById(product.getId());
        productDB.setName(product.getName());
        productDB.setDescription(product.getDescription());
        productDB.setPrice(product.getPrice());
        productDB.setCategory(product.getCategory());
        productDB.setReference(product.getReference());
        List<String> existingPhotoIds = productDB
                .getPhotos()
                .stream()
                .map(Photo::getPublicId)
                .toList();
        List<Photo> distinctPhotoIds = product
                .getPhotos()
                .stream()
                .filter((photo -> !existingPhotoIds.contains(photo.getPublicId())))
                .toList();
        productDB.getPhotos().addAll(distinctPhotoIds);
        productDB.getPhotos().forEach((photo) -> photo.setProduct(productDB));
        return save(productDB);
    }

    @Override
    public String getCustomerName(Jwt jwt) {
        String token = jwt.getTokenValue();
        return restClient
                .post()
                .uri("http://localhost:8099/users/webclient/customer")
                .headers(httpHeaders -> httpHeaders.setBearerAuth(token))
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, (request, response) -> {
                            throw new RuntimeException("Error de solicitud");
                        }
                )
                .onStatus(HttpStatusCode::is5xxServerError, (request, response) -> {
                            throw new RuntimeException("Servidor no disponible");
                        }
                )
                .body(String.class);
    }

    @Override
    public ProductRecord toProductRecord(Product product) {
        return ProductRecord.builder()
                .id(product.getId())
                .price(product.getPrice())
                .stock(product.getStock())
                .build();
    }

}
