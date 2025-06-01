package com.example.demo.mapper;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.example.demo.model.dto.ProductDto;
import com.example.demo.model.entity.Product;



@Component
public class ProductMapper {
	
	@Autowired
	private ModelMapper modelMapper;
	
	public ProductDto toDto(Product product) {
		 // 自動映射基本欄位
        ProductDto dto = modelMapper.map(product, ProductDto.class);
        if(product.getCategory()!=null) {
        	dto.setCategoryId(product.getCategory().getId());
        	dto.setCategoryName(product.getCategory().getName());
        }
		return dto;
	}
	
	public Product toEntity(ProductDto productDto) {
		// DTO 轉 Entity
		return modelMapper.map(productDto, Product.class);
	}
	
}
