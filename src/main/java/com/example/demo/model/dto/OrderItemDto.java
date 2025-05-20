package com.example.demo.model.dto;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderItemDto {
    private Long id;
    private Long orderId;     // 對應 Order 的 id
    private Long productId;   // 對應 Product 的 id
    private Integer quantity;
    private BigDecimal price;
    private BigDecimal subtotal;
}
