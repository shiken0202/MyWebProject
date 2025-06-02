package com.example.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.model.dto.ProductImageDto;
import com.example.demo.response.ApiResponse;
import com.example.demo.service.ProductImageService;

@RestController
@CrossOrigin(origins = {"http://localhost:5173"}, allowCredentials = "true")
public class ProductImageController {
	@Autowired
	private ProductImageService productImageService;
	
	@PostMapping("/{productId}/images")
	public ResponseEntity<ApiResponse<List<ProductImageDto>>> uploadImages(
            @PathVariable Long productId,
            @RequestParam("files") List<MultipartFile> files) {
        
        try {
            if (files == null || files.isEmpty()) {
                return ResponseEntity.badRequest()
                        .body(ApiResponse.error(400, "請選擇要上傳的圖片"));
            }
            
            List<ProductImageDto> uploadedImages = productImageService.uploadImages(productId, files);
            return ResponseEntity.ok(
                    ApiResponse.success("圖片上傳成功，共上傳 " + uploadedImages.size() + " 張", uploadedImages)
            );
            
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error(400, e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(ApiResponse.error(500, "伺服器錯誤：" + e.getMessage()));
        }
    }
	@DeleteMapping("/{productId}/images")
	public ResponseEntity<ApiResponse<Void>> deleteAllImages(
	        @PathVariable Long productId) {
	    try {
	        productImageService.deleteAllImagesByProductId(productId);
	        return ResponseEntity.ok(ApiResponse.success("所有圖片已刪除", null));
	    } catch (Exception e) {
	        return ResponseEntity.internalServerError()
	                .body(ApiResponse.error(500, "批次刪除失敗：" + e.getMessage()));
	    }
	}
}
