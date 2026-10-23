package com.santhosh.inventory_service.controller;

import com.santhosh.inventory_service.DTO.InventoryResponse;
import com.santhosh.inventory_service.service.InventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/inventory")
@RequiredArgsConstructor
public class InventoryController {

    private final InventoryService inventoryService;

    @GetMapping
    public List<InventoryResponse> isInStock(@RequestParam List<String> skuCode) {
        // Implement logic to check if the product is in stock based on the SKU code
        return inventoryService.isInStock(skuCode); // Placeholder return value
    }
}
