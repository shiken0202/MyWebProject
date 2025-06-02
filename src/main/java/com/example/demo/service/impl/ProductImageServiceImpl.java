package com.example.demo.service.impl;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.exception.ProductNotFoundException;
import com.example.demo.mapper.ProductImageMapper;
import com.example.demo.model.dto.ImageSortDto;
import com.example.demo.model.dto.ProductImageDto;
import com.example.demo.model.entity.Product;
import com.example.demo.model.entity.ProductImage;
import com.example.demo.repository.ProductImageRepository;
import com.example.demo.repository.ProductRepository;
import com.example.demo.service.ProductImageService;



@Service
@Transactional
public class ProductImageServiceImpl implements ProductImageService {
	@Autowired
	private ProductImageMapper productImageMapper;
	@Autowired
	private ProductImageRepository productImageRepository;
	@Autowired
	private ProductRepository productRepository;
	
	private final String uploadDir = "uploads/products/";
	
	@Override
	public List<ProductImageDto> uploadImages(Long productId, List<MultipartFile> files) throws IOException {
		Optional<Product> productOpt=productRepository.findById(productId);
		if(productOpt.isEmpty()) {
			throw new ProductNotFoundException("找不到產品");
		}
		Product product=productOpt.get();
		
		// 取得當前最大排序號
		Integer maxSortOrder=productImageRepository.findMaxSortOrderByProductId(productId);
		if(maxSortOrder==null) {
			maxSortOrder=0;
		}
		List<ProductImageDto> uploadProductImages=new ArrayList<ProductImageDto>();
		try {
			 // 建立上傳目錄
			Path uploadPath=Paths.get(uploadDir);
			if(!Files.exists(uploadPath)) {
				Files.createDirectories(uploadPath);
			}
			
			for(int i=0;i<files.size();i++) {
				MultipartFile file=files.get(i);
				// 驗證檔案
				if (file.isEmpty() || !isImageFile(file)) {
	                continue;
	            }
				 // 產生唯一檔名：productId_timestamp_index.ext
				String originalFilename=file.getOriginalFilename();
				String extension=getFileExtension(originalFilename);
				String newFilename=productId+"_"+System.currentTimeMillis()+"_"+i+extension;
				// 儲存檔案
				Path filePath=uploadPath.resolve(newFilename);
				Files.copy(file.getInputStream(), filePath,StandardCopyOption.REPLACE_EXISTING);
				ProductImage img = new ProductImage();
				img.setProduct(product);
				img.setImageUrl("/uploads/products/" +newFilename);
				img.setSortOrder(maxSortOrder+i+1);
		        ProductImage savedImage = productImageRepository.save(img);
		     // 將儲存的 Entity 轉成 DTO
		        uploadProductImages.add(productImageMapper.toDto(savedImage));
			}
			
            
			
		} catch (IOException e) {
			throw new RuntimeException("圖片上傳失敗: " + e.getMessage(), e);
		}
		
		return uploadProductImages;
	}

	@Override
	public List<ProductImageDto> getImages(Long productId) {
		 List<ProductImage> images = productImageRepository.findByProductIdOrderBySortOrder(productId);
		return images.stream()
                .map(productImageMapper::toDto)
                .collect(Collectors.toList());
	}

	@Override
	public void updateSortOrders(List<ImageSortDto> sortList) {
		for (ImageSortDto dto : sortList) {
            ProductImage image = productImageRepository.findById(dto.getImageId())
                    .orElseThrow(() -> new RuntimeException("圖片不存在，ID: " + dto.getImageId()));
            image.setSortOrder(dto.getSortOrder());
            productImageRepository.save(image);
        }
    }
		
	

	@Override
	public void deleteImage(Long imageId) throws IOException {
		 ProductImage image = productImageRepository.findById(imageId)
	                .orElseThrow(() -> new RuntimeException("圖片不存在，ID: " + imageId));
	        
	        try {
	            // 刪除本地檔案
	            Path filePath = Paths.get("." + image.getImageUrl());
	            Files.deleteIfExists(filePath);
	        } catch (IOException e) {
	            // 記錄檔案刪除失敗，但繼續刪除資料庫記錄
	            System.err.println("刪除檔案失敗: " + e.getMessage());
	        }
	        
	        // 刪除資料庫記錄
	        productImageRepository.delete(image);
		
	}

	@Override
	public boolean isImageFile(MultipartFile file) {
		 String contentType = file.getContentType();
	     return contentType != null && contentType.startsWith("image/");
	}

	@Override
	public String getFileExtension(String filename) {
		if (filename == null || !filename.contains(".")) {
            return ".jpg";
        }
        return filename.substring(filename.lastIndexOf("."));
	}

	@Override
	public void deleteAllImagesByProductId(Long productId) {
		List<ProductImage> images = productImageRepository.findByProductId(productId);
        for (ProductImage img : images) {
            try {
                Path filePath = Paths.get("." + img.getImageUrl());
                Files.deleteIfExists(filePath);
            } catch (IOException e) {
                // 可記錄日誌
            }
        }
        productImageRepository.deleteByProductId(productId);
		
	}

}
