package com.example.inventoryservice.repository;

import com.example.inventoryservice.model.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InventoryRepository extends JpaRepository<Inventory, Long> {

    boolean existsBySkuCode(String skuCode);

    Inventory findBySkuCode(String skuCode);

    List<Inventory> findBySkuCodeIn(List<String> skuCodes);

    boolean existsBySkuCodeAndQuantityIsGreaterThanEqual(String skuCode, Long quantity);
}
