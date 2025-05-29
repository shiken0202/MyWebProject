package com.example.demo.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;

import com.example.demo.model.entity.Category;
@Repository
public interface CategoryRepository  extends JpaRepository<Category, Integer>{
	// 查詢所有主分類（parent_id 為 null）
	List<Category> findByParentIsNull();
	// 查詢某主分類下的所有子分類
	List<Optional<Category>> findByParent_Id(Integer parentId);
}
