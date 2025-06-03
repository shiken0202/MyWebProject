package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.model.entity.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {
	@Query("SELECT DISTINCT p FROM Product p " +
	           "LEFT JOIN FETCH p.productImages " + // 強制載入圖片
	           "LEFT JOIN FETCH p.category " +      // 強制載入分類
	           "WHERE p.store.id = :storeId")
    List<Product> findByStoreIdWithImages(@Param("storeId") Long storeId);
	
	@Transactional
	@Modifying
	@Query("update Product p set p.isActive =true where p.id=:id")
	public int isActive(@Param("id")Long id);
	

	@Transactional
	@Modifying
	@Query("update Product p set p.isActive =false where p.id=:id")
	public int isNotActive(@Param("id")Long id);
	
	@Transactional
	@Modifying(clearAutomatically = true)
	@Query("update Product p set p.viewCount = p.viewCount+1 where p.id=:id")
	public int viewCount(@Param("id")Long id);
}
