package com.example.productservice.client;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "INVENTORYSERVICE")
public interface InventoryClient {

    @PostMapping("/api/inventory")
    void addProductToInventory(@RequestParam("skuCode") String skuCode,
                                            @RequestParam("quantity") Long quantity);

}
