package com.example.demo.service;

import java.util.List;
import com.example.demo.model.dto.CategoryDto;



public interface CategoryService {
	public List<CategoryDto>findMainCategories();
	public List<CategoryDto>findSubCategories(Integer parentId);
	public List<CategoryDto> findAllCategories();
}
