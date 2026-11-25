package com.vcn.orders.client;

import com.vcn.orders.exception.NotFoundException;
import com.vcn.orders.model.Item;
import com.vcn.orders.model.Order;
import com.vcn.orders.model.ProductRecord;
import com.vcn.orders.model.ProductResponse;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatusCode;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class ProductClient {

    private final RestClient restClient;

    public ProductClient(RestClient restClient) {
        this.restClient = restClient;
    }

    public ProductRecord findProductById(Long id, Jwt jwt) {
        String token = jwt.getTokenValue();
        return restClient
                .get()
                .uri("http://localhost:8095/products/restclient/{id}", id)
                .headers(httpHeaders -> httpHeaders.setBearerAuth(token))
                .retrieve()
                .onStatus(httpStatusCode -> httpStatusCode.value() == 404, (request, response) -> {
                    throw new NotFoundException("Producto no encontrado");
                })
                .onStatus(HttpStatusCode::is4xxClientError, (request, response) -> {
                            throw new RuntimeException("Error de solicitud");
                        }
                )
                .onStatus(HttpStatusCode::is5xxServerError, (request, response) -> {
                            throw new RuntimeException("Servidor no disponible");
                        }
                )
                .body(ProductRecord.class);
    }

    public List<ProductResponse> findProductsById(List<Long> ids, Jwt jwt) {
        String token = jwt.getTokenValue();
        return restClient
                .post()
                .uri("http://localhost:8095/products/restclient/product-response")
                .headers(httpHeaders -> httpHeaders.setBearerAuth(token))
                .body(ids)
                .retrieve()
                .onStatus(httpStatusCode -> httpStatusCode.value() == 404, (request, response) -> {
                    throw new NotFoundException("Producto no encontrado");
                })
                .onStatus(HttpStatusCode::is4xxClientError, (request, response) -> {
                            throw new NotFoundException("Error de solicitud");
                        }
                )
                .onStatus(HttpStatusCode::is5xxServerError, (request, response) -> {
                            throw new RuntimeException("Servidor no disponible");
                        }
                )
                .body(new ParameterizedTypeReference<List<ProductResponse>>() {
                });
    }

    public void updateStock(Order order, Jwt jwt) {
        String token = jwt.getTokenValue();
        Map<Long, Long> products = order.getItems()
                .stream()
                .collect(Collectors.toMap(Item::getProductId, Item::getQuantity));
        restClient
                .post()
                .uri("http://localhost:8095/products/restclient/updateStock")
                .headers(httpHeaders -> httpHeaders.setBearerAuth(token))
                .body(products)
                .retrieve()
                .onStatus(httpStatusCode -> httpStatusCode.value() == 404, (request, response) -> {
                    throw new NotFoundException("Producto no encontrado");
                })
                .onStatus(HttpStatusCode::is4xxClientError, (request, response) -> {
                            throw new RuntimeException("Error de solicitud");
                        }
                )
                .onStatus(HttpStatusCode::is5xxServerError, (request, response) -> {
                            throw new RuntimeException("Servidor no disponible");
                        }
                )
                .toBodilessEntity();
    }
}
