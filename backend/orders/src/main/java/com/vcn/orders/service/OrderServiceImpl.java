package com.vcn.orders.service;

import com.vcn.orders.exception.BadRequestException;
import com.vcn.orders.exception.ForbiddenException;
import com.vcn.orders.model.*;
import com.vcn.orders.publisher.Publisher;
import com.vcn.orders.repository.OrderRepository;
import com.vcn.orders.exception.NotFoundException;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatusCode;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClient;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final RestClient restClient;
    private final Publisher publisher;

    public OrderServiceImpl(OrderRepository orderRepository, RestClient restClient, Publisher publisher) {
        this.orderRepository = orderRepository;
        this.restClient = restClient;
        this.publisher = publisher;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Order> findAllByUsername(String username) {
        return orderRepository.findAllByUsername(username).orElseThrow(() -> new NotFoundException("User Not Found"));
    }

    @Override
    @Transactional(readOnly = true)
    public Order findById(String id) {
        return orderRepository.findById(id).orElseThrow(() -> new NotFoundException("User Not Found"));
    }

    @Override
    @Transactional
    public Order updateOrder(Order order, Jwt jwt) {
        Order orderDB = findById(order.getId());
        if (!orderDB.getUsername().equals(jwt.getSubject())) throw new ForbiddenException("Usuario no autorizado");
        if (orderDB.getStatus() != OrderStatus.CART) throw new BadRequestException("Pedido no válido");
        List<Item> itemsWithStock = checkStockAndPrice(order, jwt);
        orderDB.getItems().clear();
        orderDB.getItems().addAll(itemsWithStock);
        BigDecimal amount = orderDB.getItems()
                .stream()
                .filter(item -> item.getQuantity() != null && item.getQuantity() > 0)
                .map(item -> item.getUnitPrice().multiply(new BigDecimal(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        orderDB.setAmount(amount);
        return save(orderDB);
    }

    @Override
    public String paymentIntentHelper(Order order, Jwt jwt) {
        Order orderDB = findById(order.getId());
        List<Item> itemsWithStock = checkStockAndPrice(orderDB, jwt);
        BigDecimal amount = itemsWithStock
                .stream()
                .map(item -> item.getUnitPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        long cents = amount
                .multiply(BigDecimal.valueOf(100))
                .setScale(0, RoundingMode.HALF_UP)
                .longValueExact();
        Map<String, String> metadata = new HashMap<>();
        metadata.put("orderId", order.getId());
        IntentRequest itemRequest = new IntentRequest(cents, "eur", metadata);
        return intentPayment(itemRequest, jwt);
    }

    @Override
    public Order updateOrderStatus(StatusRequest statusRequest, Jwt jwt) {
        Order orderDB = findById(statusRequest.orderId());
        orderDB.setStatus(OrderStatus.valueOf(statusRequest.status()));
        Order saved = save(orderDB);
        updateStock(saved, jwt);
        notifyPayment(saved, jwt);
        return saved;
    }

    @Override
    public List<Item> checkStockAndPrice(Order order, Jwt jwt) {
        Map<Long, ProductRecord> productRecordMap = order.getItems()
                .stream()
                .map(item -> findProductById(item.getProductId(), jwt))
                .collect(Collectors.toMap(ProductRecord::id, productRecord -> productRecord));
        return order.getItems()
                .stream()
                .map(item -> {
                    ProductRecord productRecord = productRecordMap.get(item.getProductId());
                    item.setUnitPrice(productRecord.price());
                    if (productRecord.stock() < item.getQuantity()) item.setQuantity(productRecord.stock());
                    return item;
                })
                .toList();
    }

    @Override
    public ProductRecord findProductById(Long id, Jwt jwt) {
        String token = jwt.getTokenValue();
        return restClient
                .get()
                .uri("http://localhost:8095/products/restclient/{id}", id)
                .headers(httpHeaders -> httpHeaders.setBearerAuth(token))
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, (request, response) -> {
                            throw new RuntimeException("Producto no encontrado");
                        }
                )
                .onStatus(HttpStatusCode::is5xxServerError, (request, response) -> {
                            throw new RuntimeException("Servidor no disponible");
                        }
                )
                .body(ProductRecord.class);
    }

    /**
     * Devuelve todas las Order de un User, convertidas en OrderResponse y ProductResponse
     * Primero obtiene todas las orders usando el username que viene en el token jwt
     * Después obtiene todos los productId de las orders para buscar los product usando webclient al ms products
     * Después convierte los Items de cada Order en ProductResponse y los Order en OrderResponse
     */
    @Override
    @Transactional(readOnly = true)
    public List<OrderResponse> findAllByUsernameAndIds(Jwt jwt) {

        String username = jwt.getSubject();

        List<Order> ordersDB = findAllByUsername(username);
        if (ordersDB.isEmpty()) throw new NotFoundException("Not Found");

        // Obtiene los productId de cada Item para obtener los ProductResponse completos del ms products
        List<Long> productIds = ordersDB
                .stream()
                .flatMap(order -> order
                        .getItems()
                        .stream()
                        .map(Item::getProductId))
                .distinct()
                .toList();

        List<ProductResponse> products = findProductsById(productIds, jwt);

        return toOrderResponseList(ordersDB, products);
    }

    /**
     * Convierte los Items de cada pedido en ProductResponse con toda su información adicional
     * para mostrarlos en el listado de Orders de la vista porque dichos Item no tienen la misma
     * información que los ProductResponse (reference, photo...)
     */
    @Override
    public List<OrderResponse> toOrderResponseList(List<Order> orders, List<ProductResponse> products) {

        return orders
                .stream()
                .map(order -> {
                    // Crea un map con la key como el productId y el value su cantidad, obtenido de cada Item del pedido
                    Map<Long, Long> quantities = order.getItems()
                            .stream()
                            .collect(Collectors.toMap(Item::getProductId, Item::getQuantity));
                    /**
                     * Puebla el campo quantity de cada ProductResponse
                     * Lo he dejado así en lugar de enviar esa información el ms Product porque es un campo
                     * que pertenece al Item de ms orders, no al ms products. Así, hay más desacoplamiento
                     */
                    List<ProductResponse> productResponseList = products
                            .stream()
                            .filter(productResponse -> quantities.containsKey(productResponse.id()))
                            .map(productResponse -> {
                                return ProductResponse
                                        .builder()
                                        .id(productResponse.id())
                                        .name(productResponse.name())
                                        .price(productResponse.price())
                                        .quantity(quantities.get(productResponse.id()))
                                        .reference(productResponse.reference())
                                        .photo(productResponse.photo())
                                        .build();
                            })
                            .toList();

                    return OrderResponse
                            .builder()
                            .id(order.getId())
                            .amount(order.getAmount() != null ? order.getAmount().doubleValue() : 0.0)
                            .status(order.getStatus().name())
                            .createdDate(order.getCreatedDate())
                            .updatedDate(order.getUpdatedDate())
                            .products(productResponseList)
                            .build();
                })
                .toList();
    }

    @Override
    public List<ProductResponse> findProductsById(List<Long> ids, Jwt jwt) {
        String token = jwt.getTokenValue();
        return restClient
                .post()
                .uri("http://localhost:8095/products/product-response")
                .headers(httpHeaders -> httpHeaders.setBearerAuth(token))
                .body(ids)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, (request, response) -> {
                            throw new RuntimeException("Error de solicitud");
                        }
                )
                .onStatus(HttpStatusCode::is5xxServerError, (request, response) -> {
                            throw new RuntimeException("Servidor no disponible");
                        }
                )
                .body(new ParameterizedTypeReference<List<ProductResponse>>() {});
    }

    @Override
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

    @Override
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


    /**
     * Busca una Order con el status indicado que pertenezca al username que se ha autenticado
     * Se usa para ver si el usuario tiene un Order con status CART, si no lo tiene se crea
     */
    @Override
    @Transactional
    public Order findByStatusAndUsername(OrderStatus status, String username) {
        Optional<Order> orderOptional = orderRepository.findByStatusAndUsername(status, username);
        if (orderOptional.isEmpty()) {
            Order order = new Order();
            order.setStatus(OrderStatus.CART);
            order.setUsername(username);
            orderOptional = Optional.of(orderRepository.save(order));
        }
        return orderOptional.get();
    }

    @Override
    @Transactional
    public Order save(Order order) {
        return orderRepository.save(order);
    }

    @Override
    @Transactional
    public void deleteById(String id) {
        orderRepository.deleteById(id);
    }

    // Usa RabbitMQ para enviar un mensaje de confirmación de pedido al email del cliente
    @Override
    public void notifyPayment(Order order, Jwt jwt) {
        List<Long> productIds = order
                .getItems()
                .stream()
                .map(Item::getProductId)
                .toList();
        List<ProductResponse> products = findProductsById(productIds, jwt);
        OrderResponse orderResponse = toOrderResponseList(List.of(order), products).getFirst();
        OrderConfirmationRequest orderConfirmationRequest = OrderConfirmationRequest
                .builder()
                .recipient(order.getUsername())
                .subject("Confirmación de pedido #" + order.getId())
                .orderResponse(orderResponse)
                .build();
        publisher.send(orderConfirmationRequest);
    }
}
