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
public class OrderDto {
    private Long id;
    private Long userId;           // 對應 User 的 id
    private Long storeId;          // 對應 Store 的 id
    private BigDecimal totalAmount;
    private String status;
    private LocalDateTime createdAt;
    private LocalDateTime updateAt;
    private String address;
    private String paymentType;

    // 一張訂單有多個訂單明細，只存明細 id 清單
    private List<Long> orderItemIds;
}
