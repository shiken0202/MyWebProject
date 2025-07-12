package com.example.demo.dao;

import com.example.demo.model.entity.Product;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProductDao {
    List<Product> findByStoreIdWithImages(Long storeId);
}

