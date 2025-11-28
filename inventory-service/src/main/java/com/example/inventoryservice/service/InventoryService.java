package com.example.inventoryservice.service;

import com.example.inventoryservice.exceptions.DuplicateResourceException;
import com.example.inventoryservice.exceptions.ResourceNotFoundException;
import com.example.inventoryservice.model.Inventory;
import com.example.inventoryservice.repository.InventoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class InventoryService {

    private final InventoryRepository inventoryRepository;

    public Inventory findById(Long id){
        Optional<Inventory> inventory = inventoryRepository.findById(id);
        log.info("Fetching inventory with ID: {}", id);
        if(inventory.isPresent()){
            return inventory.get();
        } else {
            log.error("Inventory record not found with ID: {}", id);
            throw new ResourceNotFoundException("Inventory not found with id: " + id);
        }
    }

    public List<Inventory> getAllStorage(){
        log.info("Fetching all inventory records");
        List<Inventory> inventoryList = inventoryRepository.findAll();
        log.info("Retrieved {} inventory records", inventoryList.size());
        return inventoryList;
    }

    public Inventory addProductToInventory(String skuCode, Long quantity){

        log.info("Request to add inventory for SKU: {}, Quantity: {}", skuCode, quantity);

        if(inventoryRepository.existsBySkuCode(skuCode)){
            log.warn("Inventory entry already exists for SKU: {}", skuCode);
            throw new DuplicateResourceException("Inventory with SKU " + skuCode + " already exists");
        }

        Inventory current = new Inventory();
        current.setSkuCode(skuCode);
        current.setQuantity(quantity);

        Inventory savedInventory = inventoryRepository.save(current);
        log.info("Inventory added successfully with ID: {}", savedInventory.getId());
        return savedInventory;

    }

     public Inventory updateStorageQuantity(String skuCode ,Long quantity){
         log.info("Request to update quantity for SKU: {} to {}", skuCode, quantity);

         if(inventoryRepository.existsBySkuCode(skuCode)){
             Inventory inventory = inventoryRepository.findBySkuCode(skuCode);
             inventory.setQuantity(quantity);
             Inventory updatedInventory = inventoryRepository.save(inventory);
             log.info("Quantity updated successfully for SKU: {}", skuCode);
             return updatedInventory;
         } else {
             log.error("Cannot update quantity. SKU {} not found", skuCode);
             throw  new RuntimeException("can't update quantity: SKU " + skuCode + " not found.");
         }
     }

     public Inventory updateSkuCode(String currentSkuCode, String newSkuCode){
        if (inventoryRepository.existsBySkuCode(currentSkuCode)){
            Inventory inventory = inventoryRepository.findBySkuCode(currentSkuCode);
            inventory.setSkuCode(newSkuCode);
            Inventory updatedInventory = inventoryRepository.save(inventory);
            log.info("SKU code updated successfully from {} to {}", currentSkuCode, newSkuCode);
            return updatedInventory;
        } else {
            log.warn("Cannot update SKU to {}. That SKU is already in use.", newSkuCode);
            throw new DuplicateResourceException("SKU " + newSkuCode + " already exists");
        }
     }

     public void deleteById(Long id){
         log.info("Request to delete inventory record with ID: {}", id);

         if(!inventoryRepository.existsById(id)){
             log.error("Cannot delete. Inventory ID {} not found", id);
             throw new ResourceNotFoundException("Inventory not found with id: " + id);
         }

         inventoryRepository.deleteById(id);
         log.info("Inventory record {} deleted successfully", id);
     }

}
