package com.example.demo.mapper;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.example.demo.model.dto.CategoryDto;
import com.example.demo.model.dto.StoreDto;
import com.example.demo.model.dto.UserDto;
import com.example.demo.model.entity.Category;
import com.example.demo.model.entity.Store;
import com.example.demo.model.entity.User;

@Component
public class CategoryMapper {
	
	@Autowired
	private ModelMapper modelMapper;
	
	public CategoryDto toDto(Category category) {
		// Entity 轉 DTO
		return modelMapper.map(category, CategoryDto.class);
	}
	
	public Category toEntity(CategoryDto categoryDto) {
		// DTO 轉 Entity
		return modelMapper.map(categoryDto, Category.class);
	}
	
}
