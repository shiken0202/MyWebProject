package com.example.demo.service.impl;

import java.math.BigDecimal;
import java.util.List;

import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.exception.CategoryNotFoundException;
import com.example.demo.exception.ProductNotFoundException;
import com.example.demo.exception.StoreNotFoundException;
import com.example.demo.mapper.ProductMapper;
import com.example.demo.model.dto.ProductDto;
import com.example.demo.model.entity.Category;
import com.example.demo.model.entity.Product;
import com.example.demo.model.entity.Store;
import com.example.demo.repository.CategoryRepository;
import com.example.demo.repository.ProductRepository;
import com.example.demo.repository.StoreRepository;
import com.example.demo.service.ProductService;
@Service
public class ProductServiceImpl implements ProductService {
	
	@Autowired
	private ProductRepository productRepository;
	@Autowired
	private StoreRepository storeRepository;
	@Autowired
	private CategoryRepository categoryRepository;
	@Autowired
	private ProductMapper productMapper;

	@Override
	public List<ProductDto> findAllProduct() {
		List<Product>products= productRepository.findAll();
		if(products==null) {
			return null;
		}
		return products.stream().map(p->productMapper.toDto(p)).collect(Collectors.toList());
	}
	@Override
	public List<ProductDto> findProductsByStoreId(Long StoreId) {
		List<Product>products= productRepository.findByStoreId(StoreId);
		if(products==null) {
			return null;
		}
		return products.stream().map(p->productMapper.toDto(p)).collect(Collectors.toList());
	}
	@Transactional
	@Override
	public void addProduct(Long storeId,String title, String brand, Integer categoryId, BigDecimal price, Integer stock,
			String productDescription) {
		Product product=new Product();
		Optional<Store> storeOpt =storeRepository.findById(storeId);
		Optional<Category> categoryOpt=categoryRepository.findById(categoryId);
		if(storeOpt.isEmpty()) {
			throw new StoreNotFoundException("查無商店");
		}
		if(categoryOpt.isEmpty()) {
			throw new CategoryNotFoundException("查無分類");
		}
		Store store=storeOpt.get();
		Category category=categoryOpt.get();
		product.setStore(store);
		product.setTitle(title);
		product.setBrand(brand);
		product.setCategory(category);
		product.setPrice(price);
		product.setStock(stock);
		product.setDescription(productDescription);
		productRepository.save(product);
		
	}

	@Override
	public void updateProduct(Long id, String title, String brand, Integer categoryId, BigDecimal price, Integer stock,
			String productDescription) {
		Optional<Product> productOpt=productRepository.findById(id);
		Optional<Category> categoryOpt=categoryRepository.findById(categoryId);
		Category category=categoryOpt.get();
		Product product=productOpt.get();
		if(categoryOpt.isEmpty()) {
			throw new CategoryNotFoundException("查無分類");
		}
		if(productOpt.isEmpty()) {
			throw new ProductNotFoundException("查無商品");
		}
		product.setTitle(title);
		product.setBrand(brand);
		product.setCategory(category);
		product.setPrice(price);
		product.setStock(stock);
		product.setDescription(productDescription);
		productRepository.save(product);
	}

	@Override
	public void deleteProduct(Long id) {
		Optional<Product> productOpt=productRepository.findById(id);
		if(productOpt.isEmpty()) {
			throw new ProductNotFoundException("查無商品");
		}
		Product product=productOpt.get();
		productRepository.delete(product);
	}

	@Override
	public void isActiveProduct(Long id) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void isNotActiveProduct(Long id) {
		// TODO Auto-generated method stub
		
	}
	


}
