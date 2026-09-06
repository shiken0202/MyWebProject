package com.example.demo.dao.impl;

import com.example.demo.dao.ProductDao;
import com.example.demo.model.entity.Product;
import com.example.demo.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository("jpa")
public class ProductDaoJpaImpl implements ProductDao {
    @Autowired
    private ProductRepository productRepository;
    @Override
    public List<Product> findByStoreIdWithImages(Long storeId) {
        return productRepository.findByStoreIdWithImages(storeId);
    }
}
