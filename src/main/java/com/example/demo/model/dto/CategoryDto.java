package com.example.demo.model.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CategoryDto {
    private Integer id;
    private String name;
    private Integer parentId;          // 對應父分類的 id
    private List<Integer> childrenIds; // 對應所有子分類的 id 清單
    private List<Long> productIds;     // 對應所有商品的 id 清單
}
