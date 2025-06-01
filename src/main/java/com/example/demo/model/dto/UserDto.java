package com.example.demo.model.dto;

import java.time.LocalDateTime;
import java.util.List;


import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
    private Long id;
    @Pattern(regexp = "^[a-zA-Z0-9-_]{5,20}$",message = "用戶名稱需介於5-20個字母")
    private String userName;
    private String email;
    private String role; // 用 String 表示角色（如 "ADMIN"、"SELLER"、"BUYER"）
    private LocalDateTime createdAt;
    private Boolean isBanned;

    // 只放關聯 id 或簡化資訊，避免循環依賴
    private Long storeId; // 對應 Store 的 id
    private List<Long> orderIds; // 對應 Order 的 id 清單
    private List<Long> cartItemIds; // 對應 CartItem 的 id 清單
}
