package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.model.entity.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {
	List<Product>findByStoreId(Long storeId);
	
	@Transactional
	@Modifying
	@Query("update Product p set p.isActive =true where p.id=:id")
	public int isActive(Long id);
	

	@Transactional
	@Modifying
	@Query("update Product p set p.isActive =false where p.id=:id")
	public int isNotActive(Long id);
}
