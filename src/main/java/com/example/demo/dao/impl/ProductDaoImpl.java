package com.example.demo.dao.impl;

import com.example.demo.dao.ProductDao;
import com.example.demo.model.entity.Product;
import com.example.demo.myBatisMapper.ProductMyBatisMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public class ProductDaoImpl implements ProductDao {
    @Autowired
    ProductMyBatisMapper productMyBatisMapper;
    @Override
    public List<Product> findByStoreIdWithImages(Long storeId) {
    productMyBatisMapper.findByStoreIdWithImages(storeId);
        return productMyBatisMapper.findByStoreIdWithImages(storeId);
    }
}
