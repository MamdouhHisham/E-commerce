package com.example.inventoryservice.controller;

import com.example.inventoryservice.model.Inventory;
import com.example.inventoryservice.service.InventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/inventory")
@RequiredArgsConstructor
public class InventoryController {

    private final InventoryService inventoryService;

    @GetMapping("/{id}")
    public ResponseEntity<Inventory> findById(@PathVariable Long id) {
        return ResponseEntity.ok(inventoryService.findById(id));
    }

    @PostMapping
    public ResponseEntity<Inventory> addProductToInventory(@RequestParam String skuCode,
                                                           @RequestParam Long quantity){
        return ResponseEntity.ok(inventoryService.addProductToInventory(skuCode, quantity));
    }

    @PutMapping("/quantity/{skuCode}")
    public ResponseEntity<Inventory> updateStorageQuantity(@PathVariable String skuCode,
                                                           @RequestParam Long quantity){
        return ResponseEntity.ok(inventoryService.updateStorageQuantity(skuCode, quantity));
    }

    @PutMapping("/sku/{skuCode}")
    public ResponseEntity<Inventory> updateSkuCode(@PathVariable String skuCode,
                                                   @RequestParam String newSkuCode){
        return ResponseEntity.ok(inventoryService.updateSkuCode(skuCode, newSkuCode));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteById(@PathVariable Long id){
        inventoryService.deleteById(id);
        return ResponseEntity.ok("Inventory by Id: " + id + " is deleted");
    }
}
