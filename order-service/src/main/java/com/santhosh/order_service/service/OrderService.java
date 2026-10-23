package com.santhosh.order_service.service;

import com.santhosh.order_service.DTO.InventoryResponse;
import com.santhosh.order_service.DTO.OrderLineItemsDTO;
import com.santhosh.order_service.DTO.OrderRequest;
import com.santhosh.order_service.model.Order;
import com.santhosh.order_service.model.OrderLineItems;
import com.santhosh.order_service.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Arrays;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderService {

    private final OrderRepository orderRepository;
    private final WebClient.Builder webClientBuilder;

    public void placeOrder(OrderRequest orderRequest) {
        Order order= new Order();
        order.setOrderNumber(UUID.randomUUID().toString());
        order.setOrderLineItems(orderRequest.getOrderLineItemsList().stream()
                .map(this::mapToDto).toList());

        InventoryResponse[] skuCodes = webClientBuilder.build().get().uri("http://inventory-service/api/inventory",
                        uriBuilder -> uriBuilder.queryParam("skuCode",
                                        order.getOrderLineItems().stream()
                                                .map(OrderLineItems::getSkuCode)
                                                .toList())
                                .build())
                .retrieve()
                .bodyToMono(InventoryResponse[].class)
                .block();
        if(skuCodes != null) {
            boolean allInStock = Arrays.stream(skuCodes).allMatch(obj->obj.getIsInStock().equals(true));
            if(allInStock){
                orderRepository.save(order);
                log.info("Order is created successfully with order number: {}", order.getOrderNumber());
            }else{
                throw new IllegalArgumentException("Product is not in stock, please try again later");
            }
        }
    }

    private OrderLineItems mapToDto(OrderLineItemsDTO orderLineItems) {
        return OrderLineItems.builder()
                .price(orderLineItems.getPrice())
                .quantity(orderLineItems.getQuantity())
                .skuCode(orderLineItems.getSkuCode())
                .build();
    }
}
