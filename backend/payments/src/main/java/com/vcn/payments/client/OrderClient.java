package com.vcn.payments.client;

import com.vcn.payments.model.StatusRequest;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class OrderClient {

    private final RestClient restClient;

    public OrderClient(RestClient restClient) {
        this.restClient = restClient;
    }

    public void updateOrderStatus(StatusRequest statusRequest) {
        restClient
                .patch()
                .uri("http://localhost:8096/orders/restclient/update-status")
                .body(statusRequest)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, (request, response) -> {
                            throw new RuntimeException("Pedido no encontrado");
                        }
                )
                .onStatus(HttpStatusCode::is5xxServerError, (request, response) -> {
                            throw new RuntimeException("Servidor no disponible");
                        }
                )
                .toBodilessEntity();
    }
}
