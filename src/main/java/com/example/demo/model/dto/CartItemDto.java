package com.example.demo.model.dto;


import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CartItemDto {
    private Long id;
    private Long userId;         // 對應 User 的 id
    private Long productId;      // 對應 Product 的 id
    private Integer quantity;
    private LocalDateTime createdAt;
}
