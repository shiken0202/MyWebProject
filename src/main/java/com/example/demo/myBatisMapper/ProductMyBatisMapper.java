package com.example.demo.myBatisMapper;

import com.example.demo.model.entity.Product;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.data.repository.query.Param;

import java.util.List;

@Mapper
public interface ProductMyBatisMapper {

    List<Product> findByStoreIdWithImages(@Param("storeId") Long storeId);
}
