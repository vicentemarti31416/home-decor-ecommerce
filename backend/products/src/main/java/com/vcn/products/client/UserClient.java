package com.vcn.products.client;

import org.springframework.http.HttpStatusCode;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class UserClient {

    private final RestClient restClient;

    public UserClient(RestClient restClient) {
        this.restClient = restClient;
    }

    public String getCustomerName(Jwt jwt) {
        String token = jwt.getTokenValue();
        return restClient
                .get()
                .uri("http://localhost:8099/users/restclient/customer")
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
}
