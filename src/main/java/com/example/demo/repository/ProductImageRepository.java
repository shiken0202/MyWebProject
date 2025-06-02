package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.demo.model.entity.ProductImage;

public interface ProductImageRepository extends JpaRepository<ProductImage, Long>{
	List<ProductImage>findByProductIdOrderBySortOrder(Long productId);
	void deleteByProductId(Long productId);
	List<ProductImage> findByProductId(Long productId);
	@Query("SELECT COALESCE(MAX(p.sortOrder), 0) FROM ProductImage p WHERE p.product.id = :productId")
	Integer findMaxSortOrderByProductId(@Param("productId") Long productId);
}
