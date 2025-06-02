package com.example.demo.mapper;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.example.demo.model.dto.ProductDto;
import com.example.demo.model.dto.ProductImageDto;
import com.example.demo.model.entity.Product;
import com.example.demo.model.entity.ProductImage;



@Component
public class ProductMapper {
	
	@Autowired
	private ModelMapper modelMapper;
	@Autowired
	private ProductImageMapper productImageMapper;
	
	public ProductDto toDto(Product product) {
		 // 自動映射基本欄位
        ProductDto dto = modelMapper.map(product, ProductDto.class);
        if(product.getCategory()!=null) {
        	dto.setCategoryId(product.getCategory().getId());
        	dto.setCategoryName(product.getCategory().getName());
        }
     // 手動設定圖片清單（按 sortOrder 排序）
        if (product.getProductImages() != null && !product.getProductImages().isEmpty()) {
            List<ProductImageDto> imageDtos = product.getProductImages().stream()
                .sorted(Comparator.comparing(ProductImage::getSortOrder))
                .map(productImageMapper::toDto)
                .collect(Collectors.toList());
            dto.setImages(imageDtos);
        } else {
            dto.setImages(Collections.emptyList()); // 避免 null
        }
		return dto;
	}
	
	public Product toEntity(ProductDto productDto) {
		// DTO 轉 Entity
		return modelMapper.map(productDto, Product.class);
	}
	
}
