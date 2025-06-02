package com.example.demo.mapper;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.example.demo.model.dto.ProductImageDto;

import com.example.demo.model.entity.ProductImage;


@Component
public class ProductImageMapper {
	
	@Autowired
	private ModelMapper modelMapper;
	
	public ProductImageDto toDto(ProductImage productImage) {
		// Entity 轉 DTO
		return modelMapper.map(productImage, ProductImageDto.class);
	}
	
	public ProductImage toEntity(ProductImageDto productImageDto) {
		// DTO 轉 Entity
		return modelMapper.map(productImageDto, ProductImage.class);
	}
	
}
