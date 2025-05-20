package com.example.demo.model.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StoreDto {
	private Long id;
	private Long userId;
	private String storeName;
	private String description;
	// 關聯商品和訂單只傳 id 清單，避免 DTO 過大或遞迴
    private List<Long> productIds;
    private List<Long> orderIds;
}
