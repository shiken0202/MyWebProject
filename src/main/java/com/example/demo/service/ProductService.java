package com.example.demo.service;

import java.math.BigDecimal;
import java.util.List;

import com.example.demo.model.dto.ProductDto;

public interface ProductService {
	List<ProductDto> findAllProduct();
	List<ProductDto> findProductsByStoreId(Long StoreId);
	void addProduct(Long storeId,String title,String brand ,Integer categoryId,BigDecimal price,Integer stock,String productDescription);
	void updateProduct(Long id,String title,String brand ,Integer categoryId,BigDecimal price,Integer stock,String productDescription);
	void deleteProduct(Long id);
	void isActiveProduct(Long id);
	void isNotActiveProduct(Long id);
	
}
