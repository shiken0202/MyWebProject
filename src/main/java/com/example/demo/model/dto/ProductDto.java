package com.example.demo.model.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductDto {
    private Long id;
    private Long storeId;         // 對應 Store 的 id
    private Long categoryId;      // 對應 Category 的 id
    private String title;
    private String brand;
    private String stock;
    private BigDecimal price;
    private Integer viewCount;
    private Boolean isActive;
    private LocalDateTime createdAt;

    // 關聯圖片、訂單明細、購物車項目只放 id 清單，避免 DTO 過大與遞迴
    private List<Long> productImageIds;
    private List<Long> orderItemIds;
    private List<Long> cartItemIds;
}
