package com.vcn.users.client;

import com.vcn.users.exception.NotFoundException;
import org.springframework.http.HttpStatusCode;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class ProductClient {

    private final RestClient restClient;

    public ProductClient(RestClient restClient) {
        this.restClient = restClient;
    }

    public void findProductById(Long productId, Jwt jwt) {
        String token = jwt.getTokenValue();
        restClient
                .get()
                .uri("http://localhost:8095/products/restclient/{id}", productId)
                .headers(httpHeaders -> httpHeaders.setBearerAuth(token))
                .retrieve()
                .onStatus(httpStatusCode -> httpStatusCode.value() == 404, (request, response) -> {
                    throw new NotFoundException("Producto no encontrado");
                })
                .onStatus(HttpStatusCode::is4xxClientError, (request, response) -> {
                    throw new RuntimeException("Error de solicitud");
                })
                .onStatus(HttpStatusCode::is5xxServerError, (request, response) -> {
                    throw new RuntimeException("Servidor no disponible");
                })
                .toBodilessEntity();
    }
}
