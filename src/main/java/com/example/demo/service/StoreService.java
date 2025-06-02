package com.example.demo.service;

import java.util.List;

import com.example.demo.model.dto.StoreDto;

public interface StoreService {
	public void addStore(String StoreName,String description,Long userId);
	StoreDto findStoreByUserId(Long userId);
	public Boolean updateStoreDescription(String description ,Long userId);
	List<StoreDto>findAllStores();
}
