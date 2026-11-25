package com.vcn.orders.client;

import com.vcn.orders.model.IntentRequest;
import org.springframework.http.HttpStatusCode;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class PaymentClient {

    private final RestClient restClient;

    public PaymentClient(RestClient restClient) {
        this.restClient = restClient;
    }

    public String intentPayment(IntentRequest intentRequest, Jwt jwt) {
        String token = jwt.getTokenValue();
        return restClient
                .post()
                .uri("http://localhost:8097/payments/restclient/intent-payment")
                .headers(httpHeaders -> httpHeaders.setBearerAuth(token))
                .body(intentRequest)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, (request, response) -> {
                    throw new RuntimeException("Error de solicitud");
                })
                .onStatus(HttpStatusCode::is5xxServerError, (request, response) -> {
                    throw new RuntimeException("Servidor no disponible");
                })
                .body(String.class);
    }
}
