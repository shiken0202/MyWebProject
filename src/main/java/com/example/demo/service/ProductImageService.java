package com.example.demo.service;

import java.io.IOException;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.example.demo.model.dto.ImageSortDto;
import com.example.demo.model.dto.ProductImageDto;


public interface ProductImageService {
	List<ProductImageDto> uploadImages(Long productId,List<MultipartFile>files)throws IOException;
	List<ProductImageDto> getImages(Long productId);
	void updateSortOrders(List<ImageSortDto> sortList);
	void deleteImage(Long imageId) throws IOException;
	void deleteAllImagesByProductId(Long productId);
	boolean isImageFile(MultipartFile file);
	String getFileExtension(String filename);
	
}
