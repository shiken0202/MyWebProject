package com.example.demo.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;

import com.example.demo.exception.UserAlreadyExistsException;
import com.example.demo.mapper.UserMapper;
import com.example.demo.model.dto.UserDto;
import com.example.demo.model.entity.User;
import com.example.demo.model.entity.User.Role;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.UserService;
import com.example.demo.util.HashUtil;

public class UserServiceImpl implements UserService {

	@Autowired
	UserRepository userRepository;
	
	@Autowired
	UserMapper userMapper;
	
	@Override
	public void emailConfirmOK(String userName) {
		if(userName==null) {
			return;
		}
		userRepository.emailConfirmOK(userName);
	}

	@Override
	public List<UserDto> findAllUsers() {
		return null;
		// TODO Auto-generated method stub
		
	}

	@Override
	public UserDto getUserById() {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public void addUser(String userName, String userEmail,String password ,String role) {
		UserDto userDto=new UserDto();
		userDto.setUserName(userName);
		userDto.setEmail(userEmail);
		try {
			String hashSalt = HashUtil.generateSalt(); // 取得鹽
			String hashPassword = HashUtil.hashPassword(password, hashSalt); // 取 hash 密碼
			userDto.setPassword(hashPassword);
			userDto.setRole(role);
			addUser(userDto);
		} catch (Exception e) {
			e.printStackTrace();
		}	
	}
	
	@Override
	public void addUser(UserDto userDto) {
			Optional<User>optUser=userRepository.findByUsername(userDto.getUserName());
			if(optUser.isPresent()) {
				throw new UserAlreadyExistsException("新增失敗: 使用者名稱 " + userDto.getUserName() + " 已經存在"); 
			}
			User user=userMapper.toEntity(userDto);
			// 新增 user
			userRepository.save(user);
			userRepository.flush();
		
	}
	@Override
	public void updateUser(Long userId, UserDto userDto) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void updateUser(Long userId,String userName,String userEmail,String password,Role role) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteUser(Long userId, UserDto userDto) {
		// TODO Auto-generated method stub
		
	}
	

}
