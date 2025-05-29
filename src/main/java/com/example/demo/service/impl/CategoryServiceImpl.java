package com.example.demo.service.impl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.mapper.CategoryMapper;
import com.example.demo.model.dto.CategoryDto;
import com.example.demo.model.entity.Category;
import com.example.demo.repository.CategoryRepository;
import com.example.demo.service.CategoryService;

@Service
public class CategoryServiceImpl implements CategoryService {
	
	@Autowired
	private CategoryRepository categoryRepository;
	@Autowired
	private CategoryMapper categoryMapper;
	
	@Override
	public List<CategoryDto> findMainCategories() {
		List<Category> categories=categoryRepository.findByParentIsNull();
		return categories.stream().map(c->categoryMapper.toDto(c)).collect(Collectors.toList());
	}

	@Override
	public List<CategoryDto> findSubCategories(Integer parentId) {
		List<Optional<Category>> SubCategories=categoryRepository.findByParent_Id(parentId);
		if(SubCategories.isEmpty()) {
			return null;
		}
		
		return SubCategories.stream().map(subc->categoryMapper.toDto(subc.get())).collect(Collectors.toList());
	}

}
