package com.example.demo.mapper;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.example.demo.model.dto.StoreDto;
import com.example.demo.model.dto.UserDto;
import com.example.demo.model.entity.Store;
import com.example.demo.model.entity.User;

@Component
public class StoreMapper {
	
	@Autowired
	private ModelMapper modelMapper;
	
	public StoreDto toDto(Store store) {
		// Entity 轉 DTO
		return modelMapper.map(store, StoreDto.class);
	}
	
	public Store toEntity(StoreDto storeDto) {
		// DTO 轉 Entity
		return modelMapper.map(storeDto, Store.class);
	}
	
}
