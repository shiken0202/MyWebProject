package com.example.demo.service.impl;

import java.util.Optional;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.exception.StoreNotFoundException;
import com.example.demo.exception.UserNotFoundException;
import com.example.demo.mapper.StoreMapper;
import com.example.demo.model.dto.StoreDto;
import com.example.demo.model.entity.Store;
import com.example.demo.model.entity.User;
import com.example.demo.repository.StoreRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.StoreService;
@Service
public class StoreServiceImpl implements StoreService {

	@Autowired
	private StoreRepository storeRepository;
	
	@Autowired
	private UserRepository userRepository;
	@Autowired
	StoreMapper storeMapper;
	
	@Override
	public void addStore(String StoreName, String description, Long userId) {
		Store store=new Store();
		store.setStoreName(StoreName);
		store.setDescription(description);
		Optional<User> userOpt=userRepository.findById(userId);
		if(userOpt.isEmpty()) {
			throw new UserNotFoundException("找不到使用者");
		}
		User user=userOpt.get();
		store.setUser(user);
		storeRepository.save(store);
	}

	@Override
	public StoreDto findStoreByUserId(Long userId) {
		Optional<Store> storeOpt=storeRepository.findStoreByUserId(userId);
		if(storeOpt.isEmpty()) {
			return null;
		}
		Store store=storeOpt.get();
		return storeMapper.toDto(store);
	}

	@Override
	public Boolean updateStoreDescription(String description, Long id) {
		int rows= storeRepository.updateStoreDescription(description, id);
		return rows>0;
		
	}

	

}
