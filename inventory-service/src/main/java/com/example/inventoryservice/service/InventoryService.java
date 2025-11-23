package com.example.inventoryservice.service;

import com.example.inventoryservice.model.Inventory;
import com.example.inventoryservice.repository.InventoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class InventoryService {

    private final InventoryRepository inventoryRepository;

    public Inventory findById(Long id){
        return inventoryRepository.findById(id).orElseThrow(() -> new RuntimeException("Not Found"));
    }

    public List<Inventory> getAllStorage(){
        return inventoryRepository.findAll();
    }

    public Inventory addProduct(String skuCode, Long quantity){
        Inventory current = new Inventory();
        current.setSkuCode(skuCode);
        current.setQuantity(quantity);

        return inventoryRepository.save(current);
    }

     public Inventory updateStorageQuantity(String skuCode, Long quantity){
         if(inventoryRepository.existsByName(skuCode)){
             Inventory inventory = inventoryRepository.findByName(skuCode);
             inventory.setQuantity(quantity);
             return inventoryRepository.save(inventory);
         } else {
             throw  new RuntimeException("can't update quantity: SKU " + skuCode + " not found.");
         }
     }

     public Inventory updateSkuCode(String skuCode){
        if (inventoryRepository.existsByName(skuCode)){
            Inventory inventory = inventoryRepository.findByName(skuCode);
            inventory.setSkuCode(skuCode);
            return inventoryRepository.save(inventory);
        } else {
            throw new RuntimeException("can't update sku code");
        }
     }

}
