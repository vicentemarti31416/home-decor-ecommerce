package com.vcn.orders.service;

import com.vcn.orders.client.PaymentClient;
import com.vcn.orders.client.ProductClient;
import com.vcn.orders.exception.BadRequestException;
import com.vcn.orders.exception.ForbiddenException;
import com.vcn.orders.model.*;
import com.vcn.orders.publisher.Publisher;
import com.vcn.orders.repository.OrderRepository;
import com.vcn.orders.exception.NotFoundException;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final Publisher publisher;
    private final ProductClient productClient;
    private final PaymentClient paymentClient;

    public OrderServiceImpl(OrderRepository orderRepository,
                            Publisher publisher,
                            ProductClient productClient,
                            PaymentClient paymentClient) {
        this.orderRepository = orderRepository;
        this.publisher = publisher;
        this.productClient = productClient;
        this.paymentClient = paymentClient;
    }

    @Override
    public List<Order> findAllByUsername(String username) {
        return orderRepository
                .findAllByUsername(username)
                .orElseThrow(() -> new NotFoundException("Usuario no encontrado"));
    }

    @Override
    public Order findById(String id) {
        return orderRepository
                .findById(id)
                .orElseThrow(() -> new NotFoundException("Pedido no encontrado"));
    }

    @Override
    public Order save(Order order) {
        return orderRepository.save(order);
    }

    @Override
    public void deleteById(String id) {
        orderRepository.deleteById(id);
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
        return orderRepository.save(orderDB);
    }

    @Override
    @Transactional(readOnly = true)
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
        return paymentClient.intentPayment(itemRequest, jwt);
    }

    @Override
    @Transactional
    public Order updateOrderStatus(StatusRequest statusRequest, Jwt jwt) {
        Order orderDB = findById(statusRequest.orderId());
        orderDB.setStatus(OrderStatus.valueOf(statusRequest.status()));
        Order saved = orderRepository.save(orderDB);
        productClient.updateStock(saved, jwt);
        notifyPayment(saved, jwt);
        return saved;
    }

    /**
     * Devuelve todas las Order de un User, convertidas en OrderResponse y ProductResponse
     * Primero obtiene todas las orders usando el username que viene en el token jwt
     * Después obtiene todos los productId de las orders para buscar los product usando webclient al ms products
     * Después convierte los Items de cada Order en ProductResponse y los Order en OrderResponse
     */
    @Override
    @Transactional(readOnly = true)
    public Page<OrderResponse> findAllByUsernameAndIds(Jwt jwt, Pageable pageable, LocalDate start, LocalDate end) {
        String username = jwt.getSubject();
        ZoneId zoneId = ZoneId.of("Europe/Madrid");
        // Si el campo start viene nulo se le da el valor EPOCH (1970-01-01T00:00:00Z) para cubrir el rango sin límite
        Instant startNew = start == null
                ? Instant.EPOCH
                : start.atStartOfDay(zoneId).toInstant();
        // Si el campo end viene nulo se le da el valor de ahora para cubrir el rango sin límite
        Instant endNew = end == null
                ? Instant.now()
                : end.plusDays(1).atStartOfDay(zoneId).toInstant();

        Page<Order> ordersDB = orderRepository
                .findByUsernameAndUpdatedDateBetweenAndStatusNot(username, startNew, endNew, OrderStatus.CART, pageable);
        // Obtiene los productId de cada Item para obtener los ProductResponse completos del ms products
        List<Order> orders = ordersDB.getContent();
        List<Long> productIds =
                orders.stream()
                        .flatMap(order -> order
                                .getItems()
                                .stream()
                                .map(Item::getProductId))
                        .distinct()
                        .toList();
        List<ProductResponse> products = productClient.findProductsById(productIds, jwt);
        return toOrderResponsePage(ordersDB, products);
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

    private List<Item> checkStockAndPrice(Order order, Jwt jwt) {
        Map<Long, ProductRecord> productRecordMap = order.getItems()
                .stream()
                .map(item -> productClient.findProductById(item.getProductId(), jwt))
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

    // Usa RabbitMQ para enviar un mensaje de confirmación de pedido al email del cliente
    private void notifyPayment(Order order, Jwt jwt) {
        List<Long> productIds = order
                .getItems()
                .stream()
                .map(Item::getProductId)
                .toList();
        List<ProductResponse> products = productClient.findProductsById(productIds, jwt);
        OrderResponse orderResponse = toOrderResponseList(List.of(order), products).getFirst();
        OrderConfirmationRequest orderConfirmationRequest = OrderConfirmationRequest
                .builder()
                .recipient(order.getUsername())
                .subject("Confirmación de pedido #" + order.getId())
                .orderResponse(orderResponse)
                .build();
        publisher.send(orderConfirmationRequest);
    }

    private List<OrderResponse> toOrderResponseList(List<Order> orders, List<ProductResponse> products) {
        return orders.stream().map(order -> toOrderResponse(order, products)).toList();
    }

    private Page<OrderResponse> toOrderResponsePage(Page<Order> orders, List<ProductResponse> products) {
        return orders.map(order -> toOrderResponse(order, products));
    }

    /**
     * Convierte los Items de cada pedido en ProductResponse con toda su información adicional (reference, photo...)
     */
    private OrderResponse toOrderResponse(Order order, List<ProductResponse> products) {
        Map<Long, Long> quantities = order.getItems()
                .stream()
                .collect(Collectors.toMap(Item::getProductId, Item::getQuantity));
        /*
          Puebla el campo quantity de cada ProductResponse
          Lo he dejado así en lugar de enviar esa información el ms Product porque es un campo
          que pertenece al Item de ms orders, no al ms products. Así, hay más desacoplamiento
         */
        List<ProductResponse> productResponseList = products
                .stream()
                .filter(productResponse -> quantities.containsKey(productResponse.id()))
                .map(productResponse -> ProductResponse
                        .builder()
                        .id(productResponse.id())
                        .name(productResponse.name())
                        .price(productResponse.price())
                        .quantity(quantities.get(productResponse.id()))
                        .reference(productResponse.reference())
                        .photo(productResponse.photo())
                        .build()
                )
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
    }

}
