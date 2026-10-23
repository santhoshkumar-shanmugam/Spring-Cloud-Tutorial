package com.santhosh.order_service.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class OrderLineItemsDTO {
    private String skuCode;
    private BigDecimal price;
    private Integer quantity;
}
